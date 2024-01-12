/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.IOException;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class Status implements Parameter {

    private final ParameterTag tag = ParameterTag.STATUS;
    private StatusType statusType;
    private StatusInformation statusInformation;

    public Status() {
    }

    public Status(StatusType statusType, StatusInformation statusInformation) {
        this.statusType = statusType;
        this.statusInformation = statusInformation;
    }

    /**
     * @return the StatusType
     */
    public StatusType getStatusType() {
        return statusType;
    }

    /**
     * @param StatusType the StatusType to set
     */
    public void setStatusType(StatusType StatusType) {
        this.statusType = StatusType;
    }

    /**
     * @return the StatusInformation
     */
    public StatusInformation getStatusInformation() {
        return statusInformation;
    }

    /**
     * @param StatusInformation the StatusInformation to set
     */
    public void setStatusInformation(StatusInformation StatusInformation) {
        this.statusInformation = StatusInformation;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        int st = data[0] & 0xFF;
        st = st << 8;
        st = st | data[1] & 0xFF;
        this.statusType = StatusType.getInstance(st);

        int si = data[2] & 0xFF;
        si = si << 8;
        si = si | data[3] & 0xFF;
        this.statusInformation = StatusInformation.getInstance(this.statusType, si);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) (this.statusType.value() >> 8);
        data[1] = (byte) (this.statusType.value());

        data[2] = (byte) (this.statusInformation.value() >> 8);
        data[3] = (byte) (this.statusInformation.value());

        baos.encode(tag, data);
    }

    @Override
    public String toString() {
        return String.format("Status[%s %s]", statusType, statusInformation);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }
}
