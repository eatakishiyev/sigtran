/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import java.io.IOException;
import static dev.ocean.sigtran.tcap.components.Return.TAG_INVOKEID;
import static dev.ocean.sigtran.tcap.components.ReturnError.RETURN_ERROR_TAG;
import static dev.ocean.sigtran.tcap.components.ReturnError.RE_CLASS;
import static dev.ocean.sigtran.tcap.components.ReturnError.RE_PRIMITIVE;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.ComponentType;
import dev.ocean.sigtran.tcap.parameters.ErrorCodeImpl;
import dev.ocean.sigtran.tcap.parameters.OperationCodeImpl;
import dev.ocean.sigtran.tcap.parameters.Parameter;
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
public class ReturnErrorImpl implements ReturnError {

    private Short invokeId;
    private ErrorCodeImpl errorCode;
    private ParameterImpl parameter;

    protected ReturnErrorImpl() {
    }

    protected ReturnErrorImpl(short invokeId, ErrorCodeImpl errorCode) {
        this();
        this.invokeId = invokeId;
        this.errorCode = errorCode;
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
        return ComponentType.RETURN_ERROR;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (invokeId == null || errorCode == null) {
            throw new IncorrectSyntaxException("Mandatory parameters are absent.");
        }
        try {
            aos.writeTag(RE_CLASS, RE_PRIMITIVE, RETURN_ERROR_TAG);
            int position = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);

            errorCode.encode(aos);

            if (parameter != null) {
                parameter.encode(aos);
            }

            aos.FinalizeContent(position);
        } catch (AsnException | IOException exception) {
            throw new IncorrectSyntaxException(exception.getLocalizedMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE=%b TAG= %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        AsnInputStream _ais = ais.readSequenceStream();

        int tag = _ais.readTag();
        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL && _ais.isTagPrimitive() && tag == TAG_INVOKEID) {
            this.invokeId = (short) _ais.readInteger();
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", _ais.getTagClass(), _ais.isTagPrimitive(), _ais.getTag()));
        }

        tag = _ais.readTag();
        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL && _ais.isTagPrimitive() && (tag == ErrorCodeImpl.GLOBAL_ERROR_CODE_TAG_ || tag == ErrorCodeImpl.LOCAL_ERROR_CODE_TAG_)) {
            this.errorCode = ParameterFactory.createErrorCode(_ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", _ais.getTagClass(), _ais.isTagPrimitive(), _ais.getTag()));
        }

        if (_ais.available() > 0) {
            this.parameter = ParameterFactory.createParameter(_ais);
        }
    }

    @Override
    public Parameter getParameter() {
        return this.parameter;
    }

    @Override
    public void setParameter(ParameterImpl params) {
        this.parameter = params;
    }

    @Override
    public void setErrorCode(ErrorCodeImpl errCode) {
        this.errorCode = errCode;
    }

    @Override
    public ErrorCodeImpl getErrorCode() {
        return this.errorCode;
    }

    @Override
    public OperationCodeImpl getOpCode() {
        return null;
    }

}
