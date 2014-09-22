package unity.configuration;

import org.apache.commons.configuration.tree.ConfigurationNode;

import unity.common.SettingType;

public interface IConfigurationFile {

    public void open(String fileName);

    public void save();

    public void setSetting(String settingPath, String value, SettingType type);

    public String getSetting(String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound);

    public SettingType getSettingType(String settingPath);

    public void deleteSetting(String settingPath);

    public ConfigurationNode getSection(String sectionPath);

    public String[] getSectionList(String sectionPath);

    public void deleteSection(String sectionPath);
}
