/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SM-RP-DA ::= CHOICE {
 * imsi [0] IMSI,
 * lmsi [1] LMSI,
 * serviceCentreAddressDA [4] AddressString,
 * noSM-RP-DA [5] NULL}
 * @author eatakishiyev
 */
public class SM_RP_DA {

    private IMSI imsi;
    private LMSI lmsi;
    private AddressStringImpl serviceCenterAddressDA;
    private boolean noSmRpDa;

    public SM_RP_DA() {
    }

    public SM_RP_DA(IMSI imsi) {
        this.imsi = imsi;
    }

    public SM_RP_DA(LMSI lmsi) {
        this.lmsi = lmsi;
    }

    public SM_RP_DA(AddressStringImpl serviceCenterAddressDA) {
        this.serviceCenterAddressDA = serviceCenterAddressDA;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IOException, AsnException, UnexpectedDataException {
        if (this.imsi != null) {
            this.imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else if (this.lmsi != null) {
            this.lmsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        } else if (this.serviceCenterAddressDA != null) {
            this.serviceCenterAddressDA.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
        } else {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 5);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
                throw new IncorrectSyntaxException();
            }

            switch (ais.getTag()) {
                case 0x00:
                    this.imsi = new IMSI();
                    this.imsi.decode(ais);
                    break;
                case 0x01:
                    this.lmsi = new LMSI();
                    this.lmsi.decode(ais);
                    break;
                case 0x04:
                    this.serviceCenterAddressDA = new AddressStringImpl();
                    this.serviceCenterAddressDA.decode(ais);
                    break;
                case 0x05:
                    ais.readNull();
                    this.noSmRpDa = true;
                    break;
                default:
                    throw new IncorrectSyntaxException("Unknown address type");
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());

        }
    }

    /**
     * @return the lmsi
     */
    public LMSI getLmsi() {
        return lmsi;
    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    public AddressStringImpl getServiceCenterAddressDA() {
        return serviceCenterAddressDA;
    }

    public boolean isNoSmRpDa() {
        return noSmRpDa;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SM-RP-DA[");
        if (imsi != null) {
            sb.append(imsi.toString());
        } else if (lmsi != null) {
            sb.append(lmsi.toString());
        } else if (serviceCenterAddressDA != null) {
            sb.append("ServiceCentreAddressDA=").append(serviceCenterAddressDA.toString());
        } else {
            sb.append("NoSM-RP-DA");
        }
        sb.append("]");
        return sb.toString();
    }
}
