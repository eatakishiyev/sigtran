/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SubscriberState::= CHOICE { assumedIdle [0] NULL, camelBusy [1] NULL,
 * netDetNotReachable NotReachableReason, notProvidedFromVLR [2] NULL}
 *
 * @author eatakishiyev
 */
public class SubscriberState {

    private boolean assumedIdle = false;
    private boolean camelBusy = false;
    private NotReachableReason notReachableReason;
    private boolean notProvidedFromVLR = false;

    private SubscriberState() {

    }

    public static SubscriberState create() {
        return new SubscriberState();
    }

    public static SubscriberState createAssumedIdle() {
        SubscriberState instance = new SubscriberState();
        instance.assumedIdle = true;
        return instance;
    }

    public static SubscriberState createCamelBusy() {
        SubscriberState instance = new SubscriberState();
        instance.camelBusy = true;
        return instance;
    }

    public static SubscriberState createNotReachableReason(NotReachableReason notReachableReason) {
        SubscriberState instance = new SubscriberState();
        instance.notReachableReason = notReachableReason;
        return instance;
    }

    public static SubscriberState createNotProvidedFromVLR() {
        SubscriberState instance = new SubscriberState();
        instance.notProvidedFromVLR = true;
        return instance;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (assumedIdle) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
        } else if (camelBusy) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
        } else if (notReachableReason != null) {
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, notReachableReason.value());
        } else {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        int length = ais.readLength();
        int tag = ais.readTag();
        switch (tag) {
            case 0:
                this.assumedIdle = true;
                ais.readNull();
                break;
            case 1:
                this.camelBusy = true;
                ais.readNull();
                break;
            case Tag.ENUMERATED:
                this.notReachableReason = NotReachableReason.getInstance((int) ais.readInteger());
                break;
            case 2:
                this.notProvidedFromVLR = true;
                ais.readNull();
                break;
        }
    }

    /**
     * @return the assumedIdle
     */
    public Boolean isAssumedIdle() {
        return assumedIdle;
    }

    /**
     * @return the camelBusy
     */
    public Boolean isCamelBusy() {
        return camelBusy;
    }

    /**
     * @return the notReachableReason
     */
    public NotReachableReason getNotReachableReason() {
        return notReachableReason;
    }

    /**
     * @return the notProvidedFromVLR
     */
    public Boolean isNotProvidedFromVLR() {
        return notProvidedFromVLR;
    }

    public boolean isMsPurged() {
        return notReachableReason != null
                && notReachableReason == NotReachableReason.MS_PURGED;
    }

    public boolean isImsiDetached() {
        return notReachableReason != null
                && notReachableReason == NotReachableReason.IMSI_DETACHED;
    }

    public boolean isRestrictedArea() {
        return notReachableReason != null
                && notReachableReason == NotReachableReason.RESTRICTED_AREA;
    }

    public boolean isNotRegistered() {
        return notReachableReason != null
                && notReachableReason == NotReachableReason.NOT_REGISTERED;
    }

}
