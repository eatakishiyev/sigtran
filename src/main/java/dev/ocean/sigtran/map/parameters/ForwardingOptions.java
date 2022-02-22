/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ForwardingOptions ::= OCTET STRING (SIZE (1))
 * -- bit 8: notification to forwarding party
 * -- 0 no notification
 * -- 1 notification
 * -- bit 7: redirecting presentation
 * -- 0 no presentation
 * -- 1 presentation
 * -- bit 6: notification to calling party
 * -- 0 no notification
 * -- 1 notification
 * -- bit 5: 0 (unused)
 * -- bits 43: forwarding reason
 * -- 00 ms not reachable
 * -- 01 ms busy
 * -- 10 no reply
 * -- 11 unconditional when used in a SRI Result,
 * -- or call deflection when used in a RCH Argument
 * -- bits 21: 00 (unused)
 * @author eatakishiyev
 */
public class ForwardingOptions  implements MAPParameter{

    private boolean notificationToForwardingParty;
    private boolean redirectingPresentation;
    private boolean notificationToCallingParty;
    private ForwardingReason forwardingReason;

    public ForwardingOptions() {
    }

    public ForwardingOptions(boolean notificationToForwardingParty, boolean redirectingPresentation, boolean notificationToCallingParty, ForwardingReason forwardingReason) {
        this.notificationToForwardingParty = notificationToForwardingParty;
        this.redirectingPresentation = redirectingPresentation;
        this.notificationToCallingParty = notificationToCallingParty;
        this.forwardingReason = forwardingReason;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            int b = this.notificationToForwardingParty ? 1 : 0;
            b = (b << 1) | (redirectingPresentation ? 1 : 0);
            b = (b << 1) | (notificationToCallingParty ? 1 : 0);
            b = b << 1;//bit 5 unused
            b = (b << 2) | forwardingReason.getValue();
            b = (b << 2);
            aos.writeOctetString(tagClass, tag, new byte[]{(byte) b});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte b = ais.readOctetString()[0];
            this.notificationToForwardingParty = (b >> 7) == 1;
            this.redirectingPresentation = ((b >> 6) & 1) == 1;
            this.notificationToCallingParty = ((b >> 5) & 1) == 1;
            this.forwardingReason = ForwardingReason.getInstance((b >> 2) & 3);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the notificationToForwardingParty
     */
    public boolean isNotificationToForwardingParty() {
        return notificationToForwardingParty;
    }

    /**
     * @param notificationToForwardingParty the notificationToForwardingParty to
     * set
     */
    public void setNotificationToForwardingParty(boolean notificationToForwardingParty) {
        this.notificationToForwardingParty = notificationToForwardingParty;
    }

    /**
     * @return the redirectingPresentation
     */
    public boolean isRedirectingPresentation() {
        return redirectingPresentation;
    }

    /**
     * @param redirectingPresentation the redirectingPresentation to set
     */
    public void setRedirectingPresentation(boolean redirectingPresentation) {
        this.redirectingPresentation = redirectingPresentation;
    }

    /**
     * @return the notificationToCallingParty
     */
    public boolean isNotificationToCallingParty() {
        return notificationToCallingParty;
    }

    /**
     * @param notificationToCallingParty the notificationToCallingParty to set
     */
    public void setNotificationToCallingParty(boolean notificationToCallingParty) {
        this.notificationToCallingParty = notificationToCallingParty;
    }

    /**
     * @return the forwardingReason
     */
    public ForwardingReason getForwardingReason() {
        return forwardingReason;
    }

    /**
     * @param forwardingReason the forwardingReason to set
     */
    public void setForwardingReason(ForwardingReason forwardingReason) {
        this.forwardingReason = forwardingReason;
    }

}
