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
public abstract class GlobalTitleTranslationException extends Exception {

    public GlobalTitleTranslationException() {
    }

    public GlobalTitleTranslationException(String msg) {
        super(msg);
    }

    public abstract ErrorReason getReason();
}
