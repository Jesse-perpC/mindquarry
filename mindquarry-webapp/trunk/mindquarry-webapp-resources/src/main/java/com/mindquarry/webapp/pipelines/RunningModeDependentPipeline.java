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
package com.mindquarry.webapp.pipelines;

import javax.servlet.ServletContext;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.blocks.BlockCallStack;
import org.apache.cocoon.blocks.BlockContext;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.core.container.spring.RunningModeHelper;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.sitemap.SitemapErrorHandler;
import org.apache.cocoon.util.AbstractLogEnabled;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;

/**
 * ProcessingPipeline implementation that wraps another pipeline depending
 * on the running mode (eg. dev and prod). For each running mode, a different
 * implementation can be chosen, which is handy when you want caching only in
 * production mode and full reloading with no caching during development.
 * 
 * <p>
 * For each running mode there is a parameter with the same name, eg. 'prod',
 * that must contain the shorthand name of the pipeline to use, which must be
 * configured normally (but with a distinctive name), eg. 'real-caching'.
 * </p> 
 * 
 * <p>
 * Example configuration for the setting described above (noncaching in dev):
 * </p>
 * 
 * <pre>
 *   &lt;pipes default="caching"&gt;
 *       &lt;!-- the wrapping pipeline impls --&gt;
 *       
 *       &lt;pipe name="expires-caching" src="com.mindquarry.webapp.pipelines.RunningModeDependentPipeline"&gt;
 *           &lt;parameter name="dev" value="noncaching"/&gt;
 *           &lt;parameter name="prod" value="real-expires-caching"/&gt;
 *       &lt;/pipe&gt;
 *       
 *       &lt;pipe name="caching" src="org.apache.cocoon.components.pipeline.impl.CachingProcessingPipeline"&gt;
 *           &lt;parameter name="dev" value="noncaching"/&gt;
 *           &lt;parameter name="prod" value="real-caching"/&gt;           
 *       &lt;/pipe&gt;
 *       
 *       &lt;pipe name="caching-point" src="org.apache.cocoon.components.pipeline.impl.CachingPointProcessingPipeline"&gt;
 *           &lt;parameter name="dev" value="noncaching"/&gt;
 *           &lt;parameter name="prod" value="real-caching-point"/&gt;
 *       &lt;/pipe&gt;
 *
 *       &lt;!-- the real caching pipelines --&gt;
 *       
 *       &lt;pipe name="real-expires-caching" src="org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline"&gt;
 *           &lt;parameter name="cache-expires" value="86400"/&gt;
 *       &lt;/pipe&gt;
 *       
 *       &lt;pipe name="real-caching" src="org.apache.cocoon.components.pipeline.impl.CachingProcessingPipeline"/&gt;
 *       
 *       &lt;pipe name="real-caching-point" src="org.apache.cocoon.components.pipeline.impl.CachingPointProcessingPipeline"&gt;
 *           &lt;parameter name="autoCachingPoint" value="On"/&gt;
 *       &lt;/pipe&gt;
 *   &lt;/pipes&gt;
 * </pre>
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class RunningModeDependentPipeline extends AbstractLogEnabled implements
        ProcessingPipeline, Serviceable, Parameterizable, Recyclable {
    
    private String wrappedPipelineShorthand;
    private ProcessingPipeline wrappedPipeline = null;
    private boolean fixMissingCachingPrefixWithBlockServlets;

    private ServiceManager manager;

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }
    
    public void parameterize(Parameters parameters) throws ParameterException {
        String mode = RunningModeHelper.determineRunningMode(null);
        this.wrappedPipelineShorthand = parameters.getParameter(mode, "noncaching");
        
        this.fixMissingCachingPrefixWithBlockServlets = parameters.getParameterAsBoolean("fixCachingPrefix", true);
    }
    
    public void setup(Parameters parameters) {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setup(parameters);
    }
    
    public void recycle() {
        // nothing to recycle here (the wrappedPipeline must persist!)
    }

    private void ensureWrappedPipelineIsSet() throws ProcessingException {
        if (this.wrappedPipeline == null) {
            try {
                this.wrappedPipeline = (ProcessingPipeline)
                    manager.lookup(ProcessingPipeline.ROLE + "/" + wrappedPipelineShorthand);
            } catch (ServiceException e) {
                throw new ProcessingException("Could no lookup pipeline '" + wrappedPipelineShorthand + "'", e);
            }
        }
    }
    
    private void ensureWrappedPipelineIsSetNoException() {
        try {
            ensureWrappedPipelineIsSet();
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ProcessingPipeline
    ////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#addTransformer(java.lang.String, java.lang.String, org.apache.avalon.framework.parameters.Parameters, org.apache.avalon.framework.parameters.Parameters)
     */
    public void addTransformer(String arg0, String arg1, Parameters arg2,
            Parameters arg3) throws ProcessingException {
        ensureWrappedPipelineIsSet();
        this.wrappedPipeline.addTransformer(arg0, arg1, arg2, arg3);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#getGenerator()
     */
    public Generator getGenerator() {
        ensureWrappedPipelineIsSetNoException();
        return this.wrappedPipeline.getGenerator();
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#getKeyForEventPipeline()
     */
    public String getKeyForEventPipeline() {
        ensureWrappedPipelineIsSetNoException();
        return this.wrappedPipeline.getKeyForEventPipeline();
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#getValidityForEventPipeline()
     */
    public SourceValidity getValidityForEventPipeline() {
        ensureWrappedPipelineIsSetNoException();
        return this.wrappedPipeline.getValidityForEventPipeline();
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#informBranchPoint()
     */
    public void informBranchPoint() {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.informBranchPoint();
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#prepareInternal(org.apache.cocoon.environment.Environment)
     */
    public void prepareInternal(Environment env) throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        fixEnvironmentForCachingPrefix(env);
        this.wrappedPipeline.prepareInternal(env);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#process(org.apache.cocoon.environment.Environment)
     */
    public boolean process(Environment env) throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        fixEnvironmentForCachingPrefix(env);
        return this.wrappedPipeline.process(env);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#process(org.apache.cocoon.environment.Environment, org.apache.cocoon.xml.XMLConsumer)
     */
    public boolean process(Environment env, XMLConsumer arg1)
            throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        fixEnvironmentForCachingPrefix(env);
        return this.wrappedPipeline.process(env, arg1);
    }

    private void fixEnvironmentForCachingPrefix(Environment env) {
        if (this.fixMissingCachingPrefixWithBlockServlets &&
            (env.getURIPrefix() == null || env.getURIPrefix().equals(""))) {
            ServletContext context = BlockCallStack.getCurrentBlockContext();
            if (context instanceof BlockContext) {
                // use the mount path of the block as the prefix
                String mountPath = ((BlockContext) context).getMountPath() + "/";
                env.setURI(mountPath, env.getURI());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#setErrorHandler(org.apache.cocoon.sitemap.SitemapErrorHandler)
     */
    public void setErrorHandler(SitemapErrorHandler arg0)
            throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setErrorHandler(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#setGenerator(java.lang.String, java.lang.String, org.apache.avalon.framework.parameters.Parameters, org.apache.avalon.framework.parameters.Parameters)
     */
    public void setGenerator(String arg0, String arg1, Parameters arg2,
            Parameters arg3) throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setGenerator(arg0, arg1, arg2, arg3);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#setProcessorManager(org.apache.avalon.framework.service.ServiceManager)
     */
    public void setProcessorManager(ServiceManager arg0) {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setProcessorManager(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#setReader(java.lang.String, java.lang.String, org.apache.avalon.framework.parameters.Parameters, java.lang.String)
     */
    public void setReader(String arg0, String arg1, Parameters arg2, String arg3)
            throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setReader(arg0, arg1, arg2, arg3);
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#setSerializer(java.lang.String, java.lang.String, org.apache.avalon.framework.parameters.Parameters, org.apache.avalon.framework.parameters.Parameters, java.lang.String)
     */
    public void setSerializer(String arg0, String arg1, Parameters arg2,
            Parameters arg3, String arg4) throws ProcessingException {
        ensureWrappedPipelineIsSetNoException();
        this.wrappedPipeline.setSerializer(arg0, arg1, arg2, arg3, arg4);
    }

}
