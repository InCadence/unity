package com.incadencecorp.unity.common.tests;

import com.incadencecorp.unity.common.SettingsBase;

public final class TestSettings extends SettingsBase {

    private static final String CONFIG_NAME = "test.config";

    public static String getTestStringSetting(String variableName)
    {
        return TestSettings.getSetting(CONFIG_NAME, "/test/" + variableName, "", true);
    }

    public static boolean setTestStringSetting(String variableName, String value)
    {
        return TestSettings.setSetting(CONFIG_NAME, "/test/" + variableName, value);
    }

    public static boolean getTestBooleanSetting(String variableName)
    {
        return TestSettings.getSetting(CONFIG_NAME, "/test/" + variableName, false, true);
    }

    public static boolean setTestBooleanSetting(String variableName, boolean value)
    {
        return TestSettings.setSetting(CONFIG_NAME, "/test/" + variableName, value);
    }

    public static int getTestIntSetting(String variableName)
    {
        return TestSettings.getSetting(CONFIG_NAME, "/test/" + variableName, 10, true);
    }

    public static boolean setTestIntSetting(String variableName, int value)
    {
        return TestSettings.setSetting(CONFIG_NAME, "/test/" + variableName, value);
    }

    public static int getSettingWithMin(String variableName, int defaultValue, int minValue)
    {
        return getSettingWithMin(CONFIG_NAME, "/test/" + variableName, defaultValue, minValue, true);
    }

    public static int getSettingWithMax(String variableName, int defaultValue, int maxValue)
    {
        return getSettingWithMax(CONFIG_NAME, "/test/" + variableName, defaultValue, maxValue, true);
    }

}
