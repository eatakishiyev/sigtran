/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ExtForwOptions implements MAPParameter {

    /*
     * Ext-ForwOptions ::= OCTET STRING (SIZE (1..5))      *
     * -- OCTET 1:      *
     * -- bit 8: notification to forwarding party 
     * -- 0 no notification 
     * -- 1 notification      *
     * -- bit 7: redirecting presentation 
     * -- 0 no presentation 
     * -- 1 presentation      *
     * -- bit 6: notification to calling party 
     * -- 0 no notification 
     * -- 1 notification      *
     * -- bit 5: 0 (unused)      *
     * -- bits 43: forwarding reason 
     * -- 00 ms not reachable 
     * -- 01 ms busy 
     * -- 10 no reply 
     * -- 11 unconditional      *
     * -- bits 21: 00 (unused)      *
     * -- OCTETS 2-5: reserved for future use. They shall be discarded if --
     * received and not understood.
     */
    private boolean notificationToForwardingParty;
    private boolean redirectionPresentation;
    private boolean notificationtoCallingParty;
    private ForwardingReason forwardingReason;

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        int firstOctet = (notificationToForwardingParty ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | (redirectionPresentation ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | (notificationtoCallingParty ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | 0;// not used;
        firstOctet = firstOctet << 2;
        firstOctet = firstOctet | forwardingReason.getValue();
        firstOctet = firstOctet << 2;
        aos.writeTag(tagClass, true, tag);
        aos.writeLength(1);
        aos.write(firstOctet);
    }

    public void encode(ByteArrayOutputStream baos) {
        int firstOctet = (notificationToForwardingParty ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | (redirectionPresentation ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | (notificationtoCallingParty ? 1 : 0);
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | 0;// not used;
        firstOctet = firstOctet << 2;
        firstOctet = firstOctet | forwardingReason.getValue();
        firstOctet = firstOctet << 2;
        baos.write(firstOctet);
    }

    public void decode(ByteArrayInputStream bais) {
        int firstOctet = bais.read();
        this.notificationToForwardingParty = ((firstOctet >> 7) & 0x01) == 1;
        this.redirectionPresentation = ((firstOctet >> 6) & 0x01) == 1;
        this.notificationtoCallingParty = ((firstOctet >> 5) & 0x01) == 1;
        this.forwardingReason = ForwardingReason.getInstance((firstOctet >> 2) & 0x03);
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        int length = ais.readLength();
        int firstOctet = ais.read();
        this.notificationToForwardingParty = ((firstOctet >> 7) & 0x01) == 1;
        this.redirectionPresentation = ((firstOctet >> 6) & 0x01) == 1;
        this.notificationtoCallingParty = ((firstOctet >> 5) & 0x01) == 1;
        this.forwardingReason = ForwardingReason.getInstance((firstOctet >> 2) & 0x03);
    }

    public boolean isNotificationToForwardingParty() {
        return notificationToForwardingParty;
    }

    public boolean isNotificationtoCallingParty() {
        return notificationtoCallingParty;
    }

    public boolean isRedirectionPresentation() {
        return redirectionPresentation;
    }

    public ForwardingReason getForwardingReason() {
        return forwardingReason;
    }

    public void setForwardingReason(ForwardingReason forwardingReason) {
        this.forwardingReason = forwardingReason;
    }

    public void setNotificationToForwardingParty(boolean notificationToForwardingParty) {
        this.notificationToForwardingParty = notificationToForwardingParty;
    }

    public void setNotificationtoCallingParty(boolean notificationtoCallingParty) {
        this.notificationtoCallingParty = notificationtoCallingParty;
    }

    public void setRedirectionPresentation(boolean redirectionPresentation) {
        this.redirectionPresentation = redirectionPresentation;
    }

}
