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
public class CollectCallRequest implements IsupParameter {

    private boolean collectCallRequested;

    public CollectCallRequest() {
    }

    public CollectCallRequest(boolean collectCallRequested) {
        this.collectCallRequested = collectCallRequested;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (collectCallRequested ? 1 : 0)};
    }

    @Override
    public void decode(byte[] data) {
        this.collectCallRequested = (data[0] & 0b00000001) == 1;
    }

    public boolean isCollectCallRequested() {
        return collectCallRequested;
    }

    public void setCollectCallRequested(boolean collectCallRequested) {
        this.collectCallRequested = collectCallRequested;
    }

    @Override
    public int getParameterCode() {
        return COLLECT_CALL_REQUEST;
    }

}
