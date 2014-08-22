package unity.logger.log4j;



import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import unity.logger.adapter.LoggerAdapter;


public class CallResultLogger implements LoggerAdapter {

	private static Logger filelogger = LogManager.getLogger("filelogger");
	private static Logger consolelogger = LogManager.getLogger("consolelogger");
	private static Logger emaillogger = LogManager.getLogger("emaillogger");
	private static Logger xmllogger = LogManager.getLogger("xmllogger");
	
	public CallResultLogger(){
		//creates log4j2 xml file if not found. Currently not needed.
		//MyXMLConfiguration config = new MyXMLConfiguration();
	}
	
	public void toFileLog(EventLogEntryType logLevel, String logFileLocation, String logEntry){
		
		try{
			
			File logFilename = new File(logFileLocation, "events.log");
			System.setProperty("logFilename", logFilename.getAbsolutePath());
			
			org.apache.logging.log4j.core.LoggerContext context =
				    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
				context.reconfigure();
			
			switch (logLevel){
			
			case WARNING:
				filelogger.warn(logEntry);	
				break;
			case ERROR:
				filelogger.error(logEntry);	
				break;
			default:
				filelogger.info(logEntry);	
			}
		}
		catch(Exception ex){
			//do something
		}				
	}
	
	public void toConsoleLog(EventLogEntryType logLevel, String logEntry){
		
		try{
			switch (logLevel){
			
			case WARNING:
				consolelogger.warn(logEntry);	
				break;
			case ERROR:
				consolelogger.error(logEntry);	
				break;
			default:
				consolelogger.info(logEntry);	
			}
		}
	    catch(Exception ex){
	    	//do something
	    }			
	}
	
    public void toEmailLog(EventLogEntryType logLevel, String logEntry){
		
		try{
			switch (logLevel){
			
			case WARNING:
				emaillogger.warn(logEntry);	
				break;
			case ERROR:
				emaillogger.error(logEntry);	
				break;
			default:
				emaillogger.info(logEntry);	
			}
		}
	    catch(Exception ex){
	    	//do something
	    }			
	}
	
    public void toXmlFileLog(String logFilename, String callResultXml){
		
		try{
			System.setProperty("logFilename", logFilename);
			
			org.apache.logging.log4j.core.LoggerContext context =
				    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
				context.reconfigure();
			

			xmllogger.info(callResultXml);				
				
		}
		catch(Exception ex){
			
			//do something
		}   	
				
	}
}
