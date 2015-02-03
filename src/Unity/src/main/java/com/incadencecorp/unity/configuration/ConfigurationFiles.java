package com.incadencecorp.unity.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.logger.log4j.CallResultLogger;

/**
 * A Wrapper that allows accessing and manipulating multiple configuration file objects that implements the
 * {@link com.incadencecorp.unity.configuration.IConfigurationFile} interface.
 * 
 * @author InCadence
 *
 */
public class ConfigurationFiles {

    private class CallResultComparator implements Comparator<CallResult> {

        @Override
        public int compare(CallResult o1, CallResult o2)
        {
            return (o1.getDateTimeGMT().compareTo(o2.getDateTimeGMT()));
        }
    }

    /****************************
     * Private Member Variables
     ***************************/

    private Hashtable<String, IConfigurationFile> _configurationFiles;

    private String _logLocation;

    /*********************
     * Public Properties
     *********************/

    /**
     * Returns the full path where the log is located.
     * 
     * @return the full path where the log is located
     */
    public String getLogLocation()
    {

        if (this._logLocation == null && this._logLocation.isEmpty())
        {
            File basePath = new File(getBasePath());
            File logDirectory = new File(basePath, "logs");
            this._logLocation = this.getSetting("Unity.config",
                                                "Unity/Settings/LogLocation",
                                                logDirectory.getAbsolutePath(),
                                                SettingType.ST_STRING,
                                                true);
        }
        return this._logLocation;
    }

    protected void setLogLocation()
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
        _configurationFiles = new Hashtable<String, IConfigurationFile>();

