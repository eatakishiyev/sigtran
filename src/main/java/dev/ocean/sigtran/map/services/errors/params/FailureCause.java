/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum FailureCause {

    /**
     * FailureCauseParam ::= ENUMERATED {
     * limitReachedOnNumberOfConcurrentLocationRequests (0), ... } -- if unknown
     * value is received in FailureCauseParamit shall be ignored
     */
    LIMIT_REACHED_ON_NUMBER_OF_CONCURRENT_LOCATION_REQUESTS(0),
    UNKNOWN(-1);
    private int value;

    private FailureCause(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static FailureCause getInstance(int value) {
        switch (value) {
            case 0:
                return LIMIT_REACHED_ON_NUMBER_OF_CONCURRENT_LOCATION_REQUESTS;
            default:
                return UNKNOWN;
        }
    }
}
