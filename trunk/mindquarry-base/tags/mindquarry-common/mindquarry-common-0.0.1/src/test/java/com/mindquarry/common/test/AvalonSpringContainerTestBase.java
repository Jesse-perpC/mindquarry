/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.core.container.ContainerTestCase;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



/**
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class AvalonSpringContainerTestBase extends ContainerTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

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
        
        for (String resourceName : springConfigClasspathResources())
            resultList.add(new ClassPathResource(resourceName));
        
        return resultList.toArray(new Resource[resultList.size()]);
    }
    
    protected List<String> springConfigClasspathResources() {
        List<String> result = new LinkedList<String>();
        result.add("META-INF/cocoon/spring/mindquarry-commons-context.xml");
        return result;
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }
}
