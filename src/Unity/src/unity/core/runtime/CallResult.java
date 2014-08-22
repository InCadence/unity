// ----------------------------------------------------------------------------------------------
// Copyright (c) 2012-2014 InCadence Strategic Solutions. All Rights Reserved.
//
// This software is the confidential and proprietary information of InCadence 
// Strategic Solutions ("Confidential Information"). You shall not disclose 
// such Confidential Information and shall use it only in accordance with the 
// terms of the license agreement you entered into with InCadence.
// ----------------------------------------------------------------------------------------------

package unity.core.runtime;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import unity.connector.local.LocalConfigConnector;

public class CallResult {
	
	public enum CallResults{
		UNKNOWN, 
		SUCCESS, 
		FAILED,
		FAILED_ERROR, 
		CANCELED,
		LOGINSTATUS, 
		INFO;
		
	};
	
	public static class ValueResult<T> {
		
		public ValueResult(T value, CallResult rst) {
			_value = value;
			_rst = rst;
		}
		
		public ValueResult(T value, CallResults rst) {
			this(value, new CallResult(rst));
		}
		
		private T _value;
		public T value() { return _value; }
		
		private CallResult _rst;
		public CallResult result() { return _rst; }
		
		
	}
	
	//private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
	//private static Date date = new Date();
	
	private CallResults _Result;
	private Date _DateTimeGMT = new Date();
	private String _Message;
	private String _ModuleName;
	private String _MethodName;
	private int _LineNumber;
	private String _StackTrace;
	private Object _ReturnValue;
	
	private Exception _Exception; 
	

	/***************************
	 * Public Shared Variables *
	 ***************************/
	public static CallResult successCallResult = new CallResult(CallResults.SUCCESS);
	public static CallResult failedCallResult = new CallResult(CallResults.FAILED);
	
	
	/****************
	 * Constructors *
	 ****************/
	public CallResult(){
		
		this(CallResults.UNKNOWN);
		this.setDateTimeGMT();
		
		//get stack trace
		StackTraceElement[] Trace = Thread.currentThread().getStackTrace();
				
		//need n to get last element in stack trace
		int n = Trace.length-1;
				
		this._MethodName = Trace[n].getMethodName();
		this._LineNumber = Trace[n].getLineNumber();
		this._StackTrace = stackTracetoString(Trace);
	}
	
	public CallResult(CallResults result, Exception ex, String moduleName){
		
		this(result);
		
		this._Exception = ex;
		this._Message = ex.getMessage();
		this._ModuleName= moduleName;
		this.setDateTimeGMT();
		//get stack trace
		StackTraceElement[] Trace = ex.getStackTrace();
						
		//need n to get last element in stack trace
		int n = Trace.length-1;
						
		this._MethodName = Trace[n].getMethodName();
		this._LineNumber = Trace[n].getLineNumber();
		this._StackTrace = stackTracetoString(Trace);
		
		//debug print if any exceptions
		if(result == CallResults.FAILED_ERROR){
			LogOut(true, true);
			//LogOut
		}
	}
	
	public CallResult(CallResults result, String message, String moduleName){

		this(result);
		
		this._Exception = null;
		this._Message = message;
		this._ModuleName = moduleName;
		this.setDateTimeGMT();
		
		//get stack trace
		StackTraceElement[] Trace = Thread.currentThread().getStackTrace();
				
		//need n to get last element in stack trace
		int n = Trace.length-1;
				
		this._MethodName = Trace[n].getMethodName();
		this._LineNumber = Trace[n].getLineNumber();
		this._StackTrace = stackTracetoString(Trace);
				
		if(result == CallResults.FAILED_ERROR){
			LogOut(true, true);
			//LogOut
		}
	}
	
	public CallResult(CallResults result, Exception ex, Object moduleObject){
		this(result, ex, moduleObject.getClass().getName());
	}
	
	public CallResult(CallResults result, String message, Object moduleObject){
		this(result, message, moduleObject.getClass().getName());
	}
	
