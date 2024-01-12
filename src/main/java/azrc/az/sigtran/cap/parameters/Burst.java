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
 * Burst ::= SEQUENCE { numberOfBursts [0] INTEGER (1..3) DEFAULT 1,
 * burstInterval [1] INTEGER (1..1200) DEFAULT 2, * numberOfTonesInBurst [2]
 * INTEGER (1..3) DEFAULT 3, toneDuration [3] INTEGER (1..20) DEFAULT 2,
 * toneInterval [4] INTEGER (1..20) DEFAULT 2, ... } -- burstInterval,
 * toneDurartion and toneInterval are measured in 100 millisecond units
 *
 * @author eatakishiyev
 */
public class Burst {

    private int numberOfBursts = 1;
    private int burstInterval = 2;
    private int numberOfTonesInBurst = 3;
    private int toneDuration = 2;
    private int toneInterval = 2;

    public static final int MIN_VALUE = 1;
    public static final int MAX_NUMBER_OF_BURSTS = 3;
    public static final int MAX_BURST_INTERVAL = 1200;
    public static final int MAX_NUMBER_OF_TONES_IN_BURST = 3;
    public static final int MAX_TONE_DURATION = 20;
    public static final int MAX_TONE_INTERVAL = 20;

    public Burst() {
    }

    public Burst(int numberOfBursts, int burstInterval, int numberOfTonesInBurst, int toneDuration, int toneInterval) {
        this.numberOfBursts = numberOfBursts;
        this.burstInterval = burstInterval;
        this.numberOfTonesInBurst = numberOfTonesInBurst;
        this.toneDuration = toneDuration;
        this.toneInterval = toneInterval;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException, ParameterOutOfRangeException {
        this._doCheck();

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, this.numberOfBursts);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, this.burstInterval);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, this.numberOfTonesInBurst);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, this.toneDuration);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, this.toneInterval);

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);

    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(length);
        this._decode(tmpAis);

    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();
            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.numberOfBursts = ((Long) ais.readInteger()).intValue();
                    break;
                case 1:
                    this.burstInterval = ((Long) ais.readInteger()).intValue();
                    break;
                case 2:
                    this.numberOfTonesInBurst = ((Long) ais.readInteger()).intValue();
                    break;
                case 3:
                    this.toneDuration = ((Long) ais.readInteger()).intValue();
                    break;
                case 4:
                    this.toneInterval = ((Long) ais.readInteger()).intValue();
                    break;
            }
        }
        
        this._doCheck();
    }

    private void _doCheck() throws ParameterOutOfRangeException {
        if (numberOfBursts < MIN_VALUE || numberOfBursts > MAX_NUMBER_OF_BURSTS) {
            throw new ParameterOutOfRangeException("NumberOfBursts out of range [1..3]");
        }

        if (burstInterval < MIN_VALUE || burstInterval > MAX_BURST_INTERVAL) {
            throw new ParameterOutOfRangeException("BurstInterval out of range [1..1200]");

        }

        if (numberOfTonesInBurst < MIN_VALUE || numberOfTonesInBurst > MAX_NUMBER_OF_TONES_IN_BURST) {
            throw new ParameterOutOfRangeException("NumberOfTonesInBurst out of range [1..3]");
        }

        if (toneDuration < MIN_VALUE || toneDuration > MAX_TONE_DURATION) {
            throw new ParameterOutOfRangeException("ToneDuration out of range [1..20]");
        }

        if (toneInterval < MIN_VALUE || toneInterval > MAX_TONE_INTERVAL) {
            throw new ParameterOutOfRangeException("ToneInterval out of range [1..20]");
        }
    }

    /**
     * @return the numberOfBursts
     */
    public Integer getNumberOfBursts() {
        return numberOfBursts;
    }

    /**
     * @param numberOfBursts the numberOfBursts to set
     */
    public void setNumberOfBursts(Integer numberOfBursts) {
        this.numberOfBursts = numberOfBursts;
    }

    /**
     * @return the burstInterval
     */
    public Integer getBurstInterval() {
        return burstInterval;
    }

    /**
     * @param burstInterval the burstInterval to set
     */
    public void setBurstInterval(Integer burstInterval) {
        this.burstInterval = burstInterval;
    }

    /**
     * @return the numberOfTonesInBurst
     */
    public Integer getNumberOfTonesInBurst() {
        return numberOfTonesInBurst;
    }

    /**
     * @param numberOfTonesInBurst the numberOfTonesInBurst to set
     */
    public void setNumberOfTonesInBurst(Integer numberOfTonesInBurst) {
        this.numberOfTonesInBurst = numberOfTonesInBurst;
    }

    /**
     * @return the toneDuration
     */
    public Integer getToneDuration() {
        return toneDuration;
    }

    /**
     * @param toneDuration the toneDuration to set
     */
    public void setToneDuration(Integer toneDuration) {
        this.toneDuration = toneDuration;
    }

    /**
     * @return the toneInterval
     */
    public Integer getToneInterval() {
        return toneInterval;
    }

    /**
     * @param toneInterval the toneInterval to set
     */
    public void setToneInterval(Integer toneInterval) {
        this.toneInterval = toneInterval;
    }

}
