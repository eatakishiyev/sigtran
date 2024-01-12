/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.PivotPossibleIndicator;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class PivotCapability implements IsupParameter {

    private byte[] pivotCapabilities;

    public PivotCapability() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < pivotCapabilities.length; i++) {
            pivotCapabilities[i] = (byte) (pivotCapabilities[i] & 0b01111111);
        }
        pivotCapabilities[pivotCapabilities.length - 1] = (byte) (pivotCapabilities[pivotCapabilities.length - 1] | 0b10000000);
        return (pivotCapabilities);
    }

    @Override
    public void decode(byte[] data) {
        this.pivotCapabilities = data;
    }

    public void setPivotCapabilities(byte[] pivotCapabilities) {
        this.pivotCapabilities = pivotCapabilities;
    }

    public byte[] getPivotCapabilities() {
        return this.pivotCapabilities;
    }

    public static byte encodePivotCapabilityByte(boolean interworkingToRedicetionAllowed, PivotPossibleIndicator pivotPossibleIndicator) {
        int tmp = 0;
        tmp = (tmp << 1) | (interworkingToRedicetionAllowed ? 0 : 1);
        tmp = tmp << 3;
        tmp = (tmp << 3) | pivotPossibleIndicator.value();
        return (byte) tmp;
    }

    public static boolean isInterworingToRedirectionAllowed(byte b) {
        return ((b >> 6) & 0b00000001) == 0;
    }

    public static PivotPossibleIndicator getPivotPossibleIndicator(byte b) {
        return PivotPossibleIndicator.getInstance(b & 0b00000111);
    }

    @Override
    public int getParameterCode() {
        return PIVOT_CAPABILITY;
    }

}
