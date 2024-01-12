/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.TeleservNotProvParam;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
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

    @Override
    public String toString() {
        return "TeleServiceNotProvisioned{" +
                "teleservNotProvParam=" + teleservNotProvParam +
                '}';
    }
}
