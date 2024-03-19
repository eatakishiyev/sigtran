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
 * CallBarredParam ::= CHOICE {
 * callBarringCause CallBarringCause,
 * -- call BarringCause must not be used in version 3 and higher
 * extensibleCallBarredParam ExtensibleCallBarredParam
 * -- extensibleCallBarredParam must not be used in version <3
 * }
 *
 * @author eatakishiyev
 */
public class CallBarredParam {

    private CallBarringCause callBarringCause;
    private ExtensibleCallBarredParam extensibleCallBarredParam;

    public CallBarredParam() {
    }

    public CallBarredParam(CallBarringCause callBarringCause) {
        this.callBarringCause = callBarringCause;
    }

    public CallBarredParam(ExtensibleCallBarredParam extensibleCallBarredParam) {
        this.extensibleCallBarredParam = extensibleCallBarredParam;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (callBarringCause != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, callBarringCause.value());
            } else {
                extensibleCallBarredParam.encode(aos);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.ENUMERATED
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.callBarringCause = CallBarringCause.getInstance((int) ais.readInteger());
            } else if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensibleCallBarredParam = new ExtensibleCallBarredParam();
                extensibleCallBarredParam.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the callBarringCause
     */
    public CallBarringCause getCallBarringCause() {
        return callBarringCause;
    }

    /**
     * @param callBarringCause the callBarringCause to set
     */
    public void setCallBarringCause(CallBarringCause callBarringCause) {
        this.callBarringCause = callBarringCause;
    }

    /**
     * @return the extensibleCallBarredParam
     */
    public ExtensibleCallBarredParam getExtensibleCallBarredParam() {
        return extensibleCallBarredParam;
    }

    /**
     * @param extensibleCallBarredParam the extensibleCallBarredParam to set
     */
    public void setExtensibleCallBarredParam(ExtensibleCallBarredParam extensibleCallBarredParam) {
        this.extensibleCallBarredParam = extensibleCallBarredParam;
    }

    @Override
    public String toString() {
        return "CallBarredParam{" +
                "callBarringCause=" + callBarringCause +
                ", extensibleCallBarredParam=" + extensibleCallBarredParam +
                '}';
    }

    public static class ExtensibleCallBarredParam {

        private CallBarringCause callBarringCause;
        private ExtensionContainer extensionContainer;
        private Boolean unauthorisedMessageOriginator;
        private Boolean anonymousCallRejection;

        public ExtensibleCallBarredParam() {
        }

        public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
            try {
                aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int lenPos = aos.StartContentDefiniteLength();
                if (callBarringCause != null) {
                    aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, callBarringCause.value());
                }

                if (extensionContainer != null) {
                    extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
                }

                if (unauthorisedMessageOriginator) {
                    aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
                }

                if (anonymousCallRejection) {
                    aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
                }
                aos.FinalizeContent(lenPos);
            } catch (AsnException | IOException ex) {
                throw new IncorrectSyntaxException(ex);
            }
        }

        public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
            try {
                while (ais.available() > 0) {
                    int tag = ais.readTag();
                    switch (tag) {
                        case Tag.ENUMERATED:
                            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                                this.callBarringCause = CallBarringCause.getInstance((int) ais.readInteger());
                            } else {
                                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]."
                                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                            }
                            break;
                        case Tag.SEQUENCE:
                            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                                this.extensionContainer = new ExtensionContainer(ais);
                            } else {
                                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]."
                                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                            }
                            break;
                        case 1:
                            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                                this.unauthorisedMessageOriginator = true;
                                ais.readNull();
                            } else {
                                throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]."
                                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                            }
                            break;
                        case 2:
                            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                                this.anonymousCallRejection = true;
                                ais.readNull();
                            } else {
                                throw new IncorrectSyntaxException(String.format("Expecting Tag[2] Class[CONTEXT]."
                                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                            }

                            break;
                        default:
                            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                    }
                }
            } catch (AsnException | IOException ex) {
                throw new IncorrectSyntaxException(ex);
            }
        }

        /**
         * @return the callBarringCause
         */
        public CallBarringCause getCallBarringCause() {
            return callBarringCause;
        }

        /**
         * @param callBarringCause the callBarringCause to set
         */
        public void setCallBarringCause(CallBarringCause callBarringCause) {
            this.callBarringCause = callBarringCause;
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
         * @return the unauthorisedMessageOriginator
         */
        public Boolean getUnauthorisedMessageOriginator() {
            return unauthorisedMessageOriginator;
        }

        /**
         * @param unauthorisedMessageOriginator the
         * unauthorisedMessageOriginator to set
         */
        public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {
            this.unauthorisedMessageOriginator = unauthorisedMessageOriginator;
        }

        /**
         * @return the anonymousCallRejection
         */
        public Boolean getAnonymousCallRejection() {
            return anonymousCallRejection;
        }

        /**
         * @param anonymousCallRejection the anonymousCallRejection to set
         */
        public void setAnonymousCallRejection(Boolean anonymousCallRejection) {
            this.anonymousCallRejection = anonymousCallRejection;
        }

        @Override
        public String toString() {
            return "ExtensibleCallBarredParam{" +
                    "callBarringCause=" + callBarringCause +
                    ", extensionContainer=" + extensionContainer +
                    ", unauthorisedMessageOriginator=" + unauthorisedMessageOriginator +
                    ", anonymousCallRejection=" + anonymousCallRejection +
                    '}';
        }
    }
}
