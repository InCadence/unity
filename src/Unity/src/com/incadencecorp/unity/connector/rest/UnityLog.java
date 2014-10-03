package com.incadencecorp.unity.connector.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.connector.local.LocalConfigConnector;

public class UnityLog {

    private String result;

    public UnityLog()
    {
    }

    public UnityLog(String logName, String callResultXml)
    {

        try
        {
            logName = URLDecoder.decode(logName, "UTF-8");
            callResultXml = URLDecoder.decode(callResultXml, "UTF-8");

            LocalConfigConnector.log(logName, callResultXml);

            // return success
            result = "true";

        }
        catch (UnsupportedEncodingException ex)
        {
            new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Rest.Server.UnityLog");

            // return failed
            result = "false";
        }
    }

    public String getResult()
    {
        return this.result;
    }
}