        // set log location
        this.setLogLocation();
    }

    /*********************
     * Private Methods
     *********************/

    private String getBasePath()
    {

        File userDir = new File(System.getProperty("user.dir"));
        return userDir.getAbsolutePath();
    }

    /*********************
     * Public Methods
     *********************/

    /**
     * Adds a configuration file object.
     * 
     * @param configFile the configuration file object
     */
    public void add(IConfigurationFile configFile)
    {

        File file = new File(configFile.getFileName());
        if (file.exists() && !file.isDirectory())
        {
            // Add it to the hashtable

            _configurationFiles.put(file.getName(), configFile);

        }
    }

    /**
     * Creates and adds a {@link com.incadencecorp.unity.common.SettingType} object from the Fully Qualified Filename.
     * 
     * @param fileName the Fully Qualified Filename
     */
    public void add(String fileName)
    {
        // Create the configuration file object
        ConfigurationFile configFile = new ConfigurationFile(fileName);
        // Call on the other Add method to add the config file.
        this.add(configFile);
    }

    /**
     * Returns the setting value from the specified configuration file and setting. The defaultValue will be returned if the
     * setting does not exist in the configuration file. If the specified configuration file is not in the cache, it will be
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @param settingPath the setting path separated by / or . characters
     * @param defaultValue the Default Setting Value
     * @param type the {@link com.incadencecorp.unity.common.SettingType} for the Setting Value
     * @param setIfNotFound whether the setting should be added if not found
     * @return the Setting Value for the specified configuration file and setting path
     */
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType type,
                             Boolean setIfNotFound)
    {
        IConfigurationFile configFile = null;
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

    /**
     * Retrieves the {@link com.incadencecorp.unity.common.SettingType} for the specified configuration file and setting
     * path.
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @param settingPath the setting path separated by / or . characters
     * @return the SettingType for the given setting path
     */
    public SettingType getSettingType(String configurationFileName, String settingPath)
    {
        IConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // Retrieve the setting type
        if (configFile != null)
        {
            return configFile.getSettingType(settingPath);
        }
        else
        {
            return SettingType.ST_UNKNOWN;
        }
    }

    /*
     * public String getSection(String configurationFileName, String sectionPath) {
     * 
     * }
     * 
     * public String[] getSectionList(String configurationFileName, String sectionPath) { //TODO: do this return null; }
     */

    /**
     * Sets or adds a setting in the specified configuration file.
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @param settingPath the setting path separated by / or . characters
     * @param value the setting value to be set
     * @param type the {@link com.incadencecorp.unity.common.SettingType} for the setting value
     * @return true if the setting was set successfully
     */
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType type)
    {
        IConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // set the setting
        if (configFile != null)
        {
            configFile.setSetting(settingPath, value, type);
        }

        return true;
    }

    /**
     * Deletes the setting in the specified configuration file.
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @param settingPath the setting path separated by / or . characters
     */
    public void deleteSetting(String configurationFileName, String settingPath)
    {
        IConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // delete the setting
        if (configFile != null)
        {
            configFile.deleteSetting(settingPath);
        }
    }

    /**
     * Deletes the section in the specified configuration file.
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @param sectionPath the section path separated by / or . characters
     */
    public void deleteSection(String configurationFileName, String sectionPath)
    {
        IConfigurationFile configFile = null;
        // Access the ConfigurationFile object
        configFile = getConfigurationFile(configurationFileName);

        // delete the section
        if (configFile != null)
        {
            configFile.deleteSection(sectionPath);
        }
    }

    /**
     * Returns the specified configuration file object from the cache. If the specified configuration file object is not
     * found in the cache, a new configuration file object will be created from the specified configuration file name.
     * 
     * @param configurationFileName the name of the configuration file, i.e. the last name of the file's pathname's name
     *            sequence
     * @return the specified configuration file object
     */
    public IConfigurationFile getConfigurationFile(String configurationFileName)
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

    /**
     * Persists the XML generated by {@link com.incadencecorp.unity.common.CallResult} to a log file.
     * 
     * @param logName the name of the log without the .log extension
     * @param callResultXml the xml generated from a callResult
     * @return true if the logging call is successful
     */
    public Boolean log(String logName, String callResultXml)
    {

        try
        {
            // We log to locations under the Unity's logs folder. Each application
            // gets its own subfolder automatically.

            // get current date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date tempDate = new Date();
            String currentDate = dateFormat.format(tempDate);

            // Create the App Log Path and the Log Filename
            File logPathFolder = new File(this._logLocation, logName);
            String logPath = logPathFolder.getPath();

            File logFileNameFile = new File(logPath, logName + "_" + currentDate + ".log");
            String logFileName = logFileNameFile.getCanonicalPath();

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

    /**
     * Returns the full list of all log entries created in this log file. It is recommended that this method be used
     * carefully due to the amount of time it may take to process as well as the amount of processing resources that could be
     * taken up. Instead, consider using <
     * 
     * @param logName the name of the log file
     * 
     * @return the full list of all log entries created in this log file
     */
    public List<CallResult> getLogs(String logName)
    {
        return getLogs(logName, null);
    }

    /**
     * Returns the list of log entries created in this log file starting from the most recent and looking back the
     * <code>maxMillisBack</code> amount inclusively.
     * 
     * @param logName the name of the log file
     * @param maxMillisBefore the maximum number of milliseconds to look back in the log file
     * 
     * @return the list of log entries created in this log file starting from the most recent and looking back the
     *         <code>maxMillisBack</code> amount inclusively
     */
    public List<CallResult> getLogs(String logName, Long maxMillisBefore)
    {

        return getLogsBefore(logName, null, maxMillisBefore);
    }

    /**
     * Returns the list of log entries created in this log file starting after the <code>afterTime</code> provided and only
     * including newer entries within <code>maxMillisForward</code> inclusively.
     * 
     * @param logName the name of the log file
     * @param afterTime the time to start collecting log entries after in the log file
     * @param maxMillisForward the maximum number of milliseconds after the <code>afterTime</code> to collect log entries for
     * 
     * @return the list of log entries
     */
    public List<CallResult> getLogsAfter(String logName, DateTime afterTime, long maxMillisForward)
    {
        List<File> sortedFiles = getSortedLogFiles(logName);

        // Start at the beginning and work backwards
        List<CallResult> logEntries = new ArrayList<CallResult>();
        for (File file : sortedFiles)
        {
            if (addLogEntriesForwardAndCheckHasPassedDate(file, logEntries, afterTime))
            {
                break;
            }
        }

        List<CallResult> filteredLogEntries = filterLogEntriesByFutureLimit(logEntries, afterTime, maxMillisForward);

        return filteredLogEntries;

    }

    /**
     * Returns the list of log entries created in this log file starting before the <code>beforeTime</code> provided and only
     * including older entries within <code>maxMillisBefore</code> inclusively.
     * 
     * @param logName the name of the log file
     * @param beforeTime the time to start collecting log entries before in the log file
     * @param maxMillisBefore the maximum number of milliseconds before the <code>beforeTime</code> to collect log entries
     *            for
     * 
     * @return the list of log entries
     */
    public List<CallResult> getLogsBefore(String logName, DateTime beforeTime, long maxMillisBefore)
    {
        List<File> sortedFiles = getSortedLogFiles(logName);

        // Start at the beginning and work backwards
        List<CallResult> logEntries = new ArrayList<CallResult>();
        for (File file : sortedFiles)
        {
            if (!addLogEntriesAndHasNotReachLimitBack(file, logEntries, beforeTime, maxMillisBefore))
            {
                break;
            }
        }

        return logEntries;

    }

    private Boolean confirmDirectory(String path)
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

    private List<File> getSortedLogFiles(String logName)
    {

        // Create the App Log Path and the Log Filename
        File logPathFolder = new File(this._logLocation, logName);
        String logPath = logPathFolder.getPath();

        // Confirm Directory
        if (confirmDirectory(logPath))
        {

            File[] files = logPathFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name)
                {
                    return name.startsWith(name);
                }
            });

            // Sort files
            List<File> sortedFiles = sortFilesDescending(logName, files);

            return sortedFiles;

        }

        return new ArrayList<File>();

    }

    private List<File> sortFilesDescending(String logName, File[] files)
    {
        HashMap<Date, File> filesByTimestamp = new HashMap<Date, File>();
        for (File file : files)
        {
            Date tempDate = getFileDate(logName, file);

            filesByTimestamp.put(tempDate, file);

        }

        List<Date> timestamps = new ArrayList<Date>(filesByTimestamp.keySet());

        Collections.sort(timestamps);
        Collections.reverse(timestamps);

        List<File> sortedFiles = new ArrayList<File>();
        for (Date timestamp : timestamps)
        {
            sortedFiles.add(filesByTimestamp.get(timestamp));
        }

        return sortedFiles;

    }

    private Date getFileDate(String logName, File file)
    {
        int startIndex = logName.length() + 1;
        int endIndex = file.getName().length() - 4;

        String fileTimeStamp = file.getName().substring(startIndex, endIndex);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try
        {
            Date tempDate = dateFormat.parse(fileTimeStamp);
            return tempDate;
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    private boolean addLogEntriesAndHasNotReachLimitBack(File logFile,
                                                         List<CallResult> logEntries,
                                                         DateTime dateBefore,
                                                         Long maxMillisBack)
    {
        List<CallResult> tempLogEntries = loadLogEntriesFromFile(logFile);

        for (CallResult callResult : tempLogEntries)
        {
            if (addLogEntryAndTestPastTimeLimit(logEntries, callResult, dateBefore, maxMillisBack))
            {
                return false;
            }
        }

        return true;

    }

    private boolean addLogEntriesForwardAndCheckHasPassedDate(File logFile, List<CallResult> logEntries, DateTime dateAfter)
    {
        List<CallResult> tempLogEntries = loadLogEntriesFromFile(logFile);

        Date dateLimit = null;
        if (dateAfter != null)
        {
            dateLimit = dateAfter.toDate();
        }

        for (CallResult callResult : tempLogEntries)
        {
            if (dateLimit == null || callResult.getDateTimeGMT().after(dateLimit))
            {
                logEntries.add(callResult);
            }
            else
            {
                return true;
            }
        }

        return false;

    }

    private List<CallResult> filterLogEntriesByFutureLimit(List<CallResult> logEntries,
                                                           DateTime dateAfter,
                                                           Long maxMillisForward)
    {

        if (logEntries.size() == 0)
        {
            return logEntries;
        }

        CallResult earlistEntry = logEntries.get(logEntries.size() - 1);

        // Calculate the new dateBefore based on the new dateAfter time
        Date dateBefore = moveDateToDateBeforePoint(earlistEntry.getDateTimeGMT(), maxMillisForward).toDate();

        List<CallResult> filteredLogEntries = new ArrayList<CallResult>();
        for (CallResult callResult : logEntries)
        {

            if (!callResult.getDateTimeGMT().after(dateBefore))
            {
                filteredLogEntries.add(callResult);
            }
        }

        return filteredLogEntries;

    }

    private List<CallResult> loadLogEntriesFromFile(File logFile)
    {
        List<CallResult> logEntries = new ArrayList<CallResult>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                // Look for the open tag
                if (line.trim().startsWith("<CallResult>"))
                {
                    String callResultString = line;

                    while ((line = br.readLine()) != null)
                    {
                        callResultString += line;

                        // Look for the closing tag
                        if (line.trim().startsWith("</CallResult>"))
                        {
                            break;
                        }
                    }

                    CallResult logEntry = new CallResult();
                    logEntry.fromXML(callResultString);

                    logEntries.add(logEntry);

                }
            }
        }
        catch (IOException e)
        {
        }

        Collections.sort(logEntries, new CallResultComparator());

        // Log entries are assumed to be oldest to newest so flip the list.
        Collections.reverse(logEntries);

        return logEntries;

    }

    private DateTime moveDateToDateBeforePoint(Date dateToMove, Long maxMillisForward)
    {
        if (maxMillisForward == null || maxMillisForward < 1)
        {
            return null;
        }

        DateTime newDateBefore = new DateTime(dateToMove);
        if (maxMillisForward > Integer.MAX_VALUE)
        {
            long tempMillisForward = maxMillisForward;
            int millisToAdd = Integer.MAX_VALUE;

            do
            {

                newDateBefore = newDateBefore.plusMillis(millisToAdd);

                tempMillisForward -= millisToAdd;

            }
            while (tempMillisForward > 0);

        }
        else
        {
            long millis = maxMillisForward;
            newDateBefore = newDateBefore.plusMillis((int) millis);
        }

        return newDateBefore;

    }

    private boolean addLogEntryAndTestPastTimeLimit(List<CallResult> logEntries,
                                                    CallResult logEntry,
                                                    DateTime startTime,
                                                    Long maxMillisBack)
    {
        DateTime oldTime = new DateTime(logEntry.getDateTimeGMT());

        DateTime firstTime = getNewestLogTimeStamp(logEntries, startTime, oldTime);

        if (firstTime == null)
        {
            logEntries.add(logEntry);
            return false;
        }

        if (!oldTime.isBefore(firstTime))
        {
            return false;
        }

        long difference = firstTime.getMillis() - oldTime.getMillis();
        if (maxMillisBack != null && difference > maxMillisBack)
        {
            return true;
        }
        else
        {
            logEntries.add(logEntry);
            return false;
        }

    }

    private DateTime getNewestLogTimeStamp(List<CallResult> logEntries, DateTime startTime, DateTime pendingTime)
    {

        DateTime firstTime = startTime;

        if (logEntries.size() > 0)
        {
            CallResult firstLogEntry = logEntries.get(0);

            firstTime = new DateTime(firstLogEntry.getDateTimeGMT()).plusMillis(1);
        }
        else if (pendingTime.isBefore(startTime))
        {
            firstTime = pendingTime.plusMillis(1);
        }

        return firstTime;

    }
}
