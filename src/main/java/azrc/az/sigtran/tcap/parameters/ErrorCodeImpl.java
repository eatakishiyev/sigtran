/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.interfaces.ErrorCode;
import azrc.az.sigtran.tcap.messages.ErrorCodeType;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ErrorCodeImpl implements ErrorCode {

    private ErrorCodeType errType;
    private Integer localError;
    private long[] globalError;

    public ErrorCodeImpl() {
    }

    public ErrorCodeImpl(Integer errorCode) {
        this.errType = ErrorCodeType.LOCAL;
        this.localError = errorCode;
    }

    public ErrorCodeImpl(long[] globalError) {
        this.errType = ErrorCodeType.GLOBAL;
        this.globalError = globalError;
    }

    @Override
    public void setErrorCodeType(ErrorCodeType errType) {
        this.errType = errType;
    }

    @Override
    public ErrorCodeType getErrorCodeType() {
        return this.errType;
    }

    @Override
    public void setErrorCode(Integer errorCode) {
        this.localError = errorCode;
    }

    @Override
    public Integer getErrorCode() {
        return this.localError;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            switch (errType) {
                case LOCAL:
                    aos.writeInteger(this.localError);
                    break;
                case GLOBAL:
                    aos.writeObjectIdentifier(globalError);
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

                this.localError = (int) ais.readInteger();
                this.errType = ErrorCodeType.LOCAL;

            } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && ais.getTagClass() == Tag.OBJECT_IDENTIFIER) {

                this.globalError = ais.readObjectIdentifier();
                this.errType = ErrorCodeType.GLOBAL;

            } else {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the globalError
     */
    protected long[] getGlobalError() {
        return globalError;
    }

    /**
     * @param globalError the globalError to set
     */
    protected void setGlobalError(long[] globalError) {
        this.globalError = globalError;
    }
}
