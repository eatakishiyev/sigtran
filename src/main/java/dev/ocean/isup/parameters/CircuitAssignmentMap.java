/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.MapType;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CircuitAssignmentMap implements IsupParameter {

    private MapType mapType;

    private byte map1 = 0;
    private byte map2 = 0;
    private byte map3 = 0;
    private byte map4 = 0;

    public CircuitAssignmentMap() {
    }

    public CircuitAssignmentMap(MapType mapType) {
        this();
        this.mapType = mapType;
    }

    @Override
    public byte[] encode() {
        byte[] data;
        if (mapType == MapType.MAP_FORMAT_1544_KBIT_S_DIGITAL_PATH) {
            data = new byte[4];
        } else {
            data = new byte[5];
            data[4] = (byte) (map4);
        }
        data[0] = (byte) (mapType.value() & 0b00111111);
        data[1] = (byte) (map1);
        data[2] = (byte) (map2);
        data[3] = (byte) (map3);
        return data;
    }

    @Override
    public void decode(byte[] data) {
        this.mapType = MapType.getInstance(data[0] & 0b00111111);
        this.map1 = (byte) (data[1] & 0b11111111);
        this.map2 = (byte) (data[2] & 0b11111111);
        this.map3 = (byte) (data[3] & 0b11111111);
        this.map4 = (byte) (data[4] & 0b11111111);
    }

    public void toggleChannel(int channel, boolean used) {
        int bitIndex = -1;
        int value = used ? 1 : 0;
        if (channel >= 1 && channel <= 8) {
            bitIndex = (channel - 8 * 0) - 1;
            map1 = (byte) (map1 | (value << bitIndex));
        } else if (channel >= 9 && channel <= 16) {
            bitIndex = (channel - 8 * 1) - 1;
            map2 = (byte) (map2 | (value << bitIndex));
        } else if (channel >= 17 && channel <= 24) {
            bitIndex = (channel - 8 * 2) - 1;
            map3 = (byte) (map3 | (value << bitIndex));
        } else if (channel >= 25 && channel <= 31) {
            bitIndex = (channel - 8 * 3) - 1;
            map4 = (byte) (map4 | (value << bitIndex));
        } else {
            throw new IndexOutOfBoundsException("Channel index out of bound. Should be in range[1..31], current is " + channel);
        }
    }

    public boolean isChannelUsed(int channel) {
        int bitIndex = -1;
        if (channel >= 1 && channel <= 8) {
            bitIndex = (channel - 8 * 0) - 1;
            return ((map1 >> bitIndex) & 0b00000001) == 1;
        } else if (channel >= 9 && channel <= 16) {
            bitIndex = (channel - 8 * 1) - 1;
            return ((map2 >> bitIndex) & 0b00000001) == 1;
        } else if (channel >= 17 && channel <= 24) {
            bitIndex = (channel - 8 * 2) - 1;
            return ((map3 >> bitIndex) & 0b00000001) == 1;
        } else if (channel >= 25 && channel <= 31) {
            bitIndex = (channel - 8 * 3) - 1;
            return ((map4 >> bitIndex) & 0b00000001) == 1;
        } else {
            throw new IndexOutOfBoundsException("Channel index out of bound. Should be in range[1..31], current is " + channel);
        }
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    @Override
    public int getParameterCode() {
        return CIRCUIT_ASSIGNMENT_MAP;
    }

}
