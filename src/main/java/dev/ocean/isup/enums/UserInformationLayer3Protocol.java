/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum UserInformationLayer3Protocol {
    Q_931(0b00000010),
    X_25(0b00000110),
    ISO_TR_9577(0b00001011),
    UNKNOWN(-1);

    private final int value;

    private UserInformationLayer3Protocol(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static UserInformationLayer3Protocol getInstance(int value) {
        switch (value) {
            case 0b00000010:
                return Q_931;
            case 0b00000110:
                return X_25;
            case 0b00001011:
                return ISO_TR_9577;
            default:
                return UNKNOWN;
        }
    }
}
