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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.teamspace.TeamspaceQuery;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.UserRO;

/**
 *
 * @author
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
class TeamSource implements Source {
	
	private String teamId_;
	private boolean includeMembers_;
	
	private ServiceManager serviceManager_;
	
	private InputStream xmlInputStream_;
	
	public TeamSource(String teamId, boolean includeMembers, 
				ServiceManager serviceManager) {
		
		teamId_ = teamId;
		includeMembers_ = includeMembers;
		serviceManager_ = serviceManager;
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getURI() {
		// TODO Auto-generated method stub
		return null;
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
    
    private TeamspaceQuery lookupTeamsQuery() {
    	assert serviceManager_ != null;
    	
    	try {
			return (TeamspaceQuery) serviceManager_.lookup(TeamspaceQuery.ROLE);
		} catch (ServiceException e) {
			throw new InitializationException(
					"could not lookup TeamsQuery component", e);
		}
    }
    
    private TeamspaceRO queryTeamById() {
    	assert teamId_ != null;
    	
    	return lookupTeamsQuery().teamspaceById(teamId_);
    }
	
	private void toSax(ContentHandler contentHandler) throws SAXException {
		TeamspaceRO team = queryTeamById();
		
		contentHandler.startDocument();
		startElement(contentHandler, "teamspace");
		writeElementWithText(contentHandler, "id", team.getId());
		writeElementWithText(contentHandler, "name", team.getName());
		writeElementWithText(contentHandler, "description", team.getDescription());
		
		if (includeMembers_) {
			startElement(contentHandler, "users");
			for (UserRO user : team.getUsers()) {
				startElement(contentHandler, "user");
				writeElementWithText(contentHandler, "id", user.getId());
				writeElementWithText(contentHandler, "name", user.getName());
				writeElementWithText(contentHandler, "surname", user.getSurname());
				writeElementWithText(contentHandler, "email", user.getEmail());
				writeElementWithText(contentHandler, "skills", user.getSkills());
				endElement(contentHandler, "user");
			}
			endElement(contentHandler, "users");
		}
		
		endElement(contentHandler, "teamspace");
		contentHandler.endDocument();
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
