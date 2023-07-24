/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.generic;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.FacilityNotSupParam;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class FacilityNotSupported implements MAPUserError {

    private FacilityNotSupParam facilityNotSupportedParam;

    public FacilityNotSupported() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.facilityNotSupportedParam != null) {
            facilityNotSupportedParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.facilityNotSupportedParam = new FacilityNotSupParam();
                facilityNotSupportedParam.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.FACILITY_NOT_SUPPORTED;
    }

    public FacilityNotSupParam getFacilityNotSupportedParam() {
        return facilityNotSupportedParam;
    }

    public void setFacilityNotSupportedParam(FacilityNotSupParam facilityNotSupportedParam) {
        this.facilityNotSupportedParam = facilityNotSupportedParam;
    }

    @Override
    public String toString() {
        return "FacilityNotSupported{" +
                "facilityNotSupportedParam=" + facilityNotSupportedParam +
                '}';
    }
}
