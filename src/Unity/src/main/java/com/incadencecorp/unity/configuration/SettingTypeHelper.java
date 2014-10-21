package com.incadencecorp.unity.configuration;

import com.incadencecorp.unity.common.SettingType;

public class SettingTypeHelper {

    public static String settingTypeToString(SettingType type)
    {
        return type.toString();
    }

    public static SettingType stringToSettingType(String type)
    {

        SettingType settingType = null;

        switch (type.toUpperCase()) {

        case "STSTRING":
            settingType = SettingType.ST_STRING;
            break;

        case "STBOOLEAN":
            settingType = SettingType.ST_BOOLEAN;
            break;

        case "STENCRYPTEDSTRING":
            settingType = SettingType.ST_ENCRYPTED_STRING;
            break;

        case "STINTEGER":
            settingType = SettingType.ST_INTEGER;
            break;

        default:
            settingType = SettingType.ST_UNKNOWN;
            break;
        }
        return settingType;
    }
}
