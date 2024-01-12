/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import azrc.az.sigtran.cap.parameters.LegId;
import azrc.az.sigtran.cap.parameters.MiscCallInfo;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface EventReportBCSMArg extends CAPMessage {

    public LegId getLegId();

    public void setLegId(LegId legId);

    public MiscCallInfo getMiscCallInfo();

    public void setMiscCallInfo(MiscCallInfo miscCallInfo);

    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);

    public EventTypeBCSM getEventTypeBCSM();

    public EventSpecificInformationBCSM getEventSpecificInformationBCSM();

    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException;

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException;
}
