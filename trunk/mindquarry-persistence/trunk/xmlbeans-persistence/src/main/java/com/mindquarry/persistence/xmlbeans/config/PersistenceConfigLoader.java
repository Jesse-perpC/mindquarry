package com.mindquarry.persistence.xmlbeans.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public abstract class PersistenceConfigLoader {

    protected abstract InputStream resolveConfig();

    public Configuration findAndLoad() {
        InputStream configIn = resolveConfig();
        return parse(configIn);
    }

    protected Configuration parse(InputStream configIn) {
        try {
            return ConfigurationDocument.Factory.parse(
                    configIn, makeParserXmlOptions()).getConfiguration();
        } catch (XmlException e) {
            throw new InitializationException("xmlbeans could " +
                    "not parse persistence configuration file", e);
        } catch (IOException e) {
            throw new InitializationException("xmlbeans could " +
                    "not parse persistence configuration file", e);
        }
    }

    protected XmlOptions makeParserXmlOptions() {
        XmlOptions result = new XmlOptions();
        result.setCompileDownloadUrls();
        return result;
    }

}