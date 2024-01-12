/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum AlertingPattern {

    ALERTING_LEVEL_0(0x00000000B),
    ALERTING_LEVEL_1(0x00000001B),
    ALERTING_LEVEL_2(0x00000010B),
    //    
    ALERTING_CATEGORY_1(0x00000100B),
    ALERTING_CATEGORY_2(0x00000101B),
    ALERTING_CATEGORY_3(0x00000110B),
    ALERTING_CATEGORY_4(0x00000111B),
    ALERTING_CATEGORY_5(0x00001000B),
    UNKNOWN(0x01111111B);

    private final int value;

    private AlertingPattern(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static AlertingPattern getInstance(int value) {
        switch (value) {
            case 0x00000000B:
                return ALERTING_LEVEL_0;
            case 0x00000001B:
                return ALERTING_LEVEL_1;
            case 0x00000010B:
                return ALERTING_LEVEL_2;
            case 0x00000100B:
                return ALERTING_CATEGORY_1;
            case 0x00000101B:
                return ALERTING_CATEGORY_2;
            case 0x00000110B:
                return ALERTING_CATEGORY_3;
            case 0x00000111B:
                return ALERTING_CATEGORY_4;
            case 0x00001000B:
                return ALERTING_CATEGORY_5;
            default:
                return UNKNOWN;
        }
    }

}
