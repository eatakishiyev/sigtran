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

/**
 * ApplicationTimer ::=INTEGER (0..2047) -- Used by the gsmSCF to set a timer in
 * the gsmSSF. The timer is in seconds.
 *
 * @author eatakishiyev
 */
public class ApplicationTimer {

    private int value;
    public final static int MIN_TIMER_VALUE = 0;
    public final static int MAX_TIMER_VALUE = 2047;

    public ApplicationTimer() {
    }

    public ApplicationTimer(int value) {
        this.value = value;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws ParameterOutOfRangeException, IOException, AsnException {
        this.doCheck();
        aos.writeInteger(tagClass, tag, value);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.value = ((Long) ais.readInteger()).intValue();
        this.doCheck();
    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException, ParameterOutOfRangeException {
        this.value = ((Long) ais.readIntegerData(length)).intValue();
        this.doCheck();
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (this.value < MIN_TIMER_VALUE || this.value > MAX_TIMER_VALUE) {
            throw new ParameterOutOfRangeException("ApplicationTimer parameter is out of range [0..2047]");
        }
    }

    public int value() {
        return value;
    }
}
