package Unity.UnitTest;

import unity.common.SettingsBase;

public final class TestSettings extends SettingsBase {
	
	private static final String ConfigurationFileName = "test.config";
	
	public static String getTestStringSetting() {
		return TestSettings.getSetting(ConfigurationFileName, "/test/teststring", "", true);
	}
	
	public static boolean setTestStringSetting(String value) {
		return TestSettings.setSetting(ConfigurationFileName, "/test/teststring", value);
	}
	
	public static boolean getTestBooleanSetting() {
		return TestSettings.getSetting(ConfigurationFileName, "/test/testboolean", false, true);
	}
	
	public static boolean setTestBooleanSetting(boolean value) {
		return TestSettings.setSetting(ConfigurationFileName, "/test/testboolean", value);
	}

	public static int getTestIntSetting() {
		return TestSettings.getSetting(ConfigurationFileName, "/test/testint", 10, true);
	}
	
	public static boolean setTestIntSetting(int value) {
		return TestSettings.setSetting(ConfigurationFileName, "/test/testint", value);
	}

	
}
