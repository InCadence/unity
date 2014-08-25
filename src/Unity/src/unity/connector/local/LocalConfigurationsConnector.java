package unity.connector.local;

import unity.configuration.ConfigurationFiles;
import unity.configuration.IConfigurationsConnector;
import unity.configuration.SettingType;

public class LocalConfigurationsConnector implements IConfigurationsConnector{

	/****************************
	 * Private Member Variables
	 ***************************/
	private ConfigurationFiles _localConfigurations;
	
	/*********************
	 * Constructors
	 *********************/

	public LocalConfigurationsConnector() {
		//Default constructor for local use
		this._localConfigurations = new ConfigurationFiles();
	}
	
	
	/*********************
	 * Public Functions
	 *********************/
	public String getSetting(String configurationFileName, String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound) {
			return this._localConfigurations.getSetting(configurationFileName, settingPath, defaultValue, type, setIfNotFound);		
	}
	
	public SettingType getSettingType(String configurationFileName,String settingPath) {
		return this._localConfigurations.getSettingType(configurationFileName, settingPath);
	}
	/*
    public ConfigurationNode getSection(String configurationFileName,String sectionPath) {
    	
    }
	public String[] getSectionList(String configurationFileName,String sectionPath) {
		
	}*/
	
	public void setSetting(String configurationFileName,String settingPath, String value, SettingType type) {
		this._localConfigurations.setSetting(configurationFileName, settingPath, value, type);
	}
	
	public void deleteSetting(String configurationFileName,String settingPath) {
		this._localConfigurations.deleteSetting(configurationFileName, settingPath);
	}
	
	public void deleteSection(String configurationFileName,String sectionPath) {
		this._localConfigurations.deleteSection(configurationFileName, sectionPath);
	}
	
	public Boolean log(String logName, String callResultXml) {
		 return _localConfigurations.log(logName,callResultXml);
	}

}
