/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.exceptions;

/**
 *
 * @author eatakishiyev
 */
public class ListenerAlreadyExistsException extends Exception {

    public ListenerAlreadyExistsException(String message) {
        super(message);
    }

    public ListenerAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ListenerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerAlreadyExistsException() {
    }
}
