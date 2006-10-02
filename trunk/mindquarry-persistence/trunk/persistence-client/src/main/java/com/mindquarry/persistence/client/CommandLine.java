/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.Repository;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.XmlBeansSessionFactoryStandalone;

/**
 * Command line client to be used for working with the Mindquarry persistence
 * layer.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class CommandLine {
    private static final String O_REPO = "r"; //$NON-NLS-1$

    private static final String O_PERSIST = "p"; //$NON-NLS-1$

    private static final String O_DEL = "d"; //$NON-NLS-1$

    private static final char VALUE_SEPARATOR = ' ';

    /**
     * The options object for the command line applications.
     */
    private static final Options options;

    // static initialization of command line options
    static {
        Option repo = new Option(O_REPO, "repository", true,
                "The endpoint where the persistence layer is available."); //$NON-NLS-1$
        repo.setRequired(true);

        Option persist = new Option(O_PERSIST, "persist", true,
                "A list of XML files containing the types that should "
                        + "be stored in the persistence layer.");
        persist.setValueSeparator(VALUE_SEPARATOR);

        Option delete = new Option(O_DEL, "delete", true,
                "A list of URIs describing the types that should "
                        + "be deleted from the persistence layer.");
        delete.setValueSeparator(VALUE_SEPARATOR);

        OptionGroup actions = new OptionGroup();
        actions.setRequired(true);
        actions.addOption(persist);
        actions.addOption(delete);

        options = new Options();
        options.addOption(repo);
        options.addOptionGroup(actions);
    }

    /**
     * The main entry point of the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run(args);
    }

    /**
     * Evaluates the given command line arguments, initializes connection to the
     * persistence layer and starts the specified actions.
     * 
     * @param args the command line arguments
     */
    private void run(String[] args) throws Exception {
        // create the parser
        org.apache.commons.cli.CommandLine line = null;
        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: "
                    + exp.getLocalizedMessage());
            printUsage();
            return;
        }
        // init connection to persistence layer
        String repoEndpoint = line.getOptionValue(O_REPO);
        ClientRepositoryFactory clFactory = new ClientRepositoryFactory();
        Repository repo = clFactory.getRepository(repoEndpoint);
        
        XmlBeansSessionFactoryStandalone factory = new XmlBeansSessionFactoryStandalone();
        Session session = factory.currentSession();
                
        // check what actions are specified
        if (line.hasOption(O_PERSIST)) {
            persistTypes(line.getOptionValues(O_PERSIST));
        } else if (line.hasOption(O_DEL)) {
            deleteTypes(line.getOptionValues(O_DEL));
        }
    }

    private void persistTypes(String[] optionValues) {

    }

    private void deleteTypes(String[] optionValues) {

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
