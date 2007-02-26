/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.model.source;

import static com.mindquarry.common.xml.SerializerUtil.makeSerializer;
import static com.mindquarry.model.source.ReflectionUtil.invokeGetter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.common.model.SerializationIgnore;
import com.mindquarry.common.model.SerializationName;

/**
 *
 * @author
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class ModelSource implements Source {
	
	private String url_;
	private ModelSourceInterpreter businessSourceInterpreter_;
	
	private InputStream xmlInputStream_;
	
	public ModelSource(String url,
			ModelSourceInterpreter businessSourceInterpreter) {
		
		url_ = url;
		businessSourceInterpreter_ = businessSourceInterpreter;
	}
	
	public void initialize() {
		xmlInputStream_ = generateXmlInputStream();
	}

	public boolean exists() {
		return true;
	}

	public long getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public InputStream getInputStream() throws IOException {
		return xmlInputStream_;
	}

	public long getLastModified() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getMimeType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getScheme() {
		return url_.substring(0, url_.indexOf(':'));
	}

	public String getURI() {
		return url_;
	}

	public SourceValidity getValidity() {
		return null;
	}

	public void refresh() {
		xmlInputStream_ = generateXmlInputStream();
	}
	
	private InputStream generateXmlInputStream() {
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			try {
				toSax(makeSerializer(outputStream));
			} catch (SAXException e) {
				throw new ModelSourceException(e);
			} catch (IOException e) {
				throw new ModelSourceException(e);
			}
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
		finally {
			if (outputStream != null) {
				closeStream(outputStream);
			}
		}
	}
	
	private void closeStream(Closeable stream) {
		try {
			stream.close();
		} catch (IOException e) {
			throw new ModelSourceException("could not close stream", e);
		}
	}
	
	private void toSax(ContentHandler contentHandler) throws SAXException {
		
		Object context = businessSourceInterpreter_.interpret();
		
		contentHandler.startDocument();
		if (context instanceof Collection) {
			Collection<?> entityList = (Collection<?>) context;
			entityListToSax(contentHandler, entityList);
		}
		else {
			entityToSax(contentHandler, context);
		}
			
		contentHandler.endDocument();
	}
	
	private void entityToSax(ContentHandler contentHandler, 
			Object entity) throws SAXException {
		
		final String entityName = serializationName(entity.getClass());
		startElement(contentHandler, entityName);
		
		for (Entry<String, Object> property : properties(entity).entrySet()) {
			
			writeElementWithText(contentHandler, 
					property.getKey(), property.getValue().toString());
		}
		
		endElement(contentHandler, entityName);
	}
	
	private void entityListToSax(ContentHandler contentHandler, 
			Collection<?> entityList) throws SAXException {
		
		if (entityList.isEmpty())
			return;
		
		Object firstEntity = entityList.iterator().next();
		final String firstEntityName = serializationName(firstEntity.getClass());
		final String collectionName = firstEntityName + "s";
		
		startElement(contentHandler, collectionName);
		for (Object entity : entityList) {
			entityToSax(contentHandler, entity);
		}
		endElement(contentHandler, collectionName);
	}
	
	private String serializationName(Class<?> clazz) {
		SerializationName name = clazz.getAnnotation(SerializationName.class);
		return name != null ? name.value() : clazz.getSimpleName();
	}
	
	private Map<String, Object> properties(Object entity) {
		
		Class clazz = entity.getClass();
		
		Map<String, Object> result = new HashMap<String, Object>();
		for (Method method : clazz.getMethods()) {
			if (! isGetterMethod(method))
				continue;
			
			if (isMarkedAsSerializationIgnore(method))
				continue;
			
			if (hasUnsupportedReturnType(method))
				continue;
			
			String propertyName = serializationNameFromGetter(method);
			Object propertyValue = invokeGetter(method, entity);
			
			result.put(propertyName, propertyValue);
		}
		return result;
	}
	
	private String serializationNameFromGetter(Method getter) {
		SerializationName name = 
			getter.getAnnotation(SerializationName.class);
		
		if (name != null) {
			return name.value();
		}
		else {
			String methodName = getter.getName();
			StringBuilder propertyNameSB = new StringBuilder();
			propertyNameSB.append(methodName.substring(3, 4).toLowerCase());
			propertyNameSB.append(methodName.substring(4));
			return propertyNameSB.toString();
		}
	}
	
	private boolean hasUnsupportedReturnType(Method getter) {
		return (! getter.getReturnType().isPrimitive()
				&& ! getter.getReturnType().equals(String.class));
	}
	
	private boolean isGetterMethod(Method method) {
		return method.getName().startsWith("get")
				&& method.getParameterTypes().length == 0
				&& ! method.getReturnType().equals(void.class);
	}
	
	private boolean isMarkedAsSerializationIgnore(Method method) {
		return method.isAnnotationPresent(SerializationIgnore.class);
	}
	
	private void writeElementWithText(ContentHandler contentHandler, 
			String qname, String content) throws SAXException {
		startElement(contentHandler, qname);
		char[] contentChars = content.toCharArray();
		contentHandler.characters(contentChars, 0, contentChars.length);
		endElement(contentHandler, qname);
	}
	
	private void startElement(ContentHandler contentHandler, String qname) 
			throws SAXException {
		contentHandler.startElement(null, null, qname, new AttributesImpl());
	}
	
	private void endElement(ContentHandler contentHandler, String qname) 
			throws SAXException {
		contentHandler.endElement(null, null, qname);
	}
}
