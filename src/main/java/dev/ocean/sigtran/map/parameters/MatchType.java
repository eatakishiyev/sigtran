/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * MatchType::= ENUMERATED {
 * inhibiting (0),
 * enabling (1)}
 * @author eatakishiyev
 */
public enum MatchType {

    inhibiting(0),
    enabling(1);
    private final int value;

    private MatchType(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static MatchType getInstance(int value) {
        switch (value) {
            case 0:
                return inhibiting;
            case 1:
                return enabling;
            default:
                return null;
        }
    }
}
