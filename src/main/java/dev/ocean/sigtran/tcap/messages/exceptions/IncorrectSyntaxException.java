/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages.exceptions;

import dev.ocean.sigtran.tcap.primitives.tc.PAbortCause;

/**
 *
 * @author eatakishiyev
 */
public class IncorrectSyntaxException extends Exception {

    private PAbortCause abortCause;

    public IncorrectSyntaxException() {
    }

    public IncorrectSyntaxException(String message) {
        super(message);
    }

    /**
     * @return the errorCode
     */
    public PAbortCause getPAbortCause() {
        return abortCause;
    }
}
