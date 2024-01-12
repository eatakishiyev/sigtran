/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class ConnectionRequest implements IsupParameter {

    private int localReference;
    private int spc;
    private int protocolClass = - 1;
    private int credit = -1;

    public ConnectionRequest() {
    }

    public ConnectionRequest(int localReference, int spc) {
        this.localReference = localReference;
        this.spc = spc;
    }

    @Override
    public byte[] encode() {

        byte[] data;
        if (protocolClass >= 0 && credit >= 0) {
            data = new byte[7];
            data[5] = (byte) (protocolClass & 0b11111111);
            data[6] = (byte) (credit & 0b11111111);
        } else {
            data = new byte[5];
        }
        data[0] = (byte) ((localReference >> 16) & 0b11111111);
        data[1] = (byte) ((localReference >> 8) & 0b11111111);
        data[2] = (byte) ((localReference) & 0b11111111);

        data[3] = (byte) (spc);
        data[4] = (byte) ((spc >> 8) & 0b00111111);
        return data;
    }

    @Override
    public void decode(byte[] data) {
        this.localReference = data[0] & 0b11111111;
        this.localReference = (localReference << 8) & (data[1] & 0b11111111);
        this.localReference = (localReference << 8) & (data[2] & 0b11111111);

        this.spc = data[3] & 0b00111111;
        this.spc = spc << 8;
        this.spc = spc | (data[4] & 0b11111111);

        if (data.length > 5) {
            this.protocolClass = data[5] & 0b111111111;
            this.credit = data[6] & 0b11111111;
        }
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getLocalReference() {
        return localReference;
    }

    public void setLocalReference(int localReference) {
        this.localReference = localReference;
    }

    public int getProtocolClass() {
        return protocolClass;
    }

    public void setProtocolClass(int protocolClass) {
        this.protocolClass = protocolClass;
    }

    public int getSpc() {
        return spc;
    }

    public void setSpc(int spc) {
        this.spc = spc;
    }

    @Override
    public int getParameterCode() {
        return CONNECTION_REQUEST;
    }

}
