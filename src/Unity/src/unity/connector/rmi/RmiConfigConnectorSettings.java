package unity.connector.rmi;

import unity.configuration.ConfigurationFiles;
import unity.configuration.SettingType;

public class RmiConfigConnectorSettings {

	private static String _unityAddress;
	private static Boolean _RemotingEnabled = true;
	
	public static String getProductName() {
		return "ProductName";
	}
	
	public static String getServiceName() {
		return RmiConfigConnectorSettings.getProductName() + ".Configuration";
	}
	
	public static String getAddress() {
		if (!_RemotingEnabled) {
			//Return Nothing if Remoting is Disabled
			return null;
		}
		
		if (_unityAddress == null &&_unityAddress.isEmpty()) {
			//Pull Address From Config
			ConfigurationFiles configurationFiles = new ConfigurationFiles();
			_unityAddress = configurationFiles.getSetting("unity.config", "unity/addresses/UnityAddress", "127.0.0.1", SettingType.stString, true);
			//TODO: find java equivalent _unityAddress = System.Configuration.ConfigurationManager.AppSettings("UnityServer");
			
			//is set?
			if (_unityAddress == null &&_unityAddress.isEmpty()) {
				//Default to Localhost
				_unityAddress = "127.0.0.1";
			}
		}
		return _unityAddress;
	}

	public static Boolean getRemotingEnabled() {
		return _RemotingEnabled;
	}

	public static void toggleRemoting(Boolean value) {
		RmiConfigConnectorSettings._RemotingEnabled = value;
	}
}
