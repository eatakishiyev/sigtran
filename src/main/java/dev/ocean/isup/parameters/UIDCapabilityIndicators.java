/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class UIDCapabilityIndicators implements IsupParameter {

    private byte[] uidCapabilityIndicators;

    public UIDCapabilityIndicators() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UIDCapabilityIndicators[");
        for (int i = 0; i < uidCapabilityIndicators.length; i++) {
            sb.append("ThroughConnectionModificationPossible:").append(isThroughConnectionModificationPossible(uidCapabilityIndicators[i]))
                    .append("; StoppingT9TimerPossible:").append(isStoppingT9TimerPossible(uidCapabilityIndicators[i])).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < uidCapabilityIndicators.length; i++) {
            uidCapabilityIndicators[i] = (byte) (uidCapabilityIndicators[i] & 0b01111111);
        }

        uidCapabilityIndicators[uidCapabilityIndicators.length - 1] = (byte) (uidCapabilityIndicators[uidCapabilityIndicators.length - 1] | 0b10000000);
        return (uidCapabilityIndicators);
    }

    @Override
    public void decode(byte[] uidCapabilityIndicators) {
        this.uidCapabilityIndicators = uidCapabilityIndicators;
    }

    public byte[] getUidCapabilityIndicators() {
        return uidCapabilityIndicators;
    }

    public static byte encodeUIDCapabilityIndicatorByte(boolean stoppingT9TimerPossible, boolean throughConnectionModificationPossible) {
        int tmp = 0;
        tmp = (tmp << 6) | (stoppingT9TimerPossible ? 1 : 0);
        tmp = (tmp << 1) | (throughConnectionModificationPossible ? 1 : 0);
        return (byte) tmp;
    }

    public static boolean isStoppingT9TimerPossible(byte b) {
        return ((b >> 1) & 0b00000001) == 1;
    }

    public static boolean isThroughConnectionModificationPossible(byte b) {
        return (b & 0b00000001) == 1;
    }

    @Override
    public int getParameterCode() {
        return UID_CAPABILITY_INDICATORS;
    }

}
