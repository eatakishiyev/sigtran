/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.map.services.errors.params.ForwardingViolationParam;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * forwardingViolation ERROR ::= {
 * PARAMETER
 * ForwardingViolationParam
 * -- optional
 * CODE local:14 }
 *
 * @author eatakishiyev
 */
public class ForwardingViolation implements MAPUserError {

    private ForwardingViolationParam forwardingViolationParam;

    public ForwardingViolation() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (forwardingViolationParam != null) {
            forwardingViolationParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.forwardingViolationParam = new ForwardingViolationParam();
        forwardingViolationParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.FORWARDING_VIOLATION;
    }

    public ForwardingViolationParam getForwardingViolationParam() {
        return forwardingViolationParam;
    }

    public void setForwardingViolationParam(ForwardingViolationParam forwardingViolationParam) {
        this.forwardingViolationParam = forwardingViolationParam;
    }

}
