/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.common.index.SolrIndexClient;

/**
 * Command line client to be used for working with the Mindquarry persistence
 * layer.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitRMIRepositoryStandalone {
    private static final String REPO_CONF_PATH = "/com/mindquarry/jcr/jackrabbit/repository.xml";

    private static final String O_CONF = "c"; //$NON-NLS-1$

    private static final String O_LOC = "l"; //$NON-NLS-1$

    private static final String O_USER = "u"; //$NON-NLS-1$

    private static final String O_PWD = "p"; //$NON-NLS-1$

    private static final String O_WS = "w"; //$NON-NLS-1$

    private static final String REMOTE_REPO_NAME = "jackrabbit";

    private static final String MQ_JCR_XML_NODETYPES_FILE = "/com/mindquarry/jcr/jackrabbit/node-types.txt";

    /**
     * The options object for the command line applications.
     */
    private static final Options options;

    protected Repository repo;

    protected Registry reg;

    protected Session session;

    // static initialization of command line options
    static {
        Option repo = new Option(O_CONF, "config", true,
                "The crepository configuration file to be used by "
                        + "the repository instance..");

        Option location = new Option(O_LOC, "location", true,
                "The directory to be used by the repository "
                        + "for storing its data.");
        location.setRequired(true);

        Option user = new Option(O_USER, "user", true,
                "The user name to be used for login and setup the repository.");
        user.setRequired(true);

        Option pwd = new Option(O_PWD, "password", true,
                "The password to be used for login and setup the repository.");
        pwd.setRequired(true);

        Option ws = new Option(O_WS, "workspace", true,
                "The workspace to be used during login and setup of the repository.");

        options = new Options();
        options.addOption(repo);
        options.addOption(location);
        options.addOption(user);
        options.addOption(pwd);
        options.addOption(ws);
    }

    /**
     * The main entry point of the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JackrabbitRMIRepositoryStandalone repo = new JackrabbitRMIRepositoryStandalone();

        CommandLine line = repo.parse(args);
        if (line != null) {
            repo.start(line);

            System.out.println("Please press <enter> to shutdown the server.");
            System.in.read();

            repo.stop();
        }
    }

    /**
     * Evaluates the given command line arguments, initializes connection to the
     * persistence layer and starts the specified actions.
     * 
     * @param args the command line arguments
     */
    private CommandLine parse(String[] args) throws Exception {
        // create the parser
        CommandLine line = null;
        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: "
                    + exp.getLocalizedMessage());
            printUsage();
            return null;
        }
        return line;
    }

    private void start(CommandLine line) throws Exception {
        String repoConf;
        String repoConfArg = line.getOptionValue(O_CONF);
        if (null == repoConfArg)
            repoConf = createDefaultRepoConf();
        else
            repoConf = new File(repoConfArg).getAbsolutePath();

        File repoLoc = new File(line.getOptionValue(O_LOC));
        if (!repoLoc.exists()) {
            repoLoc.mkdir();
        }
        if (!repoLoc.isDirectory()) {
            throw new IllegalArgumentException(
                    "Repository location is not a directory.");
        }
        Repository repo = new TransientRepository(repoConf, repoLoc
                .getAbsolutePath());

        ServerAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remoteRepo = factory.getRemoteRepository(repo);

        reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        reg.rebind(REMOTE_REPO_NAME, remoteRepo);

        String workspace = line.getOptionValue(O_WS);
        if (null == workspace)
            workspace = "default";

        session = repo.login(new SimpleCredentials(line.getOptionValue(O_USER),
                line.getOptionValue(O_PWD).toCharArray()), workspace);

        InputStream nodeTypeDefIn = getClass().getResourceAsStream(
                MQ_JCR_XML_NODETYPES_FILE);

        IndexClient iClient = new SolrIndexClient();
        JackrabbitInitializerHelper.setupRepository(session,
                new InputStreamReader(nodeTypeDefIn),
                MQ_JCR_XML_NODETYPES_FILE, iClient);
        session.save();
    }

    private String createDefaultRepoConf() throws IOException {
        InputStream confIn = getClass().getResourceAsStream(REPO_CONF_PATH);
        File tempConfFile = File.createTempFile("repository", "xml");
        tempConfFile.deleteOnExit();
        IOUtils.copy(confIn, new FileOutputStream(tempConfFile));
        return tempConfFile.getAbsolutePath();
    }

    private void stop() throws Exception {
        // shutting down RMI repository
        session.logout();
        reg.unbind(REMOTE_REPO_NAME);
        repo = null;
        reg = null;
    }

    /**
     * Automatically generate and print the help statement.
     */
    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar persistence-client-<version>.jar", //$NON-NLS-1$
                options, true);
    }
}
