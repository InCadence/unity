package unity.connector.rmi;

import unity.connector.local.LocalConfigurationsConnector;
import unity.core.runtime.SettingsBase;

public class RmiConfigConnectorSettings extends SettingsBase {

	private static final String ConfigurationFileName = "unity.config";
	private static Boolean _RemotingEnabled = true;
	
	static {
		RmiConfigConnectorSettings.Initialize(new LocalConfigurationsConnector());
	}
	
	public static String getProductName() {
		return "ProductName";
	}
	
	public static String getServiceName() {
		return RmiConfigConnectorSettings.getProductName() + ".Configuration";
	}
	
	public static String getAddress() {
		return RmiConfigConnectorSettings.GetSetting(ConfigurationFileName, "unity/addresses/UnityAddress", "127.0.0.1", true);
	}

	public static Boolean getRemotingEnabled() {
		return _RemotingEnabled;
	}

	public static void toggleRemoting(Boolean value) {
		RmiConfigConnectorSettings._RemotingEnabled = value;
	}
}
