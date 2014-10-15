package com.incadencecorp.unity.connector.rmi;

import com.incadencecorp.unity.common.SettingsBase;

public class RmiConfigConnectorSettings extends SettingsBase {

    private static final String ConfigurationFileName = "unity.config";
    private static Boolean _RemotingEnabled = true;

    public static void initialize(String address, int port)
    {
        RmiConfigConnectorSettings.initialize(new RmiConfigurationsConnector(address, port));
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
    
    public static int getPort()
    {
        return RmiConfigConnectorSettings.getSetting(ConfigurationFileName,
                                                     "unity/addresses/UnityPort",
                                                     1099,
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
