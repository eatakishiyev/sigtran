/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v3;

import java.io.IOException;
import dev.ocean.sigtran.cap.api.ITimeDurationCharging;
import dev.ocean.sigtran.cap.parameters.AudibleIndicator;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * timeDurationCharging [0] SEQUENCE {
 * maxCallPeriodDuration [0] INTEGER (1..864000),
 * releaseIfdurationExceeded [1] BOOLEAN DEFAULT FALSE,
 * tariffSwitchInterval [2] INTEGER (1..86400) OPTIONAL,
 * audibleIndicator [3] AudibleIndicator DEFAULT tone: FALSE,
 * extensions [4] Extensions {bound} OPTIONAL,
 * ...
 * }
 *
 * @author eatakishiyev
 */
public class TimeDurationCharging implements ITimeDurationCharging {

    private final int MAX_VAL = 864000;
    private final int MIN_VAL = 1;
    private int maxCallPeriodDuration;
    private Boolean releaseIfDurationExceeded = null;
    private Integer tariffSwitchInterval;
    private AudibleIndicator audibleIndicator;
    private byte[] extensions;

    public TimeDurationCharging() {
    }

    public TimeDurationCharging(int maxCallPeriodDuration, boolean releaseIfDurationExceeded, AudibleIndicator audibleIndicator) {
        this.maxCallPeriodDuration = maxCallPeriodDuration;
        this.releaseIfDurationExceeded = releaseIfDurationExceeded;
        this.audibleIndicator = audibleIndicator;
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
            aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 1, releaseIfDurationExceeded);
        }

        if (tariffSwitchInterval != null) {
            if (tariffSwitchInterval < MIN_VAL || tariffSwitchInterval > MAX_VAL) {
                throw new ParameterOutOfRangeException("TariffSwitchInterval is out of range[1..864000]. Actual value is " + tariffSwitchInterval);
            }
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, tariffSwitchInterval);
        }

        if (audibleIndicator != null) {
            audibleIndicator.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        }

        if (extensions != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, extensions);
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
                    this.releaseIfDurationExceeded = ais.readBoolean();
                    break;
                case 2:
                    this.tariffSwitchInterval = (int) ais.readInteger();
                    if (tariffSwitchInterval < MIN_VAL || tariffSwitchInterval > MAX_VAL) {
                        throw new ParameterOutOfRangeException("TariffSwitchInterval is out of range[1..864000]. Actual value is " + tariffSwitchInterval);
                    }
                    break;
                case 3:
                    this.audibleIndicator = new AudibleIndicator();
                    this.audibleIndicator.decode(ais);
                    break;
                case 4:
                    this.extensions = ais.readSequence();
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
    public boolean isReleaseIfDurationExceeded() {
        return releaseIfDurationExceeded;
    }

    /**
     * @param releaseIfDurationExceeded the releaseIfDurationExceeded to set
     */
    public void setReleaseIfDurationExceeded(boolean releaseIfDurationExceeded) {
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

    /**
     * @return the audibleIndicator
     */
    public AudibleIndicator getAudibleIndicator() {
        return audibleIndicator;
    }

    /**
     * @param audibleIndicator the audibleIndicator to set
     */
    public void setAudibleIndicator(AudibleIndicator audibleIndicator) {
        this.audibleIndicator = audibleIndicator;
    }

    /**
     * @return the extensions
     */
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
