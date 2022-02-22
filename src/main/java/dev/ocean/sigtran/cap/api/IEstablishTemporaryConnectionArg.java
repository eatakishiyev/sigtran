/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.isup.parameters.GenericDigits;
import dev.ocean.sigtran.cap.parameters.AssistingSSPIPRoutingAddress;
import dev.ocean.isup.parameters.ScfId;
import dev.ocean.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface IEstablishTemporaryConnectionArg extends CAPMessage{

    public AssistingSSPIPRoutingAddress getAssistingSSPIPRoutingAddress();

    public void setAssistingSSPIPRoutingAddress(AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress);

    public GenericDigits getCorrelationId();

    public void setCorrelationId(GenericDigits correlationId);

    public ScfId getScfId();

    public void setScfId(ScfId scfId);

    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);

    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    public void setServiceInteractionIndicatorsTwo(ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo);

    public byte[] getNaInfo();

    public void setNaInfo(byte[] naInfo);

    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException;

    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException;
}
