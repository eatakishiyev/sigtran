/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sigtran.tcap.parameters.UserInformation;
import azrc.az.sigtran.tcap.parameters.UserInformationImpl;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.primitives.AbortReason;

/**
 *
 * @author root Application Context Name parameter shall be present if and only
 * if the abort reason parameter indicates "application context not supported"
 */
public class TCUAbort {

    private long dialogueId;
    private AbortReason abortReason;
    private ApplicationContext applicationContextName;
    private UserInformation userInformation;
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
    public ApplicationContext getApplicationContextName() {
        return applicationContextName;
    }

    /**
     * @param applicationContextName the applicationContextName to set
     */
    public void setApplicationContextName(ApplicationContext applicationContextName) {
        this.applicationContextName = applicationContextName;
    }

    /**
     * @return the userInformation
     */
    public UserInformation getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformation userInformation) {
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
