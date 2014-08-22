package unity.connector.rest;


import unity.configuration.SettingType;
import unity.core.runtime.CallResult;
import unity.core.runtime.CallResult.CallResults;

public class RestConfigConnector {

	/******************************
	 * Private Member Variables
	 ******************************/
	
	private static int _port = 8080;
	private static String _address = "127.0.0.1";
	private static RestConfigurationsConnector _connector = new RestConfigurationsConnector(_port, _address);

	/*********************
	 * Public Functions
	 *********************/
	
	public static String getAddress() {
		return _address;
	}

	public static void setAddress(String address) {
		RestConfigConnector._address = address;
		_connector = new RestConfigurationsConnector(_port, _address);
	}
	
	public static int getPort() {
		return _port;
	}
	public static void setPort(int port) {
		RestConfigConnector._port = port;
		_connector = new RestConfigurationsConnector(_port, _address);
	}

	public static String getSetting(String configurationFileName, String settingPath, String defaultValue, SettingType type) {
		return getSetting(configurationFileName, settingPath, defaultValue, type, false);
	}

	public static String getSetting(String configurationFileName,String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound) {
		try {

			String value = _connector.getSetting(configurationFileName, settingPath,defaultValue, type, setIfNotFound);

			// return success
			return value;

		} catch (Exception ex) {
			// Log Failed Error			
			new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Connector.Rest.RestConfigConnector");
			return defaultValue;
		}
	}

	public static void setSetting(String configurationFileName, String settingPath, String value, SettingType type) {
		try {
			// call on the connector
			_connector.setSetting(configurationFileName, settingPath, value, type);

		} catch (Exception ex) {
			// Log Failed Error

			new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Connector.Rest.RestConfigConnector");
		}
	}
//	public static CallResult getSettingType(String configurationFileName,String settingPath) {
//	try {
//		// call on the connector
//		SettingType type = _connector.getSettingType(configurationFileName, settingPath);
//
//		// return success
//		return new CallResult(CallResults.SUCCESS,(Object)type);
//
//	} catch (Exception ex) {
//		// Return Failed Error
//		return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
//	}
//}

/*
 * public static CallResult getSection(String configurationFileName, String
 * sectionPath, String section) { try { section =
 * _connector.getSection(configurationFileName, sectionPath);
 * 
 * //did we get back xml? if (section =="") { //no, return failed return new
 * CallResult(CallResults.FAILED, "Section not found",
 * "Unity.Runtime.ConfigConnector") } else { //return success return
 * CallResult.successCallResult; } } catch (Exception ex) { // Return Failed
 * Error return new CallResult(CallResults.FAILED_ERROR, ex,
 * "Unity.Runtime.ConfigConnector"); } }
 * 
 * 
 * public static CallResult getSectionList(String configurationFileName,
 * String sectionPath, String[] sectionList) { try { // call on connector
 * sectionList = _connector.getSectionList(configurationFileName,
 * sectionPath); //return success return CallResult.successCallResult; }
 * catch (Exception ex) { // Return Failed Error return new
 * CallResult(CallResults.FAILED_ERROR, ex,
 * "Unity.Runtime.ConfigConnector"); } }
 */
//	public static CallResult deleteSetting(String configurationFileName,
//			String settingPath) {
//		try {
//			// call on the connector
//			_connector.deleteSetting(configurationFileName, settingPath);
//			// return success
//			return new CallResult(CallResults.SUCCESS);
//		} catch (Exception ex) {
//			// Return Failed Error
//			return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
//		}
//	}

//	public static CallResult deleteSection(String configurationFileName,
//			String sectionPath) {
//		try {
//			// call on the connector
//			_connector.deleteSection(configurationFileName, sectionPath);
//			// return success
//			return new CallResult(CallResults.SUCCESS);
//		} catch (Exception ex) {
//			// Return Failed Error
//			return new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
//		}
//	}

	public static void log(String AppName, String callResultXml) {

		try {
			
			_connector.logToUnity(AppName, callResultXml);

//			if (_connector.log(logName, callResultXml)) {
//
//				return new CallResult(CallResults.SUCCESS);
//			} else {
//
//				return new CallResult(CallResults.FAILED);
//			}
		} catch (Exception ex) {
			// Return Failed Error
			new CallResult(CallResults.FAILED_ERROR, ex,"Unity.Runtime.ConfigConnector");
		}
	}
}
