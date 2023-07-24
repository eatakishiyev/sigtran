/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import dev.ocean.sigtran.map.services.errors.params.IllegalSubscriberParam;
import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * illegalSubscriber ERROR ::= {
 * PARAMETER
 * IllegalSubscriberParam
 * -- optional
 * -- IllegalSubscriberParam must not be used in version <3
 * CODE local:9 }
 * @author eataki
 * shiyev
 */
public class IllegalSubscriber implements MAPUserError {

    private IllegalSubscriberParam illegalSubscriberParam;

    public IllegalSubscriber() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (illegalSubscriberParam != null) {
            illegalSubscriberParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.illegalSubscriberParam = new IllegalSubscriberParam();
        illegalSubscriberParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ILLEGAL_SUBSCRIBER;
    }

    public IllegalSubscriberParam getIllegalSubscriberParam() {
        return illegalSubscriberParam;
    }

    public void setIllegalSubscriberParam(IllegalSubscriberParam illegalSubscriberParam) {
        this.illegalSubscriberParam = illegalSubscriberParam;
    }

    @Override
    public String toString() {
        return "IllegalSubscriber{" +
                "illegalSubscriberParam=" + illegalSubscriberParam +
                '}';
    }
}
