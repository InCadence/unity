package unity.logger.log4j;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MyXMLConfiguration {

    public MyXMLConfiguration()
    {

        try
        {

            File projectRootPath = new File(System.getProperty("user.dir"));

            // String xmlFileName = projectRootPath + "\\bin\\log4j2.xml";

            File logDirectory = new File(projectRootPath, "bin");

            // check if logDirectory exists. If not, create it
            if (!logDirectory.exists())
            {
                logDirectory.mkdir();
            }

            File fileConfig = new File(logDirectory, "log4j2.xml");

            if (fileConfig.exists() && !fileConfig.isDirectory())
            {

                // log4j2 configuration file already exists, no need to create new one
            }
            else
            {
                // log4j2 configuration file does not exists, create new one
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("Configuration");
                doc.appendChild(rootElement);

                // appender elements
                Element appenders = doc.createElement("Appenders");
                rootElement.appendChild(appenders);

                // file elements
                Element file = doc.createElement("File");
                appenders.appendChild(file);
                file.setAttribute("name", "file");
                file.setAttribute("filename", "${sys:logFilename}");

                // patternLayout
                Element patternLayoutfile = doc.createElement("PatternLayout");
                file.appendChild(patternLayoutfile);
                patternLayoutfile.setAttribute("pattern", "%d %-5p [%t] %C{2} - %msg%n");

                // file elements
                Element xmlfile = doc.createElement("File");
                appenders.appendChild(xmlfile);
                xmlfile.setAttribute("name", "xmlfile");
                xmlfile.setAttribute("filename", "${sys:logFilename}");

                // patternLayout
                Element patternLayoutfilexml = doc.createElement("PatternLayout");
                xmlfile.appendChild(patternLayoutfilexml);
                patternLayoutfilexml.setAttribute("pattern", "%msg%n");

                // console elements
                Element console = doc.createElement("Console");
                appenders.appendChild(console);
                console.setAttribute("name", "console");
                console.setAttribute("target", "SYSTEM_OUT");

                // patternLayout
                Element patternLayoutconsole = doc.createElement("PatternLayout");
                console.appendChild(patternLayoutconsole);
                patternLayoutconsole.setAttribute("pattern", "%d %-5p [%t] %C{2} - %msg%n");

                // logger elements
                Element loggers = doc.createElement("Loggers");
                rootElement.appendChild(loggers);

                // logger elements
                Element loggerfile = doc.createElement("Logger");
                loggers.appendChild(loggerfile);
                loggerfile.setAttribute("name", "filelogger");
                loggerfile.setAttribute("level", "info");

                // Appender Ref elements
                Element fileAppenderRef = doc.createElement("AppenderRef");
                loggerfile.appendChild(fileAppenderRef);
                fileAppenderRef.setAttribute("ref", "file");

                // logger elements
                Element loggerxml = doc.createElement("Logger");
                loggers.appendChild(loggerxml);
                loggerxml.setAttribute("name", "xmllogger");
                loggerxml.setAttribute("level", "info");

                // Appender Ref elements
                Element xmlAppenderRef = doc.createElement("AppenderRef");
                loggerxml.appendChild(xmlAppenderRef);
                xmlAppenderRef.setAttribute("ref", "xmlfile");

                // logger elements
                Element loggerconsole = doc.createElement("Logger");
                loggers.appendChild(loggerconsole);
                loggerconsole.setAttribute("name", "consolelogger");
                loggerconsole.setAttribute("level", "info");

                // Appender Ref elements
                Element consoleAppenderRef = doc.createElement("AppenderRef");
                loggerconsole.appendChild(consoleAppenderRef);
                consoleAppenderRef.setAttribute("ref", "console");

                // root logger element
                Element rootlogger = doc.createElement("Root");
                loggers.appendChild(rootlogger);
                rootlogger.setAttribute("level", "info");

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fileConfig);

                // Output to console for testing
                // StreamResult result = new StreamResult(System.out);

                transformer.transform(source, result);

                // System.out.println("File saved!");
            }

        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (TransformerException tfe)
        {
            tfe.printStackTrace();
        }
    }
}
