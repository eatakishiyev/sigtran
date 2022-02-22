/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause.CauseValue;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * O-CauseValueCriteria::= SEQUENCE SIZE(1..maxNumOfCAMEL-O-CauseValueCriteria)
 * OF
 * CauseValue
 * @author eatakishiyev
 */
public class OCauseValueCriteria {

    private final List<CauseValue> causeValues;

    public OCauseValueCriteria() {
        this.causeValues = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (causeValues.size() < 1
                || causeValues.size() > Constants.maxNumOfCamelOCauseValueCriteria) {
            throw new UnexpectedDataException("CauseValue count must be in [1..5] range. Current count is " + causeValues.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (CauseValue value : causeValues) {
                aos.writeOctetString(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, new byte[]{(byte) value.value()});
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    CauseValue causeValue = CauseValue.getInstance(ais.readOctetString()[0]);
                    this.causeValues.add(causeValue);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (causeValues.size() < 1
                    || causeValues.size() > Constants.maxNumOfCamelOCauseValueCriteria) {
                throw new UnexpectedDataException("CauseValue count must be in [1..5] range. Current count is " + causeValues.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<CauseValue> getCauseValues() {
        return causeValues;
    }
}
