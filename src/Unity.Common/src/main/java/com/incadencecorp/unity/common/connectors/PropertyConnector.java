/*-----------------------------------------------------------------------------'
 Copyright 2016 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

package com.incadencecorp.unity.common.connectors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingType;

/**
 * Abstract class for handing properties configuration files.
 * 
 * @author n78554
 */
public abstract class PropertyConnector implements IConfigurationsConnector, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyConnector.class);

    /*--------------------------------------------------------------------------
    Private Member Variables
    --------------------------------------------------------------------------*/

    private Hashtable<String, Properties> properties;
    private boolean isReadOnly = false;

    /*--------------------------------------------------------------------------
    Overridden Methods
    --------------------------------------------------------------------------*/

    @Override
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {

        String value;

        try
        {

            // Get Property
            value = getProperties(configurationFileName).getProperty(settingPath);

            // Property Found?
            if (value == null)
            {

                // No; Use Default
                value = defaultValue;

                if (setIfNotFound)
                {
                    // Persist Value
                    setSetting(configurationFileName, settingPath, defaultValue, type);
                }

            }

        }
        catch (IOException e)
        {
            LOGGER.error("Error in PropertyConnector", e.getMessage());
            value = defaultValue;
        }

        return value;
    }

    @Override
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {

        boolean isSuccessful = isReadOnly;

        if (!isReadOnly)
        {

            try
            {

                // Get Properties
                Properties props = getProperties(configurationFileName);

                // Set Value
                props.setProperty(settingPath, value);

                // Persist
                try (OutputStream stream = getPropertyOutputStream(configurationFileName))
                {

                    if (stream != null)
                    {
                        props.store(stream, "");
                        isSuccessful = true;
                    }

                }

            }
            catch (IOException e)
            {
                LOGGER.error(e.getMessage());
            }
        }

        // Not applicable to this implementation (Resource files are read only)
        return isSuccessful;
    }

    @Override
    public Map<String, String> getSettings(String configurationFileName)
    {
        Map<String, String> results = new HashMap<String, String>();

        try
        {
            for (Entry<Object, Object> entry : getProperties(configurationFileName).entrySet())
            {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String)
                {
                    results.put((String) entry.getKey(), (String) entry.getValue());
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage());
        }

        return results;
    }

    @Override
    public boolean log(String logName, String callResultXml)
    {
        LOGGER.info("{}: {}", logName, callResultXml);

        return true;
    }

    /**
     * @return whether the connector is configured to update the property file
     *         or not.
     */
    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    /**
     * @param isReadOnly If <code>true</code> changes to settings will not be
     *            persisted to the property files; otherwise setting changes are
     *            persisted.
     */
    public void setReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    /*--------------------------------------------------------------------------
    Abstract Methods
    --------------------------------------------------------------------------*/

    /**
     * @param key is the name of the configuration file.
     * @return Returns a for reading properties.
     * @throws FileNotFoundException
     */
    protected abstract InputStream getPropertyInputStream(String key) throws IOException;

    /**
     * @param key is the name of the configuration file.
     * @return Returns a stream for writing properties.
     * @throws FileNotFoundException
     */
    protected abstract OutputStream getPropertyOutputStream(String key) throws IOException;

    /*--------------------------------------------------------------------------
    Private Methods
    --------------------------------------------------------------------------*/

    private Properties getProperties(String key) throws IOException
    {

        Properties props = null;

        if (!getProperties().containsKey(key))
        {

            props = new Properties();

            // Load Properties
            try (InputStream stream = getPropertyInputStream(key))
            {

                if (stream != null)
                {
                    props.load(stream);
                    getProperties().put(key, props);
                }

            }
            catch (FileNotFoundException e)
            {

                // TODO Log missing configuration file.
                getProperties().put(key, props);

            }

        }
        else
        {
            props = getProperties().get(key);
        }

        return props;
    }

    private Hashtable<String, Properties> getProperties()
    {

        // Cache Initialized?
        if (properties == null)
        {

            // No; Initialize
            properties = new Hashtable<String, Properties>();

        }

        return properties;

    }

    @Override
    public void close()
    {

        if (properties != null)
        {
            properties.clear();
            properties = null;
        }

    }
}
