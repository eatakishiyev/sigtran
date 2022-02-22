/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.TypeOfShape;
import dev.ocean.isup.enums.LatitudeSign;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class EllipsoidPointShapeDescription extends AbstractShapeDescription {

    private LatitudeSign latitudeSign;
    private int latitudeDegrees;
    private int longitudeDegrees;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EllipsoidPointShapeDescription[")
                .append("LatitudeSign = ").append(latitudeSign)
                .append(";LatitudeDegrees = ").append(latitudeDegrees)
                .append(";LongitudeDegrees = ").append(longitudeDegrees)
                .append("]");
        return sb.toString();
    }

    public EllipsoidPointShapeDescription() {
    }

    public EllipsoidPointShapeDescription(LatitudeSign latitudeSign, int latitudeDegrees, int longituteDegrees) {
        this.latitudeSign = latitudeSign;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longituteDegrees;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) {
        int b = this.latitudeSign.value();
        baos.write((b << 7) | ((this.latitudeDegrees >> 16) & 0b01111111));
        baos.write((this.latitudeDegrees >> 8) & 0b11111111);
        baos.write(this.latitudeDegrees & 0b11111111);
//        
        baos.write(this.longitudeDegrees >> 16);
        baos.write((this.longitudeDegrees >> 8) & 0b11111111);
        baos.write(this.longitudeDegrees & 0b11111111);
    }

    @Override
    public void decode(ByteArrayInputStream bais) {
        int b = bais.read() & 0xFF;
        this.latitudeSign = LatitudeSign.getInstance(b >> 7);

        this.latitudeDegrees = b & 0b01111111;
        this.latitudeDegrees = (latitudeDegrees << 8) | (bais.read() & 0xFF);
        this.latitudeDegrees = (latitudeDegrees << 8) | (bais.read() & 0xFF);

        this.longitudeDegrees = bais.read() & 0xFF;
        this.longitudeDegrees = (longitudeDegrees << 8) | (bais.read() & 0xFF);
        this.longitudeDegrees = (longitudeDegrees << 8) | (bais.read() & 0xFF);
    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.ELLIPSOID_POINT;
    }

    public int getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setLatitudeDegrees(int latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public LatitudeSign getLatitudeSign() {
        return latitudeSign;
    }

    public void setLatitudeSign(LatitudeSign latitudeSign) {
        this.latitudeSign = latitudeSign;
    }

    public int getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLongitudeDegrees(int longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

}
