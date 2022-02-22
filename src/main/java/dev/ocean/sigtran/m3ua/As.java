/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.state.machines.FiniteStateMachine;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public abstract class As extends FiniteStateMachine implements Serializable {

    protected transient final List<AsStateListener> asStateListeners = new ArrayList<>();
    private boolean sendRoutingContext;
    private int rc = -1;
    private int networkAppearance = -1;
    private final List<AspImpl> attachedAspList = new ArrayList<>();

    public As(String name) {
        super(name);
    }

    public void addAsp(AspImpl asp) throws Exception {
        asp.addAs(this);
        attachedAspList.add(asp);
    }

    public List<AspImpl> getAspList() {
        return attachedAspList;
    }

    public void removeAsp(AspImpl asp) {
        attachedAspList.remove(asp);
        asp.removeAsByRc(rc);
    }

    public void removeAsp(String name) {
        for (AspImpl asp : attachedAspList) {
            if (asp.getName().equals(name)) {
                attachedAspList.remove(asp);
                asp.removeAsByRc(rc);
                break;
            }
        }
    }

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

}
