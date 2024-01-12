/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.exception;

import azrc.az.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public class RoutingFailureException extends Exception {

    private String msg;
    private ErrorReason sccpErrorReason;

    public RoutingFailureException(String msg, ErrorReason sccpErrorReason) {
        this.msg = msg;
        this.sccpErrorReason = sccpErrorReason;
    }

    public RoutingFailureException(String msg) {
        this.msg = msg;
    }

    public RoutingFailureException(ErrorReason sccpErrorReason) {
        this.sccpErrorReason = sccpErrorReason;
    }

    /**
     * @return the sccpErrorReason
     */
    public ErrorReason getSccpErrorReason() {
        return sccpErrorReason;
    }

    public String getMessage() {
        return this.msg;
    }

}
