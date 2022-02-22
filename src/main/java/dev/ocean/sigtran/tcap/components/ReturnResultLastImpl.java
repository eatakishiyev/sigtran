/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import java.io.IOException;
import static dev.ocean.sigtran.tcap.components.Return.TAG_INVOKEID;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.ComponentType;
import dev.ocean.sigtran.tcap.parameters.OperationCodeImpl;
import dev.ocean.sigtran.tcap.parameters.OperationCodeType;
import dev.ocean.sigtran.tcap.parameters.ParameterFactory;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ReturnResultLastImpl implements ReturnResultLast {

    public static final int RETURN_RESULT_LAST_TAG = 0x02;
    public static final int TAG_RRL_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean TAG_RRL_PRIMITIVE = false;
    private Short invokeId;
    private OperationCodeImpl operationCode;
    private ParameterImpl parameter;

    protected ReturnResultLastImpl() {
    }

    protected ReturnResultLastImpl(short invokeId) {
        this();
        this.invokeId = invokeId;
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
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (invokeId == null) {
                throw new IncorrectSyntaxException("Mandatory parameter is absent");
            }

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, RETURN_RESULT_LAST_TAG);
            int position = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);

            if (this.operationCode != null
                    && this.parameter != null) {

                aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int _position = aos.StartContentDefiniteLength();

                this.operationCode.encode(aos);

                this.parameter.encode(aos);

                aos.FinalizeContent(_position);
            }
            aos.FinalizeContent(position);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, IOException, AsnException {

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. Class = %d Primitive =%b Tag = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }
        //        Tag ID already fetched let start from length
        int length = ais.readLength();
        byte[] data = new byte[length];
        ais.read(data);

        AsnInputStream tmpAis = new AsnInputStream(data);

        int tag = tmpAis.readTag();
        if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == TAG_INVOKEID) {
            this.invokeId = (short) tmpAis.readInteger();
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
        }

        if (tmpAis.available() > 0) {

            tag = tmpAis.readTag();
            if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL
                    && !tmpAis.isTagPrimitive()
                    && tag == Tag.SEQUENCE) {

                AsnInputStream _ais = tmpAis.readSequenceStream();

                tag = _ais.readTag();
                if ((_ais.getTagClass() == Tag.CLASS_UNIVERSAL && _ais.isTagPrimitive())
                        && (tag == OperationCodeImpl.TAG_GLOBAL_OPCODE || tag == OperationCodeImpl.TAG_LOCAL_OPCODE)) {
                    operationCode = ParameterFactory.createOperationCode(_ais);

                    if (tag == OperationCodeImpl.TAG_GLOBAL_OPCODE) {
                        operationCode.setOperationCodeType(OperationCodeType.GLOBAL);
                    } else if (tag == OperationCodeImpl.TAG_LOCAL_OPCODE) {
                        operationCode.setOperationCodeType(OperationCodeType.LOCAL);
                    }
                }

                parameter = ParameterFactory.createParameter(_ais);

            } else {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }
        }
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.RETURN_RESULT_LAST;
    }

    /**
     * @return the operationCode
     */
    public OperationCodeImpl getOperationCode() {
        return operationCode;
    }

    /**
     * @param operationCode the operationCode to set
     */
    public void setOperationCode(OperationCodeImpl operationCode) {
        this.operationCode = operationCode;
    }

    /**
     * @return the parameter
     */
    @Override
    public ParameterImpl getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    @Override
    public void setParameter(ParameterImpl parameter) {
        this.parameter = parameter;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ReturnResultLastImpl[");
        sb.append("InvokeId = ").append(invokeId).append(",")
                .append(operationCode).append(",").
                append(parameter).append("]");
        return sb.toString();
    }

    @Override
    public OperationCodeImpl getOpCode() {
        return null;
    }

}
