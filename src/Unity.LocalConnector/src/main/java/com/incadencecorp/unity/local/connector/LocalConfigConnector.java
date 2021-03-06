package com.incadencecorp.unity.local.connector;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.SettingType;

public class LocalConfigConnector {

    /****************************
     * ' Class: Unity.Runtime.ConfigConnector ' Description: This class provides a shared method wrapper (singleton) ' around
     * a connection to the Unity.Configuration Windows Service, which ' is used for persisting and retrieving application
     * settings to and from ' XML config files. Because methods are shared, any Unity Core users ' can access the
     * Configuration service without instantiating the inner ' ConfigurationsConnector directly. The wrapper methods also
     * modify ' the behavior of the interface by passing and returning values by reference, ' and return a CallResult instead
     * of the value directly, for better error ' handling behavior in the calling code.
     ***************************/

    /****************************
     * Private Member Variables
     ***************************/
    private static LocalConfigurationsConnector _connector = new LocalConfigurationsConnector();

    /****************************
     * public shared methods
     ***************************/

    public static String getSetting(String configurationFileName, String settingPath, String defaultValue, SettingType type)
    {
        return getSetting(configurationFileName, settingPath, defaultValue, type, false);
    }

    public static String getSetting(String configurationFileName,
                                    String settingPath,
                                    String defaultValue,
                                    SettingType type,
                                    Boolean setIfNotFound)
    {
        try
        {
            // call on the connector, convert setting type to string because only serializable objects are allowed over RMI
            String value = _connector.getSetting(configurationFileName, settingPath, defaultValue, type, setIfNotFound);

            // return success
            return value;

        }
        catch (Exception ex)
        {
            // Return Failed Error
            new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");
            return defaultValue;
        }
    }

    public static CallResult getSettingType(String configurationFileName, String settingPath)
    {
        try
        {
            // call on the connector
            SettingType type = _connector.getSettingType(configurationFileName, settingPath);

            // return success
            return new CallResult(CallResults.SUCCESS, (Object) type);

        }
        catch (Exception ex)
        {
            // Return Failed Error
            return new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");
        }
    }

    /*
     * public static CallResult getSection(String configurationFileName, String sectionPath, String section) { try { section
     * = _connector.getSection(configurationFileName, sectionPath);
     * 
     * //did we get back xml? if (section =="") { //no, return failed return new CallResult(CallResults.FAILED,
     * "Section not found", "Unity.Runtime.ConfigConnector") } else { //return success return CallResult.successCallResult; }
     * } catch (Exception ex) { // Return Failed Error return new CallResult(CallResults.FAILED_ERROR, ex,
     * "Unity.Runtime.ConfigConnector"); } }
     * 
     * 
     * public static CallResult getSectionList(String configurationFileName, String sectionPath, String[] sectionList) { try
     * { // call on connector sectionList = _connector.getSectionList(configurationFileName, sectionPath); //return success
     * return CallResult.successCallResult; } catch (Exception ex) { // Return Failed Error return new
     * CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector"); } }
     */

    public static boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        try
        {
            // call on the connector
            _connector.setSetting(configurationFileName, settingPath, value, type);

            return true;

        }
        catch (Exception ex)
        {
            // Log Failed Error
            CallResult.log(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");

            return false;

        }
    }

    public static CallResult deleteSetting(String configurationFileName, String settingPath)
    {
        try
        {
            // call on the connector
            _connector.deleteSetting(configurationFileName, settingPath);
            // return success
            return new CallResult(CallResults.SUCCESS);
        }
        catch (Exception ex)
        {
            // Return Failed Error
            return new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");
        }
    }

    public static CallResult deleteSection(String configurationFileName, String sectionPath)
    {
        try
        {
            // call on the connector
            _connector.deleteSection(configurationFileName, sectionPath);
            // return success
            return new CallResult(CallResults.SUCCESS);
        }
        catch (Exception ex)
        {
            // Return Failed Error
            return new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");
        }
    }

    public static void log(String appName, String callResultXml)
    {

        try
        {

            _connector.log(appName, callResultXml);

        }
        catch (Exception ex)
        {

            // log error
            new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");
        }
    }

}
