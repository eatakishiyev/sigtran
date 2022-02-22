/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface M3UAProvider extends Serializable {

    public void send(MTPTransferMessage mTPTransferMessage) throws Exception;

    public void addUser(M3UAUser user);
    
    public int getMaxSls();
}
