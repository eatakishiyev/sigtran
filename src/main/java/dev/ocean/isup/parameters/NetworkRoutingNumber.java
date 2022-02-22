/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class NetworkRoutingNumber implements IsupParameter {

    public static final int NUMBERING_PLAN_ISDN = 0b00000001;

    public static final int NUMBER_IN_NATIONAL_FORMAT = 0b00000001;
    public static final int NUMBER_IN_NETWORK_SPECIFIC_FORMAT = 0b00000010;

    private boolean even;
    private int numberingPlanIndicator;
    private int natureOfAddressIndicator;
    private String networkRoutingNumber;

    public NetworkRoutingNumber() {

    }

    public NetworkRoutingNumber(int numberingPlanIndicator, int natureOfAddressIndicator, String address) {
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.natureOfAddressIndicator = natureOfAddressIndicator;
        this.networkRoutingNumber = address;
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException, IOException {
        Address address = new Address(networkRoutingNumber); //networkIdentification digits with stop signal
        byte[] data = address.encode();

        this.even = (data.length % 2) == 0;
        int firstByte = even ? 0 : 1;
        firstByte = firstByte << 3;
        firstByte = firstByte | numberingPlanIndicator;
        firstByte = firstByte << 4;
        firstByte = firstByte | natureOfAddressIndicator;
        baos.write(firstByte);

        baos.write(data);
    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    @Override
    public void decode(byte[] data) throws IllegalNumberFormatException {
        int firstByte = data[0] & 0xFF;
        this.even = (firstByte >> 7) == 0;
        this.numberingPlanIndicator = (firstByte >> 4) & 0b00000111;
        this.natureOfAddressIndicator = (firstByte) & 0b00001111;

        Address address_ = new Address();
        address_.decode(data, 1);
        if (even) {
            this.networkRoutingNumber = address_.getAddress();
        } else {
            this.networkRoutingNumber = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }
    }

    public void decode(ByteArrayInputStream bais) throws IllegalNumberFormatException {
        int firstByte = bais.read() & 0xFF;
        this.even = (firstByte >> 7) == 0;
        this.numberingPlanIndicator = (firstByte >> 4) & 0b00000111;
        this.natureOfAddressIndicator = (firstByte) & 0b00001111;

        Address address_ = new Address();
        address_.decode(bais);
        if (even) {
            this.networkRoutingNumber = address_.getAddress();
        } else {
            this.networkRoutingNumber = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }
    }

    public String getNetworkRoutingNumber() {
        return networkRoutingNumber;
    }

    public void setNetworkRoutingNumber(String networkRoutingNumber) {
        this.networkRoutingNumber = networkRoutingNumber;
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(int natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    @Override
    public int getParameterCode() {
        return NETWORK_ROUTING_NUMBER;
    }

}
