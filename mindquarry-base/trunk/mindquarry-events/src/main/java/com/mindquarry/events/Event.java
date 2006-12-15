/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public interface Event {
    public String getID();
    
    public long getTimestamp();
    
    public String getMessage();
    
    public Object getSource();
        
    public boolean isConsumed();
    
    public void consume();
}
