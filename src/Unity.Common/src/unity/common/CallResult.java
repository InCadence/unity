package unity.common;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

/**
 * CallResult is designed for providing uniform error handling and result messages across calling methods. CallResult
 * encapsulates success, failure or cancel states, as well as error messages and class- and method-level resolution to allow
 * the caller to identify the faulting call.
 * 
 * @author InCadence
 *
 */
public class CallResult {

    /*--------------------------------------------------------------------------
    Private Member Variables
    --------------------------------------------------------------------------*/

    private static IConfigurationsConnector _connector = null;

    public enum CallResults
    {
        UNKNOWN, SUCCESS, FAILED, FAILED_ERROR, CANCELED, LOGINSTATUS, INFO, DEBUG;

    };

    /**
     * Initializes the connector to use for logging CallResults
     * 
     * @param connector the connector to use for logging CallResults
     * */
    public static void initialize(IConfigurationsConnector connector)
    {
        _connector = connector;
    }

    public static class ValueResult<T> {

        public ValueResult(T value, CallResult rst)
        {
            _value = value;
            _rst = rst;
        }

        public ValueResult(T value, CallResults rst)
        {
            this(value, new CallResult(rst));
        }

        private T _value;

        public T value()
        {
            return _value;
        }

        private CallResult _rst;

        public CallResult result()
        {
            return _rst;
        }

    }

    // private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
    // private static Date date = new Date();

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

