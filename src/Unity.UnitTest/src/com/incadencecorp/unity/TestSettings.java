package com.incadencecorp.unity;

import com.incadencecorp.unity.common.SettingsBase;

public final class TestSettings extends SettingsBase {

    private static final String ConfigurationFileName = "test.config";

    public static String getTestStringSetting(String variableName)
    {
        return TestSettings.getSetting(ConfigurationFileName, "/test/" + variableName, "", true);
    }

    public static boolean setTestStringSetting(String variableName, String value)
    {
        return TestSettings.setSetting(ConfigurationFileName, "/test/" + variableName, value);
    }

    public static boolean getTestBooleanSetting(String variableName)
    {
        return TestSettings.getSetting(ConfigurationFileName, "/test/" + variableName, false, true);
    }

    public static boolean setTestBooleanSetting(String variableName, boolean value)
    {
        return TestSettings.setSetting(ConfigurationFileName, "/test/" + variableName, value);
    }

    public static int getTestIntSetting(String variableName)
    {
        return TestSettings.getSetting(ConfigurationFileName, "/test/" + variableName, 10, true);
    }

    public static boolean setTestIntSetting(String variableName, int value)
    {
        return TestSettings.setSetting(ConfigurationFileName, "/test/" + variableName, value);
    }

    public static int getSettingWithMin(String variableName, int defaultValue, int minValue)
    {
        return getSettingWithMin(ConfigurationFileName, "/test/" + variableName, defaultValue, minValue, true);
    }

    public static int getSettingWithMax(String variableName, int defaultValue, int maxValue)
    {
        return getSettingWithMax(ConfigurationFileName, "/test/" + variableName, defaultValue, maxValue, true);
    }

}
