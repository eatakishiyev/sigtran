/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * RoamingNotAllowedParam ::= SEQUENCE {
 * roamingNotAllowedCause RoamingNotAllowedCause,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * additionalRoamingNotAllowedCause [0] AdditionalRoamingNotAllowedCause
 * OPTIONAL }
 * -- if the additionalRoamingNotallowedCause is received by the MSC/VLR or SGSN
 * then the
 * -- roamingNotAllowedCause shall be discarded.
 * @author eatakishiyev
 */
public class RoamingNotAllowedParam {

    private RoamingNotAllowedCause roamingNotAllowedCause;
    private ExtensionContainer extensionContainer;
    private AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause;

    public RoamingNotAllowedParam() {
    }

    public RoamingNotAllowedParam(RoamingNotAllowedCause roamingNotAllowedCause) {
        this.roamingNotAllowedCause = roamingNotAllowedCause;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, roamingNotAllowedCause.value);
            if (this.extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            if (additionalRoamingNotAllowedCause != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, additionalRoamingNotAllowedCause.value);
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
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();

            tag = _ais.readTag();
            if (tag == Tag.ENUMERATED
                    && _ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.roamingNotAllowedCause = RoamingNotAllowedCause.getInstance((int) _ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUM] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, _ais.getTagClass()));
            }

            while (_ais.available() > 0) {
                tag = _ais.readTag();
                if (tag == Tag.SEQUENCE && _ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(_ais);
                } else if (tag == 0 && _ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.additionalRoamingNotAllowedCause = AdditionalRoamingNotAllowedCause.getInstance((int) _ais.readInteger());
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, _ais.getTagClass()));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public AdditionalRoamingNotAllowedCause getAdditionalRoamingNotAllowedCause() {
        return additionalRoamingNotAllowedCause;
    }

    public void setAdditionalRoamingNotAllowedCause(AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause) {
        this.additionalRoamingNotAllowedCause = additionalRoamingNotAllowedCause;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public RoamingNotAllowedCause getRoamingNotAllowedCause() {
        return roamingNotAllowedCause;
    }

    public void setRoamingNotAllowedCause(RoamingNotAllowedCause roamingNotAllowedCause) {
        this.roamingNotAllowedCause = roamingNotAllowedCause;
    }

    public enum RoamingNotAllowedCause {

        PLMN_ROAMING_NOT_ALLOWED(0),
        OPERATOR_DETERMINED_BARRING(3),
        UNKNOWN(-1);

        private final int value;

        private RoamingNotAllowedCause(int value) {
            this.value = value;
        }

        public static RoamingNotAllowedCause getInstance(int value) {
            switch (value) {
                case 0:
                    return PLMN_ROAMING_NOT_ALLOWED;
                case 3:
                    return OPERATOR_DETERMINED_BARRING;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum AdditionalRoamingNotAllowedCause {

        SUPPORTED_RAT_TYPES_NOT_ALLOWES(0),
        UNKNOWN(-1);

        private final int value;

        private AdditionalRoamingNotAllowedCause(int value) {
            this.value = value;
        }

        public static AdditionalRoamingNotAllowedCause getInstance(int value) {
            if (value == 0) {
                return SUPPORTED_RAT_TYPES_NOT_ALLOWES;
            } else {
                return UNKNOWN;
            }
        }

    }
}
