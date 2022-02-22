/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.roaming.number.enquiry;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPRoamingNumberEnquiryContextListener extends MAPListener {

    public void onMAPProvideRoamingNumberIndication(Short invokeId, ProvideRoamingNumberArg provideRoamingNumber,
            MAPRoamingNumberEnquiryDialogue mapDialogue);

    public void onMAPProvideRoamingNumberConfirmation(Short invokeId, ProvideRoamingNumberRes provideRoamingNumberRes,
            MAPRoamingNumberEnquiryDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError);

}
