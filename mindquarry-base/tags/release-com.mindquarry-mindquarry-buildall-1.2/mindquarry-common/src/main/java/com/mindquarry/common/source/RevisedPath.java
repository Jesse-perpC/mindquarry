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
package com.mindquarry.common.source;

public class RevisedPath {
    private static final String ADDED = "addition";

    private static final String UNKNOWN = null;

    private static final String DELETED = "deletion";

    private static final String REPLACED = "replacement";

    private static final String MODIFIED = "modification";

    private final String path_;

    private final String action_;

    public RevisedPath(String path, char action) {
        this.path_ = path;
        switch (action) {
        case 'A':
            this.action_ = ADDED;
            break;
        case 'D':
            this.action_ = DELETED;
            break;
        case 'R':
            this.action_ = REPLACED;
            break;
        case 'M':
            this.action_ = MODIFIED;
            break;
        default:
            this.action_ = UNKNOWN;
            break;
        }
    }

    public String getAction() {
        return action_;
    }

    public String getPath() {
        return path_;
    }
}