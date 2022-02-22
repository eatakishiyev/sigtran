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
public enum UserInformationLayer2Protocol {
    Q_921_I_441(0b00000010),
    X_25(0b00000110),
    ISO_8802_2(0b00001100),
    UNKNOWN(-1);

    private final int value;

    private UserInformationLayer2Protocol(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static UserInformationLayer2Protocol getInstance(int value) {
        switch (value) {
            case 0b00000010:
                return Q_921_I_441;
            case 0b00000110:
                return X_25;
            case 0b00001100:
                return ISO_8802_2;
            default:
                return UNKNOWN;
        }
    }
}
