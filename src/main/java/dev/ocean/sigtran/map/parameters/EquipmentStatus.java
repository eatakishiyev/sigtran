/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * EquipmentStatus ::= ENUMERATED {
 * whiteListed (0),
 * blackListed (1),
 * greyListed (2)}
 *
 * @author eatakishiyev
 */
public enum EquipmentStatus {
    WHITELISTED(0),
    BLACKLISTED(1),
    GREYLISTED(2);

    private final int value;

    private EquipmentStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static EquipmentStatus getInstance(int value) {
        switch (value) {
            case 0:
                return WHITELISTED;
            case 1:
                return BLACKLISTED;
            case 2:
                return GREYLISTED;
            default:
                return null;
        }
    }
}
