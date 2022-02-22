/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.exceptions;

/**
 *
 * @author eatakishiyev
 */
public class UnsupportedMessageTypeException extends Exception {

    private byte[] messageFragment;
    
    public UnsupportedMessageTypeException(){
        
    }

    public UnsupportedMessageTypeException(String message, byte[] messageFragment) {
        super(message);
        this.messageFragment = messageFragment;
    }

    /**
     * @return the messageFragment
     */
    public byte[] getMessageFragment() {
        return messageFragment;
    }
}
