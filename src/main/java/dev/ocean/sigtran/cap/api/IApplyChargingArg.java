/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.cap.parameters.SendingSideID;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface IApplyChargingArg extends CAPMessage{

    /**
     * @return the aChBillingChargingCharacteristics
     */
    public AChBillingChargingCharacteristics getaChBillingChargingCharacteristics();

    /**
     * @return the partyToCharge
     */
    public SendingSideID getPartyToCharge();

    public void setPartyToCharge(SendingSideID partyToCharge);

    /**
     * @return the extensions
     */
    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException;

    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException;

}
