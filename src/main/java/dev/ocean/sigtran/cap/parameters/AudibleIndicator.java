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
 * AudibleIndicator ::= CHOICE { tone BOOLEAN, burstList [1] BurstList }
 *
 * @author eatakishiyev
 */
public class AudibleIndicator {

    private Boolean tone;
    private BurstList burstList;

    public AudibleIndicator() {
    }

    public AudibleIndicator(Boolean tone) {
        this.tone = tone;
    }

    public AudibleIndicator(BurstList bursList) {
        this.burstList = bursList;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        this.doCheck();

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (tone != null) {
            aos.writeBoolean(tone);
        } else {
            this.burstList.encode(1, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    public void decode(AsnInputStream ais, int legth) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(legth);
        this._decode(tmpAis);
    }

    private void _decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass == Tag.CLASS_UNIVERSAL && tag == Tag.BOOLEAN) {
                this.tone = ais.readBoolean();
            } else if (tagClass == Tag.CLASS_CONTEXT_SPECIFIC && tag == 1) {
                this.burstList = new BurstList();
                this.burstList.decode(ais);
            }
        }

        this.doCheck();
    }

    private void doCheck() throws AsnException {
        if (tone == null && burstList == null) {
            throw new AsnException("Make correct choice. One of selections must be selected");
        }
    }

    /**
     * @return the tone
     */
    public Boolean getTone() {
        return tone;
    }

    /**
     * @return the burstList
     */
    public BurstList getBurstList() {
        return burstList;
    }
}
