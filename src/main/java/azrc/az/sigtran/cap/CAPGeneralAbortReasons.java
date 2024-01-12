/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public enum CAPGeneralAbortReasons {

    ABNORMAL_DIALOGUE(0),
    VERSION_IMCOMPATIBILITY(1),
    PROVIDER_MALFUNCTION(2),
    SUPPORTING_DIALOGUE_RELEASED(3),
    RESOURCE_LIMITATION(4),
    APPLICATION_CONTEXT_NOT_SUPPORTED(5),
    ABNORMAL_PROCESSING(6),
    NO_REASON_GIVEN(7),
    APPLICATION_TIMER_EXPIRED(8),
    CONGESTION(9),
    INVALID_REFERENCE(10),
    MISSING_REFERENCE(11),
    NO_RESPONSE_FROM_PEER(12),
    NOT_ALLOWED_PROCEDURES(13),
    OVERLAPPING_DIALOGUE(14);

    private final int value;

    private CAPGeneralAbortReasons(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static final CAPGeneralAbortReasons getInstance(int value) {
        switch (value) {
            case 0:
                return ABNORMAL_DIALOGUE;
            case 1:
                return VERSION_IMCOMPATIBILITY;
            case 2:
                return PROVIDER_MALFUNCTION;
            case 3:
                return SUPPORTING_DIALOGUE_RELEASED;
            case 4:
                return RESOURCE_LIMITATION;
            case 5:
                return APPLICATION_CONTEXT_NOT_SUPPORTED;
            case 6:
                return ABNORMAL_PROCESSING;
            case 7:
                return NO_REASON_GIVEN;
            case 8:
                return APPLICATION_TIMER_EXPIRED;
            case 9:
                return CONGESTION;
            case 10:
                return INVALID_REFERENCE;
            case 11:
                return MISSING_REFERENCE;
            case 12:
                return NO_RESPONSE_FROM_PEER;
            case 13:
                return NOT_ALLOWED_PROCEDURES;
            case 14:
                return OVERLAPPING_DIALOGUE;
            default:
                return null;
        }
    }
}
