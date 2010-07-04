package com.goodworkalan.comfort.xml;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A exception for exceptions thrown by the Comfort XML package.
 * 
 * @author Alan Gutierrez
 */
public class ComfortXMLException extends RuntimeException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** Unable to compile XPath expression  */
    public static final int XPATH_COMPILE = 101;
    
    /** Unable to evaluate XPath expression  */
    public static final int XPATH_EVALUATE = 102;

    /** The error code. */
    private final int code;
    
    /** The error message. */
    private final String message;

    /**
     * Create an exception with the given <code>code</code> that wraps the given
     * <code>cause</code> and formats an error message from an exception message
     * with the given <code>arguments</code>.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     * @param arguments
     *            The positioned message format arguments.
     */
    public ComfortXMLException(int code, Throwable cause, Object...arguments) {
        super(null, cause);
        this.code = code;
        this.message = formatMessage(code, arguments);
    }
    
    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the error message.
     * 
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Format the exception message using the message arguments to format the
     * message found with the message key in the message bundle found in the
     * package of the given context class.
     * 
     * @param contextClass
     *            The context class.
     * @param code
     *            The error code.
     * @param arguments
     *            The format message arguments.
     * @return The formatted message.
     */
    private String formatMessage(int code, Object...arguments) {
        String baseName = getClass().getPackage().getName() + ".exceptions";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
            return String.format((String) bundle.getObject(Integer.toString(code)), arguments);
        } catch (Exception e) {
            return String.format("Cannot load message key [%s] from bundle [%s] becuase [%s].", code, baseName, e.getMessage());
        }
    }
}
