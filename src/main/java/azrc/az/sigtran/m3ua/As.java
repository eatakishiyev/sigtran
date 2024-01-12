/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.messages.transfer.PayloadData;
import azrc.az.sigtran.m3ua.router.Route;
import azrc.az.state.machines.FiniteStateMachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eatakishiyev
 */
public abstract class As extends FiniteStateMachine implements Serializable {

    protected transient final List<AsStateListener> asStateListeners = new ArrayList<>();
    private boolean sendRoutingContext;
    private int rc = -1;
    private int networkAppearance = -1;
    private final List<Asp> attachedAspList = new ArrayList<>();

    public abstract List<AsStateListener> getAsStateListener();

    public abstract String getState();

    public As(String name) {
        super(name);
    }

    public void addAsp(Asp asp) throws Exception {
        asp.addAs(this);
        attachedAspList.add(asp);
    }

    public List<Asp> getAspList() {
        return attachedAspList;
    }

    public void removeAsp(Asp asp) {
        attachedAspList.remove(asp);
        asp.removeAsByRc(rc);
    }

    public void removeAsp(String name) {
        for (Asp asp : attachedAspList) {
            if (asp.getName().equals(name)) {
                attachedAspList.remove(asp);
                asp.removeAsByRc(rc);
                break;
            }
        }
    }

    public final void addAsStateListener(AsStateListener listener) {
        asStateListeners.add(listener);
    }

    public abstract boolean send(PayloadData payload);

    public int getRc() {
        return this.rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public boolean isSendRoutingContext() {
        return this.sendRoutingContext;
    }

    public void setSendRoutingContext(boolean sendRoutingContext) {
        this.sendRoutingContext = sendRoutingContext;
    }

    public int getNetworkAppearance() {
        return networkAppearance;
    }

    public void setNetworkAppearance(int networkAppearance) {
        this.networkAppearance = networkAppearance;
    }

    public abstract void removeAsStateListener(AsStateListener stateListener);

}
