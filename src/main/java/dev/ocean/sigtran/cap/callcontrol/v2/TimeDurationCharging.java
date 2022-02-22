/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v2;

import java.io.IOException;
import dev.ocean.sigtran.cap.api.ITimeDurationCharging;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * timeDurationCharging	[0] SEQUENCE {
 * maxCallPeriodDuration	[0] INTEGER (1..864000),
 * releaseIfdurationExceeded	[1] ReleaseIfDurationExceeded OPTIONAL,
 * tariffSwitchInterval	[2] INTEGER (1..86400)	OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class TimeDurationCharging implements ITimeDurationCharging {

    private final int MAX_VAL = 864000;
    private final int MIN_VAL = 1;
    private int maxCallPeriodDuration;
    private ReleaseIfDurationExceeded releaseIfDurationExceeded;
    private Integer tariffSwitchInterval;

    public TimeDurationCharging() {
    }

    public TimeDurationCharging(int maxCallPeriodDuration) {
        this.maxCallPeriodDuration = maxCallPeriodDuration;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (maxCallPeriodDuration < MIN_VAL || maxCallPeriodDuration > MAX_VAL) {
            throw new ParameterOutOfRangeException("MaxCallPeriodDuration is out of range[1..864000]. Actual value is " + maxCallPeriodDuration);
        }
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, this.maxCallPeriodDuration);

        if (releaseIfDurationExceeded != null) {
            releaseIfDurationExceeded.encode(aos);
        }

        if (tariffSwitchInterval != null) {
            if (tariffSwitchInterval < MIN_VAL || tariffSwitchInterval > MAX_VAL) {
                throw new ParameterOutOfRangeException("TariffSwitchInterval is out of range[1..864000]. Actual value is " + tariffSwitchInterval);
            }
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, tariffSwitchInterval);
        }

        aos.FinalizeContent(lenPos);
    }

    @Override
    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.maxCallPeriodDuration = (int) ais.readInteger();
                    if (maxCallPeriodDuration < MIN_VAL || maxCallPeriodDuration > MAX_VAL) {
                        throw new ParameterOutOfRangeException("MaxCallPeriodDuration is out of range[1..864000]. Actual value is " + maxCallPeriodDuration);
                    }
                    break;
                case 1:
                    this.releaseIfDurationExceeded = new ReleaseIfDurationExceeded();
                    this.releaseIfDurationExceeded.decode(ais);
                    break;
                case 2:
                    this.tariffSwitchInterval = (int) ais.readInteger();
                    if (tariffSwitchInterval < MIN_VAL || tariffSwitchInterval > MAX_VAL) {
                        throw new ParameterOutOfRangeException("TariffSwitchInterval is out of range[1..864000]. Actual value is " + tariffSwitchInterval);
                    }
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the maxCallPeriodDuration
     */
    @Override
    public int getMaxCallPeriodDuration() {
        return maxCallPeriodDuration;
    }

    /**
     * @param maxCallPeriodDuration the maxCallPeriodDuration to set
     */
    @Override
    public void setMaxCallPeriodDuration(int maxCallPeriodDuration) {
        this.maxCallPeriodDuration = maxCallPeriodDuration;
    }

    /**
     * @return the releaseIfDurationExceeded
     */
    public ReleaseIfDurationExceeded getReleaseIfDurationExceeded() {
        return releaseIfDurationExceeded;
    }

    /**
     * @param releaseIfDurationExceeded the releaseIfDurationExceeded to set
     */
    public void setReleaseIfDurationExceeded(ReleaseIfDurationExceeded releaseIfDurationExceeded) {
        this.releaseIfDurationExceeded = releaseIfDurationExceeded;
    }

    /**
     * @return the tariffSwitchInterval
     */
    @Override
    public Integer getTariffSwitchInterval() {
        return tariffSwitchInterval;
    }

    /**
     * @param tariffSwitchInterval the tariffSwitchInterval to set
     */
    @Override
    public void setTariffSwitchInterval(Integer tariffSwitchInterval) {
        this.tariffSwitchInterval = tariffSwitchInterval;
    }
}
