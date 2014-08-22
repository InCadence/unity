package unity.connector.rest;

import unity.configuration.ConfigurationFiles;
import unity.configuration.SettingType;
import unity.configuration.SettingTypeUtility;

public class SettingValue  {

	private ConfigurationFiles _configurationFiles = new ConfigurationFiles();
	private String result;
	

	
	public SettingValue() {	}
	
    public SettingValue(String configurationFileNameRest,String settingPathRest,String defaultValue,String type,String setIfNotFound) {

    	//convert input
    	String configurationFileName = configurationFileNameRest.replace('-', '.');
    	String settingPath = settingPathRest.replace('-', '/');
    	SettingType settingType = SettingTypeUtility.stringToSettingType(type);
    	Boolean setIfNotFoundBool = Boolean.parseBoolean(setIfNotFound);
    	
    	System.out.println(configurationFileName);
		System.out.println(settingPath);
		System.out.println(defaultValue);
		System.out.println(type);
		System.out.println(setIfNotFound);
    	
		this.result = _configurationFiles.getSetting(configurationFileName, settingPath, defaultValue, settingType, setIfNotFoundBool);
	}
    
    public SettingValue(String configurationFileNameRest,String settingPathRest,String value,String type) {
		
    	//convert input
    	String configurationFileName = configurationFileNameRest.replace('-', '.');
    	String settingPath = settingPathRest.replace('-', '/');
    	SettingType settingType = SettingTypeUtility.stringToSettingType(type);
    	
		_configurationFiles.setSetting(configurationFileName, settingPath, value, settingType);
		this.result = "true";
	}   

	public String getResult() {
		return this.result;
	}
	

}
