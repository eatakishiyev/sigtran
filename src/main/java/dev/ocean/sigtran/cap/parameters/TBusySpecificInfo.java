/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class TBusySpecificInfo {

    private Cause cause;
    private boolean callForwarded = false;
    private boolean routeNotPermitted = false;
    private CalledPartyNumber forwardingDestinationNumber;

    public TBusySpecificInfo() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IllegalNumberFormatException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (cause != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            cause.encode(baos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
            int lenPos_ = aos.StartContentDefiniteLength();
            aos.write(baos.toByteArray());
            aos.FinalizeContent(lenPos_);
        }

        if (callForwarded) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 50);
        }

        if (routeNotPermitted) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 51);
        }

        if (forwardingDestinationNumber != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            baos.write(forwardingDestinationNumber.encode());

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 52);
            int lenPos_ = aos.StartContentDefiniteLength();
            aos.write(baos.toByteArray());
            aos.FinalizeContent(lenPos_);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();

        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Invalid tag received. Expecting TagClass[2], found TagClass[%s]", tmpAis.getTagClass()));
            }

            switch (tag) {
                case 0:
                    byte[] data = new byte[tmpAis.readLength()];
                    tmpAis.read(data);

                    this.cause = new Cause();
                    this.cause.decode(new ByteArrayInputStream(data));
                    break;
                case 50:
                    this.callForwarded = true;
                    tmpAis.readNull();
                    break;
                case 51:
                    this.routeNotPermitted = true;
                    tmpAis.readNull();
                    break;
                case 52:
                    data = new byte[tmpAis.readLength()];
                    tmpAis.read(data);

                    this.forwardingDestinationNumber = new CalledPartyNumber();
                    this.forwardingDestinationNumber.decode((data));
                    break;
                default:
                    throw new AsnException(String.format("Invalid tag received. Tag[%s] TagClass[%s]", tag, tmpAis.getTagClass()));
            }

        }
    }

    /**
     * @return the cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    /**
     * @return the callForwarded
     */
    public boolean isCallForwarded() {
        return callForwarded;
    }

    /**
     * @param callForwarded the callForwarded to set
     */
    public void setCallForwarded(boolean callForwarded) {
        this.callForwarded = callForwarded;
    }

    /**
     * @return the routeNotPermitted
     */
    public boolean isRouteNotPermitted() {
        return routeNotPermitted;
    }

    /**
     * @param routeNotPermitted the routeNotPermitted to set
     */
    public void setRouteNotPermitted(boolean routeNotPermitted) {
        this.routeNotPermitted = routeNotPermitted;
    }

    /**
     * @return the forwardingDestinationNumber
     */
    public CalledPartyNumber getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    /**
     * @param forwardingDestinationNumber the forwardingDestinationNumber to set
     */
    public void setForwardingDestinationNumber(CalledPartyNumber forwardingDestinationNumber) {
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

}
