package unity.connector.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import unity.common.IConfigurationsConnector;
import unity.common.SettingType;
import unity.configuration.rmi.IRmiConfigurationFiles;
import unity.configuration.rmi.RmiConfigurationFiles;

public class RmiConfigurationsConnector implements IConfigurationsConnector {

    /****************************
     * Private Member Variables
     ***************************/
    private IRmiConfigurationFiles _configurations = null;

    /*********************
     * Constructors
     *********************/
    public RmiConfigurationsConnector(Integer port, String address)
    {
        comInitialize(port, address);
    }

    public void comInitialize(Integer port, String address)
    {

        if (address == null || address.isEmpty())
        {
            try
            {
                this._configurations = new RmiConfigurationFiles();
            }
            catch (RemoteException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {

            try
            {
                // look up the Registry where the unity service is listening to
                Registry unityRegistry = LocateRegistry.getRegistry(address);
                // get remote interface to gain access to call ConfigurationFiles methods remotely
                this._configurations = (IRmiConfigurationFiles) unityRegistry.lookup("configurations");
                System.out.println("look up success!");
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*********************
     * Public Functions
     *********************/
    // public SettingType getSettingType(String configurationFileName,String settingPath) throws RemoteException {
    // return this._configurations.getSettingType(configurationFileName, settingPath);
    // }
    //
    // public ConfigurationNode getSection(String configurationFileName,String sectionPath) {
    //
    // }
    // public String[] getSectionList(String configurationFileName,String sectionPath) {
    //
    // }

    // public void deleteSetting(String configurationFileName,String settingPath) throws RemoteException {
    // this._configurations.deleteSetting(configurationFileName, settingPath);
    // }

    // public void deleteSection(String configurationFileName,String sectionPath) throws RemoteException {
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
