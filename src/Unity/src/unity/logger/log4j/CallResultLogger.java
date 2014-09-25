package unity.logger.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import unity.common.CallResult.CallResults;

public class CallResultLogger {

    private static Logger filelogger = LogManager.getLogger("filelogger");
    private static Logger consolelogger = LogManager.getLogger("consolelogger");
    private static Logger emaillogger = LogManager.getLogger("emaillogger");
    private static Logger xmllogger = LogManager.getLogger("xmllogger");

    public static void toFileLog(CallResults logLevel, String logFileLocation, String logEntry)
    {
        toLog(filelogger, logLevel, logEntry);
    }

    public static void toConsoleLog(CallResults logLevel, String logEntry)
    {
        toLog(consolelogger, logLevel, logEntry);
    }

    public static void toEmailLog(CallResults logLevel, String logEntry)
    {
        toLog(emaillogger, logLevel, logEntry);
    }

    public static void toXmlFileLog(String logFilename, String callResultXml)
    {
        System.setProperty("logFilename", logFilename);

        org.apache.logging.log4j.core.LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        xmllogger.info(callResultXml);
    }

    private static void toLog(Logger logger, CallResults logLevel, String logEntry)
    {

        switch (logLevel) {

        case FAILED:
            logger.warn(logEntry);
            break;
        case FAILED_ERROR:
            logger.error(logEntry);
            break;
        case DEBUG:
            logger.debug(logEntry);
            break;
        default:
            logger.info(logEntry);
        }
    }
}
