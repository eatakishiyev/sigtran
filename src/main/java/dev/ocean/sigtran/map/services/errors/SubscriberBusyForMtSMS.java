/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.SubBusyForMTSMSParam;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * subscriberBusyForMT-SMS ERROR ::= {
 * PARAMETER
 * SubBusyForMT-SMS-Param
 * -- optional
 * CODE local:31 }
 * @author eatakishiyev
 */
public class SubscriberBusyForMtSMS implements MAPUserError {

    private SubBusyForMTSMSParam subBusyForMTSMSParam;

    public SubscriberBusyForMtSMS() {
    }

    public SubscriberBusyForMtSMS(SubBusyForMTSMSParam subBusyForMTSMSParam) {
        this.subBusyForMTSMSParam = subBusyForMTSMSParam;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (subBusyForMTSMSParam != null) {
            subBusyForMTSMSParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.subBusyForMTSMSParam = new SubBusyForMTSMSParam();
        subBusyForMTSMSParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.SUBSCRIBER_BUSY_FOR_MT_SMS;
    }

    /**
     * @return the subBusyForMTSMSParam
     */
    public SubBusyForMTSMSParam getSubBusyForMTSMSParam() {
        return subBusyForMTSMSParam;
    }

    /**
     * @param subBusyForMTSMSParam the subBusyForMTSMSParam to set
     */
    public void setSubBusyForMTSMSParam(SubBusyForMTSMSParam subBusyForMTSMSParam) {
        this.subBusyForMTSMSParam = subBusyForMTSMSParam;
    }
}
