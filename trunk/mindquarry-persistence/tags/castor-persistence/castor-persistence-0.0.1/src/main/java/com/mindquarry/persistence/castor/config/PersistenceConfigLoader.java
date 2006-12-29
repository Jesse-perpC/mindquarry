package com.mindquarry.persistence.castor.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.xmlbeans.XmlOptions;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.persistence.config.Configuration;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public abstract class PersistenceConfigLoader extends AbstractLogEnabled {

    protected abstract InputStream resolveConfig();

    public Configuration findAndLoad() {
        InputStream configIn = resolveConfig();
        return parse(configIn);
    }

    protected Configuration parse(InputStream configIn) {
        getLogger().debug("parse xmlbeans persistence configuration");
        try {
            InputStreamReader configReader = new InputStreamReader(configIn);
            return (Configuration) Configuration.unmarshal(configReader);
        } catch (MarshalException e) {
            throw new InitializationException("castor could " +
                    "not parse persistence configuration file", e);
        } catch (ValidationException e) {
            throw new InitializationException("castor could " +
                    "not parse persistence configuration file", e);
        }
    }

    protected XmlOptions makeParserXmlOptions() {
        XmlOptions result = new XmlOptions();
        result.setCompileDownloadUrls();
        return result;
    }

}