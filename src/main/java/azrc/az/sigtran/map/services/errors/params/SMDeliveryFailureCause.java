/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.SignalInfo;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SM-DeliveryFailureCause ::= SEQUENCE {
 * sm-EnumeratedDeliveryFailureCause SM-EnumeratedDeliveryFailureCause,
 * diagnosticInfo SignalInfo OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class SMDeliveryFailureCause {

    private SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause;
    private SignalInfo diagnosticInfo;
    private ExtensionContainer extensionContainer;

    public SMDeliveryFailureCause() {
    }

    public SMDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause) {
        this.sMEnumeratedDeliveryFailureCause = sMEnumeratedDeliveryFailureCause;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, sMEnumeratedDeliveryFailureCause.value());
            if (diagnosticInfo != null) {
                diagnosticInfo.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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

            tag = _ais.readTag();
            if (tag == Tag.ENUMERATED
                    && _ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.sMEnumeratedDeliveryFailureCause = SMEnumeratedDeliveryFailureCause.getInstance((int) _ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, _ais.getTagClass()));
            }

            while (_ais.available() > 0) {
                tag = _ais.readTag();
                if (_ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Unexpected Class[%s] tag received. Expecting Class[UNIVERSAL]", _ais.getTagClass()));
                }

                switch (tag) {
                    case Tag.STRING_OCTET:
                        this.diagnosticInfo = new SignalInfo();
                        diagnosticInfo.decode(_ais);
                        break;
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer(_ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the sMEnumeratedDeliveryFailureCause
     */
    public SMEnumeratedDeliveryFailureCause getsMEnumeratedDeliveryFailureCause() {
        return sMEnumeratedDeliveryFailureCause;
    }

    /**
     * @param sMEnumeratedDeliveryFailureCause the
     * sMEnumeratedDeliveryFailureCause to set
     */
    public void setsMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause) {
        this.sMEnumeratedDeliveryFailureCause = sMEnumeratedDeliveryFailureCause;
    }

    /**
     * @return the diagnosticInfo
     */
    public SignalInfo getDiagnosticInfo() {
        return diagnosticInfo;
    }

    /**
     * @param diagnosticInfo the diagnosticInfo to set
     */
    public void setDiagnosticInfo(SignalInfo diagnosticInfo) {
        this.diagnosticInfo = diagnosticInfo;
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

    @Override
    public String toString() {
        return "SMDeliveryFailureCause{" +
                "sMEnumeratedDeliveryFailureCause=" + sMEnumeratedDeliveryFailureCause +
                ", diagnosticInfo=" + diagnosticInfo +
                ", extensionContainer=" + extensionContainer +
                '}';
    }
}
