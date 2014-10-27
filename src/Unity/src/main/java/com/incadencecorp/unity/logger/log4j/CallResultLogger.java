package com.incadencecorp.unity.logger.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incadencecorp.unity.common.CallResult.CallResults;

public class CallResultLogger {

    private static Logger _filelogger = LogManager.getLogger("filelogger");
    private static Logger _consolelogger = LogManager.getLogger("consolelogger");
    private static Logger _emaillogger = LogManager.getLogger("emaillogger");
    private static Logger _xmllogger = LogManager.getLogger("xmllogger");

    public static void toFileLog(CallResults logLevel, String logFileLocation, String logEntry)
    {
        toLog(_filelogger, logLevel, logEntry);
    }

    public static void toConsoleLog(CallResults logLevel, String logEntry)
    {
        toLog(_consolelogger, logLevel, logEntry);
    }

    public static void toEmailLog(CallResults logLevel, String logEntry)
    {
        toLog(_emaillogger, logLevel, logEntry);
    }

    public static void toXmlFileLog(String logFilename, String callResultXml)
    {
        System.setProperty("logFilename", logFilename);

        org.apache.logging.log4j.core.LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        context.reconfigure();

        _xmllogger.info(callResultXml);
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
