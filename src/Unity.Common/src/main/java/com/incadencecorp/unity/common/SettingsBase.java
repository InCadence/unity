package com.incadencecorp.unity.common;

import java.util.Hashtable;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

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

/**
 * {@link com.incadencecorp.unity.common.SettingsBase} is the base class for all setting contexts which allow an application
 * to store and retrieve setting values from a local or remote source. The
 * {@link com.incadencecorp.unity.common.SettingsBase} class encapsulates a connector and uses a cache to temporary store
 * setting values.
 * 
 * @author InCadence
 *
 */
public class SettingsBase {

    /*--------------------------------------------------------------------------
    	Private Member Variables
    --------------------------------------------------------------------------*/

    private static Hashtable<String, String> _cache = null;
    private static IConfigurationsConnector _connector = null;

    private static Object _cacheLock = new Object();

    /*--------------------------------------------------------------------------
        Constructor
    --------------------------------------------------------------------------*/

    protected SettingsBase()
    {
        // Do Nothing
    }

    /*--------------------------------------------------------------------------
    	Public Functions
    --------------------------------------------------------------------------*/

    /**
     * Initializes the connector.
     * 
     * @param connector the connector to use for storing and retrieving setting values.
     */
    public static void initialize(final IConfigurationsConnector connector)
    {
        SettingsBase._connector = connector;
    }

    /**
     * Remove all entries in the cache.
     */
    public static void clearCache()
    {
        synchronized (_cacheLock)
        {
            SettingsBase.getCache().clear();
        }
    }

    /*--------------------------------------------------------------------------
    	Protected Functions
    --------------------------------------------------------------------------*/

    protected static int getSettingWithMin(final String configurationFileName,
                                           final String settingPath,
                                           final int defaultValue,
                                           final int minValue,
                                           final boolean setIfNotFound)
    {

        int value;

        // Ensure Default Value Meets Requirement
        if (defaultValue < minValue)
        {
            value = SettingsBase.getSetting(configurationFileName, settingPath, minValue, setIfNotFound);
        }
        else
        {
            value = SettingsBase.getSetting(configurationFileName, settingPath, defaultValue, setIfNotFound);
        }

        // Ensure Setting Meets Requirement
        if (value < minValue)
        {
            value = minValue;
        }

        return value;

    }

    protected static int getSettingWithMax(final String configurationFileName,
                                           final String settingPath,
                                           final int defaultValue,
                                           final int maxValue,
                                           final boolean setIfNotFound)
    {
        int value;

        // Ensure Default Value Meets Requirement
        if (defaultValue > maxValue)
        {
            value = SettingsBase.getSetting(configurationFileName, settingPath, maxValue, setIfNotFound);
        }
        else
        {
            value = SettingsBase.getSetting(configurationFileName, settingPath, defaultValue, setIfNotFound);
        }

        // Ensure Setting Meets Requirement
        if (value > maxValue)
        {
            value = maxValue;
        }

        return value;

    }

    protected static int getSetting(final String configurationFileName,
                                    final String settingPath,
                                    final int defaultValue,
                                    final boolean setIfNotFound)
    {

        return Integer.parseInt(SettingsBase.getSetting(configurationFileName,
                                                        settingPath,
                                                        Integer.toString(defaultValue),
                                                        SettingType.ST_INTEGER,
                                                        setIfNotFound));

    }

    protected static boolean getSetting(final String configurationFileName,
                                        final String settingPath,
                                        final boolean defaultValue,
                                        final boolean setIfNotFound)
    {

        return Boolean.parseBoolean(SettingsBase.getSetting(configurationFileName,
                                                            settingPath,
                                                            Boolean.toString(defaultValue),
                                                            SettingType.ST_BOOLEAN,
                                                            setIfNotFound));

    }

    protected static String getSetting(final String configurationFileName,
                                       final String settingPath,
                                       final String defaultValue,
                                       final boolean setIfNotFound)
    {

        return SettingsBase.getSetting(configurationFileName,
                                       settingPath,
                                       defaultValue,
                                       SettingType.ST_STRING,
                                       setIfNotFound);

    }

    protected static String getSetting(final String configurationFileName,
                                       final String settingPath,
                                       final String defaultValue,
                                       final SettingType type,
                                       final boolean setIfNotFound)
    {
        String value = null;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String cacheKey = SettingsBase.normalizeCacheKey(configurationFileName, settingPath);

            // Read Value From Cache
            value = SettingsBase.getCache().get(cacheKey);

            // Value Cached?
            if (value == null)
            {
                // No; User Defined Connector?
                if (_connector != null)
                {
                    // Yes; Read Configuration
                    value = _connector.getSetting(configurationFileName, settingPath, defaultValue, type, setIfNotFound);

                    // Add to Cache
                    SettingsBase.getCache().put(cacheKey, value);
                }
                else
                {
                    // No; Use Default
                    value = defaultValue;
                }

            }
        }

        return value;
    }

    protected static boolean setSetting(final String configurationFileName, final String settingPath, final int value)
    {

        return SettingsBase.setSetting(configurationFileName, settingPath, Integer.toString(value), SettingType.ST_INTEGER);

    }

    protected static boolean setSetting(final String configurationFileName, final String settingPath, final boolean value)
    {

        return SettingsBase.setSetting(configurationFileName, settingPath, Boolean.toString(value), SettingType.ST_BOOLEAN);

    }

    protected static boolean setSetting(final String configurationFileName, final String settingPath, final String value)
    {

        return SettingsBase.setSetting(configurationFileName, settingPath, value, SettingType.ST_STRING);

    }

    protected static boolean setSetting(final String configurationFileName,
                                        final String settingPath,
                                        final String value,
                                        final SettingType type)
    {

        boolean updated = true;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String cacheKey = SettingsBase.normalizeCacheKey(configurationFileName, settingPath);

            // Setting Not Cached or Modified?
            if (!SettingsBase.getCache().containsKey(cacheKey) || SettingsBase.getCache().get(cacheKey) != value)
            {

                // Yes; Replace Cached Value
                SettingsBase.getCache().put(cacheKey, value);

                // Update Configuration File
                if (_connector != null)
                {
                    updated = _connector.setSetting(configurationFileName, settingPath, value, type);
                }

            }
        }

        return updated;

    }

    protected static String normalizeCacheKey(final String configurationFileName, final String settingPath)
    {

        String cacheKey = configurationFileName + "." + settingPath;
        cacheKey = cacheKey.replace("/", ".").toUpperCase();
        return cacheKey;

    }

    private static Hashtable<String, String> getCache()
    {

        // Cache Initialized?
        if (SettingsBase._cache == null)
        {

            // No; Initialize
            SettingsBase._cache = new Hashtable<String, String>();

        }

        return SettingsBase._cache;

    }

}
