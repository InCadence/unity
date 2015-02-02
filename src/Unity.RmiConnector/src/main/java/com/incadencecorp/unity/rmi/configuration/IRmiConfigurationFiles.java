package com.incadencecorp.unity.rmi.configuration;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.joda.time.DateTime;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.configuration.ConfigurationFile;

public interface IRmiConfigurationFiles extends Remote {

    public String getLogLocation() throws RemoteException;

    public void setLogLocation() throws RemoteException;

    public void isServiceRunning() throws RemoteException;

    public String getBasePath() throws RemoteException;

    public void add(ConfigurationFile configFile) throws RemoteException;

    public void add(String fileName) throws RemoteException;

    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             String type,
                             Boolean setIfNotFound) throws RemoteException;

    public SettingType getSettingType(String configurationFileName, String settingPath) throws RemoteException;

    public void setSetting(String configurationFileName, String settingPath, String value, String type)
            throws RemoteException;

    public void deleteSetting(String configurationFileName, String settingPath) throws RemoteException;

    public void deleteSection(String configurationFileName, String sectionPath) throws RemoteException;

    public ConfigurationFile getConfigurationFile(String configurationFileName) throws RemoteException;

    public Boolean log(String logName, String callResultXml) throws RemoteException;

    /**
     * Returns the full list of all log entries created in this log file. It is recommended that this method be used
     * carefully due to the amount of time it may take to process as well as the amount of processing resources that could be
     * taken up. Instead, consider using <
     * 
     * @param logName the name of the log file
     * 
     * @return the full list of all log entries created in this log file
     */
    public List<CallResult> getLogs(String logName) throws RemoteException;

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
    public List<CallResult> getLogs(String logName, long maxMillisBack) throws RemoteException;

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
    public List<CallResult> getLogsAfter(String logName, DateTime afterTime, long maxMillisForward) throws RemoteException;

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
    public List<CallResult> getLogsBefore(String logName, DateTime beforeTime, long maxMillisBefore) throws RemoteException;
    
    public Boolean confirmDirectory(String path) throws RemoteException;
}
