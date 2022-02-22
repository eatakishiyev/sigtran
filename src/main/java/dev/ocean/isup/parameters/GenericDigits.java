/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.TypeOfDigits;
import dev.ocean.isup.enums.EncodingScheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class GenericDigits implements IsupParameter {

    private EncodingScheme encodingScheme;
    private TypeOfDigits typeOfDigits;
    private byte[] digits;

    public GenericDigits() {
    }

    public GenericDigits(EncodingScheme encodingScheme, TypeOfDigits typeOfDigits, byte[] digit) {
        this.encodingScheme = encodingScheme;
        this.typeOfDigits = typeOfDigits;
        this.digits = digit;
    }

    public void encode(ByteArrayOutputStream baos) throws IOException {
        int b = encodingScheme.ordinal();
        b = (b << 5) | typeOfDigits.ordinal();
        baos.write(b);

        baos.write(digits);
    }

    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        int b = bais.read() & 0xFF;
        this.encodingScheme = EncodingScheme.valueOf(b >> 5);
        this.typeOfDigits = TypeOfDigits.valueOf(b & 0b00011111);

        this.digits = new byte[bais.available()];
        bais.read(digits);
    }

    @Override
    public void decode(byte[] data) throws IOException {
        int b = data[0] & 0xFF;
        this.encodingScheme = EncodingScheme.valueOf(b >> 5);
        this.typeOfDigits = TypeOfDigits.valueOf(b & 0b00011111);

        this.digits = new byte[data.length - 1];
        System.arraycopy(data, 1, digits, 0, data.length - 1);
    }

    /**
     * @return the encodingScheme
     */
    public EncodingScheme getEncodingScheme() {
        return encodingScheme;
    }

    /**
     * @param encodingScheme the encodingScheme to set
     */
    public void setEncodingScheme(EncodingScheme encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    /**
     * @return the typeOfDigits
     */
    public TypeOfDigits getTypeOfDigits() {
        return typeOfDigits;
    }

    /**
     * @param typeOfDigits the typeOfDigits to set
     */
    public void setTypeOfDigits(TypeOfDigits typeOfDigits) {
        this.typeOfDigits = typeOfDigits;
    }

    /**
     * @return the digit
     */
    public byte[] getDigit() {
        return digits;
    }

    /**
     * @param digit the digit to set
     */
    public void setDigit(byte[] digit) {
        this.digits = digit;
    }

    @Override
    public int getParameterCode() {
        return GENERIC_DIGITS;
    }

}
