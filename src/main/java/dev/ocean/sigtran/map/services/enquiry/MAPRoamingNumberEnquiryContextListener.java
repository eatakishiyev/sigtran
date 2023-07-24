/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.enquiry;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPRoamingNumberEnquiryContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.ROAMING_NUMBER_ENQUIRY_CONTEXT};
    }

    public  void onMAPProvideRoamingNumberIndication(Short invokeId, ProvideRoamingNumberArg provideRoamingNumber,
                                                             MAPRoamingNumberEnquiryDialogue mapDialogue){}

    public  void onMAPProvideRoamingNumberConfirmation(Short invokeId, ProvideRoamingNumberRes provideRoamingNumberRes,
                                                               MAPRoamingNumberEnquiryDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError){}

}
