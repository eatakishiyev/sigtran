/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

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
