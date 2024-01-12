/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import azrc.az.sigtran.map.OperationCodes;
import azrc.az.sigtran.map.services.equipment.management.CheckIMEIArg;
import azrc.az.sigtran.map.services.equipment.management.CheckIMEIResponse;
import azrc.az.sigtran.map.services.ericsson.mobility.management.IN.MobilityMngtINTriggeringArg;
import azrc.az.sigtran.map.services.location.cancellation.CancelLocationArg;
import azrc.az.sigtran.map.services.location.cancellation.CancelLocationRes;
import azrc.az.sigtran.map.services.location.info.retrieval.SendRoutingInfoArg;
import azrc.az.sigtran.map.services.location.info.retrieval.SendRoutingInfoRes;
import azrc.az.sigtran.map.services.mobility.ussd.UssdArg;
import azrc.az.sigtran.map.services.mobility.ussd.UssdRes;
import azrc.az.sigtran.map.services.network.location.update.InsertSubscriberDataArg;
import azrc.az.sigtran.map.services.network.location.update.InsertSubscriberDataRes;
import azrc.az.sigtran.map.services.network.location.update.UpdateLocationArg;
import azrc.az.sigtran.map.services.network.location.update.UpdateLocationRes;
import azrc.az.sigtran.map.services.purging.PurgeMSArg;
import azrc.az.sigtran.map.services.purging.PurgeMSRes;
import azrc.az.sigtran.map.services.reporting.NoteMMEventArg;
import azrc.az.sigtran.map.services.reporting.NoteMMEventRes;
import azrc.az.sigtran.tcap.parameters.interfaces.Component;
import azrc.az.sigtran.tcap.primitives.tc.TCInvoke;
import azrc.az.sigtran.tcap.primitives.tc.TCResult;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.enquiry.AnyTimeInterrogationArg;
import azrc.az.sigtran.map.services.enquiry.AnyTimeInterrogationRes;
import azrc.az.sigtran.map.services.enquiry.ProvideRoamingNumberArg;
import azrc.az.sigtran.map.services.enquiry.ProvideRoamingNumberRes;
import azrc.az.sigtran.map.services.enquiry.ProvideSubscriberInfoArg;
import azrc.az.sigtran.map.services.enquiry.ProvideSubscriberInfoRes;
import azrc.az.sigtran.map.services.mobility.sms.AlertServiceCentreArg;
import azrc.az.sigtran.map.services.mobility.sms.InformServiceCentreArg;
import azrc.az.sigtran.map.services.mobility.sms.MAPForwardSMResponse;
import azrc.az.sigtran.map.services.mobility.sms.MOForwardSMArg;
import azrc.az.sigtran.map.services.mobility.sms.MTForwardSMArg;
import azrc.az.sigtran.map.services.mobility.sms.ReportSMDeliveryStatusArg;
import azrc.az.sigtran.map.services.mobility.sms.ReportSMDeliveryStatusRes;
import azrc.az.sigtran.map.services.mobility.sms.SendRoutingInfoForSMArg;
import azrc.az.sigtran.map.services.mobility.sms.SendRoutingInfoForSMRes;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;

/**
 *
 * @author eatakishiyev
 */
public class MAPMessageFactory {

