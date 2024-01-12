/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.IOException;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorCodes;

/**
 *
 * @author root
 */
public class ErrorCode implements Parameter {

    private final ParameterTag tag = ParameterTag.ERROR_CODE;
    private ErrorCodes errorCode;

    public ErrorCode() {
    }

    public ErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorCode
     */
    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte data[] = new byte[length];
        bais.readData(data);

        int errCode = data[0] & 0xFF;
        errCode = errCode << 8;
        errCode = errCode | data[1] & 0xFF;
        errCode = errCode << 8;
        errCode = errCode | data[2] & 0xFF;
        errCode = errCode << 8;
        errCode = errCode | data[3] & 0xFF;

        this.errorCode = ErrorCodes.getInstance(errCode);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((errorCode.value() >> 24) & 0xFF);
        data[1] = (byte) ((errorCode.value() >> 16) & 0xFF);
        data[2] = (byte) ((errorCode.value() >> 8) & 0xFF);
        data[3] = (byte) (errorCode.value() & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ErrorCode[");
        sb.append(errorCode);
        sb.append("]");
        return sb.toString();
    }
}
