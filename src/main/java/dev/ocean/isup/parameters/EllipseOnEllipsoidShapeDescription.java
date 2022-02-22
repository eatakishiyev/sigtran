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
class EllipseOnEllipsoidShapeDescription extends AbstractShapeDescription {

    private LatitudeSign latitudeSign;
    private int latitudeDegrees;
    private int longitudeDegrees;

    private int majorRadius;
    private int minorRadius;
    private int orientation;
    private int confidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EllipseOnEllipsoidShapeDescription[")
                .append("LatitudeSign = ").append(latitudeSign)
                .append(";LatitudeDegrees = ").append(latitudeDegrees)
                .append(";LongitudeDegrees = ").append(longitudeDegrees)
                .append(";MajorRadius = ").append(majorRadius)
                .append(";MinorRadius = ").append(minorRadius)
                .append(";Orientation = ").append(orientation)
                .append(";Confidence = ").append(confidence)
                .append("]");
        return sb.toString();
    }

    public EllipseOnEllipsoidShapeDescription() {

    }

    public EllipseOnEllipsoidShapeDescription(int majorRadius, int minorRadius, int orientation, int confidence, LatitudeSign latitudeSign, int latitudeDegrees, int longituteDegrees) {
        this.latitudeSign = latitudeSign;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longituteDegrees;

        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.orientation = orientation;
        this.confidence = confidence;
    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.ELLIPSE_ON_THE_ELLOPSOID;
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

        this.majorRadius = bais.read() & 0b01111111;
        this.minorRadius = bais.read() & 0b01111111;

        this.orientation = bais.read() & 0xFF;
        this.confidence = bais.read() & 0b01111111;
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

        baos.write(this.majorRadius & 0b01111111);
        baos.write(this.minorRadius & 0b01111111);
        baos.write(this.orientation);
        baos.write(this.confidence & 0b01111111);
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
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

    public int getMajorRadius() {
        return majorRadius;
    }

    public void setMajorRadius(int majorRadius) {
        this.majorRadius = majorRadius;
    }

    public int getMinorRadius() {
        return minorRadius;
    }

    public void setMinorRadius(int minorRadius) {
        this.minorRadius = minorRadius;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
