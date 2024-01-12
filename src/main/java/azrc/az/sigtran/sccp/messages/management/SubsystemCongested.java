/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.management;

import azrc.az.sigtran.sccp.address.SignallingPointCode;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.parameters.SCMGFormatIdentifiers;
import azrc.az.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SubsystemCongested implements ManagementMessage {

    private SCMGFormatIdentifiers messageType = SCMGFormatIdentifiers.SSC;
    private SubSystemNumber affectedSSN;
    private SignallingPointCode affectedPointCode;
    private SubsystemMultiplicityIndicator subsystemMultiplicityIndicator;
    private int sccpCongestionLevel;

    protected SubsystemCongested() {
    }

    protected SubsystemCongested(SubSystemNumber affectedSSN, SignallingPointCode affectedPointCode, SubsystemMultiplicityIndicator subsystemMultiplicityIndicator, int sccpCongestionLevel) {
        this.affectedSSN = affectedSSN;
        this.affectedPointCode = affectedPointCode;
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
        this.sccpCongestionLevel = sccpCongestionLevel;
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
        baos.write(sccpCongestionLevel);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        //Message type byte is already fetched
        this.affectedSSN = SubSystemNumber.getInstance(bais.read());
        this.affectedPointCode = new SignallingPointCode();
        this.affectedPointCode.decode(bais);
        this.subsystemMultiplicityIndicator = new SubsystemMultiplicityIndicator();
        this.subsystemMultiplicityIndicator.decode(bais);
        this.sccpCongestionLevel = bais.read();
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
     * @return the sccpCongestionLevel
     */
    public int getSccpCongestionLevel() {
        return sccpCongestionLevel;
    }

    /**
     * @param sccpCongestionLevel the sccpCongestionLevel to set
     */
    public void setSccpCongestionLevel(int sccpCongestionLevel) {
        this.sccpCongestionLevel = sccpCongestionLevel;
    }
    /**
     * @return the affectedSSN
     */
}
