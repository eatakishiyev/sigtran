/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * AChChargingAddress {PARAMETERS-BOUND : bound} ::= CHOICE { legID [2] LegID,
 * srfConnection [50] CallSegmentID {bound} }
 *
 * @author eatakishiyev
 */
public class AChChargingAddress {

    private LegId legId;
    private CallSegmentID srfConnection;

    public AChChargingAddress() {
    }

    public AChChargingAddress(LegId legId) {
        this.legId = legId;
    }

    public AChChargingAddress(CallSegmentID srfConnection) {
        this.srfConnection = srfConnection;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (legId != null) {
            legId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        } else {
            srfConnection.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 2:
                    this.legId = LegId.createLegId(ais.readSequenceStream());
                    break;
                case 50:
                    this.srfConnection = new CallSegmentID();
                    this.srfConnection.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the legId
     */
    public LegId getLegId() {
        return legId;
    }

    /**
     * @param legId the legId to set
     */
    public void setLegId(LegId legId) {
        this.legId = legId;
    }

    /**
     * @return the srfConnection
     */
    public CallSegmentID getSrfConnection() {
        return srfConnection;
    }

    /**
     * @param srfConnection the srfConnection to set
     */
    public void setSrfConnection(CallSegmentID srfConnection) {
        this.srfConnection = srfConnection;
    }
}
