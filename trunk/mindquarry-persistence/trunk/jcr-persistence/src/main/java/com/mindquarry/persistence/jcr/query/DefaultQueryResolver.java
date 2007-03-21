package com.mindquarry.persistence.jcr.query;

import static com.mindquarry.jcr.jackrabbit.xpath.JaxenQueryHandler.FULL_XPATH;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import com.mindquarry.common.persistence.PersistenceException;
import com.mindquarry.persistence.jcr.Configuration;
import com.mindquarry.persistence.jcr.annotations.NamedQueries;
import com.mindquarry.persistence.jcr.annotations.NamedQuery;
import com.mindquarry.persistence.jcr.api.JcrNodeIterator;
import com.mindquarry.persistence.jcr.api.JcrSession;

public class DefaultQueryResolver implements QueryResolver {

    private Map<String, String> queryMap_;
    
    public DefaultQueryResolver() {
        queryMap_ = new HashMap<String, String>();
    }
    
    public void initialize(Configuration configuration) {
        for (Class<?> clazz : configuration.getClasses()) {
            if (clazz.isAnnotationPresent(NamedQuery.class)) {
                addNamedQuery(clazz.getAnnotation(NamedQuery.class));             
            }
            else if (clazz.isAnnotationPresent(NamedQueries.class)) {
                NamedQueries queries = clazz.getAnnotation(NamedQueries.class);
                for (NamedQuery query : queries.value()) {
                    addNamedQuery(query);
                }
            }
        }
        
        for (Entry<String, String> entry : 
            configuration.getNamedQueries().entrySet()) {
            
            addNamedQuery(entry.getKey(), entry.getValue());
        }
    }
    
    private void addNamedQuery(NamedQuery queryAnnotation) {
        addNamedQuery(queryAnnotation.name(), queryAnnotation.query());
    }
    
    private void addNamedQuery(String queryName, String query) {
        if (queryMap_.containsKey(queryName)) {
            throw new QueryException("encountered duplicate query names. " +
                    " Query: " + queryMap_.get(queryName) +
                    " and query: " + query + " have identical names.");
        }
        queryMap_.put(queryName, query);
    }
    
    public JcrNodeIterator resolve(JcrSession jcrSession,
            String queryName, Object[] queryParameters) {
        
        if (! queryMap_.containsKey(queryName)) {
            throw new PersistenceException(
                    "could not find a query with name: " + queryName);
        }
        
        String query = queryMap_.get(queryName);
        String preparedQuery = prepareQuery(query, queryParameters);
        return resolveXpath(jcrSession, preparedQuery);
    }
    
    private JcrNodeIterator resolveXpath(
            JcrSession jcrSession, String expression) {
        
        try {
            QueryManager queryManager = jcrSession.getQueryManager();
            Query query = queryManager.createQuery(expression, FULL_XPATH);
            QueryResult queryResult = query.execute();
            return new JcrNodeIterator(queryResult.getNodes());
        }
        catch (InvalidQueryException e) {
            throw new QueryException("The jcr layer threw an " +
                    "InvalidQueryException. Either the query is not " +
                    "supported or the xpath expression: '" + expression + 
                    "' is syntactically wrong. Please, validate the defined " +
                    "query as well as the query parameters.", e);
        }
        catch (RepositoryException e) {
            throw new PersistenceException(e);
        }
    }

    private String prepareQuery(String query, Object[] queryParameters) {
        return QueryPreparer.prepare(query, queryParameters);
    }
}
