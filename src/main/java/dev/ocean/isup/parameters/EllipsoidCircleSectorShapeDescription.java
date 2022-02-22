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
public class EllipsoidCircleSectorShapeDescription extends AbstractShapeDescription {

    private LatitudeSign latitudeSign;
    private int latitudeDegrees;
    private int longitudeDegrees;

    private int radius;
    private int offset;
    private int includedAngle;
    private int confidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EllipsoidCircleSectorShapeDescription[")
                .append("LatitudeSign = ").append(latitudeSign)
                .append(";LatitudeDegrees = ").append(latitudeDegrees)
                .append(";LongitudeDegrees = ").append(longitudeDegrees)
                .append(";Radiues = ").append(radius)
                .append(";Offset = ").append(offset)
                .append(";IncludeAngle = ").append(includedAngle)
                .append(";Confidence = ").append(confidence)
                .append("]");
        return sb.toString();
    }

    public EllipsoidCircleSectorShapeDescription() {
    }

    public EllipsoidCircleSectorShapeDescription(int radius, int offset, int includedAngle, int confidence, LatitudeSign latitudeSign, int latitudeDegrees, int longituteDegrees) {
        this.latitudeSign = latitudeSign;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longituteDegrees;

        this.radius = radius;
        this.offset = offset;
        this.includedAngle = includedAngle;
        this.confidence = confidence;
    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.ELLIPSOID_CIRCLE_SECTOR;
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

        baos.write(this.radius & 0b01111111);
        baos.write(this.offset);
        baos.write(this.includedAngle);
        baos.write(this.confidence & 0b01111111);

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

        this.radius = bais.read() & 0b01111111;
        this.offset = bais.read() & 0xFF;
        this.includedAngle = bais.read() & 0xFF;
        this.confidence = bais.read() & 0b01111111;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getIncludedAngle() {
        return includedAngle;
    }

    public void setIncludedAngle(int includedAngle) {
        this.includedAngle = includedAngle;
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

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
