/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.messages.mgmt.ErrorCodes;
import azrc.az.sigtran.m3ua.messages.ssnm.*;
import azrc.az.sigtran.m3ua.parameters.AffectedPointCode;
import azrc.az.sigtran.m3ua.parameters.PointCode;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class SignallingNetworkManagementMessageHandler {

    private final M3UAStackImpl stack;
    private final Logger logger = LoggerFactory.getLogger(SignallingNetworkManagementMessageHandler.class);
    private final Asp asp;

    public SignallingNetworkManagementMessageHandler(Asp asp, M3UAStackImpl stack) {
        this.asp = asp;
        this.stack = stack;
    }

    public void handleDUNA(DestinationUnavailable message) throws Exception {
        if (asp.getNodeType() != NodeType.ASP) {
            logger.warn("Unexpected DUNA: " + message);
        }

        if (message.getRoutingContext() == null) {
            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                    /**
                     * For the particular case that an ASP becomes active
                     * for an AS and destinations normally accessible to the
                     * AS are inaccessible, restricted, or congested, the SG
                     * MAY send DUNA, DRST, or SCON messages for the
                     * inaccessible, restricted, or congested destinations
                     * to the ASP newly active for the AS to prevent the ASP
                     * from sending traffic for destinations that it might
                     * not otherwise know that are inaccessible, restricted,
                     * or congested. For the newly activating ASP from which
                     * the SGP has received an ASP Active message, these
                     * DUNA, DRST, and SCON messages MAY be sent before
                     * sending the ASP Active Ack that completes the
                     * activation procedure.
                     */
                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();

                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPPause(pointCode.getPointCode());
                    }
                } else {
                    logger.error("Unexpected DUNA message when ASP is in state" + aspFsm.getCurrentState());
                }
            }
        } else {

            Integer[] routingContexts = message.getRoutingContext().getRoutingContext();
            for (Integer routingContext : routingContexts) {
                AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);

                if (stateMachine == null) {

                    asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                    logger.error("DUNA message with unexpected RoutingContext parameter. RC " + routingContext);

                } else if (stateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {

                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();

                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPPause(pointCode.getPointCode());
                    }
                } else {
                    logger.error("Unexpected DUNA message when ASP is in  state" + stateMachine.getCurrentState().getName());
                }

            }
        }
        //}
//        else {
//            asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, message.getRoutingContext());
//        }
    }

    public void handleDAVA(DestinationAvailable message) throws Exception {
        if (asp.getNodeType() != NodeType.ASP) {
            logger.warn("Unexpected DAVA: " + message);
        }

        if (message.getRoutingContext() == null) {

            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {

                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPResume(pointCode.getPointCode());
                    }
                } else {
                    logger.error("Unexpected DAVA message when ASP is in  state" + aspFsm.getCurrentState().getName());
                }
            }
        } else {
            Integer[] routingContexts = message.getRoutingContext().getRoutingContext();
            for (Integer routingContext : routingContexts) {
                AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);

                if (stateMachine == null) {
                    asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                    logger.error("DAVA message with unexpected RoutingContext parameter. RC " + routingContext);
                } else if (stateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {

                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPResume(pointCode.getPointCode());
                    }

                } else {
                    logger.error("Unexpected DAVA message when ASP is in state" + stateMachine.getCurrentState().getName());
                }
            }
        }

//        } else {
//            asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, message.getRoutingContext());
//            logger.error("Unexpected DAVA");
//        }
    }

    public void handleDRST(DestinationRestricted message) throws Exception {
        if (asp.getNodeType() == NodeType.ASP) {
            logger.warn("Unexpected DRST: " + message);
        }

        if (message.getRoutingContext() == null) {

            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {

                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPPause(pointCode.getPointCode());
                    }
                } else {
                    logger.error("Unexpected DRST message when ASP is in  state" + aspFsm.getCurrentState().getName());
                }
            }
        } else {

            Integer[] routingContexts = message.getRoutingContext().getRoutingContext();
            for (Integer routingContext : routingContexts) {
                AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);
                if (stateMachine == null) {
                    asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                    logger.error("Unexpected DRST message with RoutingContext. RC " + routingContext);
                } else if (stateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {

                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPPause(pointCode.getPointCode());
                    }
                } else {
                    logger.error("Unexpected DRST message when ASP is in  state" + stateMachine.getCurrentState().getName());
                }

            }
        }
//        } else {
//            asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, message.getRoutingContext());
//            logger.error("Drst: Unexpected message");
//        }
    }

    public void handleDUPU(DestinationUserPartUnavailable message) throws Exception {
        if (asp.getNodeType() != NodeType.ASP) {
            logger.error("Unexpected DUPU: " + message);
        }

        if (message.getRoutingContext() == null) {

            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPStatus(pointCode.getPointCode(), message.getUserPartUnavailableCause().getCause(), message.getUserPartUnavailableCause().getUser());
                    }
                } else {
                    logger.error("Unexpected DUPU message when ASP is in state" + aspFsm.getCurrentState().getName());
                }
            }
        } else {
            Integer[] routingContexts = message.getRoutingContext().getRoutingContext();
            for (Integer routingContext : routingContexts) {

                AspStateMachine stateMachine = asp.getAspStateMachine(routingContext);

                if (stateMachine == null) {
                    asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{routingContext}));
                    this.logger.error("Unexpected DUPU message with RoutingContext. RC " + routingContext);

                } else if (stateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                    AffectedPointCode affectedPointCode = message.getAffectedPointCode();
                    PointCode[] pointCodes = affectedPointCode.getAffectedPointCodes();
                    for (PointCode pointCode : pointCodes) {
                        stack.fireMTPStatus(pointCode.getPointCode(),
                                message.getUserPartUnavailableCause().getCause(),
                                message.getUserPartUnavailableCause().getUser());
                    }
                } else {
                    logger.error("Unexpected DUPU message when ASP is in state" + stateMachine.getCurrentState().getName());
                }
            }
        }
//        } else {
//            asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, message.getRoutingContext());
//            logger.error("Dupu: Unexpected message");
//        }
    }

    void handleDAUD(DestinationStateAudit destinationStateAudit) throws IOException {
        asp.sendDAVA(destinationStateAudit.getAffectedPointCode());
    }
}
