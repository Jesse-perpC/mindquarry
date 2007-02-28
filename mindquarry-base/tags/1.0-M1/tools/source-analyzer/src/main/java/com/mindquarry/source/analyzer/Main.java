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
package com.mindquarry.source.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mindquarry.source.analyzer.filter.DirectoryFilter;
import com.mindquarry.source.analyzer.filter.FileExtensionFilter;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)de">Alexander
 *         Saar</a>
 */
public class Main {
    private static final String[] EXTENSIONS = { "xsl", "xslt", "js", "java",
            "xml", "jx", "html", "xmap", "css" };

    private static Map<String, Integer> loc;

    static {
        loc = new HashMap<String, Integer>();
    }

    /**
     * Main entry point of the application.
     */
    public static void main(String[] args) throws Exception {
        File home = new File("/Users/saar/MQ-Workspaces");
        processDirectory(home);
        printResults();
    }

    private static void processDirectory(File dir) throws Exception {
        // process sub directories
        File[] subDirs = dir.listFiles(new DirectoryFilter());
        for (File subDir : subDirs) {
            processDirectory(subDir);
        }
        // process files
        for (String ext : EXTENSIONS) {
            File[] files = dir.listFiles(new FileExtensionFilter(ext));
            for (File file : files) {
                countLines(file, ext);
            }
        }
    }

    private static void countLines(File file, String extension)
            throws Exception {
        if (!loc.containsKey(extension)) {
            loc.put(extension, 0);
        }
        int count = loc.get(extension);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.readLine() != null) {
            count++;
        }
        loc.put(extension, count);
    }

    private static void printResults() {
        Set<String> keys = loc.keySet();
        Iterator<String> kIt = keys.iterator();
        while (kIt.hasNext()) {
            String key = kIt.next();
            System.out.println(key + " :: " + loc.get(key));
        }
    }
}
