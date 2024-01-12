/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SubscriberStatus ::= ENUMERATED {
 * serviceGranted (0),
 * operatorDeterminedBarring (1)}
 *
 * @author eatakishiyev
 */
public class SubscriberStatus {

    private SubscriberStatusEnum subscriberStatus;

    public SubscriberStatus() {

    }

    public SubscriberStatus(SubscriberStatusEnum subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, subscriberStatus.getValue());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, subscriberStatus.getValue());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(ByteArrayOutputStream baos) {
        baos.write(subscriberStatus.getValue());
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length != 1) {
                throw new IncorrectSyntaxException("Unexpected length received, expecting 1 found " + length);
            }
            this.subscriberStatus = SubscriberStatusEnum.getInstance(ais.read());
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        int value = bais.read();
        this.subscriberStatus = SubscriberStatusEnum.getInstance(value);
    }

    /**
     * @return the subscriberStatus
     */
    public SubscriberStatusEnum getSubscriberStatus() {
        return subscriberStatus;
    }

    /**
     * @param subscriberStatus the subscriberStatus to set
     */
    public void setSubscriberStatus(SubscriberStatusEnum subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }
}
