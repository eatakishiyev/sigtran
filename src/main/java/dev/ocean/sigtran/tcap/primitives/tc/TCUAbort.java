/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.UserInformationImpl;
import dev.ocean.sigtran.tcap.primitives.AbortReason;

/**
 *
 * @author root Application Context Name parameter shall be present if and only
 * if the abort reason parameter indicates "application context not supported"
 */
public class TCUAbort {

    private long dialogueId;
    private AbortReason abortReason;
    private ApplicationContextImpl applicationContextName;
    private UserInformationImpl userInformation;
    private boolean lastComponent;

    public long getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(long dialogueId) {
        this.dialogueId = dialogueId;
    }

    /**
     * @return the abortReason
     */
    public AbortReason getAbortReason() {
        return abortReason;
    }

    /**
     * @param abortReason the abortReason to set
     */
    public void setAbortReason(AbortReason abortReason) {
        this.abortReason = abortReason;
    }

    /**
     * @return the applicationContextName
     */
    public ApplicationContextImpl getApplicationContextName() {
        return applicationContextName;
    }

    /**
     * @param applicationContextName the applicationContextName to set
     */
    public void setApplicationContextName(ApplicationContextImpl applicationContextName) {
        this.applicationContextName = applicationContextName;
    }

    /**
     * @return the userInformation
     */
    public UserInformationImpl getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformationImpl userInformation) {
        this.userInformation = userInformation;
    }

    /**
     * @return the lastComponent
     */
    public boolean isLastComponent() {
        return lastComponent;
    }

    /**
     * @param lastComponent the lastComponent to set
     */
    public void setLastComponent(boolean lastComponent) {
        this.lastComponent = lastComponent;
    }
}
