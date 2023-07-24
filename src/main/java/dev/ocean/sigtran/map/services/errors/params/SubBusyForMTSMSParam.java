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
 * SubBusyForMT-SMS-Param ::= SEQUENCE {
 * extensionContainer ExtensionContainer OPTIONAL,
 * ... ,
 * gprsConnectionSuspended NULL OPTIONAL }
 * -- If GprsConnectionSuspended is not understood it shall
 * -- be discarded
 *
 * @author eatakishiyev
 */
public class SubBusyForMTSMSParam {

    private ExtensionContainer extensionContainer;
    private Boolean gprsConnectionSuspended;

    public SubBusyForMTSMSParam() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            if (gprsConnectionSuspended) {
                aos.writeNull();
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL
                    || tag != Tag.SEQUENCE) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();
            
            while (_ais.available() > 0) {
                tag = _ais.readTag();
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(_ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                                    + "Received Tag[%s] Class[%s]", tag, _ais.getTagClass()));
                        }
                        break;
                    case Tag.NULL:
                        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.gprsConnectionSuspended = true;
                            _ais.readNull();
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[NULL] Class[UNIVERSAL]. "
                                    + "Received Tag[%s] Class[%s]", tag, _ais.getTagClass()));
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, _ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
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
     * @return the gprsConnectionSuspended
     */
    public Boolean getGprsConnectionSuspended() {
        return gprsConnectionSuspended;
    }

    /**
     * @param gprsConnectionSuspended the gprsConnectionSuspended to set
     */
    public void setGprsConnectionSuspended(Boolean gprsConnectionSuspended) {
        this.gprsConnectionSuspended = gprsConnectionSuspended;
    }

    @Override
    public String toString() {
        return "SubBusyForMTSMSParam{" +
                "extensionContainer=" + extensionContainer +
                ", gprsConnectionSuspended=" + gprsConnectionSuspended +
                '}';
    }
}
