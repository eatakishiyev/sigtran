/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import java.util.Arrays;
import java.util.List;
import dev.ocean.sigtran.tcap.QoS;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.UserInformationImpl;
import dev.ocean.sigtran.tcap.primitives.Termination;

/**
 *
 * @author root
 */
public class TCEnd {

    private TCAPDialogue dialogue;
    private ApplicationContextImpl applicationContextName;
    private UserInformationImpl userInformation;
    private Termination termination;
    private List<Operation> operations;

    @Override
    public String toString() {
        return String.format("ApplicationContext = %s Termination = %s", applicationContextName != null ? Arrays.toString(applicationContextName.getOid()) : "", termination);
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
     * @return the termination
     */
    public Termination getTermination() {
        return termination;
    }

    /**
     * @param termination the termination to set
     */
    public void setTermination(Termination termination) {
        this.termination = termination;
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
