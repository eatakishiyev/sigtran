/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum PresentationMethod {

    HIGH_LAYER_PROTOCOL_PROFILE(1);
    private final int value;

    private PresentationMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PresentationMethod getInstance(int value) {
        switch (value) {
            case 1:
                return HIGH_LAYER_PROTOCOL_PROFILE;
            default:
                return null;
        }
    }
}
