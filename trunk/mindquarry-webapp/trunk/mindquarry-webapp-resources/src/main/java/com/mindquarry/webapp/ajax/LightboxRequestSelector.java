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
package com.mindquarry.webapp.ajax;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.selection.AbstractSwitchSelector;
import org.apache.commons.lang.BooleanUtils;

public class LightboxRequestSelector extends AbstractSwitchSelector {
    public static final String LIGHTBOX_REQUEST = "lightbox-request"; //$NON-NLS-1$
    
    public Object getSelectorContext(Map objectModel, Parameters parameters) {
        Request req = ObjectModelHelper.getRequest(objectModel);
        
        return BooleanUtils.toBooleanObject(req.getParameter(LIGHTBOX_REQUEST)!=null);
    }

    public boolean select(String expression, Object selectorContext) {
        boolean test = BooleanUtils.toBoolean(expression);
        return test == ((Boolean)selectorContext).booleanValue();
    }

}
