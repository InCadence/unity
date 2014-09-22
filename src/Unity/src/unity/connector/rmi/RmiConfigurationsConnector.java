package unity.connector.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import unity.common.SettingType;
import unity.configuration.rmi.IRmiConfigurationFiles;
import unity.configuration.rmi.RmiConfigurationFiles;

public class RmiConfigurationsConnector {

    /****************************
     * Private Member Variables
     ***************************/
    private IRmiConfigurationFiles _configurations = null;

    /*********************
     * Constructors
     *********************/
    static
    {
        /*
         * Use .NET Remoting to access the Singleton Configurations object ' that is hosted and remoted by the
         * ConfigurationsService. We ' do this in this static block because it only needs ' to be done once
         */
        // TODO: Find java equiv
    }

    public RmiConfigurationsConnector(Integer port, String address)
    {
        comInitialize(port, address);
    }

    public RmiConfigurationsConnector()
    {
        // Default constructor to allow access from COM?
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
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             String type,
                             Boolean setIfNotFound) throws RemoteException
    {
        return this._configurations.getSetting(configurationFileName,
                                               settingPath,
                                               defaultValue,
                                               type.toString(),
                                               setIfNotFound);
    }

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

    public void setSetting(String configurationFileName, String settingPath, String value, SettingType type)
            throws RemoteException
    {
        this._configurations.setSetting(configurationFileName, settingPath, value, type.toString());
    }

    // public void deleteSetting(String configurationFileName,String settingPath) throws RemoteException {
    // this._configurations.deleteSetting(configurationFileName, settingPath);
    // }

    // public void deleteSection(String configurationFileName,String sectionPath) throws RemoteException {
    // this._configurations.deleteSection(configurationFileName, sectionPath);
    // }

    public boolean log(String logName, String callResultXml) throws RemoteException
    {
        return _configurations.log(logName, callResultXml);
    }

}
