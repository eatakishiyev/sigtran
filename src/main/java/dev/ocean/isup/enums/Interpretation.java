/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum Interpretation {

    FIRST_HIGH_LAYER_CHARACTERISTICS(4);
    private final int value;

    private Interpretation(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

    public static Interpretation getInstance(int value) {
        switch (value) {
            case 4:
                return FIRST_HIGH_LAYER_CHARACTERISTICS;
            default:
                return null;
        }
    }
}
