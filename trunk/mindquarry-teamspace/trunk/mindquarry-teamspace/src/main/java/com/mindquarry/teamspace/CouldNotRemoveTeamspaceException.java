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
package com.mindquarry.teamspace;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class CouldNotRemoveTeamspaceException extends Exception {

    private static final long serialVersionUID = -5521634518836088322L;

    public CouldNotRemoveTeamspaceException(String message) {
        super(message);
    }

    public CouldNotRemoveTeamspaceException(String message, Throwable t) {
        super(message, t);
    }

    public CouldNotRemoveTeamspaceException(Throwable t) {
        super(t);
    }
}
