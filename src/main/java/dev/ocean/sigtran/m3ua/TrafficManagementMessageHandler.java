/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.messages.asptm.ASPActive;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPActiveAck;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPInactive;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPInactiveAck;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorCodes;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;
import org.apache.logging.log4j.*;

/**
 *
 * @author eatakishiyev
 */
public class TrafficManagementMessageHandler {

    private final Logger logger = LogManager.getLogger(TrafficManagementMessageHandler.class);
    private final Asp asp;

    public TrafficManagementMessageHandler(Asp asp) {
        this.asp = asp;

    }

    public void handleAspActive(ASPActive message) throws Exception {
        if (asp.getNodeType() == NodeType.IPSP_SERVER) {
            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                aspFsm.doTransition("ASPAC");
                aspFsm.getAs().doTransition("AS_ACTIVE");
                asp.sendAspActiveAck(aspFsm.getAs());
            }
        }
    }

    public void handleAspActiveAck(ASPActiveAck message) throws Exception {

        if (asp.getNodeType() == NodeType.ASP
                || asp.getNodeType() == NodeType.IPSP_CLIENT) {

            if (message.getRoutingContext() == null) {
                logger.info("ASPActiveAck received: " + asp.getName() + " with empty routing context." + " NodeType = " + asp.getNodeType());
                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                    aspFsm.doTransition("ASPAC_ACK");
                    if (asp.getNodeType() == NodeType.IPSP_CLIENT) {
                        aspFsm.getAs().doTransition("AS_ACTIVE");
                    }
                }
            } else {
                Integer[] routingContexts = message.getRoutingContext().getRoutingContext();
                logger.info("ASPActiveAck received: " + asp.getName() + " with not empty routing context." + " NodeType = " + asp.getNodeType());
                for (Integer routingContext : routingContexts) {
                    logger.info("ASPActiveAck received: " + asp.getName() + " looking for AspFSM with RC " + routingContext);
                    AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);
                    if (stateMachine != null) {
                        stateMachine.doTransition("ASPAC_ACK");
                        logger.info("ASPActiveAck received: " + asp.getName() + " AspFSM  found with RC " + routingContext);
                        if (asp.getNodeType() == NodeType.IPSP_CLIENT) {
                            stateMachine.getAs().doTransition("AS_ACTIVE");
                        }
                    } else {
                        asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                        logger.error("ASPActiveAck received: " + asp.getName()
                                + "Message with unexpected RoutingContext. RC "
                                + routingContext);
                    }
                }
            }
        }
    }

    public void handleAspInactive(ASPInactive message) throws Exception {
        throw new Exception("Not implemented yet.");
    }

    public void handleAspInactiveAck(ASPInactiveAck message) throws Exception {
        if (asp.getNodeType() == NodeType.ASP) {

            if (message.getRoutingContext() == null) {
                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                    aspFsm.doTransition("ASPIA_ACK");
                }
            } else {
                Integer[] routingContexts = message.getRoutingContext().getRoutingContext();

                for (Integer routingContext : routingContexts) {
                    AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);
                    if (stateMachine != null) {
                        stateMachine.doTransition("ASPIA_ACK");
                    } else {
                        asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                        logger.error("ASPActiveAck received:" + asp.getName()
                                + "Message with unexpected RoutingContext. RC "
                                + routingContext);
                    }
                }
            }
        }
    }
}
