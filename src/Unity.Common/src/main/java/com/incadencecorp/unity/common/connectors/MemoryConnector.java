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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingType;

/**
 * This connector uses memory for storing and retrieving values.
 * 
 * @author n78554
 */
public class MemoryConnector implements IConfigurationsConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryConnector.class);

    private Map<String, Map<String, String>> cache = new HashMap<String, Map<String, String>>();

    @Override
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {
        String result = defaultValue;

        if (cache.containsKey(configurationFileName))
        {
            Map<String, String> config = cache.get(configurationFileName);

            if (config.containsKey(settingPath))
            {
                result = config.get(settingPath);
            }
        }

        return result;
    }

    @Override
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        if (!cache.containsKey(configurationFileName))
        {
            cache.put(configurationFileName, new HashMap<String, String>());
        }

        cache.get(configurationFileName).put(settingPath, value);

        return true;
    }

    @Override
    public Map<String, String> getSettings(String configurationFileName)
    {
        Map<String, String> results = new HashMap<String, String>();

        // Make Copy
        results.putAll(cache.get(configurationFileName));

        return results;
    }

    @Override
    public boolean log(String logName, String callResultXml)
    {
        LOGGER.info("{}: {}", logName, callResultXml);

        return true;
    }

}
