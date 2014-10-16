package com.incadencecorp.unity.common;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

public class SettingsBaseTest {

    protected static String _testStringValue;
    protected static int _testIntValue;

    @BeforeClass
    public static void initialize()
    {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);

        // Create Test Data
        _testStringValue = "StringData" + randomInt;
        _testIntValue = randomInt;
    }

    @Test
    public void getSettingWithMinTest()
    {
        TestSettings.setTestIntSetting("MinValue", 3);

        assertTrue(TestSettings.getSettingWithMin("MinValue", 2, 5) == 5);
        assertTrue(TestSettings.getSettingWithMin("MinValue", 10, 5) == 5);
        assertTrue(TestSettings.getSettingWithMin("MinValue", 10, 1) == 3);
        assertTrue(TestSettings.getTestIntSetting("MinValue") == 3);
    }

}
