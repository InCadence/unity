package com.incadencecorp.unity.common;

import com.incadencecorp.unity.common.SettingsBase;

public final class TestSettings {

    private static final String CONFIG_NAME = "test.config";

    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INT = 10;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final double DEFAULT_DOUBLE = 0.0;
    public static final float DEFAULT_FLOAT = 1.0f;

    private static SettingsBase settings = new SettingsBase(null);

    /*--------------------------------------------------------------------------
    Initialization
    --------------------------------------------------------------------------*/

    private TestSettings() {
        // Do Nothing
    }

    /**
     * Configures the settings to use a particular connector.
     * 
     * @param connector
     */
    public static void initialize(final IConfigurationsConnector connector) {
        settings = new SettingsBase(connector);
    }

    /*--------------------------------------------------------------------------
    Settings
    --------------------------------------------------------------------------*/

    public static void clearCache() {
        settings.clearCache();
    }

    public static String getTestStringSetting(String variableName) {
        return settings.getSetting(CONFIG_NAME, "test." + variableName, DEFAULT_STRING, true);
    }

    public static boolean setTestStringSetting(String variableName, String value) {
        return settings.setSetting(CONFIG_NAME, "test." + variableName, value);
    }

    public static boolean getTestBooleanSetting(String variableName) {
        return settings.getSetting(CONFIG_NAME, "test." + variableName, DEFAULT_BOOLEAN, true);
    }

    public static boolean setTestBooleanSetting(String variableName, boolean value) {
        return settings.setSetting(CONFIG_NAME, "test." + variableName, value);
    }

    public static int getTestIntSetting(String variableName) {
        return settings.getSetting(CONFIG_NAME, "test." + variableName, DEFAULT_INT, true);
    }

    public static boolean setTestIntSetting(String variableName, int value) {
        return settings.setSetting(CONFIG_NAME, "test." + variableName, value);
    }

    public static double getTestDoubleSetting(String variableName) {
        return settings.getSetting(CONFIG_NAME, "test." + variableName, DEFAULT_DOUBLE, true);
    }

    public static boolean setTestDoubleSetting(String variableName, double value) {
        return settings.setSetting(CONFIG_NAME, "test." + variableName, value);
    }

    public static float getTestFloatSetting(String variableName) {
        return settings.getSetting(CONFIG_NAME, "test." + variableName, DEFAULT_FLOAT, true);
    }

    public static boolean setTestFloatSetting(String variableName, float value) {
        return settings.setSetting(CONFIG_NAME, "test." + variableName, value);
    }

    public static int getSettingWithMin(String variableName, int defaultValue, int minValue) {
        return settings.getSettingWithMin(CONFIG_NAME, "test." + variableName, defaultValue,
                                          minValue, true);
    }

    public static double getSettingWithMin(String variableName, double defaultValue, double minValue) {
        return settings.getSettingWithMin(CONFIG_NAME, "test." + variableName, defaultValue,
                                          minValue, true);
    }

    public static float getSettingWithMin(String variableName, float defaultValue, float minValue) {
        return settings.getSettingWithMin(CONFIG_NAME, "test." + variableName, defaultValue,
                                          minValue, true);
    }

    public static int getSettingWithMax(String variableName, int defaultValue, int maxValue) {
        return settings.getSettingWithMax(CONFIG_NAME, "test." + variableName, defaultValue,
                                          maxValue, true);
    }

    public static double getSettingWithMax(String variableName, double defaultValue, double maxValue) {
        return settings.getSettingWithMax(CONFIG_NAME, "test." + variableName, defaultValue,
                                          maxValue, true);
    }

    public static float getSettingWithMax(String variableName, float defaultValue, float maxValue) {
        return settings.getSettingWithMax(CONFIG_NAME, "test." + variableName, defaultValue,
                                          maxValue, true);
    }

}
