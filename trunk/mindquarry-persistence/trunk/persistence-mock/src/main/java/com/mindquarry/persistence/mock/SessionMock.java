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
package com.mindquarry.persistence.mock;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.NotPredicate;

import com.mindquarry.persistence.api.EntityBase;
import com.mindquarry.persistence.api.PersistenceException;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.config.PersistenceConfiguration;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class SessionMock extends AbstractLogEnabled implements Session {
    
	private PersistenceConfiguration configuration_;
	
	private Map<Class, Map<String, EntityBase>> entities_;
    
    public SessionMock(PersistenceConfiguration configuration) {
    	configuration_ = configuration;
    	entities_ = new HashMap<Class, Map<String, EntityBase>>();
    }
    
    private EntityBase entityBy(Class clazz, String id) {
    	if (entities_.containsKey(clazz)) {
			return entities_.get(clazz).get(id);
		}
    	return null;
    }

    // not needed so far
    // will be removed from the interface soon
	public void commit() {
	}

	public boolean delete(Object object) {
		EntityBase entity = validateEntity(object);
		Class clazz = entityClass(entity);
		if (null != entityBy(clazz, entity.getId())) {
			entities_.get(clazz).remove(entity.getId());
			return true;
		}
		return false;
	}

	public void persist(Object object) {
		EntityBase entity = validateEntity(object);
		
		if (null == entityBy(entityClass(entity), entity.getId()))
			store(entity);
		else
			throw new PersistenceException("an entity with id: " + 
                    entity.getId() + " already exists.");
	}
	
	private Class resultClass(String queryKey) {
		String clazzString = configuration_.queryResultClass(queryKey);
		return configuration_.classForName(clazzString);
	}

	public List<Object> query(String queryKey, Object[] queryParams) {
		List<Object> result = new LinkedList<Object>();
		String query = configuration_.query(queryKey).trim();
		if (query.equals("byId")) {
			Class clazz = resultClass(queryKey);
			EntityBase entity = entityBy(clazz, (String) queryParams[0]);
			if (entity != null)
				result.add(entity);
		}
		else if (query.equals("byClass")) {
			String clazzString = configuration_.queryResultClass(queryKey);
			Class clazz = configuration_.classForName(clazzString);
			if (entities_.containsKey(clazz)) {
				result.addAll(entities_.get(clazz).values());
			}
		}
		else if (query.startsWith("byClass")
				&& containsPredicate(query)) {
			
			String predicate = predicate(prepareQuery(query, queryParams));
			Map<String, Predicate> queriedProperties = properties(predicate);
			
			String clazzString = configuration_.queryResultClass(queryKey);
			Class clazz = configuration_.classForName(clazzString);
			
			if (entities_.containsKey(clazz)) {
				for (EntityBase entity : entities_.get(clazz).values()) {
					if (matchProperties(entity, queriedProperties))
						result.add(entity);
				}
			}
		}
		else {
			throw new RuntimeException("query pattern not yet supported");
		}
		return result;
	}
	
	private boolean matchProperties(
			Object entity, Map<String, Predicate> properties) {
		
		for (Entry<String, Predicate> entry : properties.entrySet()) {
			
			String propertyValue = propertyValue(entity, entry.getKey());
			Predicate propertyPrediate = entry.getValue();
			
			if (! propertyPrediate.evaluate(propertyValue))
				return false;
		}
		return true;
	}
	
	private String propertyValue(Object bean, String name) {
		try {
			String value = BeanUtils.getProperty(bean, name);
			return value.substring(1, value.length() - 1);
		} catch (IllegalAccessException e) {
			throw new PersistenceException(
					"could not get property '" + name + "' for bean: " + bean +
					" to match query predicate against it", e);
		} catch (InvocationTargetException e) {
			throw new PersistenceException(
					"could not get property '" + name + "' for bean: " + bean +
					" to match query predicate against it", e);
		} catch (NoSuchMethodException e) {
			throw new PersistenceException(
					"could not get property '" + name + "' for bean: " + bean +
					" to match query predicate against it", e);
		}
	}
	
	private String prepareQuery(String unpreparedQuery, Object[] params) {
		return new QueryPreparer(unpreparedQuery, params).prepare();
	}
	
	private Map<String, Predicate> properties(String queryPredicate) {
		
		Map<String, Predicate> result = new HashMap<String, Predicate>();
		for (String simplePredicate : queryPredicate.split("and")) {
			
			String comparator = comparator(simplePredicate);
			String[] nameValue = simplePredicate.trim().split(comparator);
			
			Predicate predicate = new EqualPredicate(nameValue[1].trim().replace("'", ""));
			if (isNotEquals(comparator))
				predicate = new NotPredicate(predicate);

			result.put(nameValue[0], predicate);
		}
		return result;
	}
	
	private boolean isNotEquals(String comparator) {
		return "!=".equals(comparator);
	}
	
	private String comparator(String simplePredicate) {
		if (simplePredicate.indexOf("!=") != -1)
			return "!=";
		else if (simplePredicate.indexOf("=") != -1)
			return "=";
		else 
			throw new PersistenceException("not yet supported " +
					"comparator in query predicate: " + simplePredicate);
	}
	
	private String predicate(String query) {
		return query.substring(8, query.length() -  1);
	}
	
	private boolean containsPredicate(String query) {
		return query.startsWith("byClass[") && query.endsWith("]");
	}

	public void update(Object object) {
		EntityBase entity = validateEntity(object);
		store(entity);
	}
	
	private void store(EntityBase entity) {
		Class clazz = entityClass(entity);
		if (! entities_.containsKey(clazz))
			entities_.put(clazz, new HashMap<String, EntityBase>());
		
		entities_.get(clazz).put(entity.getId(), entity);
	}
	
	private Class entityClass(Object object) {
		return object.getClass();
	}
    
    private EntityBase validateEntity(Object object) {
        validateEntityClass(entityClass(object));
        return (EntityBase) object;
    }
    
    private Class<? extends EntityBase> validateEntityClass(Class<?> clazz) {
        assert isConfiguredEntityClazz(clazz) : clazz + " is not " +
                "configured as entity class within mindquarry-persistence.xml";
        return clazz.asSubclass(EntityBase.class);
    }
    
    private boolean isConfiguredEntityClazz(Class clazz) {
        if (configuration_.existsEntityClazz(clazz))
            return true;
        else 
            return false;
    }
}
