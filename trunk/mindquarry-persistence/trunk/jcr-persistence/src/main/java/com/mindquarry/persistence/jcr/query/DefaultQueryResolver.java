package com.mindquarry.persistence.jcr.query;

import static com.mindquarry.jcr.jackrabbit.xpath.JaxenQueryHandler.FULL_XPATH;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import com.mindquarry.common.persistence.PersistenceException;
import com.mindquarry.persistence.jcr.annotations.NamedQueries;
import com.mindquarry.persistence.jcr.annotations.NamedQuery;
import com.mindquarry.persistence.jcr.api.JcrNodeIterator;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.model.Model;

public class DefaultQueryResolver implements QueryResolver {

    private Model model_;
    private Map<String, String> queryMap_;
    
    public DefaultQueryResolver(Model model) {
        model_ = model;
        queryMap_ = new HashMap<String, String>();
    }
    
    public void initialize() {
        List<NamedQuery> namedQueries = findAllNamedQueries();
        assert uniqueNamed(namedQueries);
        for (NamedQuery namedQuery : namedQueries) {
            queryMap_.put(namedQuery.name(), namedQuery.query());
        }
    }
    
    private boolean uniqueNamed(List<NamedQuery> queries) {
        Map<String, NamedQuery> processed = new HashMap<String, NamedQuery>();
        
        for (NamedQuery query : queries) {
            if (processed.containsKey(query.name())) {
                throw new QueryException("encountered duplicate query names. " +
                        " Query: " + processed.get(query.name()) +
                        " and query: " + query + " have identical names.");
            }
            
            processed.put(query.name(), query);
        }
        return true;
    }
    
    private List<NamedQuery> findAllNamedQueries() {
        List<NamedQuery> result = new LinkedList<NamedQuery>();
        for (Class<?> clazz : model_.allEntityClasses()) {
            if (clazz.isAnnotationPresent(NamedQuery.class)) {
                result.add(clazz.getAnnotation(NamedQuery.class));                
            }
            else if (clazz.isAnnotationPresent(NamedQueries.class)) {
                NamedQueries queries = clazz.getAnnotation(NamedQueries.class);
                for (NamedQuery query : queries.value()) {
                    result.add(query);
                }
            }
        }
        return result;
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
        return new QueryPreparer(query, queryParameters).prepare();
    }
}
