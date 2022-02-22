/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

import java.io.IOException;

/**
 * @author eatakishiyev
 */
public interface ITimeDurationCharging {

    /**
     * @return the maxCallPeriodDuration
     * maxCallPeriodDuration is measured in100 millisecond units.
     */
    public int getMaxCallPeriodDuration();

    /**
     * @param maxCallPeriodDuration the maxCallPeriodDuration to set
     *                              maxCallPeriodDuration is measured in100 millisecond units.
     */
    public void setMaxCallPeriodDuration(int maxCallPeriodDuration);

    /**
     * @return the tariffSwitchInterval
     */
    public Integer getTariffSwitchInterval();

    /**
     * @param tariffSwitchInterval the tariffSwitchInterval to set
     */
    public void setTariffSwitchInterval(Integer tariffSwitchInterval);

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException;

}
