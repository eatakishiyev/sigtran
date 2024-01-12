/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tr;

import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public class TRNotice {

    private Long origTransactionId;
    private SCCPAddress callingParty;
    private SCCPAddress calledParty;
    private ErrorReason errorReason;

    public TRNotice(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    /**
     * @return the origTransactionId
     */
    public Long getOrigTransactionId() {
        return origTransactionId;
    }

    /**
     * @param origTransactionId the origTransactionId to set
     */
    public void setOrigTransactionId(Long origTransactionId) {
        this.origTransactionId = origTransactionId;
    }

    /**
     * @return the callingParty
     */
    public SCCPAddress getCallingParty() {
        return callingParty;
    }

    /**
     * @param callingParty the callingParty to set
     */
    public void setCallingParty(SCCPAddress callingParty) {
        this.callingParty = callingParty;
    }

    /**
     * @return the calledParty
     */
    public SCCPAddress getCalledParty() {
        return calledParty;
    }

    /**
     * @param calledParty the calledParty to set
     */
    public void setCalledParty(SCCPAddress calledParty) {
        this.calledParty = calledParty;
    }

    /**
     * @return the errorReason
     */
    public ErrorReason getErrorReason() {
        return errorReason;
    }

    /**
     * @param errorReason the errorReason to set
     */
    public void setErrorReason(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }
    
}
