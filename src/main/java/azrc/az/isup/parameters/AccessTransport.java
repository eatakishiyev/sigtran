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
public class AccessTransport implements IsupParameter {

    private byte[] accessTransport;

    public AccessTransport() {
    }

    public AccessTransport(byte[] accessTransport) {
        this.accessTransport = accessTransport;
    }

    @Override
    public byte[] encode() {
        return accessTransport;
    }

    @Override
    public void decode(byte[] data) {
        this.accessTransport = data;
    }

    public byte[] getAccessTransport() {
        return accessTransport;
    }

    public void setAccessTransport(byte[] accessTransport) {
        this.accessTransport = accessTransport;
    }

    @Override
    public int getParameterCode() {
        return ACCESS_TRANSPORT;
    }

}
