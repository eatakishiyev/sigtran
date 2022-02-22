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
 * @author root This class is using for both of TCBeginIndication and
 * TCBeginRequest
 */
public class TCBegin {

//    private SCCPAddress destinationAddress; //calledParty
    private ApplicationContextImpl applicationContextName;
//    private SCCPAddress originatingAddress; //callingParty
    private TCAPDialogue dialogue;
    /**
     * The user information can only be included if the application context name
     * parameter is also included or has been used at dialogue establishment.
     */
    private UserInformationImpl userInformation;
    private List<Operation> components;

    public TCBegin(TCAPDialogue dialogue) {
//        this.destinationAddress = destinationAddress;
//        this.originatingAddress = originatingAddress;
        this.dialogue = dialogue;
    }

//    /**
//     * @return the destinationAddress
//     */
//    public SCCPAddress getDestinationAddress() {
//        return destinationAddress;
//    }
//
//    /**
//     * @param destinationAddress the destinationAddress to set
//     */
//    public void setDestinationAddress(SCCPAddress destinationAddress) {
//        this.destinationAddress = destinationAddress;
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
    public void setApplicationContext(ApplicationContextImpl applicationContextName) {
        this.applicationContextName = applicationContextName;
    }

//    /**
//     * @return the originatingAddress
//     */
//    public SCCPAddress getOriginatingAddress() {
//        return originatingAddress;
//    }
//
//    /**
//     * @param originatingAddress the originatingAddress to set
//     */
//    public void setOriginatingAddress(SCCPAddress originatingAddress) {
//        this.originatingAddress = originatingAddress;
//    }

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
    public UserInformationImpl getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation

     */
    public void setUserInformation(UserInformationImpl userInformation) {
//        if (this.applicationContextName == null) {
//            throw new IOException("Regarding ITU Q.771 Table 4/Q.771, User "
//                    + "Information can only be included if the application context "
//                    + "name parameter included also.");
//        }
        this.userInformation = userInformation;
    }

    public boolean isApplicationContextIncluded() {
        return this.applicationContextName != null;
    }

    /**
     * @return the components
     */
    public List<Operation> getComponents() {
        return components;
    }

    /**
     * @param components the components to set
     */
    public void setComponents(List<Operation> components) {
        this.components = components;
    }
}
