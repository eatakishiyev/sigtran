/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * A solution was implemented with the Super-Charger realization in an effort to
 * reduce telecommunications signaling traffic. The general concept behind the
 * Super-Charger network is that the location registers in any visiting network
 * do not delete subscriber data even after a subscriber leaves their control, by,
 * for instance, exiting their coverage area. This allows the UE to jump back and
 * forth between the networks and, since the previous network, serving network, and
 * home network's HLR are Super-Charged and maintaining copies of the subscriber information,
 * the HLR does not have to re-send the subscriber information or Cancel Location request messages.
 *
 *
 * <li>Outdated subscriber information in visiting networks</li>
 * Outdated subscriber information is a concern if the UE does not re-enter the
 * previous visiting network after an extended period of time. To prevent out dating
 * of the subscriber data, an age indicator is associated with the subscriber information
 * maintained in all location registers. If the HLR deems that the age indicator in a
 * visiting network is too old, or determines the subscriber's information has changed since
 * the visiting network last received the information, the HLR will send the subscriber data like normal.
 *
 * <li>* Storage capacities in visiting networks</li>
 * If the visiting networks are maintaining subscriber information for all subscribers
 * that enter and then subsequently exit their coverage area, the visiting networks will
 * have to maintain a significant amount of subscriber information. Therefore,
 * visiting networks should implement database maintenance measures to ensure outdated
 * and unused subscriber information is removed. This will free up memory for new subscriber information.
 *
 * <li>Communication between Super-Charged entities and non-Super-Charged entities</li>
 * After an HLR receives an Update Location request from a visiting network, the HLR
 * must be able to identify whether the new and old visiting networks are Super-Charged or not.
 * If the old visiting network is Super-Charged, the HLR does not send a Cancel Location message.
 * If the old visiting network is not Super-Charged, the HLR sends the Cancel Location message.
 * If the new visiting network is Super-Charged and has previously received the subscriber
 * information and it is not outdated, the HLR does not send an Insert Subscriber Data request.
 * Otherwise, the HLR will send the Insert Subscriber Data request.
 *
 * <li>Determining whether a subscriber information deletion is due to database maintenance or instructed by a HLR</li>
 * Further signalling traffic reductions can occur by not sending acknowledgements.
 * Typically, when a HLR instructs a subscriber purge (via a Cancel Location message)
 * to a visiting network, the visiting network purges the subscriber information and
 * sends an acknowledgment back to the HLR. The networks can further cut back on signalling
 * traffic by not returning an acknowledgment, and just having the HLR assume the purge
 * was completed. However, since Super-Charged visiting networks can purge subscriber
 * data on their own due to periodic database cleanups, the visiting network register
 * must identify whether the subscriber purge is externally instructed. If the purge
 * is externally instructed, no acknowledgment is returned. If the purge is internally
 * instructed, an acknowledgment is sent to the HLR.
 *
 * @author eatakishiyev
 */
public class SuperChargerInfo {

    private Boolean sendSubscriberData;
    private AgeIndicator ageIndicator;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        if (sendSubscriberData) {
            aos.writeTag(tagClass, true, tag);
            aos.write(0x00);
        }
        if (ageIndicator != null) {
            ageIndicator.encode(tagClass, tag, aos);
        }
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        int length = ais.readLength();
        byte[] data = new byte[length];
        AsnInputStream tmpAis = new AsnInputStream(new ByteArrayInputStream(data));
        while (tmpAis.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    tmpAis.read();
                    sendSubscriberData = true;
                    break;
                case 1:
                    ageIndicator = new AgeIndicator();
                    ageIndicator.decode(tmpAis);
                    break;
            }
        }
    }

    /**
     * @return the sendSubscriberData
     */
    public Boolean getSendSubscriberData() {
        return sendSubscriberData;
    }

    /**
     * @param sendSubscriberData the sendSubscriberData to set
     */
    public void setSendSubscriberData(Boolean sendSubscriberData) {
        this.sendSubscriberData = sendSubscriberData;
        this.ageIndicator = null;
    }

    /**
     * @return the ageIndicator
     */
    public AgeIndicator getAgeIndicator() {
        return ageIndicator;
    }

    /**
     * @param ageIndicator the ageIndicator to set
     */
    public void setAgeIndicator(AgeIndicator ageIndicator) {
        this.ageIndicator = ageIndicator;
        this.sendSubscriberData = false;
    }
}
