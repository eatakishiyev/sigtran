/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum Screening {
    RESERVED(0),
    USER_PROVIDED(1),
    NETWORK_PROVIDED(3);
    
    private final int value;

    private Screening(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
    
    public static Screening getInstance(int value){
        switch(value){
            case 1:
                return USER_PROVIDED;
            case 3:
                return NETWORK_PROVIDED;
            default:
                return RESERVED;
        }
    }
    
}
