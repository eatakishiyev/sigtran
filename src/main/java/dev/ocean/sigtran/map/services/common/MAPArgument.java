/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.common;

import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.Serializable;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface MAPArgument extends Serializable,MAPMessage {


    public byte[] getRequestData();

    public boolean isRequestCorrupted();
}
