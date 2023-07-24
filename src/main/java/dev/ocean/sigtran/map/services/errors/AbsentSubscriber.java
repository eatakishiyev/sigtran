 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.AbsentSubscriberParam;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * absentSubscriber ERROR ::= {
 * PARAMETER
 * AbsentSubscriberParam
 * -- optional
 * -- AbsentSubscriberParam must not be used in version <3
 * CODE local:27 }
 *
 * @author eatakishiyev
 */
public class AbsentSubscriber implements MAPUserError {

    private AbsentSubscriberParam absentSubscriberParam;

    public AbsentSubscriber() {
    }

    public AbsentSubscriber(AbsentSubscriberParam absentSubscriberParam) {
        this.absentSubscriberParam = absentSubscriberParam;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (absentSubscriberParam != null) {
            absentSubscriberParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.absentSubscriberParam = new AbsentSubscriberParam();
        absentSubscriberParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ABSENT_SUBSCRIBER;
    }

    /**
     * @return the absentSubscriberSmParam
     */
    public AbsentSubscriberParam getAbsentSubscriberParam() {
        return absentSubscriberParam;
    }

    /**
     * @param absentSubscriberParam the absentSubscriberParam to set
     */
    public void setAbsentSubscriberParam(AbsentSubscriberParam absentSubscriberParam) {
        this.absentSubscriberParam = absentSubscriberParam;
    }

    @Override
    public String toString() {
        return "AbsentSubscriber{" +
                "absentSubscriberParam=" + absentSubscriberParam +
                '}';
    }
}
