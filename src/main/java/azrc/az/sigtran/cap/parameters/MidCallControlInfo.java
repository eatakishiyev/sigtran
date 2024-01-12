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
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MidCallControlInfo ::= SEQUENCE { minimumNumberOfDigits [0] INTEGER (1..30)
 * DEFAULT 1, maximumNumberOfDigits [1] INTEGER (1..30) DEFAULT 30,
 * endOfReplyDigit [2] OCTET STRING (SIZE (1..2)) OPTIONAL, cancelDigit [3]
 * OCTET STRING (SIZE (1..2)) OPTIONAL, startDigit [4] OCTET STRING (SIZE
 * (1..2)) OPTIONAL, interDigitTimeout [6] INTEGER (1..127) DEFAULT 10, ... } --
 * -- - minimumNumberOfDigits specifies the minumum number of digits that shall
 * be collected -- - maximumNumberOfDigits specifies the maximum number of
 * digits that shall be collected -- - endOfReplyDigit specifies the digit
 * string that denotes the end of the digits -- to be collected. -- -
 * cancelDigit specifies the digit string that indicates that the input shall --
 * be erased and digit collection shall start afresh. -- - startDigit specifies
 * the digit string that denotes the start of the digits -- to be collected. --
 * - interDigitTimeout specifies the maximum duration in seconds between
 * successive -- digits. -- -- endOfReplyDigit, cancelDigit and startDigit shall
 * contain digits in the range 0..9, '*' and '#' -- only. The collected digits
 * string, reported to the gsmSCF, shall include the endOfReplyDigit and -- the
 * startDigit, if present. -- -- endOfReplyDigit, cancelDigit and startDigit
 * shall be encoded as BCD digits. Each octet shall -- contain one BCD digit, in
 * the 4 least significant bits of each octet. -- The following encoding shall
 * be used for the over-decadic digits: 1011 (*), 1100 (#).
 *
 * @author eatakishiyev
 */
public class MidCallControlInfo {

    private Integer minimumNumberOfDigits = 1;
    private Integer maximumNumberOfDigits = 30;
    private String endOfReplyDigit;
    private String cancelDigit;
    private String startDigit;
    private Integer interDigitTimeout = 10;

    public static final int MIN_NUMBER_OF_DIGITS = 1;
    public static final int MAX_NUMBER_OF_DIGITS = 30;
    public static final int MAX_INTER_DIGIT_TIMEOUT = 127;

    private final int MIN_OCTET_STRING_LENGTH = 1;
    private final int MAX_OCTET_STRING_LENGTH = 2;

    public MidCallControlInfo() {
    }

    public MidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, Integer interDigitTimeout) {
        this.minimumNumberOfDigits = minimumNumberOfDigits;
        this.maximumNumberOfDigits = maximumNumberOfDigits;
        this.interDigitTimeout = interDigitTimeout;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        this.doCheck();
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, minimumNumberOfDigits);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, maximumNumberOfDigits);
        if (endOfReplyDigit != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            BCDStringUtils.toTBCDString(endOfReplyDigit, baos);
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 2, baos.toByteArray());
        }
        if (cancelDigit != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            BCDStringUtils.toTBCDString(cancelDigit, baos);
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, baos.toByteArray());
        }
        if (startDigit != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            BCDStringUtils.toTBCDString(startDigit, baos);
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, baos.toByteArray());
        }

        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 6, interDigitTimeout);

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
            int length = ais.readLength();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.minimumNumberOfDigits = ((Long) ais.readIntegerData(length)).intValue();
                    break;
                case 1:
                    this.maximumNumberOfDigits = ((Long) ais.readIntegerData(length)).intValue();
                    break;
                case 2:
                    if (length < MIN_OCTET_STRING_LENGTH
                            || length > MAX_OCTET_STRING_LENGTH) {
                        throw new ParameterOutOfRangeException("Octet string length is out of range [1..2].");
                    }

                    byte[] octetData = ais.readOctetStringData(length);
                    this.endOfReplyDigit = BCDStringUtils.fromBCDToString(new ByteArrayInputStream(octetData));
                    break;
                case 3:
                    if (length < MIN_OCTET_STRING_LENGTH
                            || length > MAX_OCTET_STRING_LENGTH) {
                        throw new ParameterOutOfRangeException("Octet string length is out of range [1..2].");
                    }
                    octetData = ais.readOctetStringData(length);
                    this.cancelDigit = BCDStringUtils.fromBCDToString(new ByteArrayInputStream(octetData));
                    break;
                case 4:
                    if (length < MIN_OCTET_STRING_LENGTH
                            || length > MAX_OCTET_STRING_LENGTH) {
                        throw new ParameterOutOfRangeException("Octet string length is out of range [1..2].");
                    }
                    octetData = ais.readOctetStringData(length);
                    this.startDigit = BCDStringUtils.fromBCDToString(new ByteArrayInputStream(octetData));
                    break;
                case 6:
                    this.interDigitTimeout = ((Long) ais.readIntegerData(length)).intValue();
                    break;
            }
        }
        this.doCheck();
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (minimumNumberOfDigits < MIN_NUMBER_OF_DIGITS
                || minimumNumberOfDigits > MAX_NUMBER_OF_DIGITS) {
            throw new ParameterOutOfRangeException("Parameter MinimumNumberOfDigits is out of range [1..30]");
        } else if (maximumNumberOfDigits < MIN_NUMBER_OF_DIGITS
                || maximumNumberOfDigits > MAX_NUMBER_OF_DIGITS) {
            throw new ParameterOutOfRangeException("Parameter MaximumNumberOfDigits is out of range [1..30]");
        } else if (interDigitTimeout < MIN_NUMBER_OF_DIGITS
                || interDigitTimeout > MAX_INTER_DIGIT_TIMEOUT) {
            throw new ParameterOutOfRangeException("Parameter InterDigitTimeout is out of range [1..127]");
        }
    }

    /**
     * @return the minimumNumberOfDigits
     */
    public Integer getMinimumNumberOfDigits() {
        return minimumNumberOfDigits;
    }

    /**
     * @return the maximumNumberOfDigits
     */
    public Integer getMaximumNumberOfDigits() {
        return maximumNumberOfDigits;
    }

    /**
     * @return the endOfReplyDigit
     */
    public String getEndOfReplyDigit() {
        return endOfReplyDigit;
    }

    /**
     * @param endOfReplyDigit the endOfReplyDigit to set
     */
    public void setEndOfReplyDigit(String endOfReplyDigit) {
        this.endOfReplyDigit = endOfReplyDigit;
    }

    /**
     * @return the cancelDigit
     */
    public String getCancelDigit() {
        return cancelDigit;
    }

    /**
     * @param cancelDigit the cancelDigit to set
     */
    public void setCancelDigit(String cancelDigit) {
        this.cancelDigit = cancelDigit;
    }

    /**
     * @return the startDigit
     */
    public String getStartDigit() {
        return startDigit;
    }

    /**
     * @param startDigit the startDigit to set
     */
    public void setStartDigit(String startDigit) {
        this.startDigit = startDigit;
    }

    /**
     * @return the interDigitTimeout
     */
    public Integer getInterDigitTimeout() {
        return interDigitTimeout;
    }
}
