package Unity.UnitTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		Unity.UnitTest.Local.UnityLocalConnectorTests.class, 
		Unity.UnitTest.Rest.UnityRestConnectorTests.class
	})
public class AllTests {
	
}
