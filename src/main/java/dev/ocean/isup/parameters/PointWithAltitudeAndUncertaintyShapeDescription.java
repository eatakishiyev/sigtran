/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.AltitudeSign;
import dev.ocean.isup.enums.LatitudeSign;
import dev.ocean.isup.enums.TypeOfShape;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class PointWithAltitudeAndUncertaintyShapeDescription extends AbstractShapeDescription {

    private LatitudeSign latitudeSign;
    private int latitudeDegrees;
    private int longitudeDegrees;

    private int uncertaintyCode;

    private AltitudeSign signOfAltitude;
    private int altitude;

    private int uncertaintyCode2;

    private int confidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PointWithAltitudeAndUncertaintyShapeDescription[")
                .append(";LatitudeSign = ").append(latitudeSign)
                .append(";LatitudeDegrees = ").append(latitudeDegrees)
                .append(";LongitudeDegrees = ").append(longitudeDegrees)
                .append(";UncertaintyCode = ").append(uncertaintyCode)
                .append(";SignOfAltitude = ").append(signOfAltitude)
                .append(";Altitude = ").append(altitude)
                .append(";UncertaintyCode2 = ").append(uncertaintyCode2)
                .append(";Confidence = ").append(confidence)
                .append("]");
        return sb.toString();
    }

    public PointWithAltitudeAndUncertaintyShapeDescription() {
        super();
    }

    public PointWithAltitudeAndUncertaintyShapeDescription(int uncertaintyCode, AltitudeSign signOfAltitude, int altitude, int uncertaintyCode2, int confidence, LatitudeSign latitudeSign, int latitudeDegrees, int longituteDegrees) {
        this.latitudeSign = latitudeSign;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longituteDegrees;
        this.uncertaintyCode = uncertaintyCode;
        this.signOfAltitude = signOfAltitude;
        this.altitude = altitude;
        this.uncertaintyCode2 = uncertaintyCode2;
        this.confidence = confidence;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) {
        int b = latitudeSign.value();
        baos.write((b << 7) | ((this.latitudeDegrees >> 16) & 0b01111111));
        baos.write((this.latitudeDegrees >> 8) & 0b11111111);
        baos.write(this.latitudeDegrees & 0b11111111);
//        
        baos.write(this.longitudeDegrees >> 16);
        baos.write((this.longitudeDegrees >> 8) & 0b11111111);
        baos.write(this.longitudeDegrees & 0b11111111);

        baos.write(this.uncertaintyCode & 0b01111111);
        baos.write((this.signOfAltitude.value() << 7) | ((this.altitude >> 8) & 0b01111111));
        baos.write(this.altitude & 0b11111111);
        baos.write(this.uncertaintyCode2 & 0b01111111);
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

        this.uncertaintyCode = bais.read() & 0b01111111;

        b = bais.read() & 0xFF;
        this.signOfAltitude = AltitudeSign.getInstance(b >> 7);
        this.altitude = b & 0b01111111;
        this.altitude = (altitude << 8) | (bais.read() & 0xFF);

        this.uncertaintyCode2 = bais.read() & 0b01111111;
        this.confidence = bais.read() & 0b01111111;

    }

    public int getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public LatitudeSign getLatitudeSign() {
        return latitudeSign;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLatitudeDegrees(int latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public void setLatitudeSign(LatitudeSign latitudeSign) {
        this.latitudeSign = latitudeSign;
    }

    public void setLongitudeDegrees(int longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public void setSignOfAltitude(AltitudeSign signOfAltitude) {
        this.signOfAltitude = signOfAltitude;
    }

    public void setUncertaintyCode(int uncertaintyCode) {
        this.uncertaintyCode = uncertaintyCode;
    }

    public void setUncertaintyCode2(int uncertaintyCode2) {
        this.uncertaintyCode2 = uncertaintyCode2;
    }

    /**
     * @return the uncertaintyCode
     */
    public int getUncertaintyCode() {
        return uncertaintyCode;
    }

    /**
     * @return the signOfAltitude
     */
    public AltitudeSign getSignOfAltitude() {
        return signOfAltitude;
    }

    /**
     * @return the altitude
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     * @return the uncertaintyCode2
     */
    public int getUncertaintyCode2() {
        return uncertaintyCode2;
    }

    /**
     * @return the confidence
     */
    public int getConfidence() {
        return confidence;
    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.POINT_WITH_ALTITUDE_AND_UNCERTAINTY;
    }

}
