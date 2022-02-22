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
 *
 * @author eatakishiyev
 */
public class BurstList {

    public static final int MIN_WARNING_PERIOD = 1;
    public static final int MAX_WARNING_PERIOD = 1200;
    
    private int warningPeriod = 30;
    private Burst bursts;

    public BurstList() {
    }

    public BurstList(int warningPeriod, Burst burst) {
        this.warningPeriod = warningPeriod;
        this.bursts = burst;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IOException {

        this._doCheck();
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeTag(tagClass, false, tag);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, this.warningPeriod);
        bursts.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    public void decode(AsnInputStream ais, int legth) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStreamData(legth);
        this._decode(tmpAis);
    }

    private void _doCheck() throws ParameterOutOfRangeException, AsnException {
        if (bursts == null) {
            throw new AsnException("Expecting mandatory Burst parameter");
        }

        if (this.warningPeriod < MIN_WARNING_PERIOD
                || this.warningPeriod > MAX_WARNING_PERIOD) {
            throw new ParameterOutOfRangeException("Parameter WarningPeriod is out of the range[1..1200]");
        }
    }

    /**
     * @return the warningPeriod
     */
    public int getWarningPeriod() {
        return warningPeriod;
    }

    /**
     * @return the burst
     */
    public Burst getBursts() {
        return bursts;
    }

    private void _decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.warningPeriod = ((Long) ais.readInteger()).intValue();
                    break;
                case 1:
                    this.bursts = new Burst();
                    this.bursts.decode(ais);
                    break;
            }
        }

        this._doCheck();
    }

}
