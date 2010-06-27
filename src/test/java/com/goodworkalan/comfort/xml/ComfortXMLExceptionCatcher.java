package com.goodworkalan.comfort.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

// TODO Document.
public class ComfortXMLExceptionCatcher {
    // TODO Document.
    private final int code;

    // TODO Document.
    private final Runnable runnable;

    // TODO Document.
    public ComfortXMLExceptionCatcher(int code, Runnable runnable) {
        this.code = code;
        this.runnable = runnable;
    }

    // TODO Document.
    public void run() {
        try {
            runnable.run();
        } catch (ComfortXMLException e) {
            assertEquals(e.getCode(), code);
            if (Integer.toString(e.getCode()).equals(e.getMessage())) {
                fail("No message for error code: " + e.getCode());
            }
            System.out.println(e.getMessage());
            return;
        }
        fail("Expected exception not thrown.");
    }
}
