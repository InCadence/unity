package com.incadencecorp.unity.rest.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incadencecorp.unity.connector.rest.ConfigurationValue;
import com.incadencecorp.unity.connector.rest.UnityLog;

@RestController
public class ConfigurationController {

    
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public ConfigurationValue getSetting(
			@RequestParam(value="configurationFileName", required=true) String configurationFileName,
			@RequestParam(value="settingPath", required=true) String settingPath,
			@RequestParam(value="defaultValue", required=true) String defaultValue,
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="setIfNotFound", required=true) String setIfNotFound) {
		return new ConfigurationValue(configurationFileName,settingPath,defaultValue,type,setIfNotFound);
		
	}
	
	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	public ConfigurationValue setSetting(
			@RequestParam(value="configurationFileName", required=true) String configurationFileName,
			@RequestParam(value="settingPath", required=true) String settingPath,
			@RequestParam(value="value", required=true) String value,
			@RequestParam(value="type", required=true) String type) {
		return new ConfigurationValue(configurationFileName,settingPath,value,type);
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.POST)
	public UnityLog log(
			@RequestParam(value="logName", required=true) String logName,
			@RequestParam(value="callResultXml", required=true) String callResultXml) {
		return new UnityLog(logName,callResultXml);
	}
}
