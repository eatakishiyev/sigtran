/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.common.exceptions;

/**
 *
 * @author eatakishiyev
 */
public class UnexpectedDataException extends Exception {

    public UnexpectedDataException() {
    }

    public UnexpectedDataException(String message) {
        super(message);
    }

    public UnexpectedDataException(Throwable throwable) {
        super(throwable);
    }
}
