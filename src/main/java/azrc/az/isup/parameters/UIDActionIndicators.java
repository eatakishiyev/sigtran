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
public class UIDActionIndicators implements IsupParameter {

    private byte[] data;

    public UIDActionIndicators() {
    }

    @Override
    public byte[] encode() throws Exception {
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] & 0b01111111);
        }
        data[data.length - 1] = (byte) (data[data.length - 1] | 0b10000000);
        return data;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public int getParameterCode() {
        return UID_ACTION_INDICATORS;
    }

    public static int encodeUIDActionsIndicator(boolean throughConnectionInBothDirections, boolean stopOrDoNotStartT9Timer) {
        return ((stopOrDoNotStartT9Timer ? 1 : 0 << 1) | (throughConnectionInBothDirections ? 1 : 0));
    }

    public static boolean isThroughConnectionInBothDirections(byte b) {
        return (b & 0b00000001) == 1;
    }

    public static boolean isStopOrDoNotStartT9Timer(byte b) {
        return ((b >> 1) & 0b00000001) == 1;
    }

}
