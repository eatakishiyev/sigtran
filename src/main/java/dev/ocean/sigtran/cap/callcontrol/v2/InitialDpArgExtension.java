/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v2;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InitialDpArgExtension {

    private byte[] naCarrierInformation;
    private ISDNAddressString gmscAddress;

    public InitialDpArgExtension() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IncorrectSyntaxException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (naCarrierInformation != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 0, naCarrierInformation);
        }
        if (gmscAddress != null) {
            gmscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, UnexpectedDataException, IOException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.naCarrierInformation = ais.readSequence();
                    break;
                case 1:
                    this.gmscAddress = new ISDNAddressString(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected Tag[%s] received.", tag));
            }
        }

    }

    public ISDNAddressString getGmscAddress() {
        return gmscAddress;
    }

    public void setGmscAddress(ISDNAddressString gmscAddress) {
        this.gmscAddress = gmscAddress;
    }

    public byte[] getNaCarrierInformation() {
        return naCarrierInformation;
    }

    public void setNaCarrierInformation(byte[] naCarrierInformation) {
        this.naCarrierInformation = naCarrierInformation;
    }

}
