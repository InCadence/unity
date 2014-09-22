package unity.logger.adapter;

public interface LoggerAdapter {

    public enum EventLogEntryType
    {
        WARNING, ERROR, INFORMATION;
    }

    public void toFileLog(EventLogEntryType logLevel, String logFileLocation, String logEntry);

    public void toConsoleLog(EventLogEntryType logLevel, String logEntry);

    public void toXmlFileLog(String logFilename, String callResultXml);

    public void toEmailLog(EventLogEntryType logLevel, String logEntry);
}
