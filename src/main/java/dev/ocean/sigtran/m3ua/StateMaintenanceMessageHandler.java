/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspDownAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUp;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUpAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.HeartBeat;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorCodes;
import org.apache.logging.log4j.*;

/**
 *
 * @author eatakishiyev
 */
public class StateMaintenanceMessageHandler {

    private static final Logger logger = LogManager.getLogger(StateMaintenanceMessageHandler.class);
    private final Asp asp;

    public StateMaintenanceMessageHandler(Asp asp) {
        this.asp = asp;
    }

    void handleAspUp(AspUp aspUp) throws Exception {
        if (asp.getNodeType() == NodeType.IPSP_SERVER) {

            for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                aspFsm.doTransition("ASPUP");
                aspFsm.getAs().doTransition("AS_INACTIVE");
            }

            asp.sendAspUpAck();
        }
    }

    void handleAspUpAck(AspUpAck aspUpAck) throws Exception {
        switch (asp.getNodeType()) {
            case ASP:
                this.fireTransition("ASPUP_ACK", asp);
                break;
            case IPSP_CLIENT:
                this.fireTransition("ASPUP_ACK", asp);
                for (AspStateMachine aspFsm : asp.aspStateMachines()) {
                    aspFsm.doTransition("SEND_ACTIVE");
                    asp.sendAspActive(aspFsm.getAs());
                    aspFsm.getAs().doTransition("AS_INACTIVE");
                }
                break;
            default:
                asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, null);
                logger.error(String.format("AspUpAck: Asp = %s Error: Unexpected message", asp.getName()));
                break;
        }
    }

    void handleAspDownAck(AspDownAck aspDownAck) throws Exception {
        if (asp.getNodeType() != NodeType.ASP) {
            asp.sendError(ErrorCodes.UNEXPECTED_MESSAGE, null, null);
            logger.error(String.format("AspDownAck: Asp = %s Error: Unexpected message", asp.getName()));
            return;
        }
        /**
         * If the ASP receives an ASP Down Ack without having sent an ASP Down
         * message, the ASP should now consider itself to be in the ASP-DOWN
         * state. If the ASP was previously in the ASP-ACTIVE or ASP-INACTIVE
         * state, the ASP should then initiate procedures to return itself to
         * its previous state.
         */
        this.fireTransition("ASPDN_ACK", asp);
    }

    void handleBeat(HeartBeat heartbeat) throws IOException {
        asp.sendHeartBeatAck(heartbeat.getHeartBeatData().getHeartBeatData());
    }

    private void fireTransition(String transition, Asp asp) throws Exception {

        for (AspStateMachine aspFsm : asp.aspStateMachines()) {
            aspFsm.doTransition(transition);
//            aspFsm.getAs().doTransition("AS_DOWN");
        }
    }
}
