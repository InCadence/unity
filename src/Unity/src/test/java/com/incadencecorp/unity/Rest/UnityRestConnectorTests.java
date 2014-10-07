package com.incadencecorp.unity.Rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.common.TestSettings;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.connector.rest.RestConfigConnector;
import com.incadencecorp.unity.connector.rest.RestConfigurationsConnector;

public class UnityRestConnectorTests {

    private static String _testStringValue;
    private static int _testIntValue;

    @BeforeClass
    public static void Initialize()
    {

        Random randomGenerator = new Random();

        int randomInt = randomGenerator.nextInt(100);

        // Create Test Data
        _testStringValue = "Matthew" + randomInt;
        _testIntValue = randomInt;

        RestConfigConnector.initialize("localhost", 8080);

        TestSettings.initialize(new RestConfigurationsConnector("localhost", 8080));

    }

    @Test
    public void TestSetSettings()
    {

        try
        {

            String value;
            int intValue;

            // REST Configuration
            RestConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue, SettingType.ST_STRING);
            RestConfigConnector.setSetting("app.config",
                                           "app/section1/random",
                                           Integer.toString(_testIntValue),
                                           SettingType.ST_INTEGER);

            value = RestConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.ST_STRING, false);
            intValue = Integer.parseInt(RestConfigConnector.getSetting("app.config",
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

        try
        {

            String Stringvalue;
            int intValue;

            Stringvalue = RestConfigConnector.getSetting("app.config",
                                                         "app/section1/firstname",
                                                         "",
                                                         SettingType.ST_STRING,
                                                         false);
            intValue = Integer.parseInt(RestConfigConnector.getSetting("app.config",
                                                                       "app/section1/random",
                                                                       "",
                                                                       SettingType.ST_INTEGER,
                                                                       false));

            assertTrue("Mismatch", Stringvalue.equals(_testStringValue));
            assertTrue("Mismatch", _testIntValue == intValue);

        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

    }

    @Test
    public void TestAddLogEntry()
    {

        try
        {

            CallResult rst = new CallResult(CallResults.SUCCESS);
            RestConfigConnector.log("TestLogName.log", rst.toXML(true));
            
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void TestSettingsClass()
    {

        try
        {
            assertTrue("Failed Set", TestSettings.setTestStringSetting("StringVar", _testStringValue));
            assertTrue("Failed Set", TestSettings.setTestIntSetting("IntVar", _testIntValue));
            assertTrue("Failed Set", TestSettings.setTestBooleanSetting("BoolVar", true));

            TestSettings.clearCache();

            assertTrue("Mismatch", _testStringValue.equals(TestSettings.getTestStringSetting("StringVar")));
            assertTrue("Mismatch", _testIntValue == TestSettings.getTestIntSetting("IntVar"));
            assertTrue("Mismatch", TestSettings.getTestBooleanSetting("BoolVar"));

            assertTrue("Failed Set", TestSettings.setTestBooleanSetting("BoolVar", false));

            TestSettings.clearCache();

            assertFalse("Mismatch", TestSettings.getTestBooleanSetting("BoolVar"));

        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @After
    public void Finalize()
    {

    }

}
