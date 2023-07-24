/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.location.cancellation;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPLocationCancellationContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT};
    }

    public  void onMAPCancelLocationIndication(Short invokeID, CancelLocationArg cancelLocationArg,
                                                       MAPLocationCancellationDialogue mapDialogue){}

    public  void onMAPCancelLocationConfirmation(Short invokeId, CancelLocationRes cancelLocationRes,
                                                         MAPLocationCancellationDialogue mapDialogue,
                                                 MAPUserError userError, ProviderError providerError){}
}
