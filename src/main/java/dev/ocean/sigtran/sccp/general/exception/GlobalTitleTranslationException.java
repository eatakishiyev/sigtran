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
public abstract class GlobalTitleTranslationException extends Exception {

    public GlobalTitleTranslationException() {
    }

    public GlobalTitleTranslationException(String msg) {
        super(msg);
    }

    public abstract ErrorReason getReason();
}
