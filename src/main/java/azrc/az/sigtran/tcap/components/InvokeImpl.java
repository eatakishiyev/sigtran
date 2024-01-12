/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.components;

import java.io.IOException;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.primitives.tc.OperationClass;
import azrc.az.sigtran.utils.ByteUtils;
import azrc.az.sigtran.tcap.parameters.ComponentType;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;
import azrc.az.sigtran.tcap.parameters.OperationCodeType;
import azrc.az.sigtran.tcap.parameters.ParameterFactory;
import azrc.az.sigtran.tcap.parameters.ParameterImpl;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class InvokeImpl implements Invoke {

    private Short invokeId;
    private Short linkedId;
    private OperationCodeImpl operationCode;
    private ParameterImpl parameter;
    private OperationClass invClass = OperationClass.CLASS1;
    private long invokeTimeOut;

    protected InvokeImpl() {
    }

    protected InvokeImpl(short invokeId, OperationCodeImpl operationCode) {
        this();
        this.invokeId = invokeId;
        this.operationCode = operationCode;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Invoke[");
        sb.append("InvokeId = ").append(invokeId)
                .append(",LinkedId = ").append(linkedId)
                .append(",OperationCode = ").append(operationCode)
                .append(",OperationClass = ").append(invClass)
                .append(",InvokeTimeOut = ").append(invokeTimeOut)
                .append(",Parameter = ");
        if (parameter == null) {
            sb.append("[]");
        } else {
            sb.append(ByteUtils.bytes2Hex(parameter.getData()));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (invokeId == null || operationCode == null) {
            throw new IncorrectSyntaxException("Mandatory parameters are absent.");
        }

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, INVOKE_TAG);
            int position = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);

            if (linkedId != null) {
                aos.writeInteger(TAG_LINKED_INVOKE_ID_CLASS, TAG_LINKED_INVOKE_ID, linkedId);
            }

            operationCode.encode(aos);

            if (parameter != null) {
//                parameter.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
                parameter.encode(aos);
            }

            aos.FinalizeContent(position);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        AsnInputStream tmpAis = ais.readSequenceStream();

        int tag = tmpAis.readTag();
        if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == TAG_INVOKE_ID) {
            this.invokeId = (short) tmpAis.readInteger();
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
        }

        tag = tmpAis.readTag();
        if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tmpAis.isTagPrimitive() && tag == TAG_LINKED_INVOKE_ID) {
            this.linkedId = (short) tmpAis.readInteger();
            tag = tmpAis.readTag();
        }

        if ((tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive()) && (tag == OperationCodeImpl.TAG_GLOBAL_OPCODE || tag == OperationCodeImpl.TAG_LOCAL_OPCODE)) {
            this.operationCode = ParameterFactory.createOperationCode();
            this.operationCode.setOperationCodeType(OperationCodeType.getInstance(tag));
            this.operationCode.decode(tmpAis);
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
        }

        if (tmpAis.available() > 0) {
//                tag = tmpAis.readTag();
            this.parameter = ParameterFactory.createParameter(tmpAis);
        }
    }

    @Override
    public void setLinkedInvokeId(Short id) {
        this.linkedId = id;
    }

    @Override
    public Short getLinkedInvokeId() {
        return this.linkedId;
    }

    @Override
    public void setOpCode(OperationCodeImpl opCode) {
        this.operationCode = opCode;
    }

    @Override
    public OperationCodeImpl getOpCode() {
        return this.operationCode;
    }

    @Override
    public void setParameter(ParameterImpl param) {
        this.parameter = param;
    }

    @Override
    public ParameterImpl getParameter() {
        return parameter;
    }

    @Override
    public Short getInvokeId() {
        return this.invokeId;
    }

    @Override
    public void setInvokeId(Short invokeId) {
        this.invokeId = invokeId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.INVOKE;
    }

    public OperationClass getInvokeClass() {
        return this.invClass;
    }

    public void setInvokeClass(OperationClass invClass) {
        this.invClass = invClass;
    }

    public boolean isSuccessReported() {
        return true;
    }

    public boolean isErrorReported() {
        return true;
    }

    /**
     * @return the InvokeTimeOut
     */
    public long getInvokeTimeOut() {
        return invokeTimeOut;
    }

    /**
     * @param InvokeTimeOut the InvokeTimeOut to set
     */
    public void setInvokeTimeOut(long InvokeTimeOut) {
        this.invokeTimeOut = InvokeTimeOut;
    }
}
