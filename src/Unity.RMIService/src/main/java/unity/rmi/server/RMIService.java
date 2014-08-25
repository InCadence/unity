package unity.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import unity.configuration.ConfigurationFiles;
import unity.configuration.SettingType;
import unity.configuration.rmi.RmiConfigurationFiles;

public class RMIService {


	public static void main(String[] args) {

		System.setProperty("java.rmi.server.hostname", args[0]);
		System.out.println("Address set to: " + args[0]);
		
		ConfigurationFiles configurationFiles = new ConfigurationFiles();
		configurationFiles.setSetting("unity.config", "unity/addresses/UnityAddress", args[0], SettingType.stString);
		
        try
        {
        	LocateRegistry.createRegistry(Integer.parseInt(args[1]));
        	System.out.println("Port set to: " + args[1]);
        	RmiConfigurationFiles remoteConfigurationFiles = new RmiConfigurationFiles();
        	Naming.rebind("configurations", remoteConfigurationFiles);
        	System.out.println("Unity RMI service is running...");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }

	}
}
