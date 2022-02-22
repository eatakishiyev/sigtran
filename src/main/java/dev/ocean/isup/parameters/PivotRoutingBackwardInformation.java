/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class PivotRoutingBackwardInformation implements IsupParameter {

    private byte[] data;

    @Override
    public byte[] encode() throws Exception {
        return data;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public int getParameterCode() {
        return PIVOT_ROUTING_BACKWARD_INFORMATION;
    }

    public byte[] getValue() {
        return data;
    }

    public void setValue(byte[] data) {
        this.data = data;
    }

}
