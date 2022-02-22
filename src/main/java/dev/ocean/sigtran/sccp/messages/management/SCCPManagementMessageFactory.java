/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.messages.management;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import dev.ocean.sigtran.sccp.address.SignallingPointCode;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.parameters.SCMGFormatIdentifiers;
import dev.ocean.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;

/**
 *
 * @author eatakishiyev
 */
public class SCCPManagementMessageFactory implements Serializable{

    private SCCPManagementMessageFactory(){
        
    }
    public static ManagementMessage createManagementMessage(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        SCMGFormatIdentifiers sCMGFormatIdentifiers = SCMGFormatIdentifiers.getInstance(bais.read());
        switch (sCMGFormatIdentifiers) {
            case SOG:
                SubsystemOutOfServiceGrant subsystemOutOfServiceGrant = new SubsystemOutOfServiceGrant();
                subsystemOutOfServiceGrant.decode(bais);
                return subsystemOutOfServiceGrant;
            case SOR:
                SubsystemOutOfServiceRequest subsystemOutOfServiceRequest = new SubsystemOutOfServiceRequest();
                subsystemOutOfServiceRequest.decode(bais);
                return subsystemOutOfServiceRequest;
            case SSA:
                SubsystemAllowed subsystemAllowed = new SubsystemAllowed();
                subsystemAllowed.decode(bais);
                return subsystemAllowed;
            case SSC:
                SubsystemCongested subsystemCongested = new SubsystemCongested();
                subsystemCongested.decode(bais);
                return subsystemCongested;
            case SSP:
                SubsystemProhibited subsystemProhibited = new SubsystemProhibited();
                subsystemProhibited.decode(bais);
                return subsystemProhibited;
            case SST:
                SubsystemStatusTest subsystemStatusTest = new SubsystemStatusTest();
                subsystemStatusTest.decode(bais);
                return subsystemStatusTest;
        }
        return null;
    }

    public static  SubsystemAllowed createSubsystemAllowed() {
        return new SubsystemAllowed();
    }

    public static  SubsystemAllowed createSubsystemAllowed(SubSystemNumber affectedSubSystemNumber, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        return new SubsystemAllowed(affectedSubSystemNumber, affectedPointCode, subsystemMultiplicityIndicator);
    }

    public static  SubsystemCongested createSubsystemCongested() {
        return new SubsystemCongested();
    }

    public static  SubsystemCongested createSubsystemCongested(SubSystemNumber affectedSubSystemNumber, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator, int sccpCongestionLevel) {
        return new SubsystemCongested(affectedSubSystemNumber, affectedPointCode, subsystemMultiplicityIndicator, sccpCongestionLevel);
    }

    public static  SubsystemOutOfServiceGrant createSubsystemOutOfServiceGrant() {
        return new SubsystemOutOfServiceGrant();
    }

    public static  SubsystemOutOfServiceGrant createSubsystemOutOfServiceGrant(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        return new SubsystemOutOfServiceGrant(affectedSSN, affectedPointCode, subsystemMultiplicityIndicator);
    }

    public static  SubsystemOutOfServiceRequest createSubsystemOutOfServiceRequest() {
        return new SubsystemOutOfServiceRequest();
    }

    public static  SubsystemOutOfServiceRequest createSubsystemOutOfServiceRequest(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        return new SubsystemOutOfServiceRequest(affectedSSN, affectedPointCode, subsystemMultiplicityIndicator);
    }

    public static  SubsystemProhibited createSubsystemProhibited() {
        return new SubsystemProhibited();
    }

    public static  SubsystemProhibited createSubsystemProhibited(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        return new SubsystemProhibited(affectedSSN, affectedPointCode, subsystemMultiplicityIndicator);
    }

    public static  SubsystemStatusTest createSubsystemStatusTest() {
        return new SubsystemStatusTest();
    }

    public static  SubsystemStatusTest createSubsystemStatusTest(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        return new SubsystemStatusTest(affectedSSN, affectedPointCode, subsystemMultiplicityIndicator);
    }
}
