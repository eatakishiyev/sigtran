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
 * Integer4 ::= INTEGER(0..2147483647)
 *
 * @author eatakishiyev
 */
public class Integer4 {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 2147483647;
    private int value;

    public Integer4() {
    }

    public Integer4(int value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException, ParameterOutOfRangeException {
        this.doCheck();
        aos.writeInteger(tagClass, tag, value);
    }
    
    public void encode(AsnOutputStream aos) throws IOException, AsnException, ParameterOutOfRangeException{
        this.doCheck();
        aos.writeInteger(value);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.value = (int) ais.readInteger();
        this.doCheck();
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new ParameterOutOfRangeException(String.format("Parameter is out the range [0..2147483647], value = %s", value));
        }
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

}
