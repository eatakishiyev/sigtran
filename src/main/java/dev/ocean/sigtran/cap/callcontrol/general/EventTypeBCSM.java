/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.general;

/**
 *
 * @author eatakishiyev
 */
public class EventTypeBCSM {

    /**
     * COLLECT_INFO(2),
     * ANALYZED_INFORMATION(3),
     * ROUTE_SELECT_FAILURE(4),
     * O_BUSY(5),
     * O_NO_ANSWER(6),
     * O_ANSWER(7),
     * O_MID_CALL(8),
     * O_DISCONNECT(9),
     * O_ABANDON(10),
     * TERM_ATTEMPT_AUTHORIZED(12),
     * T_BUSY(13),
     * T_NO_ANSWER(14),
     * T_ANSWER(15),
     * T_MID_CALL(16),
     * T_DISCONNECT(17),
     * T_ABANDON(18),
     * O_TERM_SEIZED(19),
     * CALL_ACCEPTED(27),
     * O_CHANGE_OF_POSITION(50),
     * T_CHANGE_OF_POSITION(51),
     * O_SERVICE_CHANGE(52),
     * T_SERVICE_CHANGE(53);
     */
    public static final int COLLECT_INFO_DP = 2;
    public static final int ANALYZED_INFORMATION_DP = 3;
    public static final int ROUTE_SELECT_FAILURE_DP = 4;
    public static final int O_CALLED_PARTY_BUSY_DP = 5;
    public static final int O_NO_ANSWER_DP = 6;
    public static final int O_ANSWER_DP = 7;
    public static final int O_MID_CAL_DP = 8;
    public static final int O_DISCONNECT_DP = 9;
    public static final int O_ABANDON_DP = 10;
    public static final int TERM_ATTEMPT_AUTHORIZED_DP = 12;
    public static final int T_BUSY_DP = 13;
    public static final int T_NO_ANSWER_DP = 14;
    public static final int T_ANSWER_DP = 15;
    public static final int T_MD_CAL_DP = 16;
    public static final int T_DISCONNECT_DP = 17;
    public static final int T_ABANDON_DP = 18;
    public static final int O_TERM_SEIZED_DP = 19;
    public static final int CALL_ACCEPTED_DP = 27;
    public static final int O_CHANGE_OF_POSITION_DP = 50;
    public static final int T_CHANGE_OF_POSITION_DP = 51;
    public static final int O_SERVICE_CHANGE_DP = 52;
    public static final int T_SERVICE_CHANGE_DP = 53;

    public static final EventTypeBCSM COLLECT_INFO = new EventTypeBCSM(COLLECT_INFO_DP);
    public static final EventTypeBCSM ANALYZED_INFORMATION = new EventTypeBCSM(ANALYZED_INFORMATION_DP);
    public static final EventTypeBCSM ROUTE_SELECT_FAILURE = new EventTypeBCSM(ROUTE_SELECT_FAILURE_DP);
    public static final EventTypeBCSM O_CALLED_PARTY_BUSY = new EventTypeBCSM(O_CALLED_PARTY_BUSY_DP);
    public static final EventTypeBCSM O_NO_ANSWER = new EventTypeBCSM(O_NO_ANSWER_DP);
    public static final EventTypeBCSM O_ANSWER = new EventTypeBCSM(O_ANSWER_DP);
    public static final EventTypeBCSM O_MID_CALL = new EventTypeBCSM(O_MID_CAL_DP);
    public static final EventTypeBCSM O_DISCONNECT = new EventTypeBCSM(O_DISCONNECT_DP);
    public static final EventTypeBCSM O_ABANDON = new EventTypeBCSM(O_ABANDON_DP);
    public static final EventTypeBCSM TERM_ATTEMPT_AUTHORIZED = new EventTypeBCSM(TERM_ATTEMPT_AUTHORIZED_DP);
    public static final EventTypeBCSM T_BUSY = new EventTypeBCSM(T_BUSY_DP);
    public static final EventTypeBCSM T_NO_ANSWER = new EventTypeBCSM(T_NO_ANSWER_DP);
    public static final EventTypeBCSM T_ANSWER = new EventTypeBCSM(T_ANSWER_DP);
    public static final EventTypeBCSM T_MID_CALL = new EventTypeBCSM(T_MD_CAL_DP);
    public static final EventTypeBCSM T_DISCONNECT = new EventTypeBCSM(T_DISCONNECT_DP);
    public static final EventTypeBCSM T_ABANDON = new EventTypeBCSM(T_ABANDON_DP);
    public static final EventTypeBCSM O_TERM_SEIZED = new EventTypeBCSM(O_TERM_SEIZED_DP);
    public static final EventTypeBCSM CALL_ACCEPTED = new EventTypeBCSM(CALL_ACCEPTED_DP);
    public static final EventTypeBCSM O_CHANGE_OF_POSITION = new EventTypeBCSM(O_CHANGE_OF_POSITION_DP);
    public static final EventTypeBCSM T_CHANGE_OF_POSITION = new EventTypeBCSM(T_CHANGE_OF_POSITION_DP);
    public static final EventTypeBCSM O_SERVICE_CHANGE = new EventTypeBCSM(O_SERVICE_CHANGE_DP);
    public static final EventTypeBCSM T_SERVICE_CHANGE = new EventTypeBCSM(T_SERVICE_CHANGE_DP);

