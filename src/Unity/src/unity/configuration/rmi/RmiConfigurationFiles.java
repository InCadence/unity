package unity.configuration.rmi;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import unity.configuration.ConfigurationFile;
import unity.configuration.SettingType;
import unity.configuration.SettingTypeUtility;
import unity.logger.adapter.LoggerAdapter;
import unity.logger.log4j.CallResultLogger;

public class RmiConfigurationFiles extends UnicastRemoteObject implements IRmiConfigurationFiles {

	/**
	 * Eclipse told me to put this here (serialVersionUID)
	 */
	private static final long serialVersionUID = 1L;
	
	/****************************
	 * Private Member Variables
	 ***************************/
	
	private Hashtable<String, ConfigurationFile> _configurationFiles = null;
	
	private String _logLocation ="";

	
	
	/*********************
	 * Public Properties
	 *********************/

	@Override
	public String getLogLocation() throws RemoteException {

		if(this._logLocation == null && this._logLocation.isEmpty()){
			File basePath = new File(getBasePath());
			File LogDirectory = new File(basePath,"logs");
			this._logLocation = this.getSetting("Unity.config", "Unity/Settings/LogLocation",LogDirectory.getAbsolutePath(), SettingType.stString.toString(), true);
		}
		return this._logLocation;
	}

	@Override
	public void setLogLocation() throws RemoteException {

		File userDir = new File(System.getProperty("user.dir"));
		File logDir = new File(userDir,"log");
		this._logLocation = logDir.getAbsolutePath();
		

	}
	
	/*********************
	 * Constructors
	 *********************/
	//TODO this is supposed to be protected...and why is it same as above?
    public RmiConfigurationFiles() throws RemoteException {
		
    	File userDir = new File(System.getProperty("user.dir"));
		File logDir = new File(userDir,"log");
		this._logLocation = logDir.getAbsolutePath();
		this._configurationFiles = new Hashtable<String,ConfigurationFile>();

	}
    
    /*********************
	 * Public Methods
	 *********************/
    

	@Override
	public void isServiceRunning() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBasePath() throws RemoteException {
		File userDir = new File(System.getProperty("user.dir"));
		return userDir.getAbsolutePath();
	}

	@Override
	public void add(ConfigurationFile configFile) throws RemoteException {
		File file = new File(configFile.getFileName());
		if(file.exists() && !file.isDirectory()){
			//Add it to the hashtable

			_configurationFiles.put(file.getName(),configFile);

		}

	}

	@Override
	public void add(String fileName) throws RemoteException {
		//Create the configuration file object
				ConfigurationFile configFile = new ConfigurationFile(fileName);
				//Call on the other Add method to add the config file.
				this.add(configFile);

	}

	@Override
	public String getSetting(String configurationFileName, String settingPath,
			String defaultValue, String type, Boolean setIfNotFound)
			throws RemoteException {
		//ConfigurationFile configFile = null;
    	//Access the ConfigurationFile object
		ConfigurationFile configFile = getConfigurationFile(configurationFileName);
		
		//convert string back to setting type 
		SettingType settingType = SettingTypeUtility.stringToSettingType(type);
				
		if (configFile != null){
			//Retrieve the Setting
			return configFile.getSetting(settingPath, defaultValue, settingType, setIfNotFound);			
		} else {
			//Return the Default Value
			return defaultValue;
		}
	}

	@Override
	public SettingType getSettingType(String configurationFileName,
			String settingPath) throws RemoteException {
		ConfigurationFile configFile = null;
    	//Access the ConfigurationFile object
    	configFile = getConfigurationFile(configurationFileName);
    	
    	//Retrieve the setting type
    	if (configFile != null) {
    		return configFile.getSettingType(settingPath);
    	} else {
    		return SettingType.stUnknown;
    	}
	}

