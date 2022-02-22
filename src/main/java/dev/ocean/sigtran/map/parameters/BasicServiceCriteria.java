/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * BasicServiceCriteria::= SEQUENCE SIZE(1..maxNumOfCamelBasicServiceCriteria)
 * OF
 * Ext-BasicServiceCode
 *
 * @author eatakishiyev
 */
public class BasicServiceCriteria implements MAPParameter {

    private final List<ExtBasicServiceCode> basicServiceCriterias;

    public BasicServiceCriteria() {
        this.basicServiceCriterias = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (basicServiceCriterias.size() < 1
                || basicServiceCriterias.size() > Constants.maxNumOfCamelBasicServiceCriteria) {
            throw new UnexpectedDataException("BasicServiceCriteri count must be in [1..5] range. Current count is " + basicServiceCriterias.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (ExtBasicServiceCode extBasicServiceCode : basicServiceCriterias) {
                extBasicServiceCode.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCode();
                    extBasicServiceCode.decode(ais);
                    basicServiceCriterias.add(extBasicServiceCode);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (basicServiceCriterias.size() < 1
                    || basicServiceCriterias.size() > Constants.maxNumOfCamelBasicServiceCriteria) {
                throw new UnexpectedDataException("BasicServiceCriteri count must be in [1..5] range. Current count is " + basicServiceCriterias.size());
            }

        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<ExtBasicServiceCode> getBasicServiceCriterias() {
        return basicServiceCriterias;
    }
}
