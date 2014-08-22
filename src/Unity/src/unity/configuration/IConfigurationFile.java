package unity.configuration;

import org.apache.commons.configuration.tree.ConfigurationNode;

public interface IConfigurationFile {

	
	public void open(String fileName);
	public abstract void save();
	public void setSetting(String settingPath, String value, SettingType type);
	public String getSetting(String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound);
	public abstract SettingType getSettingType(String settingPath);
	public void deleteSetting(String settingPath);
	public ConfigurationNode getSection(String sectionPath);
	public String[] getSectionList(String sectionPath);
	public abstract void deleteSection(String sectionPath);	
}
