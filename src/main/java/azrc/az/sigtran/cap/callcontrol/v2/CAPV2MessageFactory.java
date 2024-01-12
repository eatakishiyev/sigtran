/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import azrc.az.sigtran.cap.OperationCodes;
import azrc.az.sigtran.cap.api.CAPMessage;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.AssistRequestInstructionArg;
import azrc.az.sigtran.cap.callcontrol.general.ReleaseCallArg;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPV2MessageFactory {

    public static CAPMessage createMessge(byte[] data, OperationCodes opCode) throws Exception {
        CAPMessage message = null;
        AsnInputStream ais = new AsnInputStream(data);
        switch (opCode) {
            case APPLY_CHARGING:
                message = new ApplyChargingArg();
                message.decode(ais);
                break;
            case APPLY_CHARGING_REPORT:
                message = new ApplyChargingReportArg();
                message.decode(ais);
                break;
            case ASSIST_REQUEST_INSTRUCTIONS:
                message = new AssistRequestInstructionArg();
                message.decode(ais);
                break;
            case CONNECT:
                message = new ConnectArgImpl();
                message.decode(ais);
                break;
            case ESTABLISH_TEMPORARY_CONNECTION:
                message = new EstablishTemporaryConnectionArg();
                message.decode(ais);
                break;
            case EVENT_REPORT_BCSM:
                message = new EventReportBCSMArgImpl();
                message.decode(ais);
                break;
            case INITIAL_DP:
                message = new InitialDpArgImpl();
                message.decode(ais);
                break;
            case PLAY_ANNOUNCEMENT:
                message = new PlayAnnouncementArg();
                message.decode(ais);
                break;
            case RELEASE_CALL:
                message = new ReleaseCallArg();
                message.decode(ais);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                message = new RequestReportBCSMEventArgImpl();
                message.decode(ais);
                break;

        }
        return message;
    }
}
