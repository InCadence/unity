package com.incadencecorp.unity.common;


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

/**
 * Provides methods for Setting and retrieving configuration settings and logging.
 * 
 * @author InCadence
 *
 */
public interface IConfigurationsConnector {

    /**
     * Returns a setting value from the specified configuration file
     * 
     * @param configurationFileName the name of the configuration file
     * @param settingPath the path to the setting in the configuration file
     * @param defaultValue the default value of the setting being retrieved
     * @param type the setting type of the setting value being retrieved
     * @param setIfNotFound whether to create the setting if not found
     * @return the setting value retrieved from the configuration file
     */
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound);

    // public SettingType getSettingType(String configurationFileName,String
    // settingPath);

    // public ConfigurationNode getSection(String configurationFileName,String
    // sectionPath); public String[] getSectionList(String
    // configurationFileName,String sectionPath);

    /**
     * Returns <code>true</code> if the setting is set successfully to the specified configuration file; <code>false</code> otherwise
     * 
     * @param configurationFileName the name of the configuration file
     * @param settingPath the path to the setting in the configuration file
     * @param value the value of the setting
     * @param type the setting type of the setting value
     * @return <code>true</code> if the setting is set successfully to the specified configuration file; <code>false</code> otherwise
     */
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type);

    // public void deleteSetting(String configurationFileName, String
    // settingPath);

    // public void deleteSection(String configurationFileName, String
    // sectionPath);
    
    /**
     * Returns <code>true</code> if the String value is logged successfully; <code>false</code> otherwise
     * 
     * @param logName
     * @param callResultXml
     * @return the String value is logged successfully; <code>false</code> otherwise
     */
    public boolean log(String logName, String callResultXml);

}