/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public class Ext3QoSSubscribed extends OctetString {

    public Ext3QoSSubscribed() {
        super();
    }

    public Ext3QoSSubscribed(byte[] value) {
        super(value);
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int getMaxLength() {
        return 2;
    }

}
