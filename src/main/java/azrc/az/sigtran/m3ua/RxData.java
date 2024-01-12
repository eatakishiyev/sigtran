/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public class RxData {

    private byte[] data;
    private int streamNumber;

    protected RxData(byte[] data, int streamNumber) {
        this.data = data;
        this.streamNumber = streamNumber;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the streamNumber
     */
    public int getStreamNumber() {
        return streamNumber;
    }

    /**
     * @param streamNumber the streamNumber to set
     */
    public void setStreamNumber(int streamNumber) {
        this.streamNumber = streamNumber;
    }

}
