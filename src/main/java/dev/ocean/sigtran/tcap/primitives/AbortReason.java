/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives;

/**
 *
 * @author root
 */
public enum AbortReason {

    UNDEFINED,
    USER_SPECIFIC,
    NULL,
    NO_REASON_GIVEN,
    APPLICATION_CONTEXT_NAME_NOT_SUPPORTED,
    NO_COMMON_DIALOGUE_PORTION,
    DIALOGUE_REFUSED;
}
