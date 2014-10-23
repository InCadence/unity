package com.incadencecorp.unity.local.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.common.SettingsBaseTest;
import com.incadencecorp.unity.configuration.ConfigConnector;

public class UnityLocalConnectorTest extends SettingsBaseTest {

    @BeforeClass
    public static void initialize()
    {

        SettingsBaseTest.initialize();
        ConfigConnector.initialize(new LocalConfigurationsConnector());
    }

    @Test
    public void setSettingsTest()
    {

        try
        {

            String value;
            int intValue;

            // Local Configuration
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

    @Test
    public void getSettingsTest()
    {

        String value;
        int intValue;

        // Local Configuration
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

        assertEquals(_testStringValue, value);
        assertEquals(_testIntValue, intValue);

    }

    @Test
    public void addLogEntryTest()
    {

        CallResult rst = new CallResult(CallResults.SUCCESS);
        ConfigConnector.log("TestLogName", rst.toXML(true));
    }

    @Test
    public void logTest()
    {
        CallResult.initialize(new LocalConfigurationsConnector(), "testtest");
        CallResult.failedCallResult();
    }

    @After
    public void finalize()
    {

    }

}
