/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * Ext-SS-InfoList ::= SEQUENCE SIZE (1..maxNumOfSS) OF Ext-SS-Info
 * maxNumOfSS INTEGER ::= 30
 *
 * @author eatakishiyev
 *
 */
public class ExtSSInfoList implements MAPParameter {

    private final List<ExtSSInfo> extSSInfos = new ArrayList();

    public ExtSSInfoList() {
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        AsnInputStream _ais = ais.readSequenceStream();
        while (_ais.available() > 0) {
            int tag = _ais.readTag();
            ExtSSInfo extSSInfo = new ExtSSInfo();
            extSSInfo.decode(_ais);
            extSSInfos.add(extSSInfo);
            if (extSSInfos.size() > Constants.maxNumOfSs) {
                throw new ArrayIndexOutOfBoundsException("Max count of extSSInfos are 30. Current count is " + extSSInfos.size());
            }
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if ((extSSInfos.size() > Constants.maxNumOfSs)) {
            throw new IncorrectSyntaxException("ExtSSInfoList size must be between 1 and 30. Current size is " + extSSInfos.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ExtSSInfo extSSInfo : extSSInfos) {
                extSSInfo.encode(aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<ExtSSInfo> getExtSSInfos() {
        return extSSInfos;
    }

    public void addExtSSInfo(ExtSSInfo extSSInfo) {
        if (extSSInfos.size() > Constants.maxNumOfSs) {
            throw new ArrayIndexOutOfBoundsException("Max count of extSSInfos are 30. Current count is " + extSSInfos.size());
        }
        extSSInfos.add(extSSInfo);
    }
}
