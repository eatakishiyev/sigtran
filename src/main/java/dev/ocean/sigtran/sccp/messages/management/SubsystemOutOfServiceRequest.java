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
public class SubsystemOutOfServiceRequest implements ManagementMessage {

    private SCMGFormatIdentifiers messageType = SCMGFormatIdentifiers.SOR;
    private SubSystemNumber affectedSSN;
    private SignallingPointCode affectedPointCode;
    private SubsystemMultiplicityIndicator subsystemMultiplicityIndicator;

    protected SubsystemOutOfServiceRequest() {
    }

    protected SubsystemOutOfServiceRequest(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
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
    protected SubSystemNumber getAffectedSSN() {
        return affectedSSN;
    }

    /**
     * @param affectedSSN the affectedSSN to set
     */
    protected void setAffectedSSN(SubSystemNumber affectedSSN) {
        this.affectedSSN = affectedSSN;
    }

    /**
     * @return the affectedPointCode
     */
    protected SignallingPointCode getAffectedPointCode() {
        return affectedPointCode;
    }

    /**
     * @param affectedPointCode the affectedPointCode to set
     */
    protected void setAffectedPointCode(SignallingPointCode affectedPointCode) {
        this.affectedPointCode = affectedPointCode;
    }

    /**
     * @return the subsystemMultiplicityIndicator
     */
    protected SubsystemMultiplicityIndicator getSubsystemMultiplicityIndicator() {
        return subsystemMultiplicityIndicator;
    }

    /**
     * @param subsystemMultiplicityIndicator the subsystemMultiplicityIndicator
     * to set
     */
    protected void setSubsystemMultiplicityIndicator(SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
    }
}
