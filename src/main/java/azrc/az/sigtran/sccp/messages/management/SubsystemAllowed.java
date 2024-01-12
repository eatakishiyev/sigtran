/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.management;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import azrc.az.sigtran.sccp.address.SignallingPointCode;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.parameters.SCMGFormatIdentifiers;
import azrc.az.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;

/**
 *
 * @author eatakishiyev
 */
public class SubsystemAllowed implements ManagementMessage {

    private final SCMGFormatIdentifiers messageType = SCMGFormatIdentifiers.SSA;
    private SubSystemNumber affectedSSN;
    private SignallingPointCode affectedPointCode;
    private SubsystemMultiplicityIndicator subsystemMultiplicityIndicator;

    protected SubsystemAllowed() {
    }

    protected SubsystemAllowed(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator) {
        this.affectedSSN = affectedSSN;
        this.affectedPointCode = affectedPointCode;
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
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
        //Message type byte is already fetched
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

    /**
     * @return the messageType
     */
    @Override
    public SCMGFormatIdentifiers getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("SubsystemAllowed:[")
                .append(";SSN = ").append(affectedSSN)
                .append(";AffectedPC = ").append(affectedPointCode)
                .append(";MultiplicityIndicator = ").append(subsystemMultiplicityIndicator)
                .append("]").toString();
    }
}
