package com.incadencecorp.unity.configuration;

import com.incadencecorp.unity.common.SettingType;

/**
 * Wrapper class that wraps {@link com.incadencecorp.unity.configuration.ConfigurationFiles}. 
 * 
 * @author InCadence
 *
 */
public class ConfigurationValue {

    private ConfigurationFiles _configurationFiles = new ConfigurationFiles();
    private String _result;

    /**
     * Constructs a ConfigurationValue object.
     */
    public ConfigurationValue()
    {
    }

    /**
     * Constructs a ConfigurationValue object with. 
     */
    public ConfigurationValue(String configurationFileNameRest,
                              String settingPathRest,
                              String defaultValue,
                              String type,
                              String setIfNotFound)
    {

        // convert input
        String configurationFileName = configurationFileNameRest.replace('-', '.');
        String settingPath = settingPathRest.replace('-', '/');
        SettingType settingType = SettingTypeHelper.stringToSettingType(type);
        Boolean setIfNotFoundBool = Boolean.parseBoolean(setIfNotFound);

        _result = _configurationFiles.getSetting(configurationFileName,
                                                settingPath,
                                                defaultValue,
                                                settingType,
                                                setIfNotFoundBool);
    }

    public ConfigurationValue(String configurationFileNameRest, String settingPathRest, String value, String type)
    {

        // convert input
        String configurationFileName = configurationFileNameRest.replace('-', '.');
        String settingPath = settingPathRest.replace('-', '/');
        SettingType settingType = SettingTypeHelper.stringToSettingType(type);

        _configurationFiles.setSetting(configurationFileName, settingPath, value, settingType);
        _result = "true";
    }

    public String getResult()
    {
        return this._result;
    }

}