    /**
     * Constructs a CallResult with the state unknown
     */
    public CallResult()
    {
        this(CallResults.UNKNOWN, "", "");
    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the call
     * @param ex an exception to be stored in the CallResult
     * @param moduleName the name of the module
     */
    public CallResult(CallResults result, Exception ex, String moduleName)
    {

        this(result, ex.getMessage(), moduleName);
        this._Exception = ex;

    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the call
     * @param message a message to provide details regarding the state of the CallResult
     * @param moduleName the name of the module
     */
    public CallResult(CallResults result, String message, String moduleName)
    {

        this._Result = result;
        this._Message = message;
        this._ModuleName = moduleName;
        this.setDateTimeGMT();

        // get stack trace
        StackTraceElement[] Trace = Thread.currentThread().getStackTrace();

        // need n to get last element in stack trace
        int n = Trace.length - 1;

        this._MethodName = Trace[n].getMethodName();
        this._LineNumber = Trace[n].getLineNumber();
        this._StackTrace = stackTracetoString(Trace);

        if (result == CallResults.FAILED_ERROR)
        {
            LogOut(true, true);
            // LogOut
        }
    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the CallResult
     * @param ex an exception to be stored in the CallResult
     * @param moduleObject the module object
     */
    public CallResult(CallResults result, Exception ex, Object moduleObject)
    {
        this(result, ex, moduleObject.getClass().getName());
    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the CallResult
     * @param message a message to provide details regarding the state of the CallResult
     * @param moduleObject the module object
     */
    public CallResult(CallResults result, String message, Object moduleObject)
    {
        this(result, message, moduleObject.getClass().getName());
    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the CallResult
     */
    public CallResult(CallResults result)
    {
        this(result, "", "");
    }

    /**
     * Constructs a CallResult
     * 
     * @param result the state of the CallResult
     * @param value the object to be stored in the CallResult
     */
    public CallResult(CallResults result, Object value)
    {

        this(result, "", "");

        if (result == CallResults.SUCCESS)
        {
            this._ReturnValue = value;
        }

    }

    /**************
     * Properties *
     **************/

    /**
     * Returns the name of the product
     * 
     * @return - the name of the product
     */
    public static String productName()
    {
        // TODO: placeholder
        return "My.Application.Info.ProductName";
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Success
     * 
     * @return <code>true</code> if the state of the CallResult is Success; <code>false</code> otherwise
     */
    public boolean isSuccess()
    {
        return (this._Result == CallResults.SUCCESS);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Failed
     * 
     * @return <code>true</code> if the state of the CallResult is Failed; <code>false</code> otherwise
     */
    public boolean isFailed()
    {
        return (this._Result == CallResults.FAILED);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Failed Error
     * 
     * @return <code>true</code> if the state of the CallResult is Failed Error; <code>false</code> otherwise
     */
    public boolean isFailedError()
    {
        return (this._Result == CallResults.FAILED_ERROR);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Canceled
     * 
     * @return <code>true</code> if the state of the CallResult is Canceled; <code>false</code> otherwise
     */
    public boolean isCanceled()
    {
        return (this._Result == CallResults.CANCELED);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Info
     * 
     * @return <code>true</code> if the state of the CallResult is Info; <code>false</code> otherwise
     */
    public boolean isInfo()
    {
        return (this._Result == CallResults.INFO);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Login Status
     * 
     * @return <code>true</code> if the state of the CallResult is Login Status; <code>false</code> otherwise
     */
    public boolean isLoginStatus()
    {
        return (this._Result == CallResults.LOGINSTATUS);
    }

    /**
     * Returns <code>true</code> if the state of the CallResult is Debug Status
     * 
     * @return <code>true</code> if the state of the CallResult is Debug Status; <code>false</code> otherwise
     */
    public boolean isDebugStatus()
    {
        return (this._Result == CallResults.DEBUG);
    }

    /**
     * Returns the stack trace of the CallResult
     * 
     * @return the stack trace of the CallResult
     */
    public String getStackTrace()
    {
        return this._StackTrace;
    }

    /**
     * Returns the state of the CallResult
     * 
     * @return the state of the CallResult
     */
    public CallResults getCallResults()
    {
        return this._Result;
    }

    /**
     * Returns the object from the calling method
     * 
     * @return the object from the calling method
     */
    public Object getValue()
    {
        return this._ReturnValue;
    }

    /**
     * Sets the state of the CallResult
     * 
     * @param result the state of the CallResult to be set
     */
    public void setCallResults(CallResults result)
    {
        this._Result = result;
    }

    /**
     * Returns the message from the CallResult
     * 
     * @return the message from the CallResult
     */
    public String getMessage()
    {
        return this._Message;
    }

    /**
     * Sets the message in the CallResult
     * 
     * @param message the message to be stored in the CallResult
     */
    public void setMessage(String message)
    {
        this._Message = message;
    }

    /**
     * Returns the DateTime stamp of the CallResult in GMT
     * 
     * @return the DateTime stamp of the CallResult in GMT
     */
    public Date getDateTimeGMT()
    {
        return this._DateTimeGMT;
    }

    /**
     * Sets the DateTime stamp of the CallResult as the current instant in GMT
     */
    public void setDateTimeGMT()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date tempDate = new Date();
        try
        {
            this._DateTimeGMT = dateFormat.parse(dateFormat.format(tempDate));
        }
        catch (ParseException e)
        {
            this._DateTimeGMT = tempDate;
        }
    }

    /**
     * Returns the Exception of the CallResult
     * 
     * @return the Exception of the CallResult
     */
    public Exception getException()
    {
        return this._Exception;
    }

    /**
     * Sets the Exception of the CallResult
     * 
     * @param ex the Exception of the CallResult
     */
    public void setException(Exception ex)
    {
        this._Exception = ex;
        if (ex != null)
        {
            this._StackTrace = ex.getStackTrace().toString();
        }
    }

    /**
     * Returns the Module Name of the CallResult
     * 
     * @return the Module Name of the CallResult
     */
    public String getaModuleName()
    {
        return this._ModuleName;
    }

    /**
     * Sets the Module Name of the CallResult
     * 
     * @param name the Module Name of the CallResult
     */
    public void setModuleName(String name)
    {
        this._ModuleName = name;
    }

    /**
     * Returns the Method Name of the CallResult
     * 
     * @return the Method Name of the CallResult
     */
    public String getMethodName()
    {
        return this._MethodName;
    }

    /**
     * Sets the Method Name of the CallResult
     * 
     * @param name the Method Name of the CallResult
     */
    public void setMethodName(String name)
    {
        this._MethodName = name;
    }

    /**
     * Returns the Line Number of the CallResult
     * 
     * @return the Line Number of the CallResult
     */
    public int getLineNumber()
    {
        return this._LineNumber;
    }

    /**
     * Sets the Line Number of the CallResult
     * 
     * @param lineNumber the Line Number of the CallResult
     */
    public void setLineNumer(int lineNumber)
    {
        this._LineNumber = lineNumber;
    }

    /**
     * Sets the status of the CallResult
     * 
     * @param _Result the status of the CallResult
     */
    public void setResult(CallResults _Result)
    {
        this._Result = _Result;
    }

    /*
     * public String toXML(boolean includeDebugInformation){ try { CallResults rslts; String xml = "";
     * 
     * rslts = this.toXML(includeDebugInformation, xml);
     * 
     * if (rslts != CallResults.SUCCESS) { return ""; } else { return xml; } } catch (Exception ex) { return ""; }
     * 
     * } //
     */

    private String stackTracetoString(StackTraceElement[] Trace)
    {

        String result = "";
        for (StackTraceElement element : Trace)
        {
            result += element.toString() + "\n";
        }

        return result;
    }

    /**
     * Returns the CallResult formatted as XML
     * 
     * @param includeDebugInformation whether to include the module name, method name, line number and stacktrace in the
     *            CallResult XML
     * @return the CallResult formatted as XML
     */
    public String toXML(boolean includeDebugInformation)
    {
        try
        {
            // no variable rst because no need to initialize XML writer - no xmltextwriter class, using documentbuilder
            // instead
            // clear xml
            // String xml = "";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements and header
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("CallResult");
            doc.appendChild(rootElement);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            // Basic info
            Element result = doc.createElement("Result");
            result.appendChild(doc.createTextNode(Integer.toString(this._Result.ordinal())));
            if (this._Message == null)
            {
                this._Message = "null";
            }
            Element message = doc.createElement("Message");
            message.appendChild(doc.createTextNode(this._Message));
            Element dateTimeGMT = doc.createElement("DateTimeGMT");
            dateTimeGMT.appendChild(doc.createTextNode(dateFormat.format(this._DateTimeGMT).toString()));
            rootElement.appendChild(result);
            rootElement.appendChild(message);
            rootElement.appendChild(dateTimeGMT);

            // Debug info
            if (includeDebugInformation)
            {
                Element moduleName = doc.createElement("ModuleName");
                moduleName.appendChild(doc.createTextNode(this._ModuleName));

                Element methodName = doc.createElement("MethodName");
                methodName.appendChild(doc.createTextNode(this._MethodName));

                Element lineNumber = doc.createElement("LineNumber");
                lineNumber.appendChild(doc.createTextNode(Integer.toString(this._LineNumber)));

                if (this._StackTrace == null)
                {
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

            // return CallResults.SUCCESS;

        }
        catch (Exception ex)
        {
            // Return failed error
            System.out.println(ex);
            return "";
        }

    }

    // public CallResults toXML(boolean includeDebugInformation, String Xml)
    // {
    // return CallResults.SUCCESS;
    // }
    /**
     * Parses information into the CallResult object from a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if Parsing is successful; FAILLED_ERROR otherwise.
     * @param xml the callResult in XML format
     * @return the result of the call
     */
    public CallResults fromXML(String xml)
    {
        try
        {

            String val;
            // DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // Document xmlDoc = dBuilder.parse(new InputSource(new StringReader(xml)));

            SAXBuilder sax = new SAXBuilder();
            org.jdom2.Document xmlDoc = sax.build(new StringReader(xml));

            val = getText(xmlDoc, "//CallResult/Result");

            if (isNumeric(val))
            {
                int temp;
                temp = Integer.parseInt(val);
                this._Result = intToEnum(temp);
            }

            val = getText(xmlDoc, "//CallResult/DateTimeGMT");
            if (isDate(val))
            {
                this._DateTimeGMT = intToDate(val);
            }

            val = getText(xmlDoc, "//CallResult/LineNumber");
            if (isNumeric(val))
            {
                this._LineNumber = Integer.parseInt(val);
            }

            // Extended debug info
            this._Message = getText(xmlDoc, "//CallResult/Message");
            this._ModuleName = getText(xmlDoc, "//CallResult/ModuleName");
            this._MethodName = getText(xmlDoc, "//CallResult/MethodName");
            this._StackTrace = getText(xmlDoc, "//CallResult/StackTrace");

            // return success indicator
            return CallResults.SUCCESS;

        }
        catch (Exception ex)
        {
            // return failed error indicator
            return CallResults.FAILED_ERROR;
        }
    }

    /**
     * Logs the CallResult as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param callResult the CallResult to be logged
     * @return the result of the call
     */
    public static CallResults log(CallResult callResult)
    {

        return callResult.LogOut(true, true);
    }

    /**
     * Logs the CallResults status as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @return the result of the call
     */
    public static CallResults log(CallResults result)
    {
        return log(result, null, "", "");
    }

    /**
     * Logs the CallResults status, message and Module Object as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param message the message to be logged
     * @param moduleObject the module Object of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, String message, Object moduleObject)
    {

        return log(result, message, moduleObject.getClass());
    }

    /**
     * Logs the CallResults status, message and class of the calling method as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param message the message to be logged
     * @param objectType the class of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, String message, Class<?> objectType)
    {

        return log(result, null, message, objectType.getName());
    }

    /**
     * Logs the CallResults status, message and Module Name of the calling method as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param message the message to be logged
     * @param moduleName the Module name of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, String message, String moduleName)
    {
        return log(result, null, message, moduleName);
    }

    /**
     * Logs the CallResults status, exception and the module Object of the calling method as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param ex the exception to be logged
     * @param moduleObject the module Object of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, Exception ex, Object moduleObject)
    {
        return log(result, ex, moduleObject.getClass());
    }

    /**
     * Logs the CallResults status, exception and the class of the calling method as a CallResult formated as XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param ex the exception to be logged
     * @param objectType the class of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, Exception ex, Class<?> objectType)
    {
        return log(result, ex, ex.getMessage(), objectType.getName());
    }

    /**
     * Logs the CallResults status, exception, message and the module name of the calling method as a CallResult formated as
     * XML.
     * 
     * @see This method will return a CallResults status of SUCCESS if logging is successful; FAILLED_ERROR otherwise.
     * @param result the CallResults status
     * @param ex the exception to be logged
     * @param message the message from the calling method
     * @param moduleName the module name of the calling method
     * @return the result of the call
     */
    public static CallResults log(CallResults result, Exception ex, String message, String moduleName)
    {
        // Is Debug Result?
        if (result == CallResults.DEBUG)
        {
            // Yes; Is Debugger Attached?
            boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

            if (!isDebug)
            {
                // No; Short Circuit Logic
                return CallResults.SUCCESS;
            }
        }

        CallResult cr = new CallResult();
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        // set values
        cr.setResult(result);
        cr.setException(ex);
        cr.setMessage(message);
        cr.setModuleName(moduleName);
        cr.setDateTimeGMT();

        int n = trace.length - 1;
        cr.setMethodName(trace[n].getMethodName());
        cr.setLineNumer(trace[n].getLineNumber());

        return cr.LogOut(true, true);
    }

    protected CallResults LogOut(boolean toFile, boolean toDebugPrint)
    {
        try
        {
            /*
             * MaskExceptionEventArgs MaskArgs = new MaskExceptionEventArgs(); RaiseEvent MaskException(this,
             * MaskArgs);//TODO: find equivalent to raise event in java
             * 
             * if (MaskArgs.MaskException){ return callResults.FAILED; }
             */

            /* String appName = this.Application.Info.ProductName;//TODO: find equivalent/fix */
            String appName = "Unity";
            String xml = this.toXML(true);

            /*
             * if(rst != callResults.SUCCESS){ return rst; }
             */
            boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

            if (toDebugPrint && isDebug)
            {
                switch (_Result) {
                case DEBUG:
                    System.out.println("Debug: " + this.getMessage());
                    break;
                case FAILED:
                    System.out.println("Warning: " + this.getMessage());
                    break;
                case INFO:
                    System.out.println("Info: " + this.getMessage());
                    break;
                case FAILED_ERROR:
                case UNKNOWN:
                    System.out.println(xml);
                    break;
                default:
                    break;
                }
            }

            if (toFile && !this.isDebugStatus() && _connector != null)
            {
                // Log to File Log
                _connector.log(appName, xml);
            }

            // Return Success
            return CallResults.SUCCESS;

        }
        catch (Exception e)
        {
            // return Failed error
            return CallResults.FAILED_ERROR;

        }
    }

    protected static String getText(org.jdom2.Document node, String xPath)
    {
        try
        {
            XPathExpression<org.jdom2.Element> xpath = XPathFactory.instance().compile(xPath, Filters.element());

            org.jdom2.Element emt = xpath.evaluateFirst(node);

            if (emt == null)
            {
                // node not found
                return "";
            }
            else
            {

                return emt.getText();
            }

        }
        catch (Exception ex)
        {
            // Empty string
            ex.printStackTrace();
            return "";

        }
    }

    private boolean isNumeric(String string)
    {
        if (string.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))
        {
            return true;
        }
        else
            return false;
    }

    private boolean isDate(String dateString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try
        {
            sdf.parse(dateString);
            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    private CallResults intToEnum(int val)
    {
        CallResults enumValue = CallResults.values()[val];
        return enumValue;

    }

    private Date intToDate(String dateString)
    {

        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try
        {
            date = sdf.parse(dateString);
            return date;
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
            return null;
        }

    }
    // */

}
