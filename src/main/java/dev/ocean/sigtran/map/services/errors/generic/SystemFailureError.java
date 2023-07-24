/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.generic;

import dev.ocean.sigtran.map.services.errors.params.SystemFailureParam;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * systemFailure ERROR ::= {
 * PARAMETER
 * SystemFailureParam
 * -- optional
 * CODE local:34 }
 * @author eatakishiyev
 */
public class SystemFailureError implements MAPUserError {

    private SystemFailureParam systemFailureParam;

    public SystemFailureError() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (systemFailureParam != null) {
            systemFailureParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.systemFailureParam = new SystemFailureParam();
        systemFailureParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.SYSTEM_FAILURE;
    }

    /**
     * @return the systemFailureParam
     */
    public SystemFailureParam getSystemFailureParam() {
        return systemFailureParam;
    }

    /**
     * @param systemFailureParam the systemFailureParam to set
     */
    public void setSystemFailureParam(SystemFailureParam systemFailureParam) {
        this.systemFailureParam = systemFailureParam;
    }

    @Override
    public String toString() {
        return "SystemFailureError{" +
                "systemFailureParam=" + systemFailureParam +
                '}';
    }
}
