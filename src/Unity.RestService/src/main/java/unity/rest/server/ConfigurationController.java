package unity.rest.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationController {

    
	@RequestMapping("/configuration/reader")
	public SettingValue getSetting(
			@RequestParam(value="configurationFileName", required=true) String configurationFileName,
			@RequestParam(value="settingPath", required=true) String settingPath,
			@RequestParam(value="defaultValue", required=true) String defaultValue,
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="setIfNotFound", required=true) String setIfNotFound) {
		return new SettingValue(configurationFileName,settingPath,defaultValue,type,setIfNotFound);
	}
	
	@RequestMapping("/configuration/writer")
	public SettingValue setSetting(
			@RequestParam(value="configurationFileName", required=true) String configurationFileName,
			@RequestParam(value="settingPath", required=true) String settingPath,
			@RequestParam(value="value", required=true) String value,
			@RequestParam(value="type", required=true) String type) {
		return new SettingValue(configurationFileName,settingPath,value,type);
	}
	
	@RequestMapping("/log")
	public UnityLog log(
			@RequestParam(value="logName", required=true) String logName,
			@RequestParam(value="callResultXml", required=true) String callResultXml) {
		return new UnityLog(logName,callResultXml);
	}
}
