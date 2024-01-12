/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.TypeOfShape;
import azrc.az.isup.enums.LatitudeSign;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class EllipsoidPointWithUncertaintyShapeDescription extends AbstractShapeDescription {

    private LatitudeSign latitudeSign;
    private int latitudeDegrees;
    private int longitudeDegrees;
    private int uncertaintyCode;
    private int confidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EllipsoidPointWithUncertaintyShapeDescription[")
                .append("LatitudeSign = ").append(latitudeSign)
                .append(";LatitudeDegrees = ").append(latitudeDegrees)
                .append(";LongitudeDegrees = ").append(longitudeDegrees)
                .append(";UncertaintyCode = ").append(uncertaintyCode)
                .append(";Confidence = ").append(confidence)
                .append("]");
        return sb.toString();
    }

    public EllipsoidPointWithUncertaintyShapeDescription() {
        super();
    }

    public EllipsoidPointWithUncertaintyShapeDescription(LatitudeSign latitudeSign, int latitudeDegrees, int longitudeDegrees, int uncertaintyCode, int confidence) {
        this.latitudeSign = latitudeSign;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longitudeDegrees;
        this.uncertaintyCode = uncertaintyCode;
        this.confidence = confidence;
    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.ELLIPSOID_POINT_WITH_UNCERTAINTY;
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

        baos.write(this.uncertaintyCode & 0b01111111);
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
        this.confidence = bais.read() & 0b01111111;
    }

    /**
     * @return the uncertaintyCode
     */
    public int getUncertaintyCode() {
        return uncertaintyCode;
    }

    /**
     * @return the confidence
     */
    public int getConfidence() {
        return confidence;
    }

    public int getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public LatitudeSign getLatitudeSign() {
        return latitudeSign;
    }

    public void setLatitudeDegrees(int latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public int getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLatitudeSign(LatitudeSign latitudeSign) {
        this.latitudeSign = latitudeSign;
    }

    public void setLongitudeDegrees(int longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public void setUncertaintyCode(int uncertaintyCode) {
        this.uncertaintyCode = uncertaintyCode;
    }

}
