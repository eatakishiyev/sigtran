/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.general;

import azrc.az.isup.parameters.GenericNumber;
import azrc.az.sigtran.cap.api.CAPMessage;
import azrc.az.sigtran.cap.parameters.IPSSPCapabilities;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class AssistRequestInstructionArg implements CAPMessage {

    private GenericNumber correlationId;
    private IPSSPCapabilities iPSSPCapabilities;
    private byte[] extensions;

    public AssistRequestInstructionArg() {
    }

    public AssistRequestInstructionArg(GenericNumber correlationId, IPSSPCapabilities iPSSPCapabilities) {
        this.correlationId = correlationId;
        this.iPSSPCapabilities = iPSSPCapabilities;
    }

    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 0, correlationId.encode());

            this.iPSSPCapabilities.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);

            if (extensions != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, extensions);
            }

            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (IllegalNumberFormatException | IncorrectSyntaxException | IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);

        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received.Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.correlationId = new GenericNumber();
                    correlationId.decode(ais.readOctetString());
                    break;
                case 2:
                    this.iPSSPCapabilities = new IPSSPCapabilities();
                    this.iPSSPCapabilities.decode(ais);
                    break;
                case 3:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * @return the correlationId
     */
    public GenericNumber getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(GenericNumber correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the iPSSPCapabilities
     */
    public IPSSPCapabilities getiPSSPCapabilities() {
        return iPSSPCapabilities;
    }

    /**
     * @param iPSSPCapabilities the iPSSPCapabilities to set
     */
    public void setiPSSPCapabilities(IPSSPCapabilities iPSSPCapabilities) {
        this.iPSSPCapabilities = iPSSPCapabilities;
    }

    /**
     * @return the extensions
     */
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
