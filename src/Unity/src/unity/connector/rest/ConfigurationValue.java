package unity.connector.rest;

import unity.common.SettingType;
import unity.configuration.ConfigurationFiles;
import unity.configuration.SettingTypeUtility;

public class ConfigurationValue  {

	private ConfigurationFiles _configurationFiles = new ConfigurationFiles();
	private String result;
	

	
	public ConfigurationValue() {	}
	
    public ConfigurationValue(String configurationFileNameRest,String settingPathRest,String defaultValue,String type,String setIfNotFound) {

    	//convert input
    	String configurationFileName = configurationFileNameRest.replace('-', '.');
    	String settingPath = settingPathRest.replace('-', '/');
    	SettingType settingType = SettingTypeUtility.stringToSettingType(type);
    	Boolean setIfNotFoundBool = Boolean.parseBoolean(setIfNotFound);
    	
		result = _configurationFiles.getSetting(configurationFileName, settingPath, defaultValue, settingType, setIfNotFoundBool);
	}
    
    public ConfigurationValue(String configurationFileNameRest,String settingPathRest,String value,String type) {
		
    	//convert input
    	String configurationFileName = configurationFileNameRest.replace('-', '.');
    	String settingPath = settingPathRest.replace('-', '/');
    	SettingType settingType = SettingTypeUtility.stringToSettingType(type);
    	
		_configurationFiles.setSetting(configurationFileName, settingPath, value, settingType);
		result = "true";
	}   

	public String getResult() {
		return this.result;
	}
	

}
