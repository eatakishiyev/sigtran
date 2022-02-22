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
 *
 * @author eatakishiyev
 */
public class PDPContextInfoList {

    private List<PDPContextInfo> pdpList;

    public PDPContextInfoList() {
        this.pdpList = new ArrayList<>();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();

            for (PDPContextInfo pdpContextInfo : pdpList) {
                pdpContextInfo.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(pos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, UnexpectedDataException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                PDPContextInfo pdpContextInfo = new PDPContextInfo();
                pdpContextInfo.decode(ais);
                pdpList.add(pdpContextInfo);
            }
        }
    }

    public List<PDPContextInfo> getPdpList() {
        return pdpList;
    }

}
