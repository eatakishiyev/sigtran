/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dev.ocean.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public enum RefuseReason {
    REMOTE_NODE_NOT_REACHABLE,
    NO_REASON_GIVEN,
    ABNORMAL_PROCESSING,
    APPLICATION_TIMER_EXPIRED,
    CONGESTION,
    INVALID_REFERENCE,
    MISSING_REFERENCE,
    NOT_ALLOWED_PROCEDURES,
    OVERLAPPING_DIALOGUE,
    POTENTIAL_VERSION_INCOMPABILITY,
    APPLICATION_CONTEXT_NOT_SUPPORTED
}
