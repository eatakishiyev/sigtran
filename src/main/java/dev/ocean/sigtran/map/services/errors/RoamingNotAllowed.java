/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.map.services.errors.params.RoamingNotAllowedParam;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * roamingNotAllowed ERROR ::= {
 * PARAMETER
 * RoamingNotAllowedParam
 * CODE local:8 }
 * @author eatakishiyev
 */
public class RoamingNotAllowed implements MAPUserError {

    private RoamingNotAllowedParam roamingNotAllowedParam;

    public RoamingNotAllowed() {
    }

    public RoamingNotAllowed(RoamingNotAllowedParam roamingNotAllowedParam) {
        this.roamingNotAllowedParam = roamingNotAllowedParam;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.roamingNotAllowedParam != null) {
            roamingNotAllowedParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.roamingNotAllowedParam = new RoamingNotAllowedParam();
        roamingNotAllowedParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ROAMING_NOT_ALLOWED;
    }

    public RoamingNotAllowedParam getRoamingNotAllowedParam() {
        return roamingNotAllowedParam;
    }

    public void setRoamingNotAllowedParam(RoamingNotAllowedParam roamingNotAllowedParam) {
        this.roamingNotAllowedParam = roamingNotAllowedParam;
    }

}
