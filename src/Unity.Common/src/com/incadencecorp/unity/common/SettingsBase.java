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
 * SettingsBase is the base class for all setting contexts which allow an application to store and retrieve setting values
 * from a local or remote source. The SettingBase class encapsulates a connector and uses a cache to temporary store setting values.
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
    	Public Functions
    --------------------------------------------------------------------------*/

    /**
     * Initializes the connector
     * 
     * @param connector the connector to use for storing and retrieving setting values 
     */
    public static void initialize(IConfigurationsConnector connector)
    {
        SettingsBase._connector = connector;
    }

    /**
     * Remove all entries in the cache 
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

    protected static int getSettingWithMin(String ConfigurationFileName,
                                           String SettingPath,
                                           int DefaultValue,
                                           int MinValue,
                                           boolean SetIfNotFound)
    {

        // Ensure Default Value Meets Requirement
        if (DefaultValue < MinValue) DefaultValue = MinValue;

        // Get Setting
        int value = SettingsBase.getSetting(ConfigurationFileName, SettingPath, DefaultValue, SetIfNotFound);

        // Ensure Setting Meets Requirement
        if (value < MinValue) value = MinValue;

        return value;

    }

    protected static int getSettingWithMax(String ConfigurationFileName,
                                           String SettingPath,
                                           int DefaultValue,
                                           int MaxValue,
                                           boolean SetIfNotFound)
    {

        // Ensure Default Value Meets Requirement
        if (DefaultValue > MaxValue) DefaultValue = MaxValue;

        // Get Setting
        int value = SettingsBase.getSetting(ConfigurationFileName, SettingPath, DefaultValue, SetIfNotFound);

        // Ensure Setting Meets Requirement
        if (value > MaxValue) value = MaxValue;

        return value;

    }

    protected static int getSetting(String ConfigurationFileName, String SettingPath, int DefaultValue, boolean SetIfNotFound)
    {

        return Integer.parseInt(SettingsBase.getSetting(ConfigurationFileName,
                                                        SettingPath,
                                                        Integer.toString(DefaultValue),
                                                        SettingType.stInteger,
                                                        SetIfNotFound));

    }

    protected static boolean getSetting(String ConfigurationFileName,
                                        String SettingPath,
                                        boolean DefaultValue,
                                        boolean SetIfNotFound)
    {

        return Boolean.parseBoolean(SettingsBase.getSetting(ConfigurationFileName,
                                                            SettingPath,
                                                            Boolean.toString(DefaultValue),
                                                            SettingType.stBoolean,
                                                            SetIfNotFound));

    }

    protected static String getSetting(String ConfigurationFileName,
                                       String SettingPath,
                                       String DefaultValue,
                                       boolean SetIfNotFound)
    {

        return SettingsBase.getSetting(ConfigurationFileName, SettingPath, DefaultValue, SettingType.stString, SetIfNotFound);

    }

    protected static String getSetting(String ConfigurationFileName,
                                       String SettingPath,
                                       String DefaultValue,
                                       SettingType Type,
                                       boolean SetIfNotFound)
    {
        String value = null;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String CacheKey = SettingsBase.normalizeCacheKey(ConfigurationFileName, SettingPath);

            // Read Value From Cache
            value = SettingsBase.getCache().get(CacheKey);

            // Value Cached?
            if (value == null)
            {
                // No; User Defined Connector?
                if (_connector != null)
                {
                    // Yes; Read Configuration
                    value = _connector.getSetting(ConfigurationFileName, SettingPath, DefaultValue, Type, SetIfNotFound);

                    // Add to Cache
                    SettingsBase.getCache().put(CacheKey, value);
                }
                else
                {
                    // No; Use Default
                    value = DefaultValue;
                }

            }
        }

        return value;
    }

    protected static boolean setSetting(String ConfigurationFileName, String SettingPath, int Value)
    {

        return SettingsBase.setSetting(ConfigurationFileName, SettingPath, Integer.toString(Value), SettingType.stInteger);

    }

    protected static boolean setSetting(String ConfigurationFileName, String SettingPath, boolean Value)
    {

        return SettingsBase.setSetting(ConfigurationFileName, SettingPath, Boolean.toString(Value), SettingType.stBoolean);

    }

    protected static boolean setSetting(String ConfigurationFileName, String SettingPath, String Value)
    {

        return SettingsBase.setSetting(ConfigurationFileName, SettingPath, Value, SettingType.stString);

    }

    protected static boolean setSetting(String ConfigurationFileName, String SettingPath, String Value, SettingType Type)
    {

        boolean Updated = true;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String CacheKey = SettingsBase.normalizeCacheKey(ConfigurationFileName, SettingPath);

            // Setting Not Cached or Modified?
            if (!SettingsBase.getCache().containsKey(CacheKey) || SettingsBase.getCache().get(CacheKey) != Value)
            {

                // Yes; Replace Cached Value
                SettingsBase.getCache().put(CacheKey, Value);

                // Update Configuration File
                if (_connector != null)
                {
                    Updated = _connector.setSetting(ConfigurationFileName, SettingPath, Value, Type);
                }

            }
        }

        return Updated;

    }

    protected static String normalizeCacheKey(String ConfigurationFileName, String SettingPath)
    {

        String CacheKey = ConfigurationFileName + "." + SettingPath;
        CacheKey = CacheKey.replace("/", ".").toUpperCase();
        return CacheKey;

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
