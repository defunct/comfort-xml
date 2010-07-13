package com.goodworkalan.comfort.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import com.goodworkalan.danger.Danger;

/**
 * Executes a runnable that raises a <code>ComfortXMLException</code> that
 * is caught and its properties checked.
 * 
 * @author Alan Gutierrez
 */
public class ComfortXMLExceptionCatcher {
    /** The expected error code. */
    private final int code;

    /** The block of code to run. */
    private final Runnable runnable;

    /**
     * Create an exception catcher.
     * 
     * @param code
     *            The expected error code.
     * @param runnable
     *            The block of code to run.
     */
    public ComfortXMLExceptionCatcher(int code, Runnable runnable) {
        this.code = code;
        this.runnable = runnable;
    }

    /**
     * Run the block and assert that an exception with the correct code is
     * thrown.
     */
    public void run() {
        try {
            runnable.run();
        } catch (Danger e) {
            assertEquals(e.code, code);
            if (e.code.equals(e.getMessage())) {
                fail("No message for error code: " + e.code);
            }
            System.out.println(e.getMessage());
            return;
        }
        fail("Expected exception not thrown.");
    }
}
