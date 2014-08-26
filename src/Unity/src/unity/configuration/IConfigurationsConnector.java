package unity.configuration;

public interface IConfigurationsConnector {

	public String getSetting(String configurationFileName, String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound);

	//public SettingType getSettingType(String configurationFileName,String settingPath);

	
	//public ConfigurationNode getSection(String configurationFileName,String sectionPath); public String[] getSectionList(String configurationFileName,String sectionPath);
	 

	public boolean setSetting(String configurationFileName, String settingPath,String value, SettingType type);

	//public void deleteSetting(String configurationFileName, String settingPath);

	//public void deleteSection(String configurationFileName, String sectionPath);

	public boolean log(String logName, String callResultXml);

}
