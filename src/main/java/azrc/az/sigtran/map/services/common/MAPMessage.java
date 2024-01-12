/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface MAPMessage {

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException,
            UnexpectedDataException, IllegalNumberFormatException;

    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException;
}