	@Override
	public void setSetting(String configurationFileName, String settingPath,
			String value, SettingType type) throws RemoteException {
		ConfigurationFile configFile = null;
    	//Access the ConfigurationFile object
    	configFile = getConfigurationFile(configurationFileName);
    	
    	//set the setting
    	if (configFile != null) {
    		configFile.setSetting(settingPath, value, type);
    	}

	}

	@Override
	public void deleteSetting(String configurationFileName, String settingPath)
			throws RemoteException {
		ConfigurationFile configFile = null;
    	//Access the ConfigurationFile object
    	configFile = getConfigurationFile(configurationFileName);
    	
    	//delete the setting
    	if (configFile != null) {
    		configFile.deleteSetting(settingPath);
    	}

	}

	@Override
	public void deleteSection(String configurationFileName, String sectionPath)
			throws RemoteException {
		ConfigurationFile configFile = null;
    	//Access the ConfigurationFile object
    	configFile = getConfigurationFile(configurationFileName);
    	
    	//delete the section
    	if (configFile != null) {
    		configFile.deleteSection(sectionPath);
    	}

	}

	@Override
	public ConfigurationFile getConfigurationFile(String configurationFileName) throws RemoteException {
		

		//Check to see if our collection of ConfigurationFiles contains the Named file.
    	if (!_configurationFiles.containsKey(configurationFileName)) {
    		//add the requested config file
    		
    		File directoryLocation = new File(this.getBasePath());
    		File subdirectoryLocation = new File(directoryLocation,"config");
    		File configFile = new File(subdirectoryLocation,configurationFileName);
    		this.add(configFile.getAbsolutePath());
    	}
    	//Check again, it should be found.  If not, then we can't do anything.
    	if (_configurationFiles.containsKey(configurationFileName)) {
    		System.out.println("returning config");
    		return _configurationFiles.get(configurationFileName);
    	} else {
    		return null;
    	}
	}

	@Override
	public Boolean log(String logName, String callResultXml)
			throws RemoteException {

		try{
			//We log to locations under the Unity's logs folder.  Each application
            //gets its own subfolder automatically.
			
			String logPath;
			String logFileName;
			
			//get current date			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date tempDate = new Date();
			String currentDate = dateFormat.format(tempDate);
			
			
			//Create the App Log Path and the Log Filename
			File logPathFile = new File(this._logLocation,logName);
			logPath = logPathFile.getPath();						
			
			File logFileNameFile = new File(logPath, logName + "_"+ currentDate + ".log");
			logFileName = logFileNameFile.getCanonicalPath();

			System.out.println(logFileName);
			
			//Confirm Directory
			if(confirmDirectory(logPath)){
				
				//Does the file already exist?
				if(logFileNameFile.exists()){
					//Remove the <?xml... from the Xml, so that the Xml in 
                    //the file is well formed.  Only the first entry should
                    //have have the <?xml...
					
					Integer pos = callResultXml.indexOf('\n') - 1;
					Integer lastIndex = callResultXml.length() - 1;
					if(pos>0){
						//pos=0;
						callResultXml = callResultXml.substring(pos, lastIndex);
					}
					
				}
				
				//Append to Log File 
				LoggerAdapter loggeradapter = new CallResultLogger();
				loggeradapter.toXmlFileLog(logFileName, callResultXml);
				return true;				
			} else {
				//Failed to Create Directory; Return False
				return false;
			}
		}
		catch(Exception ex){
			return false;
		}
	}

	@Override
	public Boolean confirmDirectory(String path) throws RemoteException {
		try{
			// Note; we don't use CallResult objects for this function since
            // we're using it from within CallResults; we just use the 
            // CallResults enumeration value as a return value.
			File filepath = new File(path);
			if(filepath.exists()){
				
				//It exists; Return Success
				return true;
				
			}else{				
				//It doesn't exist; Create
				filepath.mkdir();
				
				//Check Again
				if(filepath.exists()){
					
					//Now it exists; Return Success
					return true;
					
				}else{
					//Still doesn't exist; Return Failed
					return false;
				}
			}
			
			
			
		}catch(Exception ex){
			return false;
		}
	}

}
