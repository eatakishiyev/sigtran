/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.isup.enums.ChargeIndicator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtBasicServiceCode;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class AnswerSpecificInfo {

    private CalledPartyNumber destinationAddress;
    private boolean orCall = false;
    private boolean forwardedCall = false;
    private ChargeIndicator chargeIndicator;
    private ExtBasicServiceCode extBasicServiceCode;
    private ExtBasicServiceCode extBasicServiceCode2;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            if (destinationAddress != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                baos.write(destinationAddress.encode());

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 50);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (orCall) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 51);
            }

            if (forwardedCall) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 52);
            }

            if (chargeIndicator != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 53, chargeIndicator.ordinal());
            }

            if (extBasicServiceCode != null) {
                extBasicServiceCode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 54, aos);
            }

            if (extBasicServiceCode2 != null) {
                extBasicServiceCode2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 55, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode_(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 50:
                        byte[] data = new byte[ais.readLength()];
                        ais.read(data);

                        this.destinationAddress = new CalledPartyNumber();
                        this.destinationAddress.decode((data));
                        break;
                    case 51:
                        this.orCall = true;
                        ais.readNull();
                        break;
                    case 52:
                        this.forwardedCall = true;
                        ais.readNull();
                        break;
                    case 53:
                        this.chargeIndicator = ChargeIndicator.getInstance((int) ais.readInteger());
                        break;
                    case 54:
                        this.extBasicServiceCode = new ExtBasicServiceCode();
                        this.extBasicServiceCode.decode(ais);
                        break;
                    case 55:
                        this.extBasicServiceCode2 = new ExtBasicServiceCode();
                        this.extBasicServiceCode2.decode(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Received unexpected tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the destinationAddress
     */
    public CalledPartyNumber getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(CalledPartyNumber destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the orCall
     */
    public boolean isOrCall() {
        return orCall;
    }

    /**
     * @param orCall the orCall to set
     */
    public void setOrCall(boolean orCall) {
        this.orCall = orCall;
    }

    /**
     * @return the forwardedCall
     */
    public boolean isForwardedCall() {
        return forwardedCall;
    }

    /**
     * @param forwardedCall the forwardedCall to set
     */
    public void setForwardedCall(boolean forwardedCall) {
        this.forwardedCall = forwardedCall;
    }

    /**
     * @return the chargeIndicator
     */
    public ChargeIndicator getChargeIndicator() {
        return chargeIndicator;
    }

    /**
     * @param chargeIndicator the chargeIndicator to set
     */
    public void setChargeIndicator(ChargeIndicator chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    /**
     * @return the extBasicServiceCode
     */
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    /**
     * @param extBasicServiceCode the extBasicServiceCode to set
     */
    public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode) {
        this.extBasicServiceCode = extBasicServiceCode;
    }

    /**
     * @return the extBasicServiceCode2
     */
    public ExtBasicServiceCode getExtBasicServiceCode2() {
        return extBasicServiceCode2;
    }

    /**
     * @param extBasicServiceCode2 the extBasicServiceCode2 to set
     */
    public void setExtBasicServiceCode2(ExtBasicServiceCode extBasicServiceCode2) {
        this.extBasicServiceCode2 = extBasicServiceCode2;
    }

}
