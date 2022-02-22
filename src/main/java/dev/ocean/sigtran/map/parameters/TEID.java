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
public class TEID extends OctetString{

    public TEID(){
        super();
    }
    
    public TEID(byte[] value){
        super(value);
    }
    @Override
    public int getMinLength() {
        return 4;
    }

    @Override
    public int getMaxLength() {
        return 4;
    }
    
}
