/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface MAPArgument extends Serializable,MAPMessage {


    public byte[] getRequestData();

    public boolean isRequestCorrupted();
}
