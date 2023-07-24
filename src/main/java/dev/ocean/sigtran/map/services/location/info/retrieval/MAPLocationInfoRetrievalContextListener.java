/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.location.info.retrieval;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPLocationInfoRetrievalContextListener extends MAPListener {




    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.LOCATION_INFO_RETRIEVAL_CONTEXT};
    }

    public  void onMAPSendRoutingInfoIndication(Short invokeID, SendRoutingInfoArg sendRoutingInfoArg,
                                                        MAPLocationInfoRetrievalDialogue mapDialogue){}

    public  void onMAPSendRoutingInfoConfirmation(Short invokeId, SendRoutingInfoRes sendRoutingInfoRes,
                                                          MAPLocationInfoRetrievalDialogue mapDialogue, MAPUserError userError,
                                                          ProviderError providerError){}

}
