/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import java.util.List;

import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sigtran.tcap.parameters.UserInformation;
import azrc.az.sigtran.tcap.parameters.UserInformationImpl;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;

/**
 *
 * @author root
 */
public class TCContinue {

    private ApplicationContext applicationContextName;
    private TCAPDialogue dialogue;
    private UserInformation userInformation;
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
    public UserInformation getUserInformations() {
        return userInformation;
    }

    /**
     * @param userInformations the userInformation to set
     */
    public void setUserInformation(UserInformation userInformations) {
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
