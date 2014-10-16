package com.incadencecorp.unity.rmi.configuration;

import java.rmi.Remote;
import java.rmi.RemoteException;

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

    public Boolean confirmDirectory(String path) throws RemoteException;
}