	public CallResult(CallResults result){
		
		this._Result = result;
		this._Message = "";
		this._ModuleName = "";		
		this.setDateTimeGMT();
		
		//get stack trace
		StackTraceElement[] Trace = Thread.currentThread().getStackTrace();
		
		//need n to get last element in stack trace
		int n = Trace.length-1;
		
		this._MethodName = Trace[n].getMethodName();
		this._LineNumber = Trace[n].getLineNumber();
		this._StackTrace = stackTracetoString(Trace);
		
		if(result == CallResults.FAILED_ERROR){
			LogOut(true, true);
			//LogOut
		}
	}
	
	//constructor to pass back object because java is pass by value only
    public CallResult(CallResults result, Object value){
		
		this._Result = result;
		this._Message = "";
		this._ModuleName = "";		
		this.setDateTimeGMT();
		
		//get stack trace
		StackTraceElement[] Trace = Thread.currentThread().getStackTrace();
		
		//need n to get last element in stack trace
		int n = Trace.length-1;
		
		this._MethodName = Trace[n].getMethodName();
		this._LineNumber = Trace[n].getLineNumber();
		this._StackTrace = stackTracetoString(Trace);
		
		if(result == CallResults.FAILED_ERROR){
			LogOut(true, true);
			//LogOut
		}
		
		if(result == CallResults.SUCCESS) {
			this._ReturnValue = value;
		}
	}
	
	
	/**************
	 * Properties *
	 **************/
	public static String productName(){
	return "My.Application.Info.ProductName";	
	}
	
	public boolean getIsSuccess(){
		return (this._Result == CallResults.SUCCESS);
	}
	
	public boolean getIsFailed(){
		return (this._Result == CallResults.FAILED);
	}
	
	public boolean getIsFailedError(){
		return (this._Result == CallResults.FAILED_ERROR);
	}
	
	public boolean getIsCanceled(){
		return (this._Result == CallResults.CANCELED);
	}
	
	public boolean getIsInfo(){
		return (this._Result == CallResults.INFO);
	}
	
	public boolean getIsLoginStatus(){
		return (this._Result == CallResults.LOGINSTATUS);
	}
	
	public String getStackTrace(){
		return this._StackTrace;
	}
	
	public CallResults getCallResults(){
		return this._Result;
	}
	
	public Object getValue(){
		return this._ReturnValue;
	}
	
	public void setCallResults(CallResults result){
		this._Result = result;
	}
	
	public String getMessage(){
		return this._Message;
	}
	
	public void setMessage(String message){
		this._Message = message;
	}
	
	public Date getDateTimeGMT(){
		return this._DateTimeGMT;
	}	
	
	public void setDateTimeGMT(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date tempDate = new Date();
		try {
			this._DateTimeGMT = dateFormat.parse(dateFormat.format(tempDate));
		} catch (ParseException e) {
			this._DateTimeGMT = tempDate;
		}
	}
	
	public Exception getException(){
		return this._Exception;
	}
	
	public void setException(Exception ex){
		this._Exception = ex;
		if (ex != null){
			this._StackTrace = ex.getStackTrace().toString();
		}
	}
	
	public String getaModuleName(){
		return this._ModuleName;
	}
	
	public void setModuleName(String name){
		this._ModuleName = name;
	}
	
	public String getMethodName(){
		return this._MethodName;
	}
	
	public void setMethodName(String name){
		this._MethodName = name;
	}
	
	public int getLineNumber(){
		return this._LineNumber;
	}
	
	public void setLineNumer(int lineNumber){
		this._LineNumber = lineNumber;
	}
	
	public void setResult(CallResults _Result) {
		this._Result = _Result;
	}
	/*
	public String toXML(boolean includeDebugInformation){
		try {
			CallResults rslts;
			String xml = "";
			
			rslts = this.toXML(includeDebugInformation, xml);
			
			if (rslts != CallResults.SUCCESS) {
				return "";
			} 
			else {
				return xml;
			}
		} 
		catch (Exception ex) {
			return "";
		}
		
	}
	//*/

	public String stackTracetoString(StackTraceElement[] Trace){
		
		String result="";
	      for (StackTraceElement element : Trace)
	      {
	         result+=element.toString()+"\n";
	      }
	      
	    return result;      
	}
	
