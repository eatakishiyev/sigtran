/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * InbandInfo {PARAMETERS-BOUND : bound} ::= SEQUENCE { messageID [0] MessageID
 * {bound}, numberOfRepetitions [1] INTEGER (1..127) OPTIONAL, duration [2]
 * INTEGER (0..32767) OPTIONAL, interval [3] INTEGER (0..32767) OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class InbandInfo {

    private MessageID messageId;
    private Integer numberOfRepetitions;
    private Integer duration;
    private Integer interval;

    public InbandInfo() {
    }

    public InbandInfo(MessageID messageId) {
        this.messageId = messageId;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IOException {
        this.doCheck();

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.messageId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        if (numberOfRepetitions != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, numberOfRepetitions);
        }

        if (duration != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, duration);
        }

        if (interval != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, interval);
        }

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
                    this.messageId = new MessageID();
                    this.messageId.decode(ais);
                    break;
                case 1:
                    this.numberOfRepetitions = (int) ais.readInteger();
                    break;
                case 2:
                    this.duration = (int) ais.readInteger();
                    break;
                case 3:
                    this.interval = (int) ais.readInteger();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (numberOfRepetitions < 1 || numberOfRepetitions > 127) {
            throw new ParameterOutOfRangeException(String.format("Parameter numberOfRepetitions is out the range [1..127]. numberOfRepetitions = %s", numberOfRepetitions));
        }

        if (duration < 0 || duration > 32767) {
            throw new ParameterOutOfRangeException(String.format("Parameter duration is out the range [0..32767]. duration = %s", duration));
        }

        if (interval < 0 || interval > 32767) {
            throw new ParameterOutOfRangeException(String.format("Parameter interval is out the range [0..32767]. interval = %s", interval));
        }
    }

    /**
     * @return the messageId
     */
    public MessageID getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(MessageID messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the numberOfRepetitions
     */
    public Integer getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    /**
     * @param numberOfRepetitions the numberOfRepetitions to set
     */
    public void setNumberOfRepetitions(Integer numberOfRepetitions) {
        this.numberOfRepetitions = numberOfRepetitions;
    }

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}
