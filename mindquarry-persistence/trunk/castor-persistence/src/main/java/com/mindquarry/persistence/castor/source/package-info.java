/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.persistence.castor.source;