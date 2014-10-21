package com.incadencecorp.unity.configuration;

import org.apache.commons.configuration.tree.ConfigurationNode;

import com.incadencecorp.unity.common.SettingType;

/**
 * The main Configuration File interface
 * 
 * This interface allows accessing and manipulating settings in a file used to store configuration settings.
 * 
 * @author InCadence
 *
 */
public interface IConfigurationFile {

    /**
     * Opens a new configuration file from disk for the given Fully Qualified Filename. If the given Fully Qualified Filename
     * does not exist, a new configuration file will be created with the Fully Qualified Filename.
     * 
     * @param fileName the Fully Qualified Filename
     */
    public void open(String fileName);

    /**
     * Persists the opened configuration file from memory to file.
     */
    public void save();

    /**
     * Sets or adds a setting in the opened configuration file
     * 
     * @param settingPath the setting path separated by / or . characters
     * @param value the setting value to be set
     * @param type the {@link com.incadencecorp.unity.common.SettingType} for the Setting Value
     */
    public void setSetting(String settingPath, String value, SettingType type);

    /**
     * Retrieves or adds a setting in the opened configuration file. The defaultValue will be returned if the setting does not exist
     * in the configuration file
     * 
     * @param settingPath the setting path separated by / or . characters
     * @param defaultValue the Default Setting Value
     * @param type the {@link com.incadencecorp.unity.common.SettingType} for the Setting Value
     * @param setIfNotFound whether the setting should be added if not found
     * @return the Setting Value for the given setting path
     */
    public String getSetting(String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound);

    /**
     * Retrieves the {@link com.incadencecorp.unity.common.SettingType} for the given setting path in the opened configuration file
     * 
     * @param settingPath
     * @return the SettingType for the given setting path
     */
    public SettingType getSettingType(String settingPath);

    /**
     * Deletes the setting in the opened configuration file
     * 
     * @param settingPath the setting path separated by / or . characters
     */
    public void deleteSetting(String settingPath);

    /**
     * Retrieves the Configuration Node for a given section path in the opened configuration file
     * 
     * @param sectionPath the section path separated by / or . characters
     * @return the Configuration Node for a given section path
     */
    public ConfigurationNode getSection(String sectionPath);

    /**
     * Retrieves the child sections from the given section path in the opened configuration file
     * 
     * @param sectionPath the parent section path separated by / or . characters
     * @return an array of child sections from the given section path
     */
    public String[] getSectionList(String sectionPath);

    /**
     * Deletes the section in the opened configuration file
     * 
     * @param sectionPath the section path separated by / or . characters
     */
    public void deleteSection(String sectionPath);
    
    /**
     * Returns the Fully Qualified Filename of the opened configuration file
     * 
     */
    public String getFileName();
    
    /**
     * Sets the Fully Qualified Filename of the opened configuration file
     * 
     * @param fileName the new Fully Qualified Filename of the opened configuration file
     */
    public void setFileName(String fileName);
}
