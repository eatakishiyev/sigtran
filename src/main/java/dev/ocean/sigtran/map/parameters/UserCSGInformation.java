/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;

/**
 * UserCSGInformation ::= SEQUENCE { csg-Id [0] CSG-Id, extensionContainer [1]
 * ExtensionContainer OPTIONAL, ..., accessMode [2] OCTET STRING (SIZE(1))
 * OPTIONAL, cmi [3] OCTET STRING (SIZE(1)) OPTIONAL } -- The encoding of the
 * accessMode and cmi parameters are as defined in 3GPP TS 29.060 [105].
 *
 * @author eatakishiyev
 */
public class UserCSGInformation {

    private BitSetStrictLength csgId;
    private ExtensionContainer extensionContainer;
    private OctetString accessMode;
    private OctetString cmi;

    public UserCSGInformation() {
    }

    public UserCSGInformation(BitSetStrictLength csgId) {
        this.csgId = csgId;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeBitString(Tag.CLASS_CONTEXT_SPECIFIC, 0, csgId);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (accessMode != null) {
                accessMode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (cmi != null) {
                cmi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.csgId = ais.readBitString();
            }
            while (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.accessMode = new OctetString() {
                        @Override
                        public int getMinLength() {
                            return 1;
                        }

                        @Override
                        public int getMaxLength() {
                            return 1;
                        }
                    };
                    accessMode.decode(ais);
                } else if (tag == 3 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.cmi = new OctetString() {
                        @Override
                        public int getMinLength() {
                            return 1;
                        }

                        @Override
                        public int getMaxLength() {
                            return 1;
                        }
                    };
                    cmi.decode(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the csgId
     */
    public BitSetStrictLength getCsgId() {
        return csgId;
    }

    /**
     * @param csgId the csgId to set
     */
    public void setCsgId(BitSetStrictLength csgId) {
        this.csgId = csgId;
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
     * @return the accessMode
     */
    public OctetString getAccessMode() {
        return accessMode;
    }

    /**
     * @param accessMode the accessMode to set
     */
    public void setAccessMode(OctetString accessMode) {
        this.accessMode = accessMode;
    }

    /**
     * @return the cmi
     */
    public OctetString getCmi() {
        return cmi;
    }

    /**
     * @param cmi the cmi to set
     */
    public void setCmi(OctetString cmi) {
        this.cmi = cmi;
    }

}
