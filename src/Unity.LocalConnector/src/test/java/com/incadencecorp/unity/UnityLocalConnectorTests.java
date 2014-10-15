package com.incadencecorp.unity;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.common.tests.SettingsBaseTest;
import com.incadencecorp.unity.connector.local.LocalConfigConnector;

public class UnityLocalConnectorTests extends SettingsBaseTest {

    @Test
    public void TestSetSettings()
    {

        try
        {

            String value;
            int intValue;

            // Local Configuration
            LocalConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue, SettingType.ST_STRING);
            LocalConfigConnector.setSetting("app.config",
                                            "app/section1/random",
                                            Integer.toString(_testIntValue),
                                            SettingType.ST_INTEGER);

            value = LocalConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.ST_STRING, false);
            intValue = Integer.parseInt(LocalConfigConnector.getSetting("app.config",
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
    public void TestGetSettings()
    {

        String Stringvalue;
        int intValue;

        Stringvalue = LocalConfigConnector.getSetting("app.config",
                                                      "app/section1/firstname",
                                                      "",
                                                      SettingType.ST_STRING,
                                                      false);
        intValue = Integer.parseInt(LocalConfigConnector.getSetting("app.config",
                                                                    "app/section1/random",
                                                                    "",
                                                                    SettingType.ST_INTEGER,
                                                                    false));

        assertTrue("Mismatch", Stringvalue.equals(_testStringValue));
        assertTrue("Mismatch", _testIntValue == intValue);

    }

    @Test
    public void TestAddLogEntry()
    {

        CallResult rst = new CallResult(CallResults.SUCCESS);
        LocalConfigConnector.log("TestLogName.log", rst.toXML(true));
    }

    @After
    public void Finalize()
    {

    }

}
