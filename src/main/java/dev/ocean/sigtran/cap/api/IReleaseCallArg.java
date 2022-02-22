/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface IReleaseCallArg extends CAPMessage{

    public Cause getCause();

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException;

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException;
}
