package com.incadencecorp.unity.configuration;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.logger.log4j.CallResultLogger;

public class ConfigurationFiles {

    /****************************
     * Private Member Variables
     ***************************/

    private Hashtable<String, ConfigurationFile> _configurationFiles;

    private String _logLocation;

    /*********************
     * Public Properties
     *********************/

    public String getLogLocation()
    {

        if (this._logLocation == null && this._logLocation.isEmpty())
        {
            File basePath = new File(getBasePath());
            File LogDirectory = new File(basePath, "logs");
            this._logLocation = this.getSetting("Unity.config",
                                                "Unity/Settings/LogLocation",
                                                LogDirectory.getAbsolutePath(),
                                                SettingType.stString,
                                                true);
        }
        return this._logLocation;
    }

    public void setLogLocation()
    {

        File userDir = new File(System.getProperty("user.dir"));
        File logDir = new File(userDir, "logs");
        this._logLocation = logDir.getAbsolutePath();
    }

    /*********************
     * Constructors
     *********************/

    public ConfigurationFiles()
    {

        _configurationFiles = new Hashtable<String, ConfigurationFile>();

        // set log location
        this.setLogLocation();
    }

    /*********************
     * Public Methods
     *********************/
    public void isServiceRunning()
    {

    }

    public String getBasePath()
    {

        File userDir = new File(System.getProperty("user.dir"));
        return userDir.getAbsolutePath();
    }

    public void add(ConfigurationFile configFile)
    {

        File file = new File(configFile.getFileName());
        if (file.exists() && !file.isDirectory())
        {
            // Add it to the hashtable

            _configurationFiles.put(file.getName(), configFile);

        }
    }

    public void add(String fileName)
    {
        // Create the configuration file object
        ConfigurationFile configFile = new ConfigurationFile(fileName);
        // Call on the other Add method to add the config file.
        this.add(configFile);
    }

    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {
        ConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        if (configFile != null)
        {
            // Retrieve the Setting
            return configFile.getSetting(settingPath, defaultValue, type, setIfNotFound);
        }
        else
        {
            // Return the Default Value
            return defaultValue;
        }
    }

    public SettingType getSettingType(String configurationFileName, String settingPath)
    {
        ConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // Retrieve the setting type
        if (configFile != null)
        {
            return configFile.getSettingType(settingPath);
        }
        else
        {
            return SettingType.stUnknown;
        }
    }

    /*
     * public String getSection(String configurationFileName, String sectionPath) {
     * 
     * }
     * 
     * public String[] getSectionList(String configurationFileName, String sectionPath) { //TODO: do this return null; }
     */

    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        ConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // set the setting
        if (configFile != null)
        {
            configFile.setSetting(settingPath, value, type);
        }

        return true;
    }

    public void deleteSetting(String configurationFileName, String settingPath)
    {
        ConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // delete the setting
        if (configFile != null)
        {
            configFile.deleteSetting(settingPath);
        }
    }

    public void deleteSection(String configurationFileName, String sectionPath)
    {
        ConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // delete the section
        if (configFile != null)
        {
            configFile.deleteSection(sectionPath);
        }
    }

    public ConfigurationFile getConfigurationFile(String configurationFileName)
    {
        // Check to see if our collection of ConfigurationFiles contains the Named file.
        if (!_configurationFiles.containsKey(configurationFileName))
        {
            // add the requested config file

            File directoryLocation = new File(this.getBasePath());
            File subdirectoryLocation = new File(directoryLocation, "config");
            File configFile = new File(subdirectoryLocation, configurationFileName);
            this.add(configFile.getAbsolutePath());
        }
        // Check again, it should be found. If not, then we can't do anything.
        if (_configurationFiles.containsKey(configurationFileName))
        {
            return _configurationFiles.get(configurationFileName);
        }
        else
        {
            return null;
        }
    }

    public Boolean log(String logName, String callResultXml)
    {

        try
        {
            // We log to locations under the Unity's logs folder. Each application
            // gets its own subfolder automatically.

            String logPath;
            String logFileName;

            // get current date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date tempDate = new Date();
            String currentDate = dateFormat.format(tempDate);

            // Create the App Log Path and the Log Filename
            File logPathFile = new File(this._logLocation, logName);
            logPath = logPathFile.getPath();

            File logFileNameFile = new File(logPath, logName + "_" + currentDate + ".log");
            logFileName = logFileNameFile.getCanonicalPath();

            // Confirm Directory
            if (confirmDirectory(logPath))
            {

                // Does the file already exist?
                if (logFileNameFile.exists())
                {
                    // Remove the <?xml... from the Xml, so that the Xml in
                    // the file is well formed. Only the first entry should
                    // have have the <?xml...

                    Integer pos = callResultXml.indexOf('\n') - 1;
                    Integer lastIndex = callResultXml.length() - 1;
                    if (pos > 0)
                    {
                        // pos=0;
                        callResultXml = callResultXml.substring(pos, lastIndex);
                    }

                }

                // Append to Log File
                CallResultLogger.toXmlFileLog(logFileName, callResultXml);
                return true;
            }
            else
            {
                // Failed to Create Directory; Return False
                return false;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;

        }
    }

    public Boolean confirmDirectory(String path)
    {

        try
        {
            // Note; we don't use CallResult objects for this function since
            // we're using it from within CallResults; we just use the
            // CallResults enumeration value as a return value.
            File filepath = new File(path);
            if (filepath.exists())
            {

                // It exists; Return Success
                return true;

            }
            else
            {
                // It doesn't exist; Create
                filepath.mkdirs();

                // Check Again
                if (filepath.exists())
                {

                    // Now it exists; Return Success
                    return true;

                }
                else
                {
                    // Still doesn't exist; Return Failed
                    return false;
                }
            }

        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
