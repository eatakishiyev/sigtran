/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.parameters;

import dev.ocean.sigtran.sccp.general.Encodable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SubsystemMultiplicityIndicator implements Encodable {

    public static final int UNKNOWN = 0;
    private int subsystemMultiplicityIndicator;

    @Override
    public String toString() {
        return new StringBuilder().append("SubsystemMultiplicityIndicator:[")
                .append(subsystemMultiplicityIndicator)
                .append("]").toString();
    }

    public SubsystemMultiplicityIndicator() {
    }

    public SubsystemMultiplicityIndicator(int subsystemMultiplicityIndicator) {
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        baos.write(subsystemMultiplicityIndicator);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        this.subsystemMultiplicityIndicator = bais.read();
    }

    /**
     * @return the subsystemMultiplicityIndicator
     */
    public int getSubsystemMultiplicityIndicator() {
        return subsystemMultiplicityIndicator;
    }

    /**
     * @param subsystemMultiplicityIndicator the subsystemMultiplicityIndicator
     * to set
     */
    public void setSubsystemMultiplicityIndicator(int subsystemMultiplicityIndicator) {
        this.subsystemMultiplicityIndicator = subsystemMultiplicityIndicator;
    }
}
