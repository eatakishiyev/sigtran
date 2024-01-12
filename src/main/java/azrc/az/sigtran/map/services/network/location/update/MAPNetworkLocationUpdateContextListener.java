/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.network.location.update;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

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
