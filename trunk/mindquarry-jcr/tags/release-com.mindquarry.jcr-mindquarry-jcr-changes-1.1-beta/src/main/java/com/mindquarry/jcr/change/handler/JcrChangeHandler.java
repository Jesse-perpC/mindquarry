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
package com.mindquarry.jcr.change.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Interface for handlers that can process the transformation results of JCR
 * content.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public interface JcrChangeHandler {
    /**
     * Processes the result of a JCR content tranformation and returns the
     * result as stream.
     */
    ByteArrayOutputStream process(ByteArrayInputStream is) throws Exception;
}
