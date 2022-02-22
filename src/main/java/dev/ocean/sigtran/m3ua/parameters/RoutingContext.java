/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import java.util.Arrays;

/**
 *
 * @author root
 */
public class RoutingContext implements Parameter {

    private final ParameterTag tag = ParameterTag.ROUTING_CONTEXT;
    private Integer[] routingContext = null;

    public RoutingContext() {
    }

    public RoutingContext(Integer[] routingContext) {
        this.routingContext = routingContext;
    }

    public Integer[] getRoutingContext() {
        return this.routingContext;
    }

    public void setRoutingContext(Integer[] routingContext) {
        this.routingContext = routingContext;
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        if (routingContext != null && routingContext.length > 0) {
            if (routingContext.length == 1) {
                baos.encode(this.tag, rcToOctetString(routingContext[0]));
            } else {
                ByteArrayOutputStream _baos = new ByteArrayOutputStream(4 * routingContext.length);
                for (int i = 0; i < routingContext.length; i++) {
                    _baos.write(rcToOctetString(routingContext[i]));
                }
                baos.encode(tag, _baos.toByteArray());
            }
        }
    }

    private byte[] rcToOctetString(Integer rc) {
        byte[] data = new byte[4];
        data[0] = (byte) ((rc >> 24) & 0xFF);
        data[1] = (byte) ((rc >> 16) & 0xFF);
        data[2] = (byte) ((rc >> 8) & 0xFF);
        data[3] = (byte) (rc & 0xFF);
        return data;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        int count = length / 4;
        this.routingContext = new Integer[count];
        for (int i = 0; i < count; i++) {
            int _routingContext = bais.read() & 0xFF;
            _routingContext = _routingContext << 8;
            _routingContext = _routingContext | bais.read() & 0xFF;
            _routingContext = _routingContext << 8;
            _routingContext = _routingContext | bais.read() & 0xFF;
            _routingContext = _routingContext << 8;
            _routingContext = _routingContext | bais.read() & 0xFF;
            this.routingContext[i] = _routingContext;
        }
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RoutingContexts [");
        sb.append(Arrays.toString(routingContext)).
                append("]");
        return sb.toString();
    }
}
