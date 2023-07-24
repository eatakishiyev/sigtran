/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.CallBarredParam;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * callBarred ERROR ::= {
 * PARAMETER
 * CallBarredParam
 * -- optional
 * CODE local:13 }
 * @author eatakishiyev
 */
public class CallBarred implements MAPUserError {

    private CallBarredParam callBarredParam;

    public CallBarred() {
    }

    public CallBarred(CallBarredParam callBarredParam) {
        this.callBarredParam = callBarredParam;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if(callBarredParam != null){
            callBarredParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.callBarredParam = new CallBarredParam();
        callBarredParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.CALL_BARRED;
    }

    /**
     * @return the callBarredParam
     */
    public CallBarredParam getCallBarredParam() {
        return callBarredParam;
    }

    /**
     * @param callBarredParam the callBarredParam to set
     */
    public void setCallBarredParam(CallBarredParam callBarredParam) {
        this.callBarredParam = callBarredParam;
    }

    @Override
    public String toString() {
        return "CallBarred{" +
                "callBarredParam=" + callBarredParam +
                '}';
    }
}
