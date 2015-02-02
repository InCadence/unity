package com.incadencecorp.unity.rest.connector;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.springframework.web.client.RestTemplate;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.configuration.ConfigurationValue;
import com.incadencecorp.unity.configuration.SettingTypeHelper;
import com.incadencecorp.unity.local.connector.UnityLog;

public class RestConfigurationsConnector implements IConfigurationsConnector {

    /******************************
     * Private Member Variables
     ******************************/

    private int _port;
    private String _address;

    /*********************
     * Public Constructors
     *********************/

    // Default constructor for local connections
    public RestConfigurationsConnector()
    {

        initialize("127.0.0.1", 8080);

    }

    public RestConfigurationsConnector(String address, int port)
    {

        initialize(address, port);

    }

    private void initialize(String address, int port)
    {

        try
        {

            this._port = port;
            this._address = address + ":" + port;

        }
        catch (Exception ex)
        {
            CallResult.log(CallResults.FAILED_ERROR, ex, this);
        }

    }

    /*********************
     * Public Functions
     *********************/

    public String getAddress()
    {
        return this._address;
    }

    public int getPort()
    {
        return this._port;
    }

    @Override
    public String getSetting(String configurationFileName,
                             String settingPath,
                             String defaultValue,
                             SettingType settingType,
                             Boolean setIfNotFound)
    {

        RestTemplate restTemplate = new RestTemplate();

        // replace char so they wont mess up the url
        configurationFileName = configurationFileName.replace('.', '-');
        settingPath = settingPath.replace('/', '-');
        settingPath = settingPath.replace('\\', '-');
        settingPath = settingPath.replace('.', '-');
        String type = SettingTypeHelper.settingTypeToString(settingType);

        Map<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("address", this._address);
        // hashMap.put("port", Integer.toString(this._port));
        hashMap.put("configurationFileName", configurationFileName);
        hashMap.put("settingPath", settingPath);
        hashMap.put("defaultValue", defaultValue);
        // hashMap.put("value", "changedvalue");
        hashMap.put("type", type);
        hashMap.put("setIfNotFound", setIfNotFound.toString());

        // call to rest service
        String value = restTemplate.getForObject("http://" + _address + "/configuration?"
                                                         + "configurationFileName={configurationFileName}&"
                                                         + "settingPath={settingPath}&" + "defaultValue={defaultValue}&"
                                                         + "type={type}&" + "setIfNotFound={setIfNotFound}",
                                                 ConfigurationValue.class,
                                                 hashMap).getResult();

        return value;

    }

    @Override
    public boolean setSetting(String configurationFileName, String settingPath, String value, SettingType settingType)
    {

        RestTemplate restTemplate = new RestTemplate();

        // replace char so they wont mess up the url
        configurationFileName = configurationFileName.replace('.', '-');
        settingPath = settingPath.replace('/', '-');
        settingPath = settingPath.replace('\\', '-');
        settingPath = settingPath.replace('.', '-');
        String type = SettingTypeHelper.settingTypeToString(settingType);

        Map<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("address", this._address);
        // hashMap.put("port", Integer.toString(this._port));
        hashMap.put("configurationFileName", configurationFileName);
        hashMap.put("settingPath", settingPath);
        hashMap.put("value", value);
        hashMap.put("type", type);

        // call to rest service
        restTemplate.postForObject("http://" + _address + "/configuration?"
                + "configurationFileName={configurationFileName}&" + "settingPath={settingPath}&" + "value={value}&"
                + "type={type}&", new ConfigurationValue(), ConfigurationValue.class, hashMap);

        return true;

    }

    @Override
    public boolean log(String logName, String callResultXml)
    {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> hashMap = new HashMap<String, String>();

        try
        {
            hashMap.put("logName", URLEncoder.encode(logName, "UTF-8"));
            hashMap.put("callResultXml", URLEncoder.encode(callResultXml, "UTF-8"));

            // call to rest service
            UnityLog logResult = restTemplate.postForObject("http://" + _address + "/log?" + "logName={logName}&"
                    + "callResultXml={callResultXml}&", new UnityLog(), UnityLog.class, hashMap);
            // return success
            return Boolean.parseBoolean(logResult.getResult());

        }
        catch (Exception ex)
        {
            return false;
        }

    }

    @Override
    public List<CallResult> getLogs(String logName)
    {
        throw new NotImplementedException(this.getClass().getSimpleName() + ".getLogs()");
    }

    @Override
    public List<CallResult> getLogs(String logName, long maxMillisBack)
    {
        throw new NotImplementedException(this.getClass().getSimpleName() + ".getLogs(String logName, long maxMillisBack)");
    }

    @Override
    public List<CallResult> getLogsAfter(String logName, DateTime afterTime, long maxMillisForward)
    {
        throw new NotImplementedException(this.getClass().getSimpleName() + ".getLogsAfter");
    }

    @Override
    public List<CallResult> getLogsBefore(String logName, DateTime beforeTime, long maxMillisBefore)
    {
        throw new NotImplementedException(this.getClass().getSimpleName() + ".getLogsBefore");
    }

}
