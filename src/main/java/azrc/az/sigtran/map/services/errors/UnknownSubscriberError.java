/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.UnknownSubscriberParam;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * unknownSubscriber ERROR ::= {
 * PARAMETER
 * UnknownSubscriberParam
 * -- optional
 * -- UnknownSubscriberParam must not be used in version <3
 * CODE local:1 }
 *
 * @author eatakishiyev
 */
public class UnknownSubscriberError implements MAPUserError {

    private UnknownSubscriberParam unknownSubscriberParam;

    public UnknownSubscriberError() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (unknownSubscriberParam != null) {
            unknownSubscriberParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.unknownSubscriberParam = new UnknownSubscriberParam();
        unknownSubscriberParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.UNKNOWN_SUBSCRIBER;
    }

    @Override
    public String toString() {
        return "UnknownSubscriberError{" +
                "unknownSubscriberParam=" + unknownSubscriberParam +
                '}';
    }
}
