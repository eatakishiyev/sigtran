/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.equipment.management;

import azrc.az.sigtran.map.parameters.EquipmentStatus;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.UESBI_Iu;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPResponse;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CheckIMEI-Res ::= SEQUENCE {
 * equipmentStatus EquipmentStatus OPTIONAL,
 * bmuef UESBI-Iu OPTIONAL,
 * extensionContainer [0] ExtensionContainer OPTIONAL,
 * ...}
 *
 *
 * @author eatakishiyev
 */
public class CheckIMEIResponse implements MAPResponse {

    private EquipmentStatus equipmentStatus;
    private UESBI_Iu bmuef;
    private ExtensionContainer extensionContainer;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public CheckIMEIResponse() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            if (equipmentStatus != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, equipmentStatus.value());
            }
            if (bmuef != null) {
                bmuef.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws Exception {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case Tag.ENUMERATED:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.equipmentStatus = EquipmentStatus.getInstance((int) ais.readInteger());
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[INTEGER] Class[UNIVERSAL]", tag, ais.getTagClass()));
                    }
                    break;
                case Tag.SEQUENCE:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.bmuef = new UESBI_Iu();
                        bmuef.decode(ais.readSequenceStream());
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]", tag, ais.getTagClass()));
                    }
                    break;
                case 0:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.extensionContainer = new ExtensionContainer();
                        extensionContainer.decode(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[0] Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    break;
            }
        }
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public UESBI_Iu getBmuef() {
        return bmuef;
    }

    public void setBmuef(UESBI_Iu bmuef) {
        this.bmuef = bmuef;
    }

    @Override
    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

    @Override
    public byte[] getResponseData() {
        return responseData;
    }

}
