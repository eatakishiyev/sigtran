/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.isup.parameters.GenericDigits;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * VariablePart {PARAMETERS-BOUND : bound} ::= CHOICE { integer [0] Integer4,
 * number [1] Digits {bound}, -- Generic digits time [2] OCTET STRING (SIZE(2)),
 * -- HH: MM, BCD coded date [3] OCTET STRING (SIZE(4)), -- YYYYMMDD, BCD coded
 * price [4] OCTET STRING (SIZE(4)) } -- Indicates the variable part of the
 * message. Time is BCD encoded. -- The most significant hours digit occupies
 * bits 0-3 of the first octet, and the least -- significant digit occupies bits
 * 4-7 of the first octet. The most significant minutes digit -- occupies bits
 * 0-3 of the second octet, and the least significant digit occupies bits 4-7 --
 * of the second octet. -- -- Date is BCD encoded. The year digit indicating
 * millenium occupies bits 0-3 of the first octet, -- and the year digit
 * indicating century occupies bits 4-7 of the first octet. The year digit --
 * indicating decade occupies bits 0-3 of the second octet, whilst the digit
 * indicating the year -- within the decade occupies bits 4-7 of the second
 * octet. -- The most significant month digit occupies bits 0-3 of the third
 * octet, and the least -- significant month digit occupies bits 4-7 of the
 * third octet. The most significant day digit -- occupies bits 0-3 of the
 * fourth octet, and the least significant day digit occupies bits 4-7 -- of the
 * fourth octet. -- Price is BCD encoded. The digit indicating hundreds of
 * thousands occupies bits 0-3 of the -- first octet, and the digit indicating
 * tens of thousands occupies bits 4-7 of the first octet. -- The digit
 * indicating thousands occupies bits 0-3 of the second octet, whilst the digit
 * -- indicating hundreds occupies bits 4-7 of the second octet. The digit
 * indicating tens occupies -- bits 0-3 of the third octet, and the digit
 * indicating 0 to 9 occupies bits 4-7 of the third -- octet. The tenths digit
 * occupies bits 0-3 of the fourth octet, and the hundredths digit -- occupies
 * bits 4-7 of the fourth octet. -- -- For the encoding of digits in an octet,
 * refer to the timeAndtimezone parameter
 *
 * @author eatakishiyev
 */
public class VariablePart {

    private Integer4 integer;
    private GenericDigits number;//Generic Digits
    private byte[] time;//HH:MM, BCD coded
    private byte[] date;//YYYYMMDD, BCD coded
    private byte[] price;

    private VariablePart() {

    }

    public static VariablePart create() {
        VariablePart instance = new VariablePart();
        return instance;
    }

    public static VariablePart createInteger(Integer4 integer) {
        VariablePart instance = new VariablePart();
        instance.integer = integer;
        return instance;
    }

    public static VariablePart createNumber(GenericDigits number) {
        VariablePart instance = new VariablePart();
        instance.number = number;
        return instance;
    }

    public static VariablePart createTime(byte[] time) {
        VariablePart instance = new VariablePart();
        instance.time = time;
        return instance;
    }

    public static VariablePart createDate(byte[] date) {
        VariablePart instance = new VariablePart();
        instance.date = date;
        return instance;
    }

    public static VariablePart createPrice(byte[] price) {
        VariablePart instance = new VariablePart();
        instance.price = price;
        return instance;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.encode(aos);

        aos.FinalizeContent(lenPos);
    }

    public void encode(AsnOutputStream aos) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        if (integer != null) {
            integer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }

        if (number != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, number.encode());
        }

        if (time != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 2, time);
        }

        if (date != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, date);
        }

        if (price != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, price);
        }

    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.integer = new Integer4();
                    this.integer.decode(ais);
                    break;
                case 1:
                    this.number = new GenericDigits();
                    this.number.decode(ais.readOctetString());
                    break;
                case 2:
                    this.time = ais.readOctetString();
                    break;
                case 3:
                    this.date = ais.readOctetString();
                    break;
                case 4:
                    this.price = ais.readOctetString();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * @return the integer
     */
    public Integer4 getInteger() {
        return integer;
    }

    /**
     * @return the number
     */
    public GenericDigits getNumber() {
        return number;
    }

    /**
     * @return the time
     */
    public byte[] getTime() {
        return time;
    }

    /**
     * @return the date
     */
    public byte[] getDate() {
        return date;
    }

    /**
     * @return the price
     */
    public byte[] getPrice() {
        return price;
    }

}
