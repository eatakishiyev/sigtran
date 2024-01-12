/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.gsm.string.utils.bcd.BCDStringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * CalledPartyBCDNumber {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
 * bound.&minCalledPartyBCDNumberLength ..
 * bound.&maxCalledPartyBCDNumberLength)) -- Indicates the Called Party Number,
 * including service selection information. -- Refer to 3GPP TS 24.008 [9] for
 * encoding. -- This data type carries only the 'type of number', 'numbering
 * plan -- identification' and 'number digit' fields defined in 3GPP TS 24.008
 * [9]; -- it does not carry the 'called party BCD number IEI' or 'length of
 * called -- party BCD number contents'. -- In the context of the
 * DestinationSubscriberNumber field in ConnectSMSArg or -- InitialDPSMSArg, a
 * CalledPartyBCDNumber may also contain an alphanumeric -- character string. In
 * this case, type-of-number '101'B is used, in accordance -- with 3GPP TS
 * 23.040 [6]. The address is coded in accordance with the -- GSM 7-bit default
 * alphabet definition and the SMS packing rules -- as specified in 3GPP TS
 * 23.038 [15] in this case.
 *
 * @author eatakishiyev
 */
public class CalledPartyBCDNumber {

    private TypeOfNumber typeOfNumber;
    private NumberingPlanIdentification numberingPlanIdentification;
    private String address;

    public CalledPartyBCDNumber() {
    }

    public CalledPartyBCDNumber(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification, String address) {
        this.typeOfNumber = typeOfNumber;
        this.numberingPlanIdentification = numberingPlanIdentification;
        this.address = address;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.encode(baos);
        aos.write(baos.toByteArray());
        aos.FinalizeContent(lenPos);
    }

    public void encode(ByteArrayOutputStream baos) throws IOException, AsnException {
        int b = 1;//ext bit
        b = (b << 3) | this.typeOfNumber.value();
        b = (b << 4) | this.numberingPlanIdentification.value();

        baos.write(b);
        BCDStringUtils.toTBCDString(address, baos);

    }

    public void decode(AsnInputStream ais) throws IOException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);
        this.decode(new ByteArrayInputStream(data));
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        int b = bais.read() & 0xFF;

        this.typeOfNumber = TypeOfNumber.getInstance((b >> 4) & 0b00000111);
        this.numberingPlanIdentification = NumberingPlanIdentification.getInstance(b & 0b00001111);
        this.address = BCDStringUtils.fromTBCDToString(bais);

    }

    /**
     * @return the typeOfNumber
     */
    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }

    /**
     * @return the numberingPlanIdentification
     */
    public NumberingPlanIdentification getNumberingPlanIdentification() {
        return numberingPlanIdentification;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    public enum TypeOfNumber {

        UNKNOWN(0),
        INTERNATIONAL_NUMBER(1),
        NATIONAL_NUMBER(2),
        NETWORK_SPECIFIC_NUMBER(3),
        DEDICATED_ACCESS_SHORT_CODE(4),
        RESERVED101(5),
        RESERVED110(6),
        RESERVED111(7);

        private int value;

        private TypeOfNumber(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static TypeOfNumber getInstance(int value) {
            switch (value) {
                case 0:
                    return UNKNOWN;
                case 1:
                    return INTERNATIONAL_NUMBER;
                case 2:
                    return NATIONAL_NUMBER;
                case 3:
                    return NETWORK_SPECIFIC_NUMBER;
                case 4:
                    return DEDICATED_ACCESS_SHORT_CODE;
                case 5:
                    return RESERVED101;
                case 6:
                    return RESERVED110;
                case 7:
                    return RESERVED111;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum NumberingPlanIdentification {

        UNKNOWN(0b00000000),
        ISDN_TELEPHONY(0b00000001),
        DATA_NUMBERING_PLAN(0b00000011),
        TELEX_NUMBERING_PLAN(0b00000100),
        NATIONAL_NUMBERING_PLAN(0b00001000),
        PRIVATE_NUMBERING_PLAN(0b00001001),
        RESERVED_FOR_CTS(0b00001011),
        RESERVED_FOR_EXTENSION(0b00001111);

        private int value;

        private NumberingPlanIdentification(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static NumberingPlanIdentification getInstance(int value) {
            switch (value) {
                case 0b00000000:
                    return UNKNOWN;
                case 0b00000001:
                    return ISDN_TELEPHONY;
                case 0b00000011:
                    return DATA_NUMBERING_PLAN;
                case 0b00000100:
                    return TELEX_NUMBERING_PLAN;
                case 0b00001000:
                    return NATIONAL_NUMBERING_PLAN;
                case 0b00001001:
                    return PRIVATE_NUMBERING_PLAN;
                case 0b00001011:
                    return RESERVED_FOR_CTS;
                case 0b00001111:
                    return RESERVED_FOR_EXTENSION;
                default:
                    return UNKNOWN;

            }
        }

    }
}
