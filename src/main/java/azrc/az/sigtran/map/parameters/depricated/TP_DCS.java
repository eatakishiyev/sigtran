/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters.depricated;

import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class TP_DCS {

    //3GPP TS 23.038 Specification
    private DataCodingIndicator dataCodingIndicator;
    private TextCompression textCompression;
    private MessageClassMeaning messageClassMeaning;
    private MessageClass messageClass;
    private EncodingCharacterSet encodingCharacterSet;
    private IndicationSense indicationSense;
    private IndicationType indicationType;

    public void encode(AsnOutputStream aos) {
        switch (dataCodingIndicator) {
            case generalDataCodingInd:
            case messageMarkedForAutomaticDeletionGroup:
                encode_GeneralDataCoding(aos);
                break;
            case messageWaitingInd_DiscardMessage:
            case messageWaitingInd_StoreMessageGSM7bit:
            case messageWaitingInd_StoreMessageUCS2:
                encode_MessageWaitingInd(aos);
                break;
        }
    }

    /**
     * @return the dataCodingIndicator
     */
    public DataCodingIndicator getDataCodingIndicator() {
        return dataCodingIndicator;
    }

    /**
     * @param dataCodingIndicator the dataCodingIndicator to set
     */
    public void setDataCodingIndicator(DataCodingIndicator dataCodingIndicator) {
        this.dataCodingIndicator = dataCodingIndicator;
    }

    /**
     * @return the textCompression
     */
    public TextCompression getTextCompression() {
        return textCompression;
    }

    /**
     * @param textCompression the textCompression to set
     */
    public void setTextCompression(TextCompression textCompression) {
        this.textCompression = textCompression;
    }

    /**
     * @return the messageClassMeaning
     */
    public MessageClassMeaning getMessageClassMeaning() {
        return messageClassMeaning;
    }

    /**
     * @param messageClassMeaning the messageClassMeaning to set
     */
    public void setMessageClassMeaning(MessageClassMeaning messageClassMeaning) {
        this.messageClassMeaning = messageClassMeaning;
    }

    /**
     * @return the messageClass
     */
    public MessageClass getMessageClass() {
        return messageClass;
    }

    /**
     * @param messageClass the messageClass to set
     */
    public void setMessageClass(MessageClass messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * @return the encodingCharacterSet
     */
    public EncodingCharacterSet getEncodingCharacterSet() {
        return encodingCharacterSet;
    }

    /**
     * @param encodingCharacterSet the encodingCharacterSet to set
     */
    public void setEncodingCharacterSet(EncodingCharacterSet encodingCharacterSet) {
        this.encodingCharacterSet = encodingCharacterSet;
    }

    /**
     * @return the indicationSense
     */
    public IndicationSense getIndicationSense() {
        return indicationSense;
    }

    /**
     * @param indicationSense the indicationSense to set
     */
    public void setIndicationSense(IndicationSense indicationSense) {
        this.indicationSense = indicationSense;
    }

    /**
     * @return the indicationType
     */
    public IndicationType getIndicationType() {
        return indicationType;
    }

    /**
     * @param indicationType the indicationType to set
     */
    public void setIndicationType(IndicationType indicationType) {
        this.indicationType = indicationType;
    }

    private void encode_GeneralDataCoding(AsnOutputStream aos) {
        int encoded = dataCodingIndicator.value << 6;
        encoded = encoded | (textCompression.value << 5);
        encoded = encoded | (messageClassMeaning.value << 4);
        encoded = encoded | messageClass.value;//Look at 3GPP TS 23.038
        encoded = encoded | (encodingCharacterSet.value << 2);

    }

    private void encode_MessageWaitingInd(AsnOutputStream aos) {
        int encoded = dataCodingIndicator.value << 4;
        encoded = encoded | (indicationSense.value << 3);
        encoded = encoded | (0 << 2);//Reserved
        encoded = encoded | indicationType.value;
    }

    public enum DataCodingIndicator {

        generalDataCodingInd(0),
        messageMarkedForAutomaticDeletionGroup(1),
        messageWaitingInd_DiscardMessage(12),
        messageWaitingInd_StoreMessageGSM7bit(13),
        messageWaitingInd_StoreMessageUCS2(14),
        dataCoding_MessageClass(15),
        reserved(-1);
        private int value;
        private int reservedValue;

        private DataCodingIndicator(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        /**
         *
         * @param value
         * @return DataCodingIndicator In case of reserved returned
         * getReserved() method will return reserved value
         */
        public DataCodingIndicator getInstance(int value) {
            switch (value) {
                case 0:
                    return generalDataCodingInd;
                case 1:
                    return messageMarkedForAutomaticDeletionGroup;
                case 12:
                    return messageWaitingInd_DiscardMessage;
                case 13:
                    return messageWaitingInd_StoreMessageGSM7bit;
                case 14:
                    return messageWaitingInd_StoreMessageUCS2;
                case 15:
                    return dataCoding_MessageClass;
                default:
                    reservedValue = value;
                    return reserved;

            }
        }

        public int getReserved() {
            return reservedValue;
        }
    }

    public enum TextCompression {

        compressed(0),
        uncompressed(1);
        private int value;

        private TextCompression(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum MessageClassMeaning {

        noMeaning(0),
        meaning(1);
        private int value;

        private MessageClassMeaning(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum MessageClass {

        Class0(0),
        Class1(1),
        Class2(2),
        Class3(3);
        private int value;

        private MessageClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum EncodingCharacterSet {

        GSM7bitDefaultAlphabet(0),
        _8bitData(1),
        UCS2(2),
        reserved(3);
        private int value;

        private EncodingCharacterSet(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum IndicationSense {

        Inactive(0),
        Active(1);
        private int value;

        private IndicationSense(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum IndicationType {

        VoiceMailWaiting(0),
        FaxMessageWaiting(1),
        ElectronicMailMessageWaiting(2),
        OtherMessageWaiting(3);
        private int value;

        private IndicationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
