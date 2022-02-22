/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 * LegType ::= OCTET STRING (SIZE(1)) leg1 LegType ::= '01'H leg2 LegType ::=
 * '02'H
 *
 * @author eatakishiyev
 */
public enum LegType {

    LEG1(1),
    LEG2(2);
    private int value;

    private LegType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LegType valueOf(int value) {
        return value == 1 ? LEG1 : LEG2;
    }
}
