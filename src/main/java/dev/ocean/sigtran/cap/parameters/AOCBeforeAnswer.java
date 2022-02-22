/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * AOCBeforeAnswer ::= SEQUENCE { aOCInitial [0] CAI-GSM0224, aOCSubsequent [1]
 * AOCSubsequent OPTIONAL }
 *
 * @author eatakishiyev
 */
public class AOCBeforeAnswer {

    private CAI_GSM0224 aOCInitial;
    private AOCSubsequent aOCSubsequent;

    public AOCBeforeAnswer() {
    }

    public AOCBeforeAnswer(CAI_GSM0224 aOCInitial) {
        this.aOCInitial = aOCInitial;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        if (this.aOCInitial == null) {
            throw new AsnException("Expecting mandatory AOCInitial parameter.");
        }

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.aOCInitial.encode(0, Tag.CLASS_CONTEXT_SPECIFIC, aos);

        if (aOCSubsequent != null) {
            aOCSubsequent.encode(1, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(length);
        this._decode(tmpAis);
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.aOCInitial = new CAI_GSM0224();
                    this.aOCInitial.decode(ais);
                    break;
                case 1:
                    this.aOCSubsequent = new AOCSubsequent();
                    this.aOCSubsequent.decode(ais);
                    break;
            }
        }

        if (this.aOCInitial == null) {
            throw new AsnException("Mandatory AOCInitial parameter expected.");
        }
    }

    /**
     * @return the aOCSubsequent
     */
    public AOCSubsequent getaOCSubsequent() {
        return aOCSubsequent;
    }

    /**
     * @param aOCSubsequent the aOCSubsequent to set
     */
    public void setaOCSubsequent(AOCSubsequent aOCSubsequent) {
        this.aOCSubsequent = aOCSubsequent;
    }

    public CAI_GSM0224 getAOCInitial() {
        return this.aOCInitial;
    }

}