    public static MAPResponse createResponseMessage(int opCode, byte[] data) throws
            IncorrectSyntaxException,
            UnexpectedDataException {
        if (data == null || data.length == 0) {
            return null;
        }

        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        MAPResponse message = null;
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                message = new AnyTimeInterrogationRes();
                message.decode(data);
                break;
            case UPDATE_LOCATION:
                message = new UpdateLocationRes();
                message.decode(data);
                break;
            case NOTE_MM_EVENT:
                message = new NoteMMEventRes();
                message.decode(data);
                break;
            case PURGE_MS:
                message = new PurgeMSRes();
                message.decode(data);
                break;
            case PROVIDE_ROAMING_NUMBER:
                message = new ProvideRoamingNumberRes();
                message.decode(data);
                break;
            case PROVIDE_SUBSCRIBER_INFO:
                message = new ProvideSubscriberInfoRes();
                message.decode(data);
                break;
            case CANCEL_LOCATION:
                message = new CancelLocationRes();
                message.decode(data);
                break;
            case SEND_ROUTING_INFO:
                message = new SendRoutingInfoRes();
                message.decode(data);
                break;
            case FORWARD_SM:
                message = new MAPForwardSMResponse();
                message.decode(data);
                break;
            case REPORT_SM_DELIVERY_STATUS:
                message = new ReportSMDeliveryStatusRes();
                message.decode(data);
                break;
            case SEND_ROUTING_INFO_FOR_SM:
                message = new SendRoutingInfoForSMRes();
                message.decode(data);
                break;
            case PROCESS_UNSTRUCTURED_SS_REQUEST:
            case UNSTRUCTURED_SS_NOTIFY:
            case UNSTRUCTURED_SS_REQUEST:
                message = new UssdRes();
                message.decode(data);
                break;
            case INSERT_SUBSCRIBER_DATA:
                message = new InsertSubscriberDataRes();
                message.decode(data);
                break;
            case CHECK_IMEI:
                message = new CheckIMEIResponse();
                message.decode(data);
                break;
        }
        return message;
    }

    public static MAPArgument createRequestMessage(int opCode, byte[] data) throws
            IncorrectSyntaxException,
            UnexpectedDataException {
        if (data == null || data.length == 0) {
            return null;
        }

        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        MAPArgument message = null;
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                message = new AnyTimeInterrogationArg();
                message.decode(data);
                break;
            case UPDATE_LOCATION:
                message = new UpdateLocationArg(data);
                return message;
            case NOTE_MM_EVENT:
                message = new NoteMMEventArg();
                message.decode(data);
                break;
            case PURGE_MS:
                message = new PurgeMSArg();
                message.decode(data);
                break;
            case PROVIDE_ROAMING_NUMBER:
                message = new ProvideRoamingNumberArg();
                message.decode(data);
                break;
            case PROVIDE_SUBSCRIBER_INFO:
                message = new ProvideSubscriberInfoArg();
                message.decode(data);
                break;
            case MOBILITY_MNGT_IN_TRIGGERING:
                message = new MobilityMngtINTriggeringArg();
                message.decode(data);
                break;
            case CANCEL_LOCATION:
                message = new CancelLocationArg();
                message.decode(data);
                break;
            case SEND_ROUTING_INFO:
                message = new SendRoutingInfoArg();
                message.decode(data);
                break;
            case ALERT_SERVICE_CENTRE:
                message = new AlertServiceCentreArg();
                message.decode(data);
                break;
            case INFORM_SERVICE_CENTRE:
                message = new InformServiceCentreArg();
                message.decode(data);
                break;
            case FORWARD_SM:
                try {
                    message = new MTForwardSMArg();
                    message.decode(data);
                } catch (Throwable th) {
                    try {
                        message = new MOForwardSMArg();
                        message.decode(data);
                    } catch (Throwable t) {
                        return null;
                    }
                }
                break;
            case REPORT_SM_DELIVERY_STATUS:
                message = new ReportSMDeliveryStatusArg();
                message.decode(data);
                break;
            case SEND_ROUTING_INFO_FOR_SM:
                message = new SendRoutingInfoForSMArg();
                message.decode(data);
                break;
            case UNSTRUCTURED_SS_NOTIFY:
            case UNSTRUCTURED_SS_REQUEST:
            case PROCESS_UNSTRUCTURED_SS_REQUEST:
                message = new UssdArg();
                message.decode(data);
                break;
            case INSERT_SUBSCRIBER_DATA:
                message = new InsertSubscriberDataArg();
                message.decode(data);
                break;
            case CHECK_IMEI:
                message = new CheckIMEIArg();
                message.decode(data);
                break;
        }
        return message;
    }

    public static MAPResponse createResponseMessage(TCResult result) throws Exception {

        if (result.getParameter() == null || result.getOperationCode() == null) {
            return null;
        }

        return createResponseMessage(result.getOperationCode().getLocalValue(), result.getParameter().getData());
    }

    public static MAPMessage createMessage(Component component) throws
            IncorrectSyntaxException,
            UnexpectedDataException {
        OperationCodeImpl opCode = component.getOpCode();
        if (opCode != null && component.getParameter() != null) {
            switch (component.getComponentType()) {
                case INVOKE:
                    return createRequestMessage(opCode.getLocalValue(), component.getParameter().getData());
                case RETURN_RESULT_LAST:
                    return createResponseMessage(opCode.getLocalValue(), component.getParameter().getData());
            }
        }
        return null;
    }

    public static MAPArgument createRequestMessage(TCInvoke invoke) throws
            IncorrectSyntaxException,
            UnexpectedDataException {

        return createRequestMessage(invoke.getOperationCode(), invoke.getParameter().getData());
    }

}