	public String toXML (boolean includeDebugInformation) {
		try {
			//no variable rst because no need to initialize XML writer - no xmltextwriter class, using documentbuilder instead
			//clear xml
			//String xml = "";
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			
			//root elements and header
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("CallResult");
			doc.appendChild(rootElement);
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			//Basic info
			Element result = doc.createElement("Result");
			result.appendChild(doc.createTextNode(Integer.toString(this._Result.ordinal())));
			if(this._Message == null){
				this._Message = "null";
			}
			Element message = doc.createElement("Message");
			message.appendChild(doc.createTextNode(this._Message));
			Element dateTimeGMT = doc.createElement("DateTimeGMT");
			dateTimeGMT.appendChild(doc.createTextNode(dateFormat.format(this._DateTimeGMT).toString()));
			rootElement.appendChild(result);
			rootElement.appendChild(message);
			rootElement.appendChild(dateTimeGMT);
			
			//Debug info
			if (includeDebugInformation) {
				Element moduleName = doc.createElement("ModuleName");
				moduleName.appendChild(doc.createTextNode(this._ModuleName));
				
				Element methodName = doc.createElement("MethodName");
				methodName.appendChild(doc.createTextNode(this._MethodName));
				
				Element lineNumber = doc.createElement("LineNumber");
				lineNumber.appendChild(doc.createTextNode(Integer.toString(this._LineNumber)));
				
				if(this._StackTrace == null){
					this._StackTrace = "No Value";
				}
				Element stackTrace = doc.createElement("StackTrace");
				stackTrace.appendChild(doc.createTextNode(this._StackTrace));
				
				
				rootElement.appendChild(moduleName);
				rootElement.appendChild(methodName);
				rootElement.appendChild(lineNumber);
				rootElement.appendChild(stackTrace);
			}
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StringWriter outWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(outWriter);
			transformer.transform(source, streamResult);
			StringBuffer sb = outWriter.getBuffer();
			
			return sb.toString();
			
			//return CallResults.SUCCESS;
			
		}
		catch (Exception ex) {
			//Return failed error
			System.out.println(ex);
			return "";
		}

	}
	
	public CallResults toXML (boolean includeDebugInformation, String Xml){
		return CallResults.SUCCESS;
	}
	
	public CallResults fromXML(String xml) {
		try {
			@SuppressWarnings("unused")
         CallResults rst;
			
			String val;
			//DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document xmlDoc = dBuilder.parse(new InputSource(new StringReader(xml)));
			
			SAXBuilder sax = new SAXBuilder();
			org.jdom2.Document xmlDoc = sax.build(new StringReader(xml));
						
			val = getText(xmlDoc, "//CallResult/Result");
			
			if (isNumeric(val)){
				int temp;
				temp = Integer.parseInt(val);
				this._Result = intToEnum(temp);
			}
			
			val = getText(xmlDoc, "//CallResult/DateTimeGMT");
			if (isDate(val)) {
				this._DateTimeGMT = intToDate(val);
			}
			
			val = getText(xmlDoc, "//CallResult/LineNumber");
			if (isNumeric(val)) {
				this._LineNumber = Integer.parseInt(val);
			}
			
			//Extended debug info
			this._Message = getText(xmlDoc, "//CallResult/Message");
			this._ModuleName = getText(xmlDoc, "//CallResult/ModuleName");
			this._MethodName = getText(xmlDoc, "//CallResult/MethodName");
			this._StackTrace = getText(xmlDoc, "//CallResult/StackTrace");
			
			//return success indicator
			return CallResults.SUCCESS;
		
		} catch (Exception ex) {
			//return failed error indicator
			return CallResults.FAILED_ERROR;
		}
	}
	
	public static CallResults log(CallResult callResult){
		
		return callResult.LogOut(true, true);
	}
	
	public static CallResults log(CallResults result){
		
		CallResult cr = new CallResult();
		cr.setCallResults(result);
		return cr.LogOut(true, true);
	}
	
	public static CallResults log(CallResults result, String message, Object moduleObject){
		
		return log(result, message, moduleObject.getClass());
	}
	
