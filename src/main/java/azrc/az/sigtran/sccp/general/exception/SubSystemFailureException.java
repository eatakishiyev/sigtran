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
public class SubSystemFailureException extends GlobalTitleTranslationException {

    @Override
    public ErrorReason getReason() {
        return ErrorReason.SUBSYSTEM_FAILURE;
    }

    public SubSystemFailureException() {
    }

    public SubSystemFailureException(String message) {
        super(message);
    }
}
