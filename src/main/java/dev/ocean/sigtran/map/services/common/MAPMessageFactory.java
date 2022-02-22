/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.common;

import dev.ocean.sigtran.map.services.common.MAPMessage;
import dev.ocean.sigtran.map.services.common.MAPArgument;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.OperationCodes;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.AnyTimeInterrogationArg;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.AnyTimeInterrogationRes;
import dev.ocean.sigtran.map.service.mm.event.reporting.NoteMMEventArg;
import dev.ocean.sigtran.map.service.mm.event.reporting.NoteMMEventRes;
import dev.ocean.sigtran.map.service.purging.PurgeMSArg;
import dev.ocean.sigtran.map.service.purging.PurgeMSRes;
import dev.ocean.sigtran.map.service.roaming.number.enquiry.ProvideRoamingNumberArg;
import dev.ocean.sigtran.map.service.roaming.number.enquiry.ProvideRoamingNumberRes;
import dev.ocean.sigtran.map.service.subscriber.information.enquiry.ProvideSubscriberInfoArg;
import dev.ocean.sigtran.map.service.subscriber.information.enquiry.ProvideSubscriberInfoRes;
import dev.ocean.sigtran.map.services.common.MAPResponse;
import dev.ocean.sigtran.map.services.equipment.management.CheckIMEIArg;
import dev.ocean.sigtran.map.services.equipment.management.CheckIMEIResponse;
import dev.ocean.sigtran.map.services.ericsson.mobility.management.IN.MobilityMngtINTriggeringArg;
import dev.ocean.sigtran.map.services.location.cancellation.CancelLocationArg;
import dev.ocean.sigtran.map.services.location.cancellation.CancelLocationRes;
import dev.ocean.sigtran.map.services.location.info.retrieval.SendRoutingInfoArg;
import dev.ocean.sigtran.map.services.location.info.retrieval.SendRoutingInfoRes;
import dev.ocean.sigtran.map.services.mobility.sms.AlertServiceCentreArg;
import dev.ocean.sigtran.map.services.mobility.sms.InformServiceCentreArg;
import dev.ocean.sigtran.map.services.mobility.sms.MAPForwardSMResponse;
import dev.ocean.sigtran.map.services.mobility.sms.MOForwardSMArg;
import dev.ocean.sigtran.map.services.mobility.sms.MTForwardSMArg;
import dev.ocean.sigtran.map.services.mobility.sms.ReportSMDeliveryStatusArg;
import dev.ocean.sigtran.map.services.mobility.sms.ReportSMDeliveryStatusRes;
import dev.ocean.sigtran.map.services.mobility.sms.SendRoutingInfoForSMArg;
import dev.ocean.sigtran.map.services.mobility.sms.SendRoutingInfoForSMRes;
import dev.ocean.sigtran.map.services.mobility.ussd.UssdArg;
import dev.ocean.sigtran.map.services.mobility.ussd.UssdRes;
import dev.ocean.sigtran.map.services.network.location.update.InsertSubscriberDataArg;
import dev.ocean.sigtran.map.services.network.location.update.InsertSubscriberDataRes;
import dev.ocean.sigtran.map.services.network.location.update.UpdateLocationArg;
import dev.ocean.sigtran.map.services.network.location.update.UpdateLocationRes;
import dev.ocean.sigtran.tcap.parameters.OperationCodeImpl;
import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;

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
