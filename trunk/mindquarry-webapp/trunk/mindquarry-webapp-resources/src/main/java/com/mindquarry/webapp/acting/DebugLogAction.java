/**
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * An action that logs the string in the src attribute.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class DebugLogAction extends AbstractAction implements ThreadSafe {
    public Map act(Redirector redirector, SourceResolver resolver,
            Map objectModel, String src, Parameters par) throws Exception {

        System.err.println(src);
        getLogger().info(src);

        return null;
    }

}
