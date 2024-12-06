/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v1;

import azrc.az.sigtran.cap.OperationCodes;
import azrc.az.sigtran.cap.api.CAPMessage;
import azrc.az.sigtran.cap.callcontrol.general.ReleaseCallArg;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPV1MessageFactory {

    public static CAPMessage createMessage(byte[] data, OperationCodes opCode) throws Exception {
        CAPMessage message = null;
        AsnInputStream ais = new AsnInputStream(data);
        switch (opCode) {
            case INITIAL_DP:
                message = new InitialDpArgImpl();
                message.decode(ais);
                break;
            case CONNECT:
                message = new ConnectArgImpl();
                message.decode(ais);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                message = new RequestReportBCSMEventArgImpl();
                message.decode(ais);
                break;
            case EVENT_REPORT_BCSM:
                message = new EventReportBCSMV1ArgImpl();
                message.decode(ais);
                break;
            case RELEASE_CALL:
                message = new ReleaseCallArg();
                message.decode(ais);
                break;
        }

        return message;
    }
}
