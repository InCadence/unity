package unity.rest.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import unity.connector.local.LocalConfigConnector;
import unity.core.runtime.CallResult;
import unity.core.runtime.CallResult.CallResults;

public class UnityLog {

	private String result;
	
	public UnityLog() {}
	
	public UnityLog(String logName, String callResultXml) {
		
		try {
			logName = URLDecoder.decode(logName, "UTF-8");
			callResultXml = URLDecoder.decode(callResultXml, "UTF-8");
			
			LocalConfigConnector.log(logName, callResultXml);
			
			//return success
			result = "true";
			
		} catch (UnsupportedEncodingException ex) {
			new CallResult(CallResults.FAILED_ERROR, ex, "Unity.Rest.Server.UnityLog");
			
			//return failed
			result = "false";
		}
	}
	
	public String getResult() {
		return this.result;
	}
}
