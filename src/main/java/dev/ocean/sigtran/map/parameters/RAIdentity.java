/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * RAIdentity ::= OCTET STRING (SIZE (6)) -- Routing Area Identity is coded in
 * accordance with 3GPP TS 29.060 [105]. -- It shall contain the value part
 * defined in 3GPP TS 29.060 only. I.e. the 3GPP TS 29.060 -- type identifier
 * octet shall not be included.
 *
 * @author eatakishiyev
 */
public class RAIdentity extends OctetString {

    public RAIdentity() {
    }

    public RAIdentity(byte[] value) {
        super(value);
    }

    @Override
    public int getMaxLength() {
        return 6;
    }

    @Override
    public int getMinLength() {
        return 6;
    }
    
    

}
