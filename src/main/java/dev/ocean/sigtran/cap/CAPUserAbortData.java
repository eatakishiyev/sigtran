/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.cap.parameters.CapUAbortReason;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CAPUserAbortData {

    public static final long[] USER_ABORT_DATA_OID = {0, 4, 0, 0, 1, 1, 2, 2};

    private CapUAbortReason reason;

    public CAPUserAbortData() {
    }

    public CAPUserAbortData(CapUAbortReason reason) {
        this.reason = reason;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, reason.value());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, reason.value());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.reason = CapUAbortReason.getInstance((int) ais.readInteger());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public CapUAbortReason getReason() {
        return reason;
    }

    public void setReason(CapUAbortReason reason) {
        this.reason = reason;
    }
}
