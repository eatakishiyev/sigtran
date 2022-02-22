/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs)
 *
 * @author eatakishiyev
 */
public class CallSegmentID {

    private int segmentId;

    public CallSegmentID() {
    }

    public CallSegmentID(int value) {
        this.segmentId = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeInteger(tagClass, tag, segmentId);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.segmentId = (int) ais.readInteger();
    }

    /**
     * @return the value
     */
    public int getSegmentId() {
        return segmentId;
    }

    /**
     * @param segmentId the value to set
     */
    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

}
