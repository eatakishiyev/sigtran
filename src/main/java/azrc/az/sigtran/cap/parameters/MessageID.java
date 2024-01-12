/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MessageID {PARAMETERS-BOUND : bound} ::= CHOICE { elementaryMessageID [0]
 * Integer4, text [1] SEQUENCE { messageContent [0] IA5String (SIZE(
 * bound.&minMessageContentLength .. bound.&maxMessageContentLength)),
 * attributes [1] OCTET STRING (SIZE( bound.&minAttributesLength ..
 * bound.&maxAttributesLength)) OPTIONAL }, elementaryMessageIDs [29] SEQUENCE
 * SIZE (1.. bound.&numOfMessageIDs) OF Integer4, variableMessage [30] SEQUENCE
 * { elementaryMessageID [0] Integer4, variableParts [1] SEQUENCE SIZE (1..5) OF
 * VariablePart {bound} } } -- Use of the text parameter is network
 * operator/equipment vendor specific.
 *
 * @author eatakishiyev
 */
public class MessageID {

    private Integer4 elementaryMessageID;
    private Text text;
    private List<Integer4> elementaryMessageIDs;
    private VariableMessage variableMessage;

    public MessageID() {
    }

    public MessageID(Integer4 elementaryMessageID) {
        this.elementaryMessageID = elementaryMessageID;
    }

    public MessageID(Text text) {
        this.text = text;
    }

    public MessageID(List<Integer4> elementaryMessageIDs) {
        this.elementaryMessageIDs = elementaryMessageIDs;
    }

    public MessageID(VariableMessage variableMessage) {
        this.variableMessage = variableMessage;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (elementaryMessageID != null) {
            this.elementaryMessageID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else if (text != null) {
            this.text.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        } else if (elementaryMessageIDs != null) {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 29);
            int lenPos_ = aos.StartContentDefiniteLength();
            for (Integer4 elementaryMessageId : elementaryMessageIDs) {
                elementaryMessageId.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);
            }
            aos.FinalizeContent(lenPos_);
        } else if (variableMessage != null) {
            this.variableMessage.encode(Tag.CLASS_CONTEXT_SPECIFIC, 30, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);

        this.decode_(new AsnInputStream(data));
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.elementaryMessageID = new Integer4();
                    this.elementaryMessageID.decode(ais);
                    break;
                case 1:
                    this.text = new Text();
                    this.text.decode(ais);
                    break;
                case 29:
                    this.elementaryMessageIDs = new ArrayList();
                    AsnInputStream tmpAis = ais.readSequenceStream();
                    while (tmpAis.available() > 0) {
                        Integer4 elementaryMessageId = new Integer4();
                        elementaryMessageId.decode(tmpAis);
                        elementaryMessageIDs.add(elementaryMessageId);
                    }
                    break;
                case 30:
                    this.variableMessage = new VariableMessage();
                    this.variableMessage.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the elementaryMessageID
     */
    public Integer4 getElementaryMessageID() {
        return elementaryMessageID;
    }

    /**
     * @return the text
     */
    public Text getText() {
        return text;
    }

    /**
     * @return the elementaryMessageIDs
     */
    public List<Integer4> getElementaryMessageIDs() {
        return elementaryMessageIDs;
    }

    /**
     * @return the variableMessage
     */
    public VariableMessage getVariableMessage() {
        return variableMessage;
    }

    public static class Text {

        private String messageContent;
        private byte[] attributes;

        public Text() {
        }

        public Text(String messageContent) {
            this.messageContent = messageContent;
        }

        public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeStringIA5(Tag.CLASS_CONTEXT_SPECIFIC, 0, messageContent);
            if (attributes != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, attributes);
            }

            aos.FinalizeContent(lenPos);
        }

        public void decode(AsnInputStream ais) throws IOException, AsnException {
            this.decode_(ais.readSequenceStream());
        }

        private void decode_(AsnInputStream ais) throws IOException, AsnException {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case 0:
                        this.messageContent = ais.readIA5String();
                        break;
                    case 1:
                        this.attributes = ais.readOctetString();
                        break;
                }
            }
        }

        /**
         * @return the messageContent
         */
        public String getMessageContent() {
            return messageContent;
        }

        /**
         * @param messageContent the messageContent to set
         */
        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        /**
         * @return the attributes
         */
        public byte[] getAttributes() {
            return attributes;
        }

        /**
         * @param attributes the attributes to set
         */
        public void setAttributes(byte[] attributes) {
            this.attributes = attributes;
        }

    }

    public static class VariableMessage {

        private Integer4 elementaryMessageID;
        private List<VariablePart> variableParts;

        public VariableMessage() {
        }

        public VariableMessage(Integer4 elementaryMessageID, List<VariablePart> variableParts) {
            this.elementaryMessageID = elementaryMessageID;
            this.variableParts = variableParts;
        }

        public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            this.elementaryMessageID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 1);
            int lenPos_ = aos.StartContentDefiniteLength();
            for (VariablePart variablePart : variableParts) {
                variablePart.encode(aos);
            }
            aos.FinalizeContent(lenPos_);
            aos.FinalizeContent(lenPos);
        }

        public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
            this.decode_(ais.readSequenceStream());
        }

        private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.elementaryMessageID = new Integer4();
                        this.elementaryMessageID.decode(ais);
                        break;
                    case 1:
                        this.variableParts = new ArrayList();

                        AsnInputStream tmpAis = ais.readSequenceStream();
                        while (tmpAis.available() > 0) {
                            VariablePart variablePart = VariablePart.create();
                            variablePart.decode(tmpAis);
                            this.variableParts.add(variablePart);
                        }
                }
            }
        }

        /**
         * @return the elementaryMessageID
         */
        public Integer4 getElementaryMessageID() {
            return elementaryMessageID;
        }

        /**
         * @param elementaryMessageID the elementaryMessageID to set
         */
        public void setElementaryMessageID(Integer4 elementaryMessageID) {
            this.elementaryMessageID = elementaryMessageID;
        }

        /**
         * @return the variableParts
         */
        public List<VariablePart> getVariableParts() {
            return variableParts;
        }

        /**
         * @param variableParts the variableParts to set
         */
        public void setVariableParts(List<VariablePart> variableParts) {
            this.variableParts = variableParts;
        }

    }
}
