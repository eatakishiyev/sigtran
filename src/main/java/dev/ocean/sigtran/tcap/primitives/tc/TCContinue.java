/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import java.util.List;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.UserInformationImpl;

/**
 *
 * @author root
 */
public class TCContinue {

    private ApplicationContextImpl applicationContextName;
    private TCAPDialogue dialogue;
    private UserInformationImpl userInformation;
    private List<Operation> operations;

    /**
     * @return the originatingAddress
     */
//    public SCCPAddress getOriginatingAddress() {
//        return originatingAddress;
//    }
    /**
     * @param originatingAddress the originatingAddress to set
     */
//    public void setOriginatingAddress(SCCPAddress originatingAddress) {
//        this.originatingAddress = originatingAddress;
//    }
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
     * @return the dialogue
     */
    public TCAPDialogue getDialogue() {
        return dialogue;
    }

    /**
     * @param dialogue the dialogue to set
     */
    public void setDialogue(TCAPDialogue dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * @return the userInformation
     */
    public UserInformationImpl getUserInformations() {
        return userInformation;
    }

    /**
     * @param userInformations the userInformation to set
     */
    public void setUserInformation(UserInformationImpl userInformations) {
        this.userInformation = userInformations;
    }

    /**
     * @return the operations
     */
    public List<Operation> getComponents() {
        return operations;
    }

    /**
     * @param operations the operations to set
     */
    public void setComponents(List<Operation> operations) {
        this.operations = operations;
    }
}
