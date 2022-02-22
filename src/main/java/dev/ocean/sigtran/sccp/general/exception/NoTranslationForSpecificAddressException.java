/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general.exception;

import dev.ocean.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public class NoTranslationForSpecificAddressException extends Exception {

    public final ErrorReason REASON = ErrorReason.NO_TRANSLATION_FOR_SPECIFIC_ADDRESS;

    public NoTranslationForSpecificAddressException() {
    }

    public NoTranslationForSpecificAddressException(String message) {
        super(message);
    }
}
