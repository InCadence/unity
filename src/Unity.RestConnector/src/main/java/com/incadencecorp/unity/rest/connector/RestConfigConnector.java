package com.incadencecorp.unity.rest.connector;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.SettingType;

public class RestConfigConnector {

    /******************************
     * Private Member Variables
     ******************************/

    private static RestConfigurationsConnector _connector = null;

    /*********************
     * Public Functions
     *********************/

    public static void initialize(String address, int port)
    {

        _connector = new RestConfigurationsConnector(address, port);

    }

    public static String getAddress()
    {
        return _connector.getAddress();
    }

    public static int getPort()
    {
        return _connector.getPort();
    }

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

            String value = _connector.getSetting(configurationFileName, settingPath, defaultValue, type, setIfNotFound);

            // return success
            return value;

        }
        catch (Exception ex)
        {
            // Log Failed Error
            CallResult.log(CallResults.FAILED_ERROR, ex, "Unity.Connector.Rest.RestConfigConnector");
            return defaultValue;
        }
    }

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

            CallResult.log(CallResults.FAILED_ERROR, ex, "Unity.Connector.Rest.RestConfigConnector");

            return false;
        }
    }

    // public static CallResult getSettingType(String configurationFileName,String settingPath) {
    // try {
    // // call on the connector
    // SettingType type = _connector.getSettingType(configurationFileName, settingPath);
    //
    // // return success
    // return new CallResult(CallResults.SUCCESS,(Object)type);
    //
    // } catch (Exception ex) {
    // // Return Failed Error
    // return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
    // }
    // }

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
    // public static CallResult deleteSetting(String configurationFileName,
    // String settingPath) {
    // try {
    // // call on the connector
    // _connector.deleteSetting(configurationFileName, settingPath);
    // // return success
    // return new CallResult(CallResults.SUCCESS);
    // } catch (Exception ex) {
    // // Return Failed Error
    // return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
    // }
    // }

    // public static CallResult deleteSection(String configurationFileName,
    // String sectionPath) {
    // try {
    // // call on the connector
    // _connector.deleteSection(configurationFileName, sectionPath);
    // // return success
    // return new CallResult(CallResults.SUCCESS);
    // } catch (Exception ex) {
    // // Return Failed Error
    // return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
    // }
    // }

    public static boolean log(String AppName, String callResultXml)
    {

        try
        {

            if (_connector.log(AppName, callResultXml))
            {

                return true;
            }
            else
            {

                return false;
            }
        }
        catch (Exception ex)
        {
            // Return Failed Error
            CallResult.log(CallResults.FAILED_ERROR, ex, "Unity.Runtime.ConfigConnector");

            return false;
        }
    }
}
