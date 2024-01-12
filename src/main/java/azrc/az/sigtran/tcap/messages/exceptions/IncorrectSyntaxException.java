/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages.exceptions;

import azrc.az.sigtran.tcap.primitives.tc.PAbortCause;

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
