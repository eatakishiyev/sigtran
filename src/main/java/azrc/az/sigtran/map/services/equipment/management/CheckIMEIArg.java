/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.equipment.management;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMEI;
import azrc.az.sigtran.map.parameters.RequestedEquipmentInfo;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPArgument;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
 
/**
 * CheckIMEI-Arg ::= SEQUENCE { imei IMEI, requestedEquipmentInfo
 * RequestedEquipmentInfo, extensionContainer ExtensionContainer OPTIONAL, ...}
 *
 * @author eatakishiyev
 */
public class CheckIMEIArg implements MAPArgument {

    private IMEI imei;
    private RequestedEquipmentInfo requestedEquipmentInfo;
    private ExtensionContainer extensionContainer;
    private byte[] argData;
    protected boolean corruptedArgument = false;

    public CheckIMEIArg() {
    }

    public CheckIMEIArg(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo) {
        this.imei = imei;
        this.requestedEquipmentInfo = requestedEquipmentInfo;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            imei.encode(aos);
            requestedEquipmentInfo.encode(aos);
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.argData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        int tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.imei = new IMEI(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.STRING_BIT && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.requestedEquipmentInfo = new RequestedEquipmentInfo();
            requestedEquipmentInfo.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        if (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensionContainer = new ExtensionContainer(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public IMEI getImei() {
        return imei;
    }

    public void setImei(IMEI imei) {
        this.imei = imei;
    }

    public RequestedEquipmentInfo getRequestedEquipmentInfo() {
        return requestedEquipmentInfo;
    }

    public void setRequestedEquipmentInfo(RequestedEquipmentInfo requestedEquipmentInfo) {
        this.requestedEquipmentInfo = requestedEquipmentInfo;
    }

    @Override
    public byte[] getRequestData() {
        return argData;
    }

    protected void setArgData(byte[] argData) {
        this.argData = argData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return corruptedArgument;
    }

}
