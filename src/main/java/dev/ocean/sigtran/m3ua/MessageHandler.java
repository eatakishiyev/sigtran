/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.messages.MessageFactory;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspDownAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUp;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUpAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.HeartBeat;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPActive;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPActiveAck;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPInactiveAck;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorCodes;
import dev.ocean.sigtran.m3ua.messages.mgmt.Notify;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationAvailable;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationRestricted;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationStateAudit;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationUnavailable;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationUserPartUnavailable;
import dev.ocean.sigtran.m3ua.messages.transfer.PayloadData;
import dev.ocean.sigtran.utils.ByteUtils;
import java.util.zip.CRC32;
import org.apache.logging.log4j.*;

/**
 *
 * @author eatakishiyev
 */
public class MessageHandler implements Runnable {

    private final static CRC32 crc32 = new CRC32();
    private final ThreadLocal<Long> logTrackId = new ThreadLocal() {
        @Override
        protected Long initialValue() {
            return new Long(0);
        }

    };
    private final Logger logger = LogManager.getLogger(MessageHandler.class);
    private final byte[] data;
    private final AspImpl link;
    private final M3UAStackImpl stack;

    public MessageHandler(byte[] data, AspImpl link, M3UAStackImpl stack) {
        crc32.update(data);
        StringBuilder sb = new StringBuilder();
        sb.append(crc32.getValue()).append("-").append(System.nanoTime());
        ThreadContext.put("correlation_id", sb.toString());
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
                            link.aspUpCounter.increment();
                            this.link.getStateMaintenanceMessageHandler().handleAspUp((AspUp) message);

                            break;
                        case ASPUP_ACK:
                            link.aspUpAckCounter.increment();
                            this.link.getStateMaintenanceMessageHandler().handleAspUpAck((AspUpAck) message);
                            break;
                        case ASPDN:
                            link.aspDownCounter.increment();
                            break;
                        case ASPDN_ACK:
                            link.aspDownAckCounter.increment();
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
                            link.aspActiveCounter.increment();
                            this.link.getTrafficManagementMessageHandler().handleAspActive((ASPActive) message);
                            break;
                        case ASPAC_ACK:
                            link.aspActiveAckCounter.increment();
                            this.link.getTrafficManagementMessageHandler().handleAspActiveAck((ASPActiveAck) message);
                            break;
                        case ASPIA:
                            link.aspInactiveCounter.increment();
                            break;
                        case ASPIA_ACK:
                            link.aspInactiveAckCounter.increment();
                            this.link.getTrafficManagementMessageHandler().handleAspInactiveAck((ASPInactiveAck) message);
                            break;
                        default:
                            break;
                    }
                    break;
                case MGMT:
                    switch (message.getMessageType()) {
                        case ERR:
                            link.receivedErrorCounter.increment();
                            this.link.getManagementMessageHandler().handleErr((dev.ocean.sigtran.m3ua.messages.mgmt.ErrorMessage) message);
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
                            link.receivedPayloadCounter.increment();
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
                                        logger.error(String.format("%s:[M3UAMessageHandler]:Received PayloadData with RoutingContext parameter."
                                                + "No user found for received RoutingContext", logTrackId));
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
                                    logger.error(String.format("%s:[M3UAMessageHandler]:Received PayloadData without RoutingContext parameter."
                                            + "But there are more than one As configured. Sending error NO_CONFIGURED_AS_FOR_ASP", logTrackId));
                                } else {
                                    As as = stack.getM3uaManagement().getNullRcAs();
                                    if (as == null) {
                                        logger.error(String.format("%s:[M3UAMessageHandler]:No application servers configured on this node", logTrackId));
                                    } else if (as.getCurrentState().getName().equals(AsState.AS_ACTIVE.value())
                                            || as.getCurrentState().getName().equals(AsState.AS_PENDIGN.value())) {
                                        M3UAUser user = stack.getUser(si);
                                        if (user != null) {
                                            if (logger.isDebugEnabled()) {
                                                logger.debug(String.format("%s:[M3UAMessageHandler].Passing data to the user. ", logTrackId));
                                            }
                                            user.onMtpTransferIndication(m3UATransferMessage);
                                        } else {
                                            logger.error(String.format("%s:[M3UAMessageHandler]:No user found.", logTrackId));
                                        }
                                    } else {
                                        logger.error(String.format("%s:[M3UAMessageHandler]:"
                                                + " AS State machine is not in AS_ACTIVE/AS_PENDING state to handle incoming message", logTrackId));
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
            logger.error(String.format("%s:[M3UAMessageHandler]:Error occured "
                    + "while receive data. %s", logTrackId, ByteUtils.bytes2Hex(data)), exception);
            link.sendError(ErrorCodes.PROTOCOL_ERROR, null, null);
        } finally {
            ThreadContext.clearAll();
        }
    }
}
