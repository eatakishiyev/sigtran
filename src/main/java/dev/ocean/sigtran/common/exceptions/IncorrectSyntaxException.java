/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.common.exceptions;

/**
 *
 * @author eatakishiyev
 */
public class IncorrectSyntaxException extends Exception {

    public IncorrectSyntaxException() {
    }

    public IncorrectSyntaxException(String message) {
        super(message);
    }

    public IncorrectSyntaxException(Throwable throwable) {
        super(throwable);
    }
}
