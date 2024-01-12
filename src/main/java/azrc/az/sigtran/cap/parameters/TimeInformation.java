/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * TimeInformation ::= CHOICE { timeIfNoTariffSwitch [0] TimeIfNoTariffSwitch,
 * timeIfTariffSwitch [1] TimeIfTariffSwitch } -- Indicates call duration
 * information
 *
 * @author eatakishiyev
 */
public class TimeInformation {

    private TimeIfNoTariffSwitch timeIfNoTariffSwitch;
    private TimeIfTariffSwitch timeIfTariffSwitch;

    public TimeInformation() {
    }

    public TimeInformation(TimeIfNoTariffSwitch timeIfNoTariffSwitch) {
        this.timeIfNoTariffSwitch = timeIfNoTariffSwitch;
    }

    public TimeInformation(TimeIfTariffSwitch timeIfTariffSwitch) {
        this.timeIfTariffSwitch = timeIfTariffSwitch;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (timeIfNoTariffSwitch != null) {
            timeIfNoTariffSwitch.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else {
            timeIfTariffSwitch.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        aos.FinalizeContent(lenPos);

    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.timeIfNoTariffSwitch = new TimeIfNoTariffSwitch();
                    this.timeIfNoTariffSwitch.decode(ais);
                    break;
                case 1:
                    this.timeIfTariffSwitch = new TimeIfTariffSwitch();
                    this.timeIfTariffSwitch.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the timeIfNoTariffSwitch
     */
    public TimeIfNoTariffSwitch getTimeIfNoTariffSwitch() {
        return timeIfNoTariffSwitch;
    }

    /**
     * @param timeIfNoTariffSwitch the timeIfNoTariffSwitch to set
     */
    public void setTimeIfNoTariffSwitch(TimeIfNoTariffSwitch timeIfNoTariffSwitch) {
        this.timeIfNoTariffSwitch = timeIfNoTariffSwitch;
    }

    /**
     * @return the timeIfTariffSwitch
     */
    public TimeIfTariffSwitch getTimeIfTariffSwitch() {
        return timeIfTariffSwitch;
    }

    /**
     * @param timeIfTariffSwitch the timeIfTariffSwitch to set
     */
    public void setTimeIfTariffSwitch(TimeIfTariffSwitch timeIfTariffSwitch) {
        this.timeIfTariffSwitch = timeIfTariffSwitch;
    }
}
