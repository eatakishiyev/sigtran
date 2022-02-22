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
public class UnEquippedUserException extends GlobalTitleTranslationException {

    public UnEquippedUserException() {
    }

    public UnEquippedUserException(String message) {
        super(message);
    }

    @Override
    public ErrorReason getReason() {
        return ErrorReason.UNEQUIPPED_USER;
    }

}
