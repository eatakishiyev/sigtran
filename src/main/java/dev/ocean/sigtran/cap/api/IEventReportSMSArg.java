/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.cap.callcontrol.general.EventTypeSMS;
import dev.ocean.sigtran.cap.parameters.MiscCallInfo;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface IEventReportSMSArg extends CAPMessage{

    public MiscCallInfo getMiscCallInfo();

    public void setMiscCallInfo(MiscCallInfo miscCallInfo);
    
    public EventTypeSMS getEventTypeSMS();
    
    public void setEventTypeSMS(EventTypeSMS eventTypeSMS);

    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException;

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException;
}
