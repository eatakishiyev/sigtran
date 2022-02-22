/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.general;

import dev.ocean.sigtran.cap.api.CAPMessage;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ApplyChargingReportArg implements CAPMessage {

    private CAMELCallResult camelCallResult;

    public ApplyChargingReportArg() {
    }

    public ApplyChargingReportArg(CAMELCallResult camelCallResult) {
        this.camelCallResult = camelCallResult;
    }

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting TagClass[0] Tag[4], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }

            byte[] data = ais.readOctetString();
            camelCallResult = new CAMELCallResult();
            camelCallResult.decode(new AsnInputStream(data));
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            int lenPos = aos.StartContentDefiniteLength();
            camelCallResult.encode(aos);
            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the cAMELCallResult
     */
    public CAMELCallResult getCAMELCallResult() {
        return camelCallResult;
    }

}
