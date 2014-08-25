package Unity.UnitTest.Local;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import unity.configuration.SettingType;
import unity.connector.local.LocalConfigConnector;
import unity.core.runtime.CallResult;
import unity.core.runtime.CallResult.CallResults;

public class UnityLocalConnectorTests {

	private static String _testStringValue;
	private static int _testIntValue; 
	
	@BeforeClass
	public static void Initialize() {
		
		Random randomGenerator = new Random();
		
		int randomInt = randomGenerator.nextInt(100);
		
		// Create Test Data
		_testStringValue = "Matthew" + randomInt;
		_testIntValue = randomInt;

	}
	
	@Test
	public void TestSetSettings() {

		try{

			String value; 
			int intValue;
			
			// Local Configuration
			LocalConfigConnector.setSetting("app.config", "app/section1/firstname", _testStringValue + "local", SettingType.stString);
			LocalConfigConnector.setSetting("app.config", "app/section1/random", Integer.toString(_testIntValue), SettingType.stInteger);
			
			value = LocalConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.stString, false);
			intValue = Integer.parseInt(LocalConfigConnector.getSetting("app.config", "app/section1/random", "", SettingType.stInteger, false));
			
			assertTrue("Mismatch", value.equals(_testStringValue + "local"));
			assertTrue("Mismatch", _testIntValue == intValue);

		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
	}
	
	@Test
	public void TestGetSettings() {
		
		String Stringvalue; 
		int intValue;
		
		Stringvalue = LocalConfigConnector.getSetting("app.config", "app/section1/firstname", "", SettingType.stString, false);
		intValue = Integer.parseInt(LocalConfigConnector.getSetting("app.config", "app/section1/random", "", SettingType.stInteger, false));
			
		assertTrue("Mismatch", Stringvalue.equals(_testStringValue));
		assertTrue("Mismatch", _testIntValue == intValue);
		
	}
	
	@Test
	public void TestAddLogEntry() {
		
		CallResult rst = new CallResult(CallResults.SUCCESS);
		LocalConfigConnector.log("TestLogName.log", rst.toXML(true));
	}

	@After
	public void Finalize() {
		
	}
	
}
