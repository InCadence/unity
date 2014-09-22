package Unity.UnitTest.Rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import unity.common.SettingType;
import unity.connector.rest.RestConfigConnector;
import unity.connector.rest.RestConfigurationsConnector;
import unity.core.runtime.CallResult;
import unity.core.runtime.CallResult.CallResults;
import Unity.UnitTest.TestSettings;

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
            RestConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue, SettingType.stString);
            RestConfigConnector.setSetting("app.config",
                                           "app/section1/random",
                                           Integer.toString(_testIntValue),
                                           SettingType.stInteger);

            value = RestConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.stString, false);
            intValue = Integer.parseInt(RestConfigConnector.getSetting("app.config",
                                                                       "app/section1/random",
                                                                       "",
                                                                       SettingType.stInteger,
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
                                                         SettingType.stString,
                                                         false);
            intValue = Integer.parseInt(RestConfigConnector.getSetting("app.config",
                                                                       "app/section1/random",
                                                                       "",
                                                                       SettingType.stInteger,
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
            assertTrue("Failed Set", TestSettings.setTestStringSetting(_testStringValue));
            assertTrue("Failed Set", TestSettings.setTestIntSetting(_testIntValue));
            assertTrue("Failed Set", TestSettings.setTestBooleanSetting(true));

            TestSettings.clearCache();

            assertTrue("Mismatch", _testStringValue.equals(TestSettings.getTestStringSetting()));
            assertTrue("Mismatch", _testIntValue == TestSettings.getTestIntSetting());
            assertTrue("Mismatch", TestSettings.getTestBooleanSetting());

            assertTrue("Failed Set", TestSettings.setTestBooleanSetting(false));

            TestSettings.clearCache();

            assertFalse("Mismatch", TestSettings.getTestBooleanSetting());

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
