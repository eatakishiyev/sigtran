/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import java.io.IOException;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.*;

/**
 *
 * @author root
 */
public class OperationCodeImpl implements OperationCode {

    private OperationCodeType operationCodeType;
    private int localValue;
    private long[] globalValue;

    public OperationCodeImpl() {
    }

    public OperationCodeImpl(int localValue) {
        this.operationCodeType = OperationCodeType.LOCAL;
        this.localValue = localValue;
    }

    public OperationCodeImpl(long[] globalValue) {
        this.operationCodeType = OperationCodeType.GLOBAL;
        this.globalValue = globalValue;
    }

    @Override
    public String toString() {
        return String.format("OperationCode: [OpCodeType = %s; localValue = %s]", operationCodeType, localValue);
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            switch (operationCodeType) {
                case LOCAL:
                    aos.writeInteger(localValue);
                    break;
                case GLOBAL:
                    aos.writeObjectIdentifier(globalValue);
                    break;
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && ais.getTag() == Tag.INTEGER) {
                this.localValue = (int) ais.readInteger();

            } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && ais.getTag() == Tag.OBJECT_IDENTIFIER) {
                this.globalValue = ais.readObjectIdentifier();

            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    @Override
    public OperationCodeType getOperationCodeType() {
        return this.operationCodeType;
    }

    @Override
    public void setOperationCodeType(OperationCodeType opCodeType) {
        this.operationCodeType = opCodeType;
    }

    @Override
    public int getLocalValue() {
        return this.localValue;
    }

    @Override
    public void setLocalValue(int value) {
        this.localValue = value;
    }

    /**
     * @return the globalValue
     */
    protected long[] getGlobalValue() {
        return globalValue;
    }

    /**
     * @param globalValue the globalValue to set
     */
    protected void setGlobalValue(long[] globalValue) {
        this.globalValue = globalValue;
    }
}
