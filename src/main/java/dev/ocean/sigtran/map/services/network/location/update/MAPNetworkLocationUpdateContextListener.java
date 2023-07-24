/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.network.location.update;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 *
 * @author eatakishiyev
 */
public abstract class MAPNetworkLocationUpdateContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.NETWORK_LOCUP_CONTEXT};
    }

    public  void onMAPUpdateLocationConfirmation(Short invokeId, UpdateLocationRes updateLocationRes,
                                                MAPNetworkLocationUpdateDialogue mapDialogue, MAPUserError mapUserError,
                                                ProviderError providerError){}

    public  void onMAPUpdateLocationIndication(Short invokeID, UpdateLocationArg updateLocationArg,
                                              MAPNetworkLocationUpdateDialogue mapDialogue){}

    public  void onMAPInsertSubscriberDataConfirmation(Short invokeId, InsertSubscriberDataRes insertSubscriberDataRes,
                                                      MAPNetworkLocationUpdateDialogue mapDialogue,
                                                       ProviderError providerError, MAPUserError userError){}

    public  void onMAPInsertSubscriberDataIndication(Short invokeId, InsertSubscriberDataArg insertSubscriberDataArg,
                                                    MAPNetworkLocationUpdateDialogue mapDialogue){}

}
