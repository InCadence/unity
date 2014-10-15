package com.incadencecorp.unity.test.connector.rmi;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.common.tests.SettingsBaseTest;
import com.incadencecorp.unity.connector.rmi.RmiConfigConnector;

public class UnityRmiConnectorTests extends SettingsBaseTest {

    @BeforeClass
    public static void initialize()
    {
        RmiConfigConnector.initialize("127.0.0.1", 1099);
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
            RmiConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue, SettingType.ST_STRING);

            RmiConfigConnector.setSetting("app.config",
                                          "app/section1/random",
                                          Integer.toString(_testIntValue),
                                          SettingType.ST_INTEGER);

            value = RmiConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.ST_STRING, false);
            intValue = Integer.parseInt(RmiConfigConnector.getSetting("app.config",
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
