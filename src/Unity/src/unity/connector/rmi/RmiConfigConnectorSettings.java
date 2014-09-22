package unity.connector.rmi;

import unity.common.SettingsBase;
import unity.connector.local.LocalConfigurationsConnector;

public class RmiConfigConnectorSettings extends SettingsBase {

    private static final String ConfigurationFileName = "unity.config";
    private static Boolean _RemotingEnabled = true;

    static
    {
        RmiConfigConnectorSettings.initialize(new LocalConfigurationsConnector());
    }

    public static String getProductName()
    {
        return "ProductName";
    }

    public static String getServiceName()
    {
        return RmiConfigConnectorSettings.getProductName() + ".Configuration";
    }

    public static String getAddress()
    {
        return RmiConfigConnectorSettings.getSetting(ConfigurationFileName,
                                                     "unity/addresses/UnityAddress",
                                                     "127.0.0.1",
                                                     true);
    }

    public static Boolean getRemotingEnabled()
    {
        return _RemotingEnabled;
    }

    public static void toggleRemoting(Boolean value)
    {
        RmiConfigConnectorSettings._RemotingEnabled = value;
    }
}
