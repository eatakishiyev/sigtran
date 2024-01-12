/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.common.exceptions;

/**
 *
 * @author eatakishiyev
 */
public class IllegalNumberFormatException extends Exception {

    public IllegalNumberFormatException(Throwable ex) {
        super(ex);
    }

    public IllegalNumberFormatException(String message) {
        super(message);
    }
}
