/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface RequestReportBCSMEventArg extends CAPMessage{

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, ParameterOutOfRangeException;

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException;
}
