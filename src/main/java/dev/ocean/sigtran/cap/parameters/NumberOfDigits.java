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
 * NumberOfDigits ::= INTEGER (1..255) -- Indicates the number of digits to be
 * collected.
 *
 * @author eatakishiyev
 */
public class NumberOfDigits {

    private int value;
    public static final int MIN_NUMBER_OF_DIGITS = 1;
    public static final int MAX_NUMBER_OF_DIGITS = 255;

    public NumberOfDigits() {

    }

    public NumberOfDigits(int value) {
        this.value = value;
    }

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws ParameterOutOfRangeException, IOException, AsnException {
        if (value < MIN_NUMBER_OF_DIGITS || value > MAX_NUMBER_OF_DIGITS) {
            throw new ParameterOutOfRangeException("Parameter NumberOfDigits is out of range [1..255]");
        }

        aos.writeInteger(tagClass, tag, value);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.value = (int) ais.readInteger();
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
