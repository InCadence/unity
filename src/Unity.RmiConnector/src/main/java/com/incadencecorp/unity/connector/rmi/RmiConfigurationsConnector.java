package com.incadencecorp.unity.connector.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.configuration.rmi.IRmiConfigurationFiles;
import com.incadencecorp.unity.configuration.rmi.RmiConfigurationFiles;

public class RmiConfigurationsConnector implements IConfigurationsConnector {

    /****************************
     * Private Member Variables
     ***************************/
    private IRmiConfigurationFiles _configurations = null;

    /*********************
     * Constructors
     *********************/
    public RmiConfigurationsConnector(String address, Integer port)
    {
        try
        {
            comInitialize(port, address);
        }
        catch (RemoteException | NotBoundException e)
        {
            CallResult.log(CallResults.DEBUG, e, this);
        }
    }

    public void comInitialize(Integer port, String address) throws RemoteException, NotBoundException
    {

        if (address == null || address.isEmpty())
        {
            this._configurations = new RmiConfigurationFiles();
        }
        else
        {
            // look up the Registry where the unity service is listening to
            Registry unityRegistry = LocateRegistry.getRegistry(address);
            // get remote interface to gain access to call ConfigurationFiles methods remotely
            this._configurations = (IRmiConfigurationFiles) unityRegistry.lookup("configurations");
        }
    }

    /*********************
     * Public Functions
     *********************/
    // public SettingType getSettingType(String configurationFileName,String
    // settingPath) throws RemoteException {
    // return this._configurations.getSettingType(configurationFileName,
    // settingPath);
    // }
    //
    // public ConfigurationNode getSection(String configurationFileName,String
    // sectionPath) {
    //
    // }
    // public String[] getSectionList(String configurationFileName,String
    // sectionPath) {
    //
    // }

    // public void deleteSetting(String configurationFileName,String
    // settingPath) throws RemoteException {
    // this._configurations.deleteSetting(configurationFileName, settingPath);
    // }

    // public void deleteSection(String configurationFileName,String
    // sectionPath) throws RemoteException {
    // this._configurations.deleteSection(configurationFileName, sectionPath);
    // }

    @Override
    public boolean log(String logName, String callResultXml)
    {
        try
        {
            return _configurations.log(logName, callResultXml);
        }
        catch (RemoteException e)
        {
            return false;
        }
    }

    @Override
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {
        try
        {
            return this._configurations.getSetting(configurationFileName,
                                                   settingPath,
                                                   defaultValue,
                                                   type.toString(),
                                                   setIfNotFound);
        }
        catch (RemoteException e)
        {
            return null;
        }
    }

    @Override
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        try
        {
            this._configurations.setSetting(configurationFileName, settingPath, value, type.toString());
            return true;
        }
        catch (RemoteException e)
        {
            return false;
        }
    }

}
