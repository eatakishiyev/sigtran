/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author eatakishiyev
 */
public interface RequestReportBCSMEventArg extends CAPMessage {

    void encode(AsnOutputStream aos) throws IncorrectSyntaxException, ParameterOutOfRangeException;

    void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException;

    boolean isEventTypeBCSMRequested(EventTypeBCSM eventTypeBCSM);

}
