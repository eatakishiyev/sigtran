/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.AbsentSubscriberSmParam;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * absentSubscriberSM ERROR ::= {
 * PARAMETER
 * AbsentSubscriberSM-Param
 * -- optional
 * CODE local:6 }
 * @author eatakishiyev
 */
public class AbsentSubscriberSM implements MAPUserError {

    private AbsentSubscriberSmParam absentSubscriberSmParam;

    public AbsentSubscriberSM() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (absentSubscriberSmParam != null) {
            absentSubscriberSmParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.absentSubscriberSmParam = new AbsentSubscriberSmParam();
        this.absentSubscriberSmParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ABSENT_SUBSCRIBER_SM;
    }

    /**
     * @return the absentSubscriberSmParam
     */
    public AbsentSubscriberSmParam getAbsentSubscriberSmParam() {
        return absentSubscriberSmParam;
    }

    /**
     * @param absentSubscriberSmParam the absentSubscriberSmParam to set
     */
    public void setAbsentSubscriberSmParam(AbsentSubscriberSmParam absentSubscriberSmParam) {
        this.absentSubscriberSmParam = absentSubscriberSmParam;
    }

    @Override
    public String toString() {
        return "AbsentSubscriberSM{" +
                "absentSubscriberSmParam=" + absentSubscriberSmParam +
                '}';
    }
}
