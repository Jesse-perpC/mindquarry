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
package com.mindquarry.jcr.change;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Properties;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.mindquarry.jcr.change.handler.JcrChangeHandler;

public class ChangeClient {
    private static final String O_REPO = "r"; //$NON-NLS-1$

    private static final String O_USER = "u"; //$NON-NLS-1$

    private static final String O_PWD = "p"; //$NON-NLS-1$

    private static final String O_XSLT = "x"; //$NON-NLS-1$

    private static final String O_DRY = "d"; //$NON-NLS-1$

    private static final String O_FOLDER = "f"; //$NON-NLS-1$

    private static Log log;

    /**
     * The options object for the command line applications.
     */
    private static final Options options;

    // static initialization of command line options
    static {
        Option repo = new Option(O_REPO, "repository", true, //$NON-NLS-1$
                "The endpoint where the repository is available.");
        repo.setRequired(true);

        Option user = new Option(O_USER, "user", true, //$NON-NLS-1$
                "The user name to be used for login to the repository.");
        user.setRequired(true);

        Option pwd = new Option(O_PWD, "password", true, //$NON-NLS-1$
                "The password to be used for login to the repository.");
        pwd.setRequired(true);

        Option xslt = new Option(O_XSLT, "xslt", true, //$NON-NLS-1$
                "The XSLT stylesheet to be used for making the changes.");
        xslt.setRequired(true);

        Option debug = new Option(
                O_DRY,
                "dry", false, //$NON-NLS-1$
                "Indicates if changes should be done as a dry run. "
                        + "In this mode no modifications to the repository are done. "
                        + "All changes are stored in local files for debugging purposes.");

        Option folder = new Option(O_FOLDER, "folder", true, //$NON-NLS-1$
                "The folder on which the transformation should be applied.");
        folder.setRequired(true);

        options = new Options();
        options.addOption(repo);
        options.addOption(user);
        options.addOption(pwd);
        options.addOption(xslt);
        options.addOption(debug);
        options.addOption(folder);
    }

    public static void main(String[] args) {
        log = LogFactory.getLog(ChangeClient.class);
        log.info("Starting persistence client...");

        // parse command line arguments
        CommandLine line = null;
        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException e) {
            // oops, something went wrong
            log.error("Parsing of command line failed.");
            printUsage();
            return;
        }
        // check debug option
        boolean debug = false;
        if (line.hasOption(O_DRY)) {
            log.info("Running in 'dry' mode.");
            debug = true;
        }
        // start change client
        ChangeClient cl = new ChangeClient();
        try {
            cl.run(line.getOptionValue(O_REPO), line.getOptionValue(O_USER),
                    line.getOptionValue(O_PWD), line.getOptionValue(O_XSLT),
                    line.getOptionValue(O_FOLDER), debug);
        } catch (Exception e) {
            log.error("Error while applying changes.", e);
        }
        log.info("Changes applied.");
    }

    private void run(String repoLocation, String login, String pwd,
            String xslt, String folder, boolean debug) throws Exception {
        Session session = login(repoLocation, login, pwd);

        ByteArrayOutputStream bos = exportRepositoryContent(session, folder);
        if (debug) {
            storeData(bos, new FileOutputStream("old-content.xml")); //$NON-NLS-1$
        }

        ByteArrayOutputStream result = applyTranformation(xslt, bos);
        result = applyHandlerTransformations(result);
        if (debug) {
            storeData(result, new FileOutputStream("changed-content.xml")); //$NON-NLS-1$
        } else {
            applyChanges(session, result, folder);
        }
    }

    private ByteArrayOutputStream applyHandlerTransformations(
            ByteArrayOutputStream result) throws Exception {
        // load handler definitions
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "transform-handler.properties"); //$NON-NLS-1$
        Properties handlerProps = new Properties();
        handlerProps.load(is);

        // process handler
        Iterator<Object> keys = handlerProps.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.startsWith("com.mindquarry.jcr.change.handler.")) { //$NON-NLS-1$
                String handlerName = handlerProps.getProperty(key);
                Class handlerClass = Class.forName(handlerName);
                JcrChangeHandler handler = (JcrChangeHandler) handlerClass
                        .newInstance();
                result = handler.process(new ByteArrayInputStream(result
                        .toByteArray()));
            }
        }
        return result;
    }

    private ByteArrayOutputStream applyTranformation(String xslt,
            ByteArrayOutputStream bos)
            throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, TransformerException {
        log.info("Applying content transformation...");

        // JAXP reads data using the Source interface
        Source xmlSource = new StreamSource(new ByteArrayInputStream(bos
                .toByteArray()));
        Source xsltSource = new StreamSource(new File(xslt));

        ByteArrayOutputStream result = new ByteArrayOutputStream();

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        trans.transform(xmlSource, new StreamResult(result));

        return result;
    }

    private void applyChanges(Session session, ByteArrayOutputStream bos,
            String folder) throws VersionException, LockException,
            ConstraintViolationException, RepositoryException, IOException,
            ParserConfigurationException, SAXException {
        log.info("Applying changes to repository...");

        // delete old node
        Node node = session.getRootNode().getNode(folder);
        node.remove();
        session.save();

        // store new content
        session.getWorkspace().importXML(
                "/" + folder + "/..", //$NON-NLS-1$ //$NON-NLS-2$
                new ByteArrayInputStream(bos.toByteArray()),
                ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
        session.save();
    }

    private ByteArrayOutputStream exportRepositoryContent(Session session,
            String folder) throws IOException, PathNotFoundException,
            RepositoryException, SAXException,
            TransformerConfigurationException {
        log.info("Exporting repository content...");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // HACK: look at
        // http://www.mail-archive.com/users@jackrabbit.apache.org/msg01434.html
        // if you need a workaround for exporting jcr:root without jcr:system

        session.exportDocumentView("/" + folder, bos, false, false); //$NON-NLS-1$
        return bos;
    }

    private Session login(String repoLocation, String login, String pwd)
            throws MalformedURLException, NotBoundException, RemoteException,
            LoginException, RepositoryException {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory.getRepository(repoLocation);
        Session session = repo.login(new SimpleCredentials(login, pwd
                .toCharArray()));
        return session;
    }

    private void storeData(ByteArrayOutputStream bos, FileOutputStream out)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory tmp = DocumentBuilderFactory.newInstance();
        tmp.setNamespaceAware(true);
        DocumentBuilder builder = tmp.newDocumentBuilder();
        Document doc = builder
                .parse(new ByteArrayInputStream(bos.toByteArray()));

        OutputFormat format = new OutputFormat();
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);

        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
    }

    /**
     * Automatically generate and print the help statement.
     */
    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar mindquarry-jcr-changes-<version>.jar", //$NON-NLS-1$
                options, true);
    }
}
