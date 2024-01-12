/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v3;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InitialDpArgExtension {

    private ISDNAddressString gmscAddress;

    public InitialDpArgExtension() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, AsnException, IOException, IllegalNumberFormatException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);

        int lenPos = aos.StartContentDefiniteLength();

        if (gmscAddress != null) {
            this.gmscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, IllegalNumberFormatException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.gmscAddress = new ISDNAddressString();
                    this.gmscAddress.decode(ais);
                    break;
            }
        }
    }

    public ISDNAddressString getGmscAddress() {
        return gmscAddress;
    }

    public void setGmscAddress(ISDNAddressString gmscAddress) {
        this.gmscAddress = gmscAddress;
    }

}
