/**
 * The xmlbeans persistence is used within the cocoon mindquarry-webapp 
 * but it is also used by mailets running within the mail servcer james.
 * So there is a need encapsulate the resolving of jcr sources.
 * JcrSourceResolverCocoon handles the resolution in a component container 
 * setup like in cocoon. The JcrSourceResolverStandalone links and 
 * references directly to the JCRSourceFactory. Thus it is useful in
 * non-container setups.
 * 
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
package com.mindquarry.persistence.xmlbeans.source;