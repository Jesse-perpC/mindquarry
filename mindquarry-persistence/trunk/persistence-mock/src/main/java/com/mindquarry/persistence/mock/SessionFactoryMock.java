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
package com.mindquarry.persistence.mock;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.config.PersistenceConfiguration;

public class SessionFactoryMock implements SessionFactory {
	
	private PersistenceConfiguration configuration_;
	private Session session_;

    /**
	 * setter for PersistenceConfiguration component
	 */
	public void setConfiguration(PersistenceConfiguration configuration_) {
		this.configuration_ = configuration_;
	}
	
	public void initialize() {
		session_ = new SessionMock(configuration_);
	}


	public Session currentSession() {
    	return session_;
    }
}