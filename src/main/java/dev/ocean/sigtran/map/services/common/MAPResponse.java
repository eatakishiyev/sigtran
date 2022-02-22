/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.common;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface MAPResponse extends Serializable, MAPMessage {

    public byte[] getResponseData();

    public boolean isResponseCorrupted();
}
