/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.errors;

/**
 *
 * @author eatakishiyev
 */
public class UnknownCapErrorException extends Throwable {

    public UnknownCapErrorException() {
    }

    public UnknownCapErrorException(String message) {
        super(message);
    }

    public UnknownCapErrorException(Throwable ex) {
        super(ex);
    }
}
