/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MiscCallInfo ::= SEQUENCE { messageType [0] ENUMERATED {request(0),
 * notification(1)}, dpAssignment [1] ENUMERATED {individualLine(0),
 * groupBased(1), officeBased(2)} OPTIONAL }
 *
 * @author eatakishiyev
 */
public class MiscCallInfo {

    private MessageType messageType = MessageType.REQUEST;
    private DpAssigment dpAssigment;

    public MiscCallInfo() {

    }

    public MiscCallInfo(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public DpAssigment getDpAssigment() {
        return dpAssigment;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, messageType.ordinal());

        if (dpAssigment != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, dpAssigment.ordinal());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.messageType = MessageType.valueOf((int) ais.readInteger());
                    break;
                case 1:
                    this.dpAssigment = DpAssigment.valueOf((int) ais.readInteger());
                    break;
            }
        }
    }

    public enum MessageType {

        REQUEST(0),
        NOTIFICATION(1);

        private final int value;

        private MessageType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static MessageType valueOf(int value) {
            return value == 0 ? REQUEST : NOTIFICATION;
        }
    }

    public enum DpAssigment {

        INDIVIDUAL_LINE(0),
        GROUP_BASED(1),
        OFFICE_BASED(2);

        private final int value;

        private DpAssigment(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static DpAssigment valueOf(int value) {
            switch (value) {
                case 0:
                    return INDIVIDUAL_LINE;
                case 1:
                    return GROUP_BASED;
                case 2:
                    return OFFICE_BASED;
                default:
                    return INDIVIDUAL_LINE;
            }
        }
    }

}
