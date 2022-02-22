/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum RedirectingIndicator {

    NO_REDIRECTION(0),
    CALL_REROUTED(1),
    CALL_REROUTED_REDIRECTION_INFORMATION_PRESENTATION_RESTRICTED(2),
    CALL_DIVERTED(3),
    CALL_DIVERTED_ALL_REDIRECTION_INFORMATION_PRESENTATION_RESTRICTED(4),
    CALL_REROUTED_REDIRECTION_NUMBER_PRESENTATION_RESTRICTED(5),
    CALL_DIVERSION_REDIRECTION_NUMBER_PRESENTATION_RESTRICTED(6),
    SPARE(7);

    private final int value;

    private RedirectingIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RedirectingIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_REDIRECTION;
            case 1:
                return CALL_REROUTED;
            case 2:
                return CALL_REROUTED_REDIRECTION_INFORMATION_PRESENTATION_RESTRICTED;
            case 3:
                return CALL_DIVERTED;
            case 4:
                return CALL_DIVERTED_ALL_REDIRECTION_INFORMATION_PRESENTATION_RESTRICTED;
            case 5:
                return CALL_REROUTED_REDIRECTION_NUMBER_PRESENTATION_RESTRICTED;
            case 6:
                return CALL_DIVERSION_REDIRECTION_NUMBER_PRESENTATION_RESTRICTED;
            default:
                return SPARE;
        }
    }
}
