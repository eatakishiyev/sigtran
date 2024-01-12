/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import java.io.IOException;

import azrc.az.sigtran.cap.parameters.AnswerSpecificInfo;
import azrc.az.sigtran.cap.parameters.CallAcceptedSpecificInfo;
import azrc.az.sigtran.cap.parameters.CalledPartyBusySpecificInfo;
import azrc.az.sigtran.cap.parameters.ChangeOfPositionSpecificInfo;
import azrc.az.sigtran.cap.parameters.DisconnectSpecificInfo;
import azrc.az.sigtran.cap.parameters.DpSpecificInfoAlt;
import azrc.az.sigtran.cap.parameters.MidCallSpecificInfo;
import azrc.az.sigtran.cap.parameters.OAbandonSpecificInfo;
import azrc.az.sigtran.cap.parameters.ONoAnswerSpecificInfo;
import azrc.az.sigtran.cap.parameters.OTermSeizedSpecificInfo;
import azrc.az.sigtran.cap.parameters.RouteSelectFailureSpecificInfo;
import azrc.az.sigtran.cap.parameters.TBusySpecificInfo;
import azrc.az.sigtran.cap.parameters.TNoAnswerSpecificInfo;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface EventSpecificInformationBCSM {

    void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException;

    void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException;

    /**
     * @return the callAcceptedSpecificInfo
     */
    CallAcceptedSpecificInfo getCallAcceptedSpecificInfo();

    /**
     * @return the dpSpecificInfoAlt
     */
    DpSpecificInfoAlt getDpSpecificInfoAlt();

    /**
     * @return the oAbandonSpecificInfo
     */
    OAbandonSpecificInfo getOAbandonSpecificInfo();

    /**
     * @return the oAnswerSpecificInfo
     */
    AnswerSpecificInfo getOAnswerSpecificInfo();

    /**
     * @return the oCalledPartyBusySpecificInfo
     */
    CalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo();

    /**
     * @return the oChangeOfPositionSpecificInfo
     */
    ChangeOfPositionSpecificInfo getOChangeOfPositionSpecificInfo();

    /**
     * @return the oDisconnectSpecificInfo
     */
    DisconnectSpecificInfo getODisconnectSpecificInfo();

    /**
     * @return the omidCallSpecificInfo
     */
    MidCallSpecificInfo getOMidCallSpecificInfo();

    /**
     * @return the oNoAnswerSpecificInfo
     */
    ONoAnswerSpecificInfo getONoAnswerSpecificInfo();

    /**
     * @return the oTermSeizedSpecificInfo
     */
    OTermSeizedSpecificInfo getOTermSeizedSpecificInfo();

    /**
     * @return the routeSelectFailureSpecificInfo
     */
    RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo();

    /**
     * @return the tAnswerSpecificInfo
     */
    AnswerSpecificInfo getTAnswerSpecificInfo();

    /**
     * @return the tBusySpecificInfo
     */
    TBusySpecificInfo getTBusySpecificInfo();

    /**
     * @return the tChangeOfPositionSpecificInfo
     */
    ChangeOfPositionSpecificInfo getTChangeOfPositionSpecificInfo();

    /**
     * @return the tDisconnectSpecificInfo
     */
    DisconnectSpecificInfo getTDisconnectSpecificInfo();

    /**
     * @return the tMidCallSpecificInfo
     */
    MidCallSpecificInfo getTMidCallSpecificInfo();

    /**
     * @return the tNoAnswerSpecificInfo
     */
    TNoAnswerSpecificInfo getTNoAnswerSpecificInfo();
    
}
