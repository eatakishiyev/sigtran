/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum PDPInitiationType {

    MS_INITIATED(0),
    NETWORK_INITIATED(1);

    private int value;

    private PDPInitiationType(int value) {
        this.value = value;
    }

    public static PDPInitiationType getInstance(int value) {
        switch (value) {
            case 0:
                return MS_INITIATED;
            case 1:
                return NETWORK_INITIATED;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
