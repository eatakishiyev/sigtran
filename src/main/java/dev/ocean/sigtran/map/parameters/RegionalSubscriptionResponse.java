/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * RegionalSubscriptionResponse ::= ENUMERATED {
 * networkNode-AreaRestricted (0),
 * tooManyZoneCodes (1),
 * zoneCodesConflict (2),
 * regionalSubscNotSupported (3)}
 * @author eatakishiyev
 */
public class RegionalSubscriptionResponse {

    private RegionalSubscriptionResponses regionalSubscriptionResponseEnum;

    public RegionalSubscriptionResponse() {
    }

    public RegionalSubscriptionResponse(RegionalSubscriptionResponses regionalSubscriptionResponseEnum) {
        this.regionalSubscriptionResponseEnum = regionalSubscriptionResponseEnum;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, regionalSubscriptionResponseEnum.value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.regionalSubscriptionResponseEnum = RegionalSubscriptionResponses.getInstance((int) ais.readInteger());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the regionalSubscriptionResponseEnum
     */
    public RegionalSubscriptionResponses getRegionalSubscriptionResponseEnum() {
        return regionalSubscriptionResponseEnum;
    }

    /**
     * @param regionalSubscriptionResponseEnum the
     * regionalSubscriptionResponseEnum to set
     */
    public void setRegionalSubscriptionResponseEnum(RegionalSubscriptionResponses regionalSubscriptionResponseEnum) {
        this.regionalSubscriptionResponseEnum = regionalSubscriptionResponseEnum;
    }

    public enum RegionalSubscriptionResponses {

        networkNodeAreaRestricted(0),
        tooManyZoneCodes(1),
        zoneCodesConflict(2),
        regionalSubscNotSupported(3);
        private final int value;

        private RegionalSubscriptionResponses(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static RegionalSubscriptionResponses getInstance(int value) {
            switch (value) {
                case 0:
                    return networkNodeAreaRestricted;
                case 1:
                    return tooManyZoneCodes;
                case 2:
                    return zoneCodesConflict;
                case 3:
                    return regionalSubscNotSupported;
                default:
                    return null;
            }
        }
    }
}
