/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * TimeIfTariffSwitch ::= SEQUENCE { timeSinceTariffSwitch [0]
 * INTEGER(0..864000), tariffSwitchInterval [1] INTEGER(1..864000) OPTIONAL } --
 * timeSinceTariffSwitch and tariffSwitchInterval are measured in 100
 * millisecond intervals
 *
 * @author eatakishiyev
 */
public class TimeIfTariffSwitch {

    private Integer timeSinceTariffSwitch;
    private Integer tariffSwitchInterval;

    public TimeIfTariffSwitch() {
    }

    public TimeIfTariffSwitch(Integer timeSinceTariffSwitch) {
        this.timeSinceTariffSwitch = timeSinceTariffSwitch;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        doCheck();
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, timeSinceTariffSwitch);
        if (tariffSwitchInterval != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, tariffSwitchInterval);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.decode_(ais.readSequenceStream());
        this.doCheck();
    }

    private void decode_(AsnInputStream ais) throws AsnException, IOException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagCLASS[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.timeSinceTariffSwitch = (int) ais.readInteger();
                    break;
                case 1:
                    this.tariffSwitchInterval = (int) ais.readInteger();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (timeSinceTariffSwitch < 0 || timeSinceTariffSwitch > 864000) {
            throw new ParameterOutOfRangeException(String.format("Parameter timeSinceTariffSwitch out of range [0..864000]. Actual value is %s", timeSinceTariffSwitch));
        }

        if (tariffSwitchInterval < 0 || tariffSwitchInterval > 864000) {
            throw new ParameterOutOfRangeException(String.format("Parameter tariffSwitchInterval out of range [0..864000]. Actual value is %s", tariffSwitchInterval));
        }
    }

    /**
     * @return the timeSinceTariffSwitch
     */
    public Integer getTimeSinceTariffSwitch() {
        return timeSinceTariffSwitch;
    }

    /**
     * @param timeSinceTariffSwitch the timeSinceTariffSwitch to set
     */
    public void setTimeSinceTariffSwitch(Integer timeSinceTariffSwitch) {
        this.timeSinceTariffSwitch = timeSinceTariffSwitch;
    }

    /**
     * @return the tariffSwitchInterval
     */
    public Integer getTariffSwitchInterval() {
        return tariffSwitchInterval;
    }

    /**
     * @param tariffSwitchInterval the tariffSwitchInterval to set
     */
    public void setTariffSwitchInterval(Integer tariffSwitchInterval) {
        this.tariffSwitchInterval = tariffSwitchInterval;
    }

}
