package com.incadencecorp.unity.configuration;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingType;

/**
 * This class provides a static method wrapper around a
 * {@link com.incadencecorp.unity.common.IConfigurationsConnector} which
 * is used for persisting and retrieving application settings to and from config files. The static
 * methods in this class
 * allows any Unity Core users to access the methods in
 * {@link com.incadencecorp.unity.common.IConfigurationsConnector} Configuration service without
 * instantiating the inner ConfigurationsConnector directly.
 * 
 * @author InCadence
 */
public class ConfigConnector {

    /******************************
     * Private Member Variables
     ******************************/

    private static IConfigurationsConnector _connector = null;

    /*********************
     * Public Functions
     *********************/
    public static void initialize(IConfigurationsConnector connector) {
        _connector = connector;
    }

    public static String getSetting(String configurationFileName, String settingPath,
            String defaultValue, SettingType type) {
        return getSetting(configurationFileName, settingPath, defaultValue, type, false);
    }

    public static String getSetting(String configurationFileName, String settingPath,
            String defaultValue, SettingType type, Boolean setIfNotFound) {
        try {

            String value =
                    _connector.getSetting(configurationFileName, settingPath, defaultValue, type,
                                          setIfNotFound);

            // return success
            return value;

        } catch (Exception ex) {
            // Log Failed Error
            CallResult.log(CallResults.FAILED_ERROR, ex,
                           "com.incadencecorp.unity.configuration.ConfigConnector");
            return defaultValue;
        }
    }

    public static boolean setSetting(String configurationFileName, String settingPath,
            String value, SettingType type) {
        try {
            // call on the connector
            _connector.setSetting(configurationFileName, settingPath, value, type);

            return true;

        } catch (Exception ex) {
            // Log Failed Error

            CallResult.log(CallResults.FAILED_ERROR, ex,
                           "com.incadencecorp.unity.configuration.ConfigConnector");

            return false;
        }
    }

    public static boolean log(String appName, String callResultXml) {

        try {

            if (_connector.log(appName, callResultXml)) {

                return true;
            } else {

                return false;
            }
        } catch (Exception ex) {
            // Return Failed Error
            CallResult.log(CallResults.FAILED_ERROR, ex,
                           "com.incadencecorp.unity.configuration.ConfigConnector");

            return false;
        }
    }

}
