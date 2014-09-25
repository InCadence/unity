package unity.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import unity.common.CallResult;
import unity.common.SettingType;
import unity.common.CallResult.CallResults;
import unity.configuration.ConfigurationFiles;
import unity.configuration.rmi.RmiConfigurationFiles;

public class RMIService {


	public static void main(String[] args) {

		System.setProperty("java.rmi.server.hostname", args[0]);
		
		CallResult.log(CallResults.INFO, "Address set to: " + args[0], "RMI Service");
		
		ConfigurationFiles configurationFiles = new ConfigurationFiles();
		configurationFiles.setSetting("unity.config", "unity/addresses/UnityAddress", args[0], SettingType.stString);
		
        try
        {
        	LocateRegistry.createRegistry(Integer.parseInt(args[1]));

            CallResult.log(CallResults.INFO, "Port set to: " + args[1], "RMI Service");

        	RmiConfigurationFiles remoteConfigurationFiles = new RmiConfigurationFiles();
        	Naming.rebind("configurations", remoteConfigurationFiles);

            CallResult.log(CallResults.INFO,"Unity RMI service is running...", "RMI Service");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }

	}
}
