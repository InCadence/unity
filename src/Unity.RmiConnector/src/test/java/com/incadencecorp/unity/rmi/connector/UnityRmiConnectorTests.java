package com.incadencecorp.unity.rmi.connector;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.common.SettingsBaseTest;
import com.incadencecorp.unity.configuration.ConfigConnector;

public class UnityRmiConnectorTests extends SettingsBaseTest {

    @BeforeClass
    public static void initialize()
    {
        ConfigConnector.initialize(new RmiConfigurationsConnector("127.0.0.1", 1099));
        SettingsBaseTest.initialize();
    }

    @Test
    public void TestSetSettings()
    {
        try
        {

            String value;
            int intValue;

            // RMI Configuration
            ConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue, SettingType.ST_STRING);

            ConfigConnector.setSetting("app.config",
                                       "app/section1/random",
                                       Integer.toString(_testIntValue),
                                       SettingType.ST_INTEGER);

            value = ConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.ST_STRING, false);
            intValue = Integer.parseInt(ConfigConnector.getSetting("app.config",
                                                                   "app/section1/random",
                                                                   "",
                                                                   SettingType.ST_INTEGER,
                                                                   false));

            assertTrue("Mismatch", value.equals(_testStringValue));
            assertTrue("Mismatch", _testIntValue == intValue);

        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

}
