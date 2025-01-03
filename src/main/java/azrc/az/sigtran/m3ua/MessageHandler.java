/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.messages.MessageFactory;
import azrc.az.sigtran.m3ua.messages.aspsm.AspDownAck;
import azrc.az.sigtran.m3ua.messages.aspsm.AspUp;
import azrc.az.sigtran.m3ua.messages.aspsm.AspUpAck;
import azrc.az.sigtran.m3ua.messages.aspsm.HeartBeat;
import azrc.az.sigtran.m3ua.messages.asptm.ASPActive;
import azrc.az.sigtran.m3ua.messages.asptm.ASPActiveAck;
import azrc.az.sigtran.m3ua.messages.asptm.ASPInactiveAck;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorCodes;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorMessage;
import azrc.az.sigtran.m3ua.messages.mgmt.Notify;
import azrc.az.sigtran.m3ua.messages.ssnm.*;
import azrc.az.sigtran.m3ua.messages.transfer.PayloadData;
import azrc.az.sigtran.utils.ByteUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.zip.CRC32;

/**
 *
 * @author eatakishiyev
 */
public class MessageHandler implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final byte[] data;
    private final AspImpl link;
    private final M3UAStackImpl stack;

    public MessageHandler(byte[] data, AspImpl link, M3UAStackImpl stack) {
        this.data = data;
        this.link = link;
        this.stack = stack;
    }

    @Override
    public void run() {
        try {
            Message message = MessageFactory.createMessage(data);
            if (message == null) {
                logger.warn(String.format("Rx:Can not create message from received data. Data = %s", ByteUtils.bytes2Hex(data)));
                return;
            }

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Rx: Message created on base of incoming data %s. Message [%s]",
                        ByteUtils.bytes2Hex(data), message));
            }

            switch (message.getMessageClass()) {
                case ASPSM:
                    switch (message.getMessageType()) {
                        case ASPUP:

                            this.link.getStateMaintenanceMessageHandler().handleAspUp((AspUp) message);

                            break;
                        case ASPUP_ACK:

                            this.link.getStateMaintenanceMessageHandler().handleAspUpAck((AspUpAck) message);
                            break;
                        case ASPDN:

                            break;
                        case ASPDN_ACK:

                            this.link.getStateMaintenanceMessageHandler().handleAspDownAck((AspDownAck) message);
                            break;
                        case BEAT:
                            this.link.getStateMaintenanceMessageHandler().handleBeat((HeartBeat) message);
                            break;
                        case BEAT_ACK:
                            break;
                        default:
                            break;
                    }
                    break;
                case ASPTM:
                    switch (message.getMessageType()) {
                        case ASPAC:

                            this.link.getTrafficManagementMessageHandler().handleAspActive((ASPActive) message);
                            break;
                        case ASPAC_ACK:

                            this.link.getTrafficManagementMessageHandler().handleAspActiveAck((ASPActiveAck) message);
                            break;
                        case ASPIA:

                            break;
                        case ASPIA_ACK:

                            this.link.getTrafficManagementMessageHandler().handleAspInactiveAck((ASPInactiveAck) message);
                            break;
                        default:
                            break;
                    }
                    break;
                case MGMT:
                    switch (message.getMessageType()) {
                        case ERR:

                            this.link.getManagementMessageHandler().handleErr((ErrorMessage) message);
                            break;
                        case NTFY:
                            this.link.getManagementMessageHandler().handleNtfy((Notify) message);
                            break;
                        default:
                            break;
                    }
                    break;
                case SSNM:
                    switch (message.getMessageType()) {
                        case DUNA:
                            this.link.getSignallingNetworkManagementMessageHandler().handleDUNA((DestinationUnavailable) message);
                            break;
                        case DRST:
                            this.link.getSignallingNetworkManagementMessageHandler().handleDRST((DestinationRestricted) message);
                            break;
                        case DAUD:
                            this.link.getSignallingNetworkManagementMessageHandler().handleDAUD((DestinationStateAudit) message);
                            break;
                        case DUPU:
                            this.link.getSignallingNetworkManagementMessageHandler().handleDUPU((DestinationUserPartUnavailable) message);
                            break;
                        case SCON:
                            break;
                        case DAVA:
                            this.link.getSignallingNetworkManagementMessageHandler().handleDAVA((DestinationAvailable) message);
                            break;
                        default:
                            break;
                    }
                    break;
                case TRANSFER_MESSAGE:
                    switch (message.getMessageType()) {
                        case DATA:

                            PayloadData payload = (PayloadData) message;
                            ServiceIdentificator si = payload.getProtocolData().getSi();
                            NetworkIndicator ni = payload.getProtocolData().getNi();

                            MTPTransferMessage m3UATransferMessage = new MTPTransferMessage(payload.getProtocolData().getOpc(),
                                    payload.getProtocolData().getDpc(), payload.getProtocolData().getSls(),
                                    si, payload.getProtocolData().getMp(), ni, payload.getProtocolData().getUserProtocolData());

                            if (payload.getRoutingContext() != null) {
                                //Payload Data contains only one Routing Context parameter. Look at RFC 4666/ 3.3.1
                                As as = stack.getM3uaManagement().getAs(payload.getRoutingContext().getRoutingContext()[0]);
                                if (as != null
                                        && (as.getCurrentState().getName().equals(AsState.AS_ACTIVE.value())
                                        || as.getCurrentState().getName().equals(AsState.AS_PENDIGN.value()))) {
                                    M3UAUser user = stack.getUser(si);
                                    if (user != null) {
                                        user.onMtpTransferIndication(m3UATransferMessage);
                                    } else {
                                        logger.error(("[M3UAMessageHandler]:Received PayloadData with RoutingContext parameter."
                                                + "No user found for received RoutingContext"));
                                    }
                                }
                            } else {
                                int configuredAsCount = link.getRcCount();
                                if (configuredAsCount > 1) {
                                    //Where multiple Routing Keys and Routing
                                    //Contexts are used across a common association, the Routing Context
                                    //MUST be sent to identify the traffic flow, assisting in the
                                    //internal distribution of Data messages.
                                    //The "No Configured AS for ASP" error is sent if a message is received
                                    //from a peer without a Routing Context parameter and it is not known
                                    //by configuration data which Application Servers are referenced.
                                    link.sendError(ErrorCodes.NO_CONFIGURED_AS_FOR_ASP, null, null);
                                    logger.error(("[M3UAMessageHandler]:Received PayloadData without RoutingContext parameter."
                                            + "But there are more than one As configured. Sending error NO_CONFIGURED_AS_FOR_ASP"));
                                } else {
                                    As as = stack.getM3uaManagement().getNullRcAs();
                                    if (as == null) {
                                        logger.error(("[M3UAMessageHandler]:No application servers configured on this node"));
                                    } else if (as.getCurrentState().getName().equals(AsState.AS_ACTIVE.value())
                                            || as.getCurrentState().getName().equals(AsState.AS_PENDIGN.value())) {
                                        M3UAUser user = stack.getUser(si);
                                        if (user != null) {
                                            if (logger.isDebugEnabled()) {
                                                logger.debug(("[M3UAMessageHandler].Passing data to the user. "));
                                            }
                                            user.onMtpTransferIndication(m3UATransferMessage);
                                        } else {
                                            logger.error(("[M3UAMessageHandler]:No user found."));
                                        }
                                    } else {
                                        logger.error(("[M3UAMessageHandler]:"
                                                + " AS State machine is not in AS_ACTIVE/AS_PENDING state to handle incoming message"));
                                    }
                                }
                            }
                            break;
                        default:
                            logger.error("Unknown Payload Type receveide." + ByteUtils.bytes2Hex(data));
                            break;
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            logger.error(String.format("[M3UAMessageHandler]:Error occured "
                    + "while receive data. %s", ByteUtils.bytes2Hex(data)), exception);
            link.sendError(ErrorCodes.PROTOCOL_ERROR, null, null);
        }
    }
}
