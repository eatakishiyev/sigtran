/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general.exception;

import azrc.az.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public class NoTranslationForSuchNatureException extends Exception {

    public static final ErrorReason REASON = ErrorReason.NO_TRANSLATION_FOR_ADDRESS_SUCH_NATURE;

    public NoTranslationForSuchNatureException() {
        super();
    }

    public NoTranslationForSuchNatureException(String message) {
        super(message);
    }

}
