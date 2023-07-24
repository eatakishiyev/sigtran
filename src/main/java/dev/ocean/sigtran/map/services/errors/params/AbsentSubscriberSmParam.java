/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.AbsentSubscriberDiagnosticSM;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * AbsentSubscriberSM-Param ::= SEQUENCE {
 * absentSubscriberDiagnosticSM AbsentSubscriberDiagnosticSM OPTIONAL,
 * -- AbsentSubscriberDiagnosticSM can be either for non-GPRS
 * -- or for GPRS
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * additionalAbsentSubscriberDiagnosticSM [0] AbsentSubscriberDiagnosticSM
 * OPTIONAL }
 * -- if received, additionalAbsentSubscriberDiagnosticSM
 * -- is for GPRS and absentSubscriberDiagnosticSM is
 * -- for non-GPRS
 *
 * @author eatakishiyev
 */
public class AbsentSubscriberSmParam {

    private AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM;
    private ExtensionContainer extensionContainer;
    private AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM;

    public AbsentSubscriberSmParam() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (absentSubscriberDiagnosticSM != null) {
                absentSubscriberDiagnosticSM.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (additionalAbsentSubscriberDiagnosticSM != null) {
                additionalAbsentSubscriberDiagnosticSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE
                    || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();
            
            while (_ais.available() > 0) {
                tag = _ais.readTag();
                switch (tag) {
                    case Tag.INTEGER:
                        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.absentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                            absentSubscriberDiagnosticSM.decode(_ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expected Class[UNIVERSAL] tag. Received Class[%s]", _ais.getTagClass()));
                        }
                        break;
                    case Tag.SEQUENCE:
                        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(_ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expected Class[UNIVERSAL] tag. Received Class[%s]", _ais.getTagClass()));
                        }
                        break;
                    case 0:
                        if (_ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.additionalAbsentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                            additionalAbsentSubscriberDiagnosticSM.decode(_ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expected Class[CONTEXT] tag. Received Class[%s]", _ais.getTagClass()));
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the absentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM() {
        return absentSubscriberDiagnosticSM;
    }

    /**
     * @param absentSubscriberDiagnosticSM the absentSubscriberDiagnosticSM to
     * set
     */
    public void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM) {
        this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
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
     * @return the additionalAbsentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAdditionalAbsentSubscriberDiagnosticSM() {
        return additionalAbsentSubscriberDiagnosticSM;
    }

    /**
     * @param additionalAbsentSubscriberDiagnosticSM the
     * additionalAbsentSubscriberDiagnosticSM to set
     */
    public void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
    }

    @Override
    public String toString() {
        return "AbsentSubscriberSmParam{" +
                "absentSubscriberDiagnosticSM=" + absentSubscriberDiagnosticSM +
                ", extensionContainer=" + extensionContainer +
                ", additionalAbsentSubscriberDiagnosticSM=" + additionalAbsentSubscriberDiagnosticSM +
                '}';
    }
}
