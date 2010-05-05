package com.goodworkalan.comfort.xml;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.danger.CodedDanger;

public class ComfortXMLException extends CodedDanger {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** The cache of resource bundles for the parent exception class. */
    private final static ConcurrentMap<String, ResourceBundle> BUNDLE = new ConcurrentHashMap<String, ResourceBundle>();
    
    /** Unable to compile XPath expression  */
    public static final int XPATH_COMPILE = 101;
    
    /** Unable to evaluate XPath expression  */
    public static final int XPATH_EVALUATE = 102;

    public ComfortXMLException(int code, Throwable cause, Object...arguments) {
        super(BUNDLE, code, cause, arguments);
    }
}
