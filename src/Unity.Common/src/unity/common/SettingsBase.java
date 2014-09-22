package unity.common;

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

public class SettingsBase {

    /*--------------------------------------------------------------------------
    	Private Member Variables
    --------------------------------------------------------------------------*/

    private static Hashtable<String, String> _Cache = null;
    private static IConfigurationsConnector _Connector = null;

    private static Object _cacheLock = new Object();

    /*--------------------------------------------------------------------------
    	Public Functions
    --------------------------------------------------------------------------*/

    public static void Initialize(IConfigurationsConnector connector)
    {
        SettingsBase._Connector = connector;
    }

    public static void ClearCache()
    {
        synchronized (_cacheLock)
        {
            SettingsBase.GetCache().clear();
        }
    }

    /*--------------------------------------------------------------------------
    	Protected Functions
    --------------------------------------------------------------------------*/

    protected static int GetSettingWithMin(String ConfigurationFileName,
                                           String SettingPath,
                                           int DefaultValue,
                                           int MinValue,
                                           boolean SetIfNotFound)
    {

        // Ensure Default Value Meets Requirement
        if (DefaultValue < MinValue) DefaultValue = MinValue;

        // Get Setting
        int value = SettingsBase.GetSetting(ConfigurationFileName, SettingPath, DefaultValue, SetIfNotFound);

        // Ensure Setting Meets Requirement
        if (value < MinValue) value = MinValue;

        return value;

    }

    protected static int GetSettingWithMax(String ConfigurationFileName,
                                           String SettingPath,
                                           int DefaultValue,
                                           int MaxValue,
                                           boolean SetIfNotFound)
    {

        // Ensure Default Value Meets Requirement
        if (DefaultValue > MaxValue) DefaultValue = MaxValue;

        // Get Setting
        int value = SettingsBase.GetSetting(ConfigurationFileName, SettingPath, DefaultValue, SetIfNotFound);

        // Ensure Setting Meets Requirement
        if (value > MaxValue) value = MaxValue;

        return value;

    }

    protected static int GetSetting(String ConfigurationFileName, String SettingPath, int DefaultValue, boolean SetIfNotFound)
    {

        return Integer.parseInt(SettingsBase.GetSetting(ConfigurationFileName,
                                                        SettingPath,
                                                        Integer.toString(DefaultValue),
                                                        SettingType.stInteger,
                                                        SetIfNotFound));

    }

    protected static boolean GetSetting(String ConfigurationFileName,
                                        String SettingPath,
                                        boolean DefaultValue,
                                        boolean SetIfNotFound)
    {

        return Boolean.parseBoolean(SettingsBase.GetSetting(ConfigurationFileName,
                                                            SettingPath,
                                                            Boolean.toString(DefaultValue),
                                                            SettingType.stBoolean,
                                                            SetIfNotFound));

    }

    protected static String GetSetting(String ConfigurationFileName,
                                       String SettingPath,
                                       String DefaultValue,
                                       boolean SetIfNotFound)
    {

        return SettingsBase.GetSetting(ConfigurationFileName, SettingPath, DefaultValue, SettingType.stString, SetIfNotFound);

    }

    protected static String GetSetting(String ConfigurationFileName,
                                       String SettingPath,
                                       String DefaultValue,
                                       SettingType Type,
                                       boolean SetIfNotFound)
    {
        String value = null;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String CacheKey = SettingsBase.NormalizeCacheKey(ConfigurationFileName, SettingPath);

            // Read Value From Cache
            value = SettingsBase.GetCache().get(CacheKey);

            // Value Cached?
            if (value == null)
            {
                // No; User Defined Connector?
                if (_Connector != null)
                {
                    // Yes; Read Configuration
                    value = _Connector.getSetting(ConfigurationFileName, SettingPath, DefaultValue, Type, SetIfNotFound);

                    // Add to Cache
                    SettingsBase.GetCache().put(CacheKey, value);
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

    protected static boolean SetSetting(String ConfigurationFileName, String SettingPath, int Value)
    {

        return SettingsBase.SetSetting(ConfigurationFileName, SettingPath, Integer.toString(Value), SettingType.stInteger);

    }

    protected static boolean SetSetting(String ConfigurationFileName, String SettingPath, boolean Value)
    {

        return SettingsBase.SetSetting(ConfigurationFileName, SettingPath, Boolean.toString(Value), SettingType.stBoolean);

    }

    protected static boolean SetSetting(String ConfigurationFileName, String SettingPath, String Value)
    {

        return SettingsBase.SetSetting(ConfigurationFileName, SettingPath, Value, SettingType.stString);

    }

    protected static boolean SetSetting(String ConfigurationFileName, String SettingPath, String Value, SettingType Type)
    {

        boolean Updated = true;

        synchronized (_cacheLock)
        {
            // Normalize Key
            String CacheKey = SettingsBase.NormalizeCacheKey(ConfigurationFileName, SettingPath);

            // Setting Not Cached or Modified?
            if (!SettingsBase.GetCache().containsKey(CacheKey) || SettingsBase.GetCache().get(CacheKey) != Value)
            {

                // Yes; Replace Cached Value
                SettingsBase.GetCache().put(CacheKey, Value);

                // Update Configuration File
                if (_Connector != null)
                {
                    Updated = _Connector.setSetting(ConfigurationFileName, SettingPath, Value, Type);
                }

            }
        }

        return Updated;

    }

    protected static String NormalizeCacheKey(String ConfigurationFileName, String SettingPath)
    {

        String CacheKey = ConfigurationFileName + "." + SettingPath;
        CacheKey = CacheKey.replace("/", ".").toUpperCase();
        return CacheKey;

    }

    private static Hashtable<String, String> GetCache()
    {

        // Cache Initialized?
        if (SettingsBase._Cache == null)
        {

            // No; Initialize
            SettingsBase._Cache = new Hashtable<String, String>();

        }

        return SettingsBase._Cache;

    }

}
