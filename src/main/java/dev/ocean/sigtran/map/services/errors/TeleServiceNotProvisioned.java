/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.TeleservNotProvParam;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * teleserviceNotProvisioned ERROR ::= {
 * PARAMETER
 * TeleservNotProvParam
 * -- optional
 * -- TeleservNotProvParam must not be used in version <3
 * CODE local:11 }
 *
 * @author eatakishiyev
 */
public class TeleServiceNotProvisioned implements MAPUserError {

    private TeleservNotProvParam teleservNotProvParam;

    public TeleServiceNotProvisioned() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (teleservNotProvParam != null) {
            teleservNotProvParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.setTeleservNotProvParam(new TeleservNotProvParam());
        getTeleservNotProvParam().decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.TELESERVICE_NOT_PROVISIONED;
    }

    /**
     * @return the teleservNotProvParam
     */
    public TeleservNotProvParam getTeleservNotProvParam() {
        return teleservNotProvParam;
    }

    /**
     * @param teleservNotProvParam the teleservNotProvParam to set
     */
    public void setTeleservNotProvParam(TeleservNotProvParam teleservNotProvParam) {
        this.teleservNotProvParam = teleservNotProvParam;
    }
}
