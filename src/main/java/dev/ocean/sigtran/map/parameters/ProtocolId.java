/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * ProtocolId ::= ENUMERATED {
 * gsm-0408 (1),
 * gsm-0806 (2),
 * gsm-BSSMAP (3),
 * -- Value 3 is reserved and must not be used
 * ets-300102-1 (4)}
 * @author eatakishiyev
 */
public enum ProtocolId {

    GSM_0408(1),
    GSM_0806(2),
    GSM_BSSMAP(3),
    ETS_300102(4),
    UNKNOWN(-1);

    private final int value;

    private ProtocolId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ProtocolId getInstance(int value) {
        switch (value) {
            case 1:
                return GSM_0408;
            case 2:
                return GSM_0806;
            case 3:
                return GSM_BSSMAP;
            case 4:
                return ETS_300102;
            default:
                return UNKNOWN;
        }
    }
}
