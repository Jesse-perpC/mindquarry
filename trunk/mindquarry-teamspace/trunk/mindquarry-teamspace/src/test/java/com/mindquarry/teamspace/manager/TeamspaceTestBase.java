/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.mindquarry.jcr.jackrabbit.JCRTestBase;
import com.mindquarry.teamspace.TeamspaceAdmin;



/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
abstract class TeamspaceTestBase extends JCRTestBase {
    

    // add spring bean definitions to component configuration settings
    protected void addSettings(BeanDefinitionRegistry registry) {
                
        GenericApplicationContext ctx = new GenericApplicationContext();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        
        Resource[] springConfigResources = findSpringConfigResources();
        
        for (Resource resource : springConfigResources)
            xmlReader.loadBeanDefinitions(resource);
        
        
        for (String beanName : ctx.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = ctx.getBeanDefinition(beanName);
            registry.registerBeanDefinition(beanName, beanDefinition);
            
            for (String alias : ctx.getAliases(beanName)) 
                registry.registerAlias(beanName, alias);
        }

        super.addSettings(registry);
    }

    private Resource[] findSpringConfigResources() {
        
        List<Resource> resultList = new LinkedList<Resource>();
        
        String[] resourceNames = new String[] {
                "META-INF/cocoon/spring/teamspace-context.xml"
        };
        
        for (String resourceName : resourceNames)
            resultList.add(new ClassPathResource(resourceName));
        
        return resultList.toArray(new Resource[resultList.size()]);
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }

    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    protected void prepare() throws Exception {
        String className = TeamspaceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = classLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }
    
    protected TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }
}
