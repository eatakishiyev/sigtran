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
public class SccpFailureException extends GlobalTitleTranslationException {

    @Override
    public ErrorReason getReason() {
        return ErrorReason.SCCP_FAILURE;
    }

    public SccpFailureException() {
    }

    public SccpFailureException(String message) {
        super(message);
    }
}
