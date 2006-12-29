/**
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
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
