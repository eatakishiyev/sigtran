/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * DpSpecificCriteriaAlt {PARAMETERS-BOUND : bound} ::= SEQUENCE { ...,
 * changeOfPositionControlInfo [0] ChangeOfPositionControlInfo {bound}
 * numberOfDigits [1] NumberOfDigits OPTIONAL, interDigitTimeout [2] INTEGER
 * (1..127) OPTIONAL } -- interDigitTimeout duration in seconds.
 *
 * @author eatakishiyev
 */
public class DpSpecificCriteriaAlt {

    private ChangeOfPositionControlInfo changeOfPositionControlInfo;
    private NumberOfDigits numberOfDigits;
    private Integer interDigitTimeout;
//    
    public static final int MIN_INTER_DIGIT_TIMEOUT = 1;
    public static final int MAX_INTER_DIGIT_TIMEOUT = 127;

    public DpSpecificCriteriaAlt() {
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IOException {
        this.doCheck();
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.changeOfPositionControlInfo.encode(0, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        if (numberOfDigits != null) {
            this.numberOfDigits.encode(1, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        } else if (interDigitTimeout != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, this.interDigitTimeout);
        }

        aos.FinalizeContent(lenPos);

    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    public DpSpecificCriteriaAlt(ChangeOfPositionControlInfo changeOfPositionControlInfo) {
        this.changeOfPositionControlInfo = changeOfPositionControlInfo;
    }

    /**
     * @return the changeOfPositionControlInfo
     */
    public ChangeOfPositionControlInfo getChangeOfPositionControlInfo() {
        return changeOfPositionControlInfo;
    }

    /**
     * @return the numberOfDigits
     */
    public NumberOfDigits getNumberOfDigits() {
        return numberOfDigits;
    }

    /**
     * @param numberOfDigits the numberOfDigits to set
     */
    public void setNumberOfDigits(NumberOfDigits numberOfDigits) {
        this.numberOfDigits = numberOfDigits;
    }

    /**
     * @return the interDigitTimeout
     */
    public Integer getInterDigitTimeout() {
        return interDigitTimeout;
    }

    /**
     * @param interDigitTimeout the interDigitTimeout to set
     */
    public void setInterDigitTimeout(Integer interDigitTimeout) {
        this.interDigitTimeout = interDigitTimeout;
    }

    private void doCheck() throws AsnException, ParameterOutOfRangeException {
        if (changeOfPositionControlInfo == null) {
            throw new AsnException("Expecting mandatory ChangeOfPositionControlInfo parameter.");
        } else if (interDigitTimeout < MIN_INTER_DIGIT_TIMEOUT || interDigitTimeout > MAX_INTER_DIGIT_TIMEOUT) {
            throw new ParameterOutOfRangeException("Parameter InterDigitTimeout is out of range [1..127]");
        }

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
                    this.changeOfPositionControlInfo = new ChangeOfPositionControlInfo();
                    this.changeOfPositionControlInfo.decode(ais);
                    break;
                case 1:
                    this.numberOfDigits = new NumberOfDigits();
                    this.numberOfDigits.decode(ais);
                    break;
                case 2:
                    this.interDigitTimeout = (int) ais.readInteger();
                    break;
            }
        }
        this.doCheck();
    }

}
