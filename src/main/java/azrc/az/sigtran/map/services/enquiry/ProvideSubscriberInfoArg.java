/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPArgument;
import azrc.az.sigtran.map.parameters.EMLPPPriority;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.LMSI;
import azrc.az.sigtran.map.parameters.RequestedInfo;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * * ProvideSubscriberInfoArg ::= SEQUENCE {
 * imsi [0] IMSI,
 * lmsi [1] LMSI OPTIONAL,
 * requestedInfo [2] RequestedInfo,
 * extensionContainer [3] ExtensionContainer OPTIONAL,
 * ...,
 * callPriority [4] EMLPP-Priority OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class ProvideSubscriberInfoArg implements MAPArgument {

    private IMSI imsi;
    private LMSI lmsi;
    private RequestedInfo requestedInfo;
    private ExtensionContainer extensionContainer;
    private EMLPPPriority callPriority;
    private byte[] requestData;
    protected boolean requestCorrupted;

    public ProvideSubscriberInfoArg() {
    }

    public ProvideSubscriberInfoArg(IMSI imsi, RequestedInfo requestedInfo) {
        this.imsi = imsi;
        this.requestedInfo = requestedInfo;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (lmsi != null) {
                lmsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            requestedInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (callPriority != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, lenPos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[0] TagClass[CONTEXT]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.imsi = new IMSI(ais);
                    break;
                case 1:
                    this.lmsi = new LMSI(ais);
                    break;
                case 2:
                    this.requestedInfo = new RequestedInfo(ais);
                    break;
                case 3:
                    this.extensionContainer = new ExtensionContainer(ais);
                    break;
                case 4:
                    this.callPriority = EMLPPPriority.getInstance((int) ais.readInteger());
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Received unexpected tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
        if (imsi == null || requestedInfo == null) {
            throw new IncorrectSyntaxException(String.format("Absent mandatory parameter. IMSI = %s; RequestedInfo = %s", imsi, requestedInfo));
        }

    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the lmsi
     */
    public LMSI getLmsi() {
        return lmsi;
    }

    /**
     * @param lmsi the lmsi to set
     */
    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
    }

    /**
     * @return the requestedInfo
     */
    public RequestedInfo getRequestedInfo() {
        return requestedInfo;
    }

    /**
     * @param requestedInfo the requestedInfo to set
     */
    public void setRequestedInfo(RequestedInfo requestedInfo) {
        this.requestedInfo = requestedInfo;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    /**
     * @return the callPriority
     */
    public EMLPPPriority getCallPriority() {
        return callPriority;
    }

    /**
     * @param callPriority the callPriority to set
     */
    public void setCallPriority(EMLPPPriority callPriority) {
        this.callPriority = callPriority;
    }

    @Override
    public byte[] getRequestData() {
        return requestData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
