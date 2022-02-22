/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.ReceivingSideID;
import dev.ocean.sigtran.cap.parameters.TimeInformation;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface ITimeDurationChargingResult {

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException;

    public void encode(AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException;

    /**
     * @return the partyToCharge
     */
    public ReceivingSideID getPartyToCharge();

    /**
     * @return the timeInformation
     */
    public TimeInformation getTimeInformation();

    /**
     * @return the legActive
     */
    public boolean isLegActive();

    /**
     * @param legActive the legActive to set
     */
    public void setLegActive(boolean legActive);

    /**
     * @param partyToCharge the partyToCharge to set
     */
    public void setPartyToCharge(ReceivingSideID partyToCharge);

    /**
     * @param timeInformation the timeInformation to set
     */
    public void setTimeInformation(TimeInformation timeInformation);

}
