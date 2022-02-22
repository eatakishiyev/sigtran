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
public enum DomainType {
    CS_DOMAIN(0),
    PS_DOMAIN(1),
    UNKNOWN(-1);

    private final int value;

    private DomainType(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
    
    public static DomainType getInstance(int v){
        switch(v){
            case 0:
                return CS_DOMAIN;
            case 1:
                return PS_DOMAIN;
            default:
                return UNKNOWN;
        }
    }
}
