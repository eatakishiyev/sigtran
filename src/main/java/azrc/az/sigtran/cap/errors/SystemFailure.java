/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.errors;

import azrc.az.sigtran.cap.parameters.UnavailableNetworkResource;
import azrc.az.sigtran.cap.CAPUserErrorCodes;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class SystemFailure extends CAPUserError {

    private UnavailableNetworkResource unavailableNetworkResource;

    public SystemFailure() {
    }

    public SystemFailure(UnavailableNetworkResource unavailableNetworkResource) {
        this.unavailableNetworkResource = unavailableNetworkResource;
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            ais.readTag();
            this.unavailableNetworkResource = UnavailableNetworkResource.getInstance((int) ais.readInteger());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws Exception {
        aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, unavailableNetworkResource.value());
    }

    @Override
    public CAPUserErrorCodes getErrorCode() {
        return CAPUserErrorCodes.SYSTEM_FAILURE;
    }

    /**
     * @return the unavailableNetworkResource
     */
    public UnavailableNetworkResource getUnavailableNetworkResource() {
        return unavailableNetworkResource;
    }

}
