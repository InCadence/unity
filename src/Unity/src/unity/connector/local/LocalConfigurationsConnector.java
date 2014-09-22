package unity.connector.local;

import unity.common.IConfigurationsConnector;
import unity.common.SettingType;
import unity.configuration.ConfigurationFiles;

public class LocalConfigurationsConnector implements IConfigurationsConnector {

    /****************************
     * Private Member Variables
     ***************************/
    private ConfigurationFiles _localConfigurations;

    /*********************
     * Constructors
     *********************/

    public LocalConfigurationsConnector()
    {
        // Default constructor for local use
        this._localConfigurations = new ConfigurationFiles();
    }

    /*********************
     * Public Functions
     *********************/

    @Override
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {
        return this._localConfigurations.getSetting(configurationFileName, settingPath, defaultValue, type, setIfNotFound);
    }

    public SettingType getSettingType(String configurationFileName, String settingPath)
    {
        return this._localConfigurations.getSettingType(configurationFileName, settingPath);
    }

    /*
     * public ConfigurationNode getSection(String configurationFileName,String sectionPath) {
     * 
     * } public String[] getSectionList(String configurationFileName,String sectionPath) {
     * 
     * }
     */

    @Override
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        return this._localConfigurations.setSetting(configurationFileName, settingPath, value, type);
    }

    public void deleteSetting(String configurationFileName, String settingPath)
    {
        this._localConfigurations.deleteSetting(configurationFileName, settingPath);
    }

    public void deleteSection(String configurationFileName, String sectionPath)
    {
        this._localConfigurations.deleteSection(configurationFileName, sectionPath);
    }

    @Override
    public boolean log(String logName, String callResultXml)
    {
        return _localConfigurations.log(logName, callResultXml);
    }

}
