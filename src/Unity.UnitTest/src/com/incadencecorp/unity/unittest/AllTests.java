package com.incadencecorp.unity.unittest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		com.incadencecorp.unity.unittest.Local.UnityLocalConnectorTests.class, 
		com.incadencecorp.unity.unittest.Rest.UnityRestConnectorTests.class
	})
public class AllTests {
	
}
