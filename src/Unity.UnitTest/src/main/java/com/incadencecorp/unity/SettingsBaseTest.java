package com.incadencecorp.unity;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.incadencecorp.unity.common.TestSettings;

public class SettingsBaseTest {

    //private static int _testIntValue;

    @BeforeClass
    public static void Initialize()
    {
        //Random randomGenerator = new Random();
        //_testIntValue = randomGenerator.nextInt(100);
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
