/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v3;

import azrc.az.sigtran.cap.OperationCodes;
import azrc.az.sigtran.cap.api.CAPMessage;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.ReleaseCallArg;
import azrc.az.sigtran.cap.smscontrol.general.ConnectSMSArg;
import azrc.az.sigtran.cap.smscontrol.general.ReleaseSMSArg;
import azrc.az.sigtran.cap.smscontrol.v3.EventReportSMSArg;
import azrc.az.sigtran.cap.smscontrol.v3.InitialDpSMSArgImpl;
import azrc.az.sigtran.cap.smscontrol.v3.RequestReportSMSEventArg;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPV3MessageFactory {

    public static CAPMessage createMessage(byte[] data, OperationCodes opCode) throws Exception {
        CAPMessage message = null;
        AsnInputStream ais = new AsnInputStream(data);
        switch (opCode) {
            case INITIAL_DP:
                message = new InitialDpArgImpl();
                message.decode(ais);
                break;
            case APPLY_CHARGING:
                message = new ApplyChargingArg();
                message.decode(ais);
                break;
            case APPLY_CHARGING_REPORT:
                message = new ApplyChargingReportArg();
                message.decode(ais);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                message = new RequestReportBCSMEventArgImpl();
                message.decode(ais);
                break;
            case EVENT_REPORT_BCSM:
                message = new EventReportBCSMV3ArgImpl();
                message.decode(ais);
                break;
            case RELEASE_CALL:
                message = new ReleaseCallArg();
                message.decode(ais);
                break;
            case CONNECT:
                message = new ConnectArgImpl();
                message.decode(ais);
                break;
            case INITIAL_DP_SMS:
                message = new InitialDpSMSArgImpl();
                message.decode(ais);
                break;
            case EVENT_REPORT_SMS:
                message = new EventReportSMSArg();
                message.decode(ais);
                break;
            case REQUEST_REPORT_SMS_EVENT:
                message = new RequestReportSMSEventArg();
                message.decode(ais);
                break;
            case RELEASE_SMS:
                message = new ReleaseSMSArg();
                message.decode(ais);
                break;
            case CONNECT_SMS:
                message = new ConnectSMSArg();
                message.decode(ais);
                break;
        }
        return message;
    }
}
