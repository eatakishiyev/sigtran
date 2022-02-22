/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.Serializable;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.TCUser;

/**
 *
 * @author root
 */
public interface SCCPUser extends Serializable {

    /**
     * Method calling by underlying layer (M3UA/MTP3 etc.) when incoming data
     * arrived for SCCP Layer. This is MTP-TRANSFER Indication
     *
     * @param calledParty
     * @param callingParty
     * @param userData
     * @param sequenceControl
     * @param sequenceNumber
     * @param messageHandling
     */
    public void onMessage(SCCPAddress calledParty, SCCPAddress callingParty, byte[] userData, boolean sequenceControl, Integer sequenceNumber, MessageHandling messageHandling);

    /**
     * Method calling by or SCCP Routing Control component when translation
     * fails due to prohibited Remote PC/ Remote SSN.
     *
     * @param calledParty
     * @param callingParty
     * @param userData
     * @param errorReason
     * @param importance
     */
    public void onNotice(SCCPAddress calledParty, SCCPAddress callingParty, byte[] userData, ErrorReason errorReason, int importance);

    public void setUser(TCUser tcUser);
//

    public TCUser getUser();

    public void setSccpProvider(SCCPProvider sccpProvider);

}
