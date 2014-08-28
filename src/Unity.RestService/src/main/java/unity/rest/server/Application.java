package unity.rest.server;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args)
    {

        SpringApplication.run(Application.class, args);
        System.out.println("Service started on:");

        try
        {
            System.out.println("IPv4 Address: " + InetAddress.getLocalHost().getHostAddress());

            // Retrieve JVM arguments
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            List<String> arguments = runtimeMxBean.getInputArguments();

            // check if port was changed from default
            String port = "";

            for (String arg : arguments)
            {
                if (arg.startsWith("-Dserver.port="))
                {
                    port = arg.substring(14);
                    System.out.println("Port: " + port);
                    break;
                }
            }

            if (port.isEmpty())
            {
                System.out.println("Port: 8080");
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
