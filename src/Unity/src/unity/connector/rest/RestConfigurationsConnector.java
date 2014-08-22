package unity.connector.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import unity.configuration.IConfigurationsConnector;
import unity.configuration.SettingType;
import unity.configuration.SettingTypeUtility;
import unity.connector.local.LocalConfigConnector;

public class RestConfigurationsConnector implements IConfigurationsConnector {

	/******************************
	 * Private Member Variables
	 ******************************/
	
	private Integer _port;
	private String _address;
	private RestTemplate _restTemplate;
	
	/*********************
	 * Public Constructors
	 *********************/
	
	//Default constructor for local connections
	public RestConfigurationsConnector() {
		this._port = 8080;
		this._address = "localhost";
		this._restTemplate = new RestTemplate();
	}
	
	public RestConfigurationsConnector(int port, String address) {
		this._port = port;
		this._address = address+":"+port;
		this._restTemplate = new RestTemplate();
	}

	/*********************
	 * Public Functions
	 *********************/
	
	public String getSetting(String configurationFileName, String settingPath, String defaultValue, SettingType settingType, Boolean setIfNotFound) {
		
        //replace char so they wont mess up the url
		configurationFileName = configurationFileName.replace('.', '-');
		settingPath = settingPath.replace('/', '-');
		settingPath = settingPath.replace('\\', '-');
		settingPath = settingPath.replace('.', '-');
		String type = SettingTypeUtility.settingTypeToString(settingType);
		
		Map<String,String> hashMap = new HashMap<String,String>();
		
		hashMap.put("address", this._address);
		hashMap.put("port", this._port.toString());
		hashMap.put("configurationFileName", configurationFileName);
		hashMap.put("settingPath", settingPath);
		hashMap.put("defaultValue", defaultValue);
		hashMap.put("value", "changedvalue");
		hashMap.put("type", type);
		hashMap.put("setIfNotFound", setIfNotFound.toString());
		
		//call to rest service
		return this._restTemplate.getForObject("http://"+_address+"/configuration/reader?"
				+ "configurationFileName={configurationFileName}&"
				+ "settingPath={settingPath}&"
				+ "defaultValue={defaultValue}&"
				+ "type={type}&"
				+ "setIfNotFound={setIfNotFound}",
				SettingValue.class, hashMap).getResult();
	}

	public void setSetting(String configurationFileName, String settingPath, String value, SettingType settingType) {
		        
		RestTemplate restTemplate = new RestTemplate();
		        //replace char so they wont mess up the url
				configurationFileName = configurationFileName.replace('.', '-');
				settingPath = settingPath.replace('/', '-');
				settingPath = settingPath.replace('\\', '-');
				settingPath = settingPath.replace('.', '-');
				String type = SettingTypeUtility.settingTypeToString(settingType);
				
				Map<String,String> hashMap = new HashMap<String,String>();
				
				hashMap.put("address", this._address);
				hashMap.put("port", this._port.toString());
				hashMap.put("configurationFileName", configurationFileName);
				hashMap.put("settingPath", settingPath);
				hashMap.put("value", value);
				hashMap.put("type", type);
				
				//call to rest service
				restTemplate.getForObject("http://"+_address+"/configuration/writer?"
						+ "configurationFileName={configurationFileName}&"
						+ "settingPath={settingPath}&"
						+ "value={value}&"
						+ "type={type}&",
						SettingValue.class, hashMap);

	}

	
	public void logToUnity(String logName, String callResultXml) throws UnsupportedEncodingException {
		
		RestTemplate restTemplate = new RestTemplate();
		Map<String,String> hashMap = new HashMap<String,String>();
		
		hashMap.put("logName", URLEncoder.encode(logName, "UTF-8") );
		hashMap.put("callResultXml", URLEncoder.encode(callResultXml, "UTF-8") );
		
		//call to rest service
		restTemplate.getForObject("http://"+_address+"/log?"
				+ "logName={logName}&"
				+ "callResultXml={callResultXml}&",
				UnityLog.class, hashMap);

	}
	
	public void logToLocal(String logName, String callResultXml) {

         LocalConfigConnector.log(logName, callResultXml);
	}

}
