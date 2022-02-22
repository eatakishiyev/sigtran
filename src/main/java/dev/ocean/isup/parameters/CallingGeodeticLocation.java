/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.LocationPresentationRestrictedIndicator;
import dev.ocean.isup.enums.LocationScreeningIndicator;
import dev.ocean.isup.enums.TypeOfShape;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */


public class CallingGeodeticLocation implements IsupParameter {

    private LocationPresentationRestrictedIndicator lpri;
    private LocationScreeningIndicator screening;
    private AbstractShapeDescription shapeDescription;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallingGeodeticLocation[")
                .append("LocationPresentationRestrictedIndicator = ").append(lpri)
                .append("LocationScreeningIndicator = ").append(screening)
                .append("ShapeDescription = ").append(shapeDescription)
                .append("]");
        return sb.toString();
    }

    public CallingGeodeticLocation() {
    }

    public CallingGeodeticLocation(LocationPresentationRestrictedIndicator lpri, LocationScreeningIndicator screening, AbstractShapeDescription shapeDescription) {
        this.lpri = lpri;
        this.screening = screening;
        this.shapeDescription = shapeDescription;
    }

    @Override
    public byte[] encode() throws IncorrectSyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b = 0;//spare
        b = b << 4;//spare

        b = (b << 2) | (lpri.value() & 0b00000011);

        int ext;

        //NOTE – If the Geodetic Location parameter is included and the LPRI indicates location not available, 
        //octets 3 to n are omitted, the subfield (c) is coded with 0000000 and the subfield (b) is coded 11. 
        if (lpri == LocationPresentationRestrictedIndicator.LOCATION_NOT_AVAILABLE) {
            b = (b << 2) | (LocationScreeningIndicator.NETWORK_PROVIDED.value() & 0b00000011);
            baos.write(b);

            ext = 1;
            b = (ext << 7) | (TypeOfShape.ELLIPSOID_POINT.value());
            baos.write(b);

        } else {
            b = (b << 2) | (screening.value() & 0b00000011);
            baos.write(b);

            ext = 0;
            b = (ext << 7) | shapeDescription.getType().value();
            baos.write(b);

            shapeDescription.encode(baos);
        }
//        

        return baos.toByteArray();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            int b = 0;//spare
            b = b << 4;//spare

            b = (b << 2) | (lpri.value() & 0b00000011);

            int ext;

            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);

            //NOTE – If the Geodetic Location parameter is included and the LPRI indicates location not available, 
            //octets 3 to n are omitted, the subfield (c) is coded with 0000000 and the subfield (b) is coded 11. 
            if (lpri == LocationPresentationRestrictedIndicator.LOCATION_NOT_AVAILABLE) {
                b = (b << 2) | (LocationScreeningIndicator.NETWORK_PROVIDED.value() & 0b00000011);
                baos.write(b);

                ext = 1;
                b = (ext << 7) | (TypeOfShape.ELLIPSOID_POINT.value());
                baos.write(b);

            } else {
                b = (b << 2) | (screening.value() & 0b00000011);
                baos.write(b);

                ext = 0;
                b = (ext << 7) | shapeDescription.getType().value();
                baos.write(b);

                shapeDescription.encode(baos);
            }
//        
            aos.writeOctetString(tagClass, tag, baos.toByteArray());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(ais.readOctetString());

            int b = bais.read() & 0xFF;

            this.screening = LocationScreeningIndicator.getInstance(b & 0b00000011);
            this.lpri = LocationPresentationRestrictedIndicator.getInstance((b >> 2) & 0b00000011);

            b = bais.read() & 0xFF;
            int ext = (b >> 7) & 0b00000001;
            TypeOfShape typeOfShape = TypeOfShape.getInstance(b & 0b01111111);
            switch (typeOfShape) {
                case ELLIPSOID_POINT:
                    this.shapeDescription = new EllipsoidPointShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
                case ELLIPSOID_POINT_WITH_UNCERTAINTY:
                    this.shapeDescription = new EllipsoidPointWithUncertaintyShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
                case POINT_WITH_ALTITUDE_AND_UNCERTAINTY:
                    this.shapeDescription = new PointWithAltitudeAndUncertaintyShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
                case ELLIPSE_ON_THE_ELLOPSOID:
                    this.shapeDescription = new EllipseOnEllipsoidShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
                case ELLIPSOID_CIRCLE_SECTOR:
                    this.shapeDescription = new EllipsoidCircleSectorShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
                case POLYGON:
                    this.shapeDescription = new PolygonShapeDescription();
                    this.shapeDescription.decode(bais);
                    break;
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {

        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        int b = bais.read() & 0xFF;

        this.screening = LocationScreeningIndicator.getInstance(b & 0b00000011);
        this.lpri = LocationPresentationRestrictedIndicator.getInstance((b >> 2) & 0b00000011);

        b = bais.read() & 0xFF;
        int ext = (b >> 7) & 0b00000001;
        TypeOfShape typeOfShape = TypeOfShape.getInstance(b & 0b01111111);
        switch (typeOfShape) {
            case ELLIPSOID_POINT:
                this.shapeDescription = new EllipsoidPointShapeDescription();
                this.shapeDescription.decode(bais);
                break;
            case ELLIPSOID_POINT_WITH_UNCERTAINTY:
                this.shapeDescription = new EllipsoidPointWithUncertaintyShapeDescription();
                this.shapeDescription.decode(bais);
                break;
            case POINT_WITH_ALTITUDE_AND_UNCERTAINTY:
                this.shapeDescription = new PointWithAltitudeAndUncertaintyShapeDescription();
                this.shapeDescription.decode(bais);
                break;
            case ELLIPSE_ON_THE_ELLOPSOID:
                this.shapeDescription = new EllipseOnEllipsoidShapeDescription();
                this.shapeDescription.decode(bais);
                break;
            case ELLIPSOID_CIRCLE_SECTOR:
                this.shapeDescription = new EllipsoidCircleSectorShapeDescription();
                this.shapeDescription.decode(bais);
                break;
            case POLYGON:
                this.shapeDescription = new PolygonShapeDescription();
                this.shapeDescription.decode(bais);
                break;
        }

    }

    /**
     * @return the lpri
     */
    public LocationPresentationRestrictedIndicator getLpri() {
        return lpri;
    }

    /**
     * @return the screening
     */
    public LocationScreeningIndicator getScreening() {
        return screening;
    }

    /**
     * @return the shapeDescription
     */
    public AbstractShapeDescription getShapeDescription() {
        return shapeDescription;
    }

    public void setShapeDescription(AbstractShapeDescription shapeDescription) {
        this.shapeDescription = shapeDescription;
    }

    @Override
    public int getParameterCode() {
        return CALLING_GEODETIC_LOCATION;
    }

}
