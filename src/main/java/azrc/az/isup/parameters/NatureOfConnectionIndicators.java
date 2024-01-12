/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.ContinuityCheckIndicator;
import azrc.az.isup.enums.EchoControlDeviceIndicator;
import azrc.az.isup.enums.SatelliteIndicator;
import java.io.ByteArrayInputStream;

/**
 *
 * @author eatakishiyev
 */
public class NatureOfConnectionIndicators implements IsupParameter {

    private SatelliteIndicator satelliteIndicator;
    private ContinuityCheckIndicator continuityCheckIndicator;
    private EchoControlDeviceIndicator echoControlDeviceIndicator;

    public NatureOfConnectionIndicators() {
    }

    public NatureOfConnectionIndicators(SatelliteIndicator satelliteIndicator, ContinuityCheckIndicator continuityCheckIndicator, EchoControlDeviceIndicator echoControlDeviceIndicator) {
        this.satelliteIndicator = satelliteIndicator;
        this.continuityCheckIndicator = continuityCheckIndicator;
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public SatelliteIndicator getSatelliteIndicator() {
        return satelliteIndicator;
    }

    public ContinuityCheckIndicator getContinuityCheckIndicator() {
        return continuityCheckIndicator;
    }

    public EchoControlDeviceIndicator getEchoControlDeviceIndicator() {
        return echoControlDeviceIndicator;
    }

    @Override
    public byte[] encode() {
        int b = 0;
        b = (b << 3) | echoControlDeviceIndicator.value();
        b = (b << 2) | continuityCheckIndicator.value();
        b = (b << 2) | satelliteIndicator.value();

        return new byte[]{(byte) b};
    }

    @Override
    public void decode(byte[] data) {
        int b = data[0] & 0xFF;
        this.satelliteIndicator = SatelliteIndicator.getInstance(b & 0x03);
        this.continuityCheckIndicator = ContinuityCheckIndicator.getInstance((b >> 2) & 0x03);
        this.echoControlDeviceIndicator = EchoControlDeviceIndicator.getInstance((b >> 4) & 0x01);
    }

    public void decode(ByteArrayInputStream bais) {
        int b = bais.read();
        this.satelliteIndicator = SatelliteIndicator.getInstance(b & 0x03);
        this.continuityCheckIndicator = ContinuityCheckIndicator.getInstance((b >> 2) & 0x03);
        this.echoControlDeviceIndicator = EchoControlDeviceIndicator.getInstance((b >> 4) & 0x01);
    }

    @Override
    public int getParameterCode() {
        return NATURE_OF_CONNECTIN_INDICATORS;
    }

}
