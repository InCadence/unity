package com.incadencecorp.unity.configuration;

import com.incadencecorp.unity.common.SettingType;

public class SettingTypeUtility {

    public static String settingTypeToString(SettingType type)
    {
        return type.toString();
    }

    public static SettingType stringToSettingType(String type)
    {

        SettingType settingType = null;

        switch (type.toUpperCase()) {

        case "STSTRING":
            settingType = SettingType.stString;
            break;

        case "STBOOLEAN":
            settingType = SettingType.stBoolean;
            break;

        case "STENCRYPTEDSTRING":
            settingType = SettingType.stEncryptedString;
            break;

        case "STINTEGER":
            settingType = SettingType.stInteger;
            break;

        default:
            settingType = SettingType.stUnknown;
            break;
        }
        return settingType;
    }
}
