package com.incadencecorp.unity.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import com.incadencecorp.unity.common.CallResult;
import com.incadencecorp.unity.common.CallResult.CallResults;
import com.incadencecorp.unity.common.SettingType;
import com.incadencecorp.unity.configuration.ConfigurationFiles;
import com.incadencecorp.unity.rmi.configuration.RmiConfigurationFiles;

public class RMIService {

    public static void main(String[] args)
    {

        try
        {
            System.setProperty("java.rmi.server.hostname", args[0]);

            CallResult.log(CallResults.INFO, "Address set to: " + args[0], "RMI Service");

            ConfigurationFiles configurationFiles = new ConfigurationFiles();
            configurationFiles.setSetting("unity.config", "unity/addresses/UnityAddress", args[0], SettingType.ST_STRING);

            LocateRegistry.createRegistry(Integer.parseInt(args[1]));

            CallResult.log(CallResults.INFO, "Port set to: " + args[1], "RMI Service");

            RmiConfigurationFiles remoteConfigurationFiles = new RmiConfigurationFiles();
            Naming.rebind("configurations", remoteConfigurationFiles);

            CallResult.log(CallResults.INFO, "Unity RMI service is running...", "RMI Service");
        }
        catch (IndexOutOfBoundsException ex)
        {
            CallResult.log(CallResults.FAILED_ERROR, "Input Arg: {Address} {port}", "RMI Service");
        }
        catch (Exception ex)
        {
            CallResult.log(CallResults.FAILED_ERROR, ex, ex.getMessage(), "RMI Service");
            System.exit(1);
        }

    }
}
