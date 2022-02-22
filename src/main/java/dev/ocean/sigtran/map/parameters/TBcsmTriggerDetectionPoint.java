/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum TBcsmTriggerDetectionPoint {

    termAttempAuthorized(12),
    tBusy(13),
    tNoAnswer(14);
    private int value;

    private TBcsmTriggerDetectionPoint(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static TBcsmTriggerDetectionPoint getInstance(int value) {
        switch (value) {
            case 12:
                return termAttempAuthorized;
            case 13:
                return tBusy;
            case 14:
                return tNoAnswer;
            default:
                return null;
        }
    }
}
