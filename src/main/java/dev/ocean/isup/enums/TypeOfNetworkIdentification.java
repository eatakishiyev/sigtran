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
public enum TypeOfNetworkIdentification {

    CCITT_ITU_T_STANDARDIZED_IDENTIFICATION(0),
    NATIONAL_NETWORK(2),
    UNKNOWN(-1);

    private final int value;

    private TypeOfNetworkIdentification(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TypeOfNetworkIdentification getInstance(int value) {
        switch (value) {
            case 0:
                return CCITT_ITU_T_STANDARDIZED_IDENTIFICATION;
            case 2:
                return NATIONAL_NETWORK;
            default:
                return UNKNOWN;
        }
    }
}
