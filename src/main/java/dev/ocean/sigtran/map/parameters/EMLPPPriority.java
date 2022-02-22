/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * EMLPP-Priority ::= INTEGER (0..15)
 * -- The mapping from the values A,B,0,1,2,3,4 to the integer-value is
 * -- specified as follows where A is the highest and 4 is the lowest
 * -- priority level
 * -- the integer values 7-15 are spare and shall be mapped to value 4
 *
 * priorityLevelA EMLPP-Priority ::= 6
 * priorityLevelB EMLPP-Priority ::= 5
 * priorityLevel0 EMLPP-Priority ::= 0
 * priorityLevel1 EMLPP-Priority ::= 1
 * priorityLevel2 EMLPP-Priority ::= 2
 * priorityLevel3 EMLPP-Priority ::= 3
 * priorityLevel4 EMLPP-Priority ::= 4
 * @author eatakishiyev
 */
public enum EMLPPPriority {

    PRIORITY_LEVEL_0(0),
    PRIORITY_LEVEL_1(1),
    PRIORITY_LEVEL_2(2),
    PRIORITY_LEVEL_3(3),
    PRIORITY_LEVEL_4(4),
    PRIORITY_LEVEL_B(5),
    PRIORITY_LEVEL_A(6);
    private final int value;

    private EMLPPPriority(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static EMLPPPriority getInstance(int value) {
        switch (value) {
            case 0:
                return PRIORITY_LEVEL_0;
            case 1:
                return PRIORITY_LEVEL_1;
            case 2:
                return PRIORITY_LEVEL_2;
            case 3:
                return PRIORITY_LEVEL_3;
            case 4:
                return PRIORITY_LEVEL_4;
            case 5:
                return PRIORITY_LEVEL_B;
            case 6:
                return PRIORITY_LEVEL_A;
            default:
                return null;
        }
    }
}
