/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.parameters.Cause;
import dev.ocean.sigtran.m3ua.parameters.UserPartUnavailableCause;

/**
 *
 * @author eatakishiyev
 */
public class NotifyCause {

    private Integer congestionLevel;
    private Cause cause;
    private Boolean isCongested;

    /**
     * @return the congestionLevel
     */
    public Integer getCongestionLevel() {
        return congestionLevel;
    }

    /**
     * @param congestionLevel the congestionLevel to set
     */
    public void setCongestionLevel(Integer congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    /**
     * @return the cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    /**
     * @return the isCongested
     */
    public Boolean getIsCongested() {
        return isCongested;
    }

    /**
     * @param isCongested the isCongested to set
     */
    public void setIsCongested(Boolean isCongested) {
        this.isCongested = isCongested;
    }
}
