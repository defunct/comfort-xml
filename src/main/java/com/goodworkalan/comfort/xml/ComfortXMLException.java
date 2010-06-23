package com.goodworkalan.comfort.xml;

import com.goodworkalan.danger.CodedDanger;

/**
 * A exception for exceptions thrown by the Comfort XML package.
 * 
 * @author Alan Gutierrez
 */
public class ComfortXMLException extends CodedDanger {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** Unable to compile XPath expression  */
    public static final int XPATH_COMPILE = 101;
    
    /** Unable to evaluate XPath expression  */
    public static final int XPATH_EVALUATE = 102;

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
        super(code, cause, arguments);
    }
}
