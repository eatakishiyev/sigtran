/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.state.machines.FiniteStateMachine;
import dev.ocean.state.machines.State;
import dev.ocean.state.machines.StateHandler;

import java.util.List;

/**
 * @author eatakishiyev
 */
public class AspStateMachine extends FiniteStateMachine {

    protected final AspImpl asp;
    protected final As as;

    public AspStateMachine(AspImpl asp, As as) throws Exception {
        super(asp.getName());
        this.asp = asp;
        this.as = as;
        initFSM();
    }

    private void initFSM() throws Exception {
        if (asp.getNodeType() == NodeType.ASP || asp.getNodeType() == NodeType.IPSP_CLIENT) {
            /**
             * If the ASP was previously in the ASP-ACTIVE or ASP-INACTIVE
             * state, the ASP should then initiate procedures to return itself
             * to its previous state
             */
            State stateASP_DOWN = createState(AspState.ASP_DOWN.name()).setEntryAction(new StateHandler() {
                @Override
                public void execute() throws Exception {

                    boolean hasActiveAsp = false;
                    List<AspImpl> aspList = as.getAspList();
                    for (AspImpl otherAsp : aspList) {//Traverse through Application Servers Asp list to check if any active state Asp available
                        if (otherAsp.getAspStateMachine(as.getRc()).getCurrentState()
                                .getName().equals(AspState.ASP_ACTIVE.name())) {
                            hasActiveAsp = true;
                            break;
                        }
                    }
                    if (!hasActiveAsp) {
                        as.doTransition(AsState.AS_DOWN.name());
                        for (AsStateListener asStateListener : as.asStateListeners) {
                            asStateListener.onAsDown(as.getRc());
                        }

                    }
                }
            });

            State stateUP_SENT = createState(AspState.UP_SENT.name()).
                    setStateTimeout(this.asp.getAspUpTimeOut(), new UpSentTimeOutHandler(asp));

            State stateASP_INACTIVE = createState(AspState.ASP_INACTIVE.name());

            State stateASP_ACTIVE = createState(AspState.ASP_ACTIVE.name()).setEntryAction(new StateHandler() {
                @Override
                public void execute() throws Exception {
                    for (AsStateListener aspStateListener : as.asStateListeners) {
                        aspStateListener.onAsActive(as.getRc());
                    }
                }
            });

            State stateDOWN_SENT = createState(AspState.DOWN_SENT.name()).
                    setStateTimeout(this.asp.getAspDownTimeOut(), new DownSentTimeOutHandler(asp));

            State stateINACTIVE_SENT = createState(AspState.INACTIVE_SENT.name()).
                    setStateTimeout(this.asp.getAspInactiveTimeOut(), new InactiveSentTimeOutHandler(asp));

            State stateACTIVE_SENT = createState(AspState.ACTIVE_SENT.name()).
                    setStateTimeout(this.asp.getAspActiveTimeOut(), new ActiveSentTimeOutHandler(asp, this));

            //Set initial state of finite state machine
            setStart(stateASP_DOWN);

//=================================================      ASP_DOWN          ========================================================
            stateASP_DOWN.createTransition("SEND_UP", stateUP_SENT);

            stateASP_DOWN.createTransition("COMM_LOST", stateASP_DOWN);
//=================================================      UP_SENT           ========================================================
            stateUP_SENT.createTransition("ASPUP_ACK", stateASP_INACTIVE);

            stateUP_SENT.createTransition("COMM_LOST", stateASP_DOWN);
//=================================================      ASP_INACTIVE      =========================================================
            stateASP_INACTIVE.createTransition("SEND_ACTIVE", stateACTIVE_SENT);

            stateASP_INACTIVE.createTransition("SEND_DOWN", stateDOWN_SENT);

            stateASP_INACTIVE.createTransition("COMM_LOST", stateASP_DOWN);
//=================================================      ACTIVE_SENT       =========================================================
            stateACTIVE_SENT.createTransition("ASPAC_ACK", stateASP_ACTIVE);

            stateACTIVE_SENT.createTransition("COMM_LOST", stateASP_DOWN);
//=================================================      ASP_ACTIVE        =========================================================
            stateASP_ACTIVE.createTransition("SEND_DOWN", stateDOWN_SENT);

            stateASP_ACTIVE.createTransition("ALTERNATE_ASP_ACTIVE", stateASP_INACTIVE);

            stateASP_ACTIVE.createTransition("SEND_INACTIVE", stateINACTIVE_SENT);

            stateASP_ACTIVE.createTransition("COMM_LOST", stateASP_DOWN);

            stateASP_ACTIVE.createTransition("ASP_INACTIVE", stateASP_INACTIVE);
//================================================      DOWN_SENT          =========================================================
            stateDOWN_SENT.createTransition("ASPDN_ACK", stateASP_DOWN);

            stateDOWN_SENT.createTransition("COMM_LOST", stateASP_DOWN);
//================================================      INACTIVE_SENT      =========================================================
            stateINACTIVE_SENT.createTransition("ASPIA_ACK", stateASP_INACTIVE);

            stateINACTIVE_SENT.createTransition("COMM_LOST", stateASP_DOWN);
        } else if (asp.getNodeType() == NodeType.IPSP_SERVER) {
            State stateDOWN = createState(AspState.ASP_DOWN.name());
            State stateINACTIVE = createState(AspState.ASP_INACTIVE.name());
            State stateACTIVE = createState(AspState.ASP_ACTIVE.name()).setEntryAction(new StateHandler() {
                @Override
                public void execute() throws Exception {
                    for (AsStateListener asStateListener : as.asStateListeners) {
                        asStateListener.onAsActive(as.getRc());
                    }
                }
            });

            stateDOWN.createTransition("ASPUP", stateINACTIVE);
            stateDOWN.createTransition("COMM_LOST", stateDOWN);

            stateINACTIVE.createTransition("ASPAC", stateACTIVE);
            stateINACTIVE.createTransition("ASPDOWN", stateDOWN);
            stateINACTIVE.createTransition("COMM_LOST", stateDOWN);

            stateACTIVE.createTransition("ASPINACTIVE", stateINACTIVE);
            stateACTIVE.createTransition("ASPDOWN", stateDOWN);
            stateACTIVE.createTransition("COMM_LOST", stateDOWN);

            setStart(stateDOWN);
        }
    }

    public As getAs() {
        return as;
    }

}
