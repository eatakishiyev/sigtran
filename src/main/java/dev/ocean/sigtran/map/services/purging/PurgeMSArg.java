/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.purging;

import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.services.common.MAPArgument;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * PurgeMS-Arg ::= [3] SEQUENCE {
 * imsi IMSI,
 * vlr-Number [0] ISDN-AddressString OPTIONAL,
 * sgsn-Number [1] ISDN-AddressString OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class PurgeMSArg implements MAPArgument {

    private IMSI imsi;
    private ISDNAddressString vlrNumber;
    private ISDNAddressString sgsnNumber;
    private ExtensionContainer extensionContainer;
    private byte[] rawData;

    public PurgeMSArg() {
    }

    public PurgeMSArg(IMSI imsi) {
        this.imsi = imsi;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 3);
            int lenPos = aos.StartContentDefiniteLength();
            imsi.encode(aos);

            if (vlrNumber != null) {
                vlrNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (sgsnNumber != null) {
                sgsnNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            aos.FinalizeContent(lenPos);
            this.rawData = aos.toByteArray();

        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.rawData = data;

        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != 3 && ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[3] TagClass[CONTEXT]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (IOException | IncorrectSyntaxException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws UnexpectedDataException,
            IncorrectSyntaxException, IOException {
        int tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.imsi = new IMSI(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting Tag[STRING_OCTET] Class[UNIVERSAL]."
                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            switch (tag) {
                case 0:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.vlrNumber = new ISDNAddressString(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]."
                                + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                    }
                    break;
                case 1:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.sgsnNumber = new ISDNAddressString(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]."
                                + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                    }
                    break;
                case Tag.SEQUENCE:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.extensionContainer = new ExtensionContainer(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                                + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                    }
                    break;
            }
        }
    }

    @Override
    public byte[] getRequestData() {
        return rawData;
    }

    @Override
    public boolean isRequestCorrupted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }

    public ISDNAddressString getVlrNumber() {
        return vlrNumber;
    }

    public void setVlrNumber(ISDNAddressString vlrNumber) {
        this.vlrNumber = vlrNumber;
    }

}