	public static CallResults log(CallResults result, String message, Class objectType){
		
		return log(result, message, objectType.getName());
	}
	
    public static CallResults log(CallResults result, String message, String moduleName){
		
    	CallResult cr = new CallResult();
    	StackTraceElement[] trace =  Thread.currentThread().getStackTrace();
    	
    	//set values
    	cr.setResult(result);
    	cr.setException(null);
    	cr.setMessage(message);
    	cr.setModuleName(moduleName);
    	cr.setDateTimeGMT();
    	
    	int n = trace.length - 1;
    	cr.setMethodName(trace[n].getMethodName());
    	cr.setLineNumer(trace[n].getLineNumber());   	

		return cr.LogOut(true,true);
	}
    
    public static CallResults log(CallResults result, Exception ex, Object moduleObject){
		
		return log(result, ex, moduleObject.getClass());
	}
	
	public static CallResults log(CallResults result, Exception ex, Class objectType){
		
		return log(result, ex, objectType.getName());
	}
	
    public static CallResults log(CallResults result, Exception ex, String moduleName){
		
    	CallResult cr = new CallResult();
    	StackTraceElement[] trace =  Thread.currentThread().getStackTrace();
    	
    	//set values
    	cr.setResult(result);
    	cr.setException(ex);
    	cr.setMessage(ex.getMessage());
    	cr.setModuleName(moduleName);
    	cr.setDateTimeGMT();
    	
    	int n = trace.length - 1;
    	cr.setMethodName(trace[n].getMethodName());
    	cr.setLineNumer(trace[n].getLineNumber());   	
    	
		return cr.LogOut(true,true);
	}
    

	protected CallResults LogOut(boolean toFile, boolean toDebugPrint ){
		try{
			
			CallResults rst;
			/*MaskExceptionEventArgs MaskArgs = new MaskExceptionEventArgs();
			RaiseEvent MaskException(this, MaskArgs);//TODO: find equivalent to raise event in java

			if (MaskArgs.MaskException){
				return callResults.FAILED;
			}*/

			/*String appName = this.Application.Info.ProductName;//TODO: find equivalent/fix*/
			String appName ="Unity";
			String xml = this.toXML(true);
			//rst = 
			
			
			/*
			if(rst != callResults.SUCCESS){
					return rst;
			}
			*/
			boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean()
						      .getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
			
			if (toDebugPrint) {
				System.out.println(xml);
				if (isDebug) {
					try {
						//JOptionPane.showMessageDialog(null,  "CallResult: /n /n" + xml, appName, JOptionPane.DEFAULT_OPTION);
					} catch (Exception ex){}
				}
			}
			
			if (toFile) {
				
				// Log to File Log 
				LocalConfigConnector localConfigConnector = new LocalConfigConnector();
				
				localConfigConnector.log(appName, xml);
                /*
				if (this.ModuleName == "Unity.Runtime.Configuration"){//TODO: Find out what this is in the java project;
					//do Nothing
				} else {
					if ()
				}*/
			}
			
			//Return Success
			return CallResults.SUCCESS;

		} catch(Exception e){
			//return Failed error
			return CallResults.FAILED_ERROR;

		}
	}
	
	
	protected static String getText(org.jdom2.Document node, String xPath) {
		try {
		   XPathExpression<org.jdom2.Element> xpath = XPathFactory.instance().compile(xPath, Filters.element());
		   
		   org.jdom2.Element emt = xpath.evaluateFirst(node);
		   
			if (emt == null) {
				//node not found
				return "";
			} else {
				
				return emt.getText();
			}

		} catch (Exception ex) {
			//Empty string
			ex.printStackTrace();
			return "";
			
		}
	}
	
	private boolean isNumeric(String string) {
		if (string.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){
			return true;
		} else return false;
	}
	
	private boolean isDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			sdf.parse(dateString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	private CallResults intToEnum(int val){
		CallResults enumValue = CallResults.values()[val];
		return enumValue;
		
	}
	
	private Date intToDate(String dateString) {
		
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			date = sdf.parse(dateString);
			return date;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	//*/
	
}
