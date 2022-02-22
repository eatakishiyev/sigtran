/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * BasicServiceCriteria ::= SEQUENCE SIZE(1..maxNumOfCamelBasicServiceCriteria)
 * OF Ext-BasicServiceCode
 * @author eatakishiyev
 */
public class ExtBasicServiceCodeCriteria implements MAPParameter{

    private final List<ExtBasicServiceCode> basicServiceCodes;

    public ExtBasicServiceCodeCriteria() {
        this.basicServiceCodes = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (basicServiceCodes.size() < 1
                || basicServiceCodes.size() > Constants.maxNumOfCamelBasicServiceCriteria) {
            throw new UnexpectedDataException(String.format("ExtBasicServiceCode count must be in [1..5] range. Current count is ", basicServiceCodes.size()));
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (ExtBasicServiceCode extBasicServiceCode : basicServiceCodes) {
                extBasicServiceCode.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        while (ais.available() > 0) {
            ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCode();
            extBasicServiceCode.decode(ais);
            basicServiceCodes.add(extBasicServiceCode);
        }
        if (basicServiceCodes.size() < 1
                || basicServiceCodes.size() > Constants.maxNumOfCamelBasicServiceCriteria) {
            throw new UnexpectedDataException(String.format("ExtBasicServiceCode count must be in [1..5] range. Current count is ", basicServiceCodes.size()));
        }

    }

    public List<ExtBasicServiceCode> getExtBasicServiceCodes() {
        return basicServiceCodes;
    }

}
