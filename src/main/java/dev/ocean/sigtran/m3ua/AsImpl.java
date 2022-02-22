/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.messages.transfer.PayloadData;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;
import java.io.IOException;
import dev.ocean.sigtran.m3ua.parameters.StatusInformation;
import dev.ocean.state.machines.State;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public class AsImpl extends As {
    private transient final M3UAStackImpl stack;

    @Override
    public String toString() {
        return String.format("As [Name = %s ;  RoutingContext = %s ; State = %s ; ]",
                getName(), getRc(), getCurrentState().getName());
    }

    protected AsImpl(String name, int routingContext, M3UAStackImpl stack) throws Exception {
        super(name);
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name parameter can not be null");
        }
        this.stack = stack;

        setRc(routingContext);

        initFsm();
    }

    public boolean send(PayloadData payload) {

        AsState asState = AsState.getInstance(getCurrentState().getName());

        if (this.getNetworkAppearance() >= 0) {
            payload.setNetworkAppearance(this.getNetworkAppearance());
        }

        if (this.isSendRoutingContext()) {
            payload.setRoutingContext(new RoutingContext(new Integer[]{this.getRc()}));
        }

        if (asState == AsState.AS_ACTIVE) {
            int aspIndex = payload.getProtocolData().getSls() & stack.aspSelectMask;
            aspIndex = aspIndex % getAspList().size();

            AspImpl asp = getAspList().get(aspIndex);
            AspState aspState = AspState.getInstance(asp.getAspStateMachine(this.getRc()).
                    getCurrentState().getName());
            if (aspState == AspState.ASP_ACTIVE) {
                asp.sendPayload(payload);
                return true;
            } else {
                for (AspImpl alternateAsp : getAspList()) {
                    aspState = AspState.getInstance(alternateAsp.
                            getAspStateMachine(this.getRc()).getCurrentState().getName());
                    if (aspState == AspState.ASP_ACTIVE) {
                        asp.sendPayload(payload);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void addAsStateListener(AsStateListener listener) {
        asStateListeners.add(listener);
    }

    public void removeAsStateListener(AsStateListener listener) {
        asStateListeners.remove(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AsImpl) {
            AsImpl as = (AsImpl) obj;
            return as.getName().trim().toLowerCase().equals(getName().trim().toLowerCase());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + getName().hashCode();
        return hash;
    }

    public List<AsStateListener> getAsStateListener() {
        return asStateListeners;
    }

    private void initFsm() throws Exception {
//          States
        State asDown = createState("AS_DOWN");
        State asInactive = createState("AS_INACTIVE");
        State asActive = createState("AS_ACTIVE");
        State asPending = createState("AS_PENDING");

        setStart(asDown);
//          Transitions
        asDown.createTransition(StatusInformation.AS_INACTIVE.name(), asInactive);
        asDown.createTransition("AS_DOWN", asDown);
        asInactive.createTransition("AS_DOWN", asDown);
        asInactive.createTransition(StatusInformation.AS_ACTIVE.name(), asActive);
        asInactive.createTransition(StatusInformation.AS_PENDING.name(), asInactive);
        asInactive.createTransition(StatusInformation.AS_INACTIVE.name(), asInactive);
        asActive.createTransition("AS_DOWN", asDown);
        asActive.createTransition(StatusInformation.AS_PENDING.name(), asPending);
        asActive.createTransition(StatusInformation.AS_ACTIVE.name(), asActive);

        asPending.createTransition("AS_DOWN", asDown);
        asPending.createTransition(StatusInformation.AS_ACTIVE.name(), asActive);
        asPending.createTransition(StatusInformation.AS_INACTIVE.name(), asInactive);

    }

    public String getState() {
        return super.getCurrentState().getName();
    }
}
