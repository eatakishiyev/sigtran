package dev.ocean.sigtran.map.parameters.location.information;

import dev.ocean.isup.enums.LatitudeSign;
import java.io.IOException;
import java.io.Serializable;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * GeographicalInformation::= OCTET STRING (SIZE (8)) -- Refers to geographical
 * Information defined in 3GPP TS 23.032. -- Only the description of an
 * ellipsoid point with uncertainty circle -- as specified in 3GPP TS 23.032 is
 * allowed to be used -- The internal structure according to 3GPP TS 23.032 is
 * as follows: -- Type of shape (ellipsoid point with uncertainty circle) 1
 * octet -- Degrees of Latitude 3 octets -- Degrees of Longitude 3 octets --
 * Uncertainty code 1 octet
 *
 * @author eatakishiyev
 */
public class GeographicalInformation implements Serializable {

    private final int TYPE_OF_SHAPE = 0b00000001;
    private LatitudeSign signOfLatitude;
    private int degreesOfLatitude;
    private int degreesOfLongitude;
    private int uncertaintyCode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeographicalInformation[")
                .append("SignOfLatitude = ").append(signOfLatitude).append("\n")
                .append("DegreesOfLatitude = ").append(degreesOfLatitude).append("\n")
                .append("DegreesOfLongitude = ").append(degreesOfLongitude).append("\n")
                .append("UncertaintyCode = ").append(uncertaintyCode).append("\n")
                .append("]");
        return sb.toString();
    }

    public GeographicalInformation() {
    }

    public GeographicalInformation(LatitudeSign signOfLatitude, int degreesOfLatitude, int degreesOfLongitude, int uncertaintyCode) {
        this.signOfLatitude = signOfLatitude;
        this.degreesOfLatitude = degreesOfLatitude;
        this.degreesOfLongitude = degreesOfLongitude;
        this.uncertaintyCode = uncertaintyCode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        byte[] tmpBuf = new byte[8];

        tmpBuf[0] = (TYPE_OF_SHAPE << 4);

        byte tmp = (byte) ((signOfLatitude.value() << 7) | ((this.degreesOfLatitude >> 16) & 0b01111111));
        tmpBuf[1] = tmp;
        tmpBuf[2] = (byte) ((degreesOfLatitude >> 8) & 0b11111111);
        tmpBuf[3] = (byte) (degreesOfLatitude & 0b11111111);

        tmpBuf[4] = (byte) ((this.degreesOfLongitude >> 16) & 0b11111111);
        tmpBuf[5] = (byte) ((this.degreesOfLongitude >> 8) & 0b11111111);
        tmpBuf[6] = (byte) (this.degreesOfLongitude & 0b11111111);

        tmpBuf[7] = (byte) (this.uncertaintyCode & 0b01111111);

        try {
            aos.writeOctetString(tagClass, tag, tmpBuf);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            byte[] tmpBuf = new byte[length];
            ais.read(tmpBuf);
            int octet1 = tmpBuf[0] & 0xFF;
//
            int octet2 = tmpBuf[1] & 0xFF;
            this.signOfLatitude = LatitudeSign.getInstance((octet2 >> 7) & 0b00000001);
//
            this.degreesOfLatitude = octet2 & 0b01111111;
            this.degreesOfLatitude = (degreesOfLatitude << 8) | (tmpBuf[2] & 0b11111111);
            this.degreesOfLatitude = (degreesOfLatitude << 8) | (tmpBuf[3] & 0b11111111);
//        
            this.degreesOfLongitude = tmpBuf[4] & 0b11111111;
            this.degreesOfLongitude = (degreesOfLongitude << 8) | (tmpBuf[5] & 0b11111111);
            this.degreesOfLongitude = (degreesOfLongitude << 8) | (tmpBuf[6] & 0b11111111);

//            this.uncertaintyCode = tmpBuf[7] & 0b01111111;
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);

        }
    }

    /**
     * @return the signOfLatitude
     */
    public LatitudeSign getSignOfLatitude() {
        return signOfLatitude;
    }

    /**
     * @return the degreesOfLatitude
     */
    public int getDegreesOfLatitude() {
        return degreesOfLatitude;
    }

    /**
     * @return the degreesOfLongitude
     */
    public int getDegreesOfLongitude() {
        return degreesOfLongitude;
    }

    /**
     * @return the uncertaintyCode
     */
    public int getUncertaintyCode() {
        return uncertaintyCode;
    }

}
