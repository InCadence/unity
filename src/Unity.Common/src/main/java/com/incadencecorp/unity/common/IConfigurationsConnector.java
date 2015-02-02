package com.incadencecorp.unity.common;

import java.util.List;

import org.joda.time.DateTime;

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
 */
public interface IConfigurationsConnector {

    String getAddress();

    int getPort();

    /**
     * Returns a setting value from the specified configuration file.
     * 
     * @param configurationFileName the name of the configuration file.
     * @param settingPath the path to the setting in the configuration file.
     * @param defaultValue the default value of the setting being retrieved.
     * @param type the setting type of the setting value being retrieved.
     * @param setIfNotFound whether to create the setting if not found.
     * @return the setting value retrieved from the configuration file.
     */
    String getSetting(String configurationFileName,
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
     * Returns <code>true</code> if the setting is set successfully to the specified configuration file; <code>false</code>
     * otherwise.
     * 
     * @param configurationFileName the name of the configuration file.
     * @param settingPath the path to the setting in the configuration file.
     * @param value the value of the setting.
     * @param type the setting type of the setting value.
     * @return <code>true</code> if the setting is set successfully to the specified configuration file; <code>false</code>
     *         otherwise.
     */
    boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type);

    // public void deleteSetting(String configurationFileName, String
    // settingPath);

    // public void deleteSection(String configurationFileName, String
    // sectionPath);

    /**
     * Returns <code>true</code> if the String value is logged successfully; <code>false</code> otherwise.
     * 
     * @param logName the name of the log file
     * @param callResultXml
     * @return the String value is logged successfully; <code>false</code> otherwise.
     */
    boolean log(String logName, String callResultXml);

    /**
     * Returns the full list of all log entries created in this log file. It is recommended that this method be used
     * carefully due to the amount of time it may take to process as well as the amount of processing resources that could be
     * taken up. Instead, consider using <
     * 
     * @param logName the name of the log file
     * 
     * @return the full list of all log entries created in this log file
     */
    List<CallResult> getLogs(String logName);

    /**
     * Returns the list of log entries created in this log file starting from the most recent and looking back the
     * <code>maxMillisBack</code> amount inclusively.
     * 
     * @param logName the name of the log file
     * @param maxTimeBack the maximum number of milliseconds to look back in the log file
     * 
     * @return the list of log entries created in this log file starting from the most recent and looking back the
     *         <code>maxMillisBack</code> amount inclusively
     */
    List<CallResult> getLogs(String logName, long maxMillisBack);

    /**
     * Returns the list of log entries created in this log file starting after the <code>afterTime</code> provided and only
     * including newer entries within <code>maxMillisForward</code> inclusively.
     * 
     * @param logName the name of the log file
     * @param afterTime the time to start collecting log entries after in the log file
     * @param maxMillisForward the maximum number of milliseconds after the <code>afterTime</code> to collect log entries for
     * 
     * @return the list of log entries
     */
    List<CallResult> getLogsAfter(String logName, DateTime afterTime, long maxMillisForward);

    /**
     * Returns the list of log entries created in this log file starting before the <code>beforeTime</code> provided and only
     * including older entries within <code>maxMillisBefore</code> inclusively.
     * 
     * @param logName the name of the log file
     * @param beforeTime the time to start collecting log entries before in the log file
     * @param maxMillisBefore the maximum number of milliseconds before the <code>beforeTime</code> to collect log entries for
     * 
     * @return the list of log entries
     */
    List<CallResult> getLogsBefore(String logName, DateTime beforeTime, long maxMillisBefore);
}
