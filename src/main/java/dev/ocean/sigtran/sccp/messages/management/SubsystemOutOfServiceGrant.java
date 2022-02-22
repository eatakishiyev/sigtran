/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.messages.management;

import dev.ocean.sigtran.sccp.address.SignallingPointCode;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.parameters.SCMGFormatIdentifiers;
import dev.ocean.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SubsystemOutOfServiceGrant implements ManagementMessage {

    private SCMGFormatIdentifiers messageType = SCMGFormatIdentifiers.SOG;
    private SubSystemNumber affectedSSN;
    private SignallingPointCode affectedPointCode;
    private SubsystemMultiplicityIndicator subsystemMultiplicityIndicator;

    protected SubsystemOutOfServiceGrant() {
    }

    protected SubsystemOutOfServiceGrant(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        this.affectedSSN = affectedSSN;
        this.affectedPointCode = affectedPointCode;
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
    }

    @Override
    public SCMGFormatIdentifiers getMessageType() {
        return this.messageType;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        baos.write(messageType.value());
        baos.write(affectedSSN.value());
        affectedPointCode.encode(baos);
        subsystemMultiplicityIndicator.encode(baos);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        //Message type byte already fetched
        this.affectedSSN = SubSystemNumber.getInstance(bais.read());
        this.affectedPointCode = new SignallingPointCode();
        this.affectedPointCode.decode(bais);
        this.subsystemMultiplicityIndicator = new SubsystemMultiplicityIndicator();
        this.subsystemMultiplicityIndicator.decode(bais);
    }

    /**
     * @return the affectedSSN
     */
    public SubSystemNumber getAffectedSSN() {
        return affectedSSN;
    }

    /**
     * @param affectedSSN the affectedSSN to set
     */
    public void setAffectedSSN(SubSystemNumber affectedSSN) {
        this.affectedSSN = affectedSSN;
    }

    /**
     * @return the affectedPointCode
     */
    public SignallingPointCode getAffectedPointCode() {
        return affectedPointCode;
    }

    /**
     * @param affectedPointCode the affectedPointCode to set
     */
    public void setAffectedPointCode(SignallingPointCode affectedPointCode) {
        this.affectedPointCode = affectedPointCode;
    }

    /**
     * @return the subsystemMultiplicityIndicator
     */
    public SubsystemMultiplicityIndicator getSubsystemMultiplicityIndicator() {
        return subsystemMultiplicityIndicator;
    }

    /**
     * @param subsystemMultiplicityIndicator the subsystemMultiplicityIndicator
     * to set
     */
    public void setSubsystemMultiplicityIndicator(SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
    }
}
