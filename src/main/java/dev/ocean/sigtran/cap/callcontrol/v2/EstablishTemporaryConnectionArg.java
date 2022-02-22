/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v2;

import dev.ocean.isup.parameters.GenericDigits;
import dev.ocean.sigtran.cap.api.IEstablishTemporaryConnectionArg;
import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.AssistingSSPIPRoutingAddress;
import dev.ocean.isup.parameters.ScfId;
import dev.ocean.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EstablishTemporaryConnectionArg ::= SEQUENCE {
 * assistingSSPIPRoutingAddress	[0] AssistingSSPIPRoutingAddress,
 * correlationID	[1] CorrelationID	OPTIONAL,
 * scfID	[3] ScfID	OPTIONAL,
 * extensions	[4] SEQUENCE SIZE(1..numOfExtensions)	OF
 * ExtensionField OPTIONAL,
 * serviceInteractionIndicatorsTwo	[7] ServiceInteractionIndicatorsTwo	OPTIONAL,
 * ...,
 * na-info	[50] NA-Info	OPTIONAL
 * }
 * --	na-info is included at the discretion of the
 * --	gsmSCF operator.
 *
 * @author eatakishiyev
 */
public class EstablishTemporaryConnectionArg implements IEstablishTemporaryConnectionArg {

    private AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress;
    private GenericDigits correlationId;
    private ScfId scfId;
    private byte[] extensions;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private byte[] naInfo;

    public EstablishTemporaryConnectionArg() {
    }

    public EstablishTemporaryConnectionArg(AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            this.assistingSSPIPRoutingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (correlationId != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, correlationId.encode());
            }

            if (scfId != null) {
                scfId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (extensions != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 4, extensions);
            }

            if (serviceInteractionIndicatorsTwo != null) {
                this.serviceInteractionIndicatorsTwo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (naInfo != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 50, naInfo);
            }
            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Unexpected tag received. Expecting Tag[16] TagClass[0], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.assistingSSPIPRoutingAddress = new AssistingSSPIPRoutingAddress();
                    this.assistingSSPIPRoutingAddress.decode(ais);
                    break;
                case 1:
                    this.correlationId = new GenericDigits();
                    correlationId.decode(ais.readOctetString());
                    break;
                case 3:
                    this.scfId = new ScfId();
                    scfId.decode(ais);
                    break;
                case 4:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(this.extensions);
                    break;
                case 7:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwo();
                    this.serviceInteractionIndicatorsTwo.decode(ais);
                    break;
                case 50:
                    this.naInfo = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    @Override
    public AssistingSSPIPRoutingAddress getAssistingSSPIPRoutingAddress() {
        return this.assistingSSPIPRoutingAddress;
    }

    @Override
    public void setAssistingSSPIPRoutingAddress(AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
    }

    @Override
    public GenericDigits getCorrelationId() {
        return correlationId;
    }

    @Override
    public void setCorrelationId(GenericDigits correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public ScfId getScfId() {
        return scfId;
    }

    @Override
    public void setScfId(ScfId scfId) {
        this.scfId = scfId;
    }

    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public void setServiceInteractionIndicatorsTwo(ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo) {
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
    }

    @Override
    public byte[] getNaInfo() {
        return naInfo;
    }

    @Override
    public void setNaInfo(byte[] naInfo) {
        this.naInfo = naInfo;
    }

}
