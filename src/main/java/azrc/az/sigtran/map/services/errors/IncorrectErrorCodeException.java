/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

/**
 *
 * @author eatakishiyev
 */
public class IncorrectErrorCodeException extends Exception {

    public IncorrectErrorCodeException() {
    }

    public IncorrectErrorCodeException(String message) {
        super(message);
    }
}
