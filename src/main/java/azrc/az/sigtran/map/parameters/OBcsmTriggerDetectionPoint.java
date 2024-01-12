/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * O-BcsmTriggerDetectionPoint ::= ENUMERATED {
 * collectedInfo (2),
 * ...,
 * routeSelectFailure (4) }
 * @author eatakishiyev
 */
public enum OBcsmTriggerDetectionPoint {

    COLLECTED_INFO(2),
    ROUTE_SELECT_FAILURE(4);
    private final int value;

    private OBcsmTriggerDetectionPoint(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static OBcsmTriggerDetectionPoint getInstance(int value) {
        switch (value) {
            case 2:
                return COLLECTED_INFO;
            case 4:
                return ROUTE_SELECT_FAILURE;
            default:
                return null;
        }
    }
}
