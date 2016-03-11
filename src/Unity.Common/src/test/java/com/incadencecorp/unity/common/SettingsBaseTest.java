package com.incadencecorp.unity.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

public class SettingsBaseTest {

    protected static String _testStringValue;
    protected static int _testIntValue;

    @BeforeClass
    public static void initialize() {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);

        // Create Test Data
        _testStringValue = "StringData" + randomInt;
        _testIntValue = randomInt;
    }

    @Test
    public void getSettingWithMinTest() {

        TestSettings.setTestIntSetting("IntMinValue", 3);
        TestSettings.setTestDoubleSetting("DoubleMinValue", 3.5);
        TestSettings.setTestFloatSetting("FloatMinValue", 3.5f);

        // int
        assertEquals(5, TestSettings.getSettingWithMin("IntMinValue", 2, 5));
        assertEquals(5, TestSettings.getSettingWithMin("IntMinValue", 10, 5));
        assertEquals(3, TestSettings.getSettingWithMin("IntMinValue", 10, 1));
        assertEquals(3, TestSettings.getTestIntSetting("IntMinValue"));

        // double
        assertEquals(5.2, TestSettings.getSettingWithMin("DoubleMinValue", 2.1, 5.2), 0);
        assertEquals(5.2, TestSettings.getSettingWithMin("DoubleMinValue", 10.1, 5.2), 0);
        assertEquals(3.5, TestSettings.getSettingWithMin("DoubleMinValue", 10.2, 1.5), 0);
        assertEquals(3.5, TestSettings.getTestDoubleSetting("DoubleMinValue"), 0);

        // float
        assertEquals(5.2f, TestSettings.getSettingWithMin("FloatMinValue", 2.1f, 5.2f), .5);
        assertEquals(5.2f, TestSettings.getSettingWithMin("FloatMinValue", 10.1f, 5.2f), .5);
        assertEquals(3.5f, TestSettings.getSettingWithMin("FloatMinValue", 10.2f, 1.5f), .5);
        assertEquals(3.5f, TestSettings.getTestFloatSetting("FloatMinValue"), .5);

    }
}
