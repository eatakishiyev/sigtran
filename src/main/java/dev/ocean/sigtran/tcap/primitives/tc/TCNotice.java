/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.primitives.ReportCause;

/**
 *
 * @author root
 */
public class TCNotice {

    private TCAPDialogue dialogue;
    private SCCPAddress originatingAddress;//callingParty
    private SCCPAddress destinationAddress;//calledParty
    private ReportCause reportCause;
    private boolean lastComponent;

    /**
     * @return the dialogue
     */
    public TCAPDialogue getDialogue() {
        return dialogue;
    }

    /**
     * @param dialogue the dialogue to set
     */
    public void setDialogue(TCAPDialogue dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * @return the originatingAddress
     */
    public SCCPAddress getOriginatingAddress() {
        return originatingAddress;
    }

    /**
     * @param originatingAddress the originatingAddress to set
     */
    public void setOriginatingAddress(SCCPAddress originatingAddress) {
        this.originatingAddress = originatingAddress;
    }

    /**
     * @return the destinationAddress
     */
    public SCCPAddress getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(SCCPAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the reportCause
     */
    public ReportCause getReportCause() {
        return reportCause;
    }

    /**
     * @param reportCause the reportCause to set
     */
    public void setReportCause(ReportCause reportCause) {
        this.reportCause = reportCause;
    }

    /**
     * @return the lastComponent
     */
    public boolean isLastComponent() {
        return lastComponent;
    }

    /**
     * @param lastComponent the lastComponent to set
     */
    public void setLastComponent(boolean lastComponent) {
        this.lastComponent = lastComponent;
    }
}