    private final int eventTypeBCSM;

    private EventTypeBCSM(int eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    public static EventTypeBCSM fromInt(int value) {
        switch (value) {
            case COLLECT_INFO_DP:
                return COLLECT_INFO;
            case ANALYZED_INFORMATION_DP:
                return ANALYZED_INFORMATION;
            case O_ABANDON_DP:
                return O_ABANDON;
            case O_ANSWER_DP:
                return O_ANSWER;
            case O_MID_CAL_DP:
                return O_MID_CALL;
            case O_CALLED_PARTY_BUSY_DP:
                return O_CALLED_PARTY_BUSY;
            case O_DISCONNECT_DP:
                return O_DISCONNECT;
            case O_NO_ANSWER_DP:
                return O_NO_ANSWER;
            case ROUTE_SELECT_FAILURE_DP:
                return ROUTE_SELECT_FAILURE;
            case TERM_ATTEMPT_AUTHORIZED_DP:
                return TERM_ATTEMPT_AUTHORIZED;
            case T_ABANDON_DP:
                return T_ABANDON;
            case T_ANSWER_DP:
                return T_ANSWER;
            case T_MD_CAL_DP:
                return T_MID_CALL;
            case T_BUSY_DP:
                return T_BUSY;
            case T_DISCONNECT_DP:
                return T_DISCONNECT;
            case T_NO_ANSWER_DP:
                return T_NO_ANSWER;
            case O_TERM_SEIZED_DP:
                return O_TERM_SEIZED;
            case CALL_ACCEPTED_DP:
                return CALL_ACCEPTED;
            case O_CHANGE_OF_POSITION_DP:
                return O_CHANGE_OF_POSITION;
            case T_CHANGE_OF_POSITION_DP:
                return T_CHANGE_OF_POSITION;
            case O_SERVICE_CHANGE_DP:
                return O_SERVICE_CHANGE;
            case T_SERVICE_CHANGE_DP:
                return T_SERVICE_CHANGE;
            default:
                throw new IllegalArgumentException("Argument is not one of the integer EventTypeBCSM DP representation. " + value);
        }
    }

    public int toInt() {
        return this.eventTypeBCSM;
    }

    public boolean isCollectInfo() {
        return eventTypeBCSM == COLLECT_INFO_DP;
    }

    public boolean isRouteSelectFailure() {
        return eventTypeBCSM == ROUTE_SELECT_FAILURE_DP;
    }

    public boolean isOCalledPartyBusy() {
        return eventTypeBCSM == O_CALLED_PARTY_BUSY_DP;
    }

    public boolean isONoAnswer() {
        return eventTypeBCSM == O_NO_ANSWER_DP;
    }

    public boolean isOAnswer() {
        return eventTypeBCSM == O_ANSWER_DP;
    }

    public boolean isODisconnect() {
        return eventTypeBCSM == O_DISCONNECT_DP;
    }

    public boolean isOAbandon() {
        return eventTypeBCSM == O_ABANDON_DP;
    }

    public boolean isTermAttemptAuthorized() {
        return eventTypeBCSM == TERM_ATTEMPT_AUTHORIZED_DP;
    }

    public boolean isTBusy() {
        return eventTypeBCSM == T_BUSY_DP;
    }

    public boolean isTNoAnswer() {
        return eventTypeBCSM == T_NO_ANSWER_DP;
    }

    public boolean isTAnswer() {
        return eventTypeBCSM == T_ANSWER_DP;
    }

    public boolean isOMidCall() {
        return eventTypeBCSM == O_MID_CAL_DP;
    }

    public boolean isTMidCall() {
        return eventTypeBCSM == T_MD_CAL_DP;
    }

    public boolean isAnalyzedInformation() {
        return eventTypeBCSM == ANALYZED_INFORMATION_DP;
    }

    public boolean isCallAccepted() {
        return eventTypeBCSM == CALL_ACCEPTED_DP;
    }

    public boolean isOChangeOfPosition() {
        return eventTypeBCSM == O_CHANGE_OF_POSITION_DP;
    }

    public boolean isTChangeOfPosition() {
        return eventTypeBCSM == T_CHANGE_OF_POSITION_DP;
    }

    public boolean isOServiceChange() {
        return eventTypeBCSM == O_SERVICE_CHANGE_DP;
    }

    public boolean isTServiceChange() {
        return eventTypeBCSM == T_SERVICE_CHANGE_DP;
    }
    
    public boolean isTDisconnect(){
        return eventTypeBCSM == T_DISCONNECT_DP;
    }
    
    public boolean isTAbandon(){
        return eventTypeBCSM == T_ABANDON_DP;
    }
    
    public boolean isOTermSeized(){
        return eventTypeBCSM == O_TERM_SEIZED_DP;
    }

}
