package Unity.UnitTest;
import unity.core.runtime.SettingsBase;

public final class TestSettings extends SettingsBase {
	
	private static final String ConfigurationFileName = "test.config";
	
	public static String getTestStringSetting() {
		return TestSettings.GetSetting(ConfigurationFileName, "/test/teststring", "", true);
	}
	
	public static boolean setTestStringSetting(String value) {
		return TestSettings.SetSetting(ConfigurationFileName, "/test/teststring", value);
	}
	
	public static boolean getTestBooleanSetting() {
		return TestSettings.GetSetting(ConfigurationFileName, "/test/testboolean", false, true);
	}
	
	public static boolean setTestBooleanSetting(boolean value) {
		return TestSettings.SetSetting(ConfigurationFileName, "/test/testboolean", value);
	}

	public static int getTestIntSetting() {
		return TestSettings.GetSetting(ConfigurationFileName, "/test/testint", 10, true);
	}
	
	public static boolean setTestIntSetting(int value) {
		return TestSettings.SetSetting(ConfigurationFileName, "/test/testint", value);
	}

	
}
