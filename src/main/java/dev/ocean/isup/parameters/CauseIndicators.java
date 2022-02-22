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
public class CauseIndicators implements IsupParameter {

    private byte[] data;

    public CauseIndicators() {
    }

    @Override
    public byte[] encode() throws Exception {
        return this.data;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public int getParameterCode() {
        return CAUSE_INDICATORS;
    }

}
