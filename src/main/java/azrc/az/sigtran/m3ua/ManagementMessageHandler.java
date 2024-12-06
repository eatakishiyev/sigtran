/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.messages.mgmt.ErrorCodes;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorMessage;
import azrc.az.sigtran.m3ua.messages.mgmt.Notify;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;
import azrc.az.sigtran.m3ua.parameters.StatusInformation;
import azrc.az.sigtran.m3ua.parameters.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class ManagementMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementMessageHandler.class);
    private final Asp asp;

    public ManagementMessageHandler(Asp asp) {
        this.asp = asp;
    }

    public void handleErr(ErrorMessage error) {
        LOGGER.warn(String.format("Err: Message: %s", error.toString()));
    }

    public void handleNtfy(Notify message) throws IOException, Exception {
        if (asp.getNodeType() == NodeType.ASP) {
            RoutingContext routingContext = message.getRoutingContext();

            StatusInformation statusInformation = message.getStatus().getStatusInformation();
            StatusType statusType = message.getStatus().getStatusType();

            switch (statusType) {
                case APPLICATION_SERVER_STATE_CHANGE:
                    switch (statusInformation) {
                        case AS_ACTIVE:
                            //If RC from NTFY message is Null   

                            if (routingContext == null) {
                                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                                    aspFsm.getAs().doTransition(statusInformation.name());

                                    LOGGER.info("AS state changed from "
                                            + aspFsm.getAs().getCurrentState().getName()
                                            + " to ACTIVE state. RC " + message.getRoutingContext());

                                }
                            } else {
                                //If RC from NTFY message is notNull
                                Integer[] rcs = routingContext.getRoutingContext();
                                for (Integer rc : rcs) {
                                    AspStateMachine stateMachine = asp.getAspStateMachine(rc);
                                    if (stateMachine == null) {
                                        asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{rc}));
                                        LOGGER.error("Unknown RC received. RC " + rc);
                                    } else {
                                        stateMachine.getAs().doTransition(statusInformation.name());
                                    }
                                }
                            }

                            break;
                        case AS_PENDING:
                        case AS_INACTIVE:
                            if (routingContext == null) {
                                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                                    if (aspFsm.getCurrentState().getName().equals(AspState.ASP_INACTIVE.value())) {
                                        asp.sendAspActive(aspFsm.getAs());
                                        aspFsm.doTransition("SEND_ACTIVE");
                                        aspFsm.getAs().doTransition(statusInformation.name());
                                    }
                                }
                            } else {

                                Integer[] rcs = message.getRoutingContext().getRoutingContext();
                                for (Integer rc : rcs) {
                                    AspStateMachine aspStateMachine = asp.getAspStateMachine(rc);

                                    if (aspStateMachine != null) {

                                        if (aspStateMachine.getCurrentState().getName().equals(AspState.ASP_INACTIVE.value())) {
                                            asp.sendAspActive(aspStateMachine.getAs());
                                            aspStateMachine.doTransition("SEND_ACTIVE");
                                            aspStateMachine.getAs().doTransition(statusInformation.name());
                                        }
                                    } else {

                                        asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{rc}));
                                        LOGGER.error("Unexpected NTFY with unknown RC. RC " + routingContext);

                                    }
                                }
                            }
                            break;
                    }
                    break;
                case OTHER:
                    switch (statusInformation) {
                        case INSUFFICIENT_ASP_RESOURCES_ACTIVE_IN_AS:
                            if (routingContext == null) {

                                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                                    if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                                        asp.sendAspActive(aspFsm.getAs());
                                        aspFsm.doTransition("SEND_ACTIVE");

                                    }
                                }
                            } else {

                                Integer[] routingContexts = routingContext.getRoutingContext();
                                for (Integer rc : routingContexts) {
                                    AspStateMachine stateMachine = asp.getAspStateMachine(rc);
                                    if (stateMachine != null) {
                                        if (stateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                                            asp.sendAspActive(stateMachine.getAs());
                                            stateMachine.doTransition("SEND_ACTIVE");

                                        }
                                    } else {
                                        this.asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{rc}));
                                        LOGGER.error("Unexpected NTFY without RC");
                                    }
                                }
                            }
                            break;
                        case ALTERNATE_ASP_ACTIVE:
                            if (routingContext == null) {
                                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                                    if (aspFsm.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                                        aspFsm.doTransition("ASP_INACTIVE");
                                    }
                                }
                            } else {

                                Integer[] routingContexts = routingContext.getRoutingContext();
                                for (Integer rc : routingContexts) {
                                    AspStateMachine aspStateMachine = asp.getAspStateMachine(rc);
                                    if (aspStateMachine != null) {
                                        if (aspStateMachine.getCurrentState().getName().equals(AspState.ASP_ACTIVE.value())) {
                                            aspStateMachine.doTransition("ASP_INACTIVE");
                                        }
                                    } else {
                                        this.asp.sendError(ErrorCodes.INVALID_ROUTING_CONTEXT, null, new RoutingContext(new Integer[]{rc}));
                                        LOGGER.error("Unexpected NTFY without RC");
                                    }
                                }
                            }
                            break;
                    }
                    break;
            }
        } else {
            this.asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, message.getRoutingContext());
            LOGGER.error(String.format("Ntfy: Error: Unexpected message"));
        }
    }
}
