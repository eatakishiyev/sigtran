/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * AbsentSubscriberParam ::= SEQUENCE {
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * absentSubscriberReason [0] AbsentSubscriberReason OPTIONAL}
 * @author eatakishiyev
 */
public class AbsentSubscriberParam {

    private ExtensionContainer extensionContainer;
    private AbsentSubscriberReason absentSubscriberReason;

    public AbsentSubscriberParam() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            if (absentSubscriberReason != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, absentSubscriberReason.value());
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE
                    || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Recevied Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
            AsnInputStream _ais = ais.readSequenceStream();
            
            while (_ais.available() > 0) {

                tag = _ais.readTag();
                if (tag == Tag.SEQUENCE && _ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(_ais);
                } else if (tag == 0 && _ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.absentSubscriberReason = AbsentSubscriberReason.getInstance((int) _ais.readInteger());
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.",
                            tag, _ais.getTagClass()));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public AbsentSubscriberReason getAbsentSubscriberReason() {
        return absentSubscriberReason;
    }

    public void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason) {
        this.absentSubscriberReason = absentSubscriberReason;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }


    @Override
    public String toString() {
        return "AbsentSubscriberParam{" +
                "extensionContainer=" + extensionContainer +
                ", absentSubscriberReason=" + absentSubscriberReason +
                '}';
    }
}
