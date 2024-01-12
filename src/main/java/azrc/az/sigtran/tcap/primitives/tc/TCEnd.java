/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import java.util.Arrays;
import java.util.List;

import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sigtran.tcap.parameters.UserInformation;
import azrc.az.sigtran.tcap.parameters.UserInformationImpl;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.primitives.Termination;

/**
 *
 * @author root
 */
public class TCEnd {

    private TCAPDialogue dialogue;
    private ApplicationContext applicationContextName;
    private UserInformation userInformation;
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
