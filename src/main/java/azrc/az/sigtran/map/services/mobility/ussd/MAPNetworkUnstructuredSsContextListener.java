/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.ussd;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPNetworkUnstructuredSsContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.NETWORK_UNSTRUCTURED_SS_CONTEXT};
    }

    public  void onMAPProcessUnstructuredSsRequestIndication(Short invokeID, UssdArg ussdArg,
                                                             MAPNetworkUnstructuredSsDialogue mapDialogue){}
    public  void onMAPUnstructuredSsRequestIndication(Short invokeID, UssdArg ussdArg,
                                                              MAPNetworkUnstructuredSsDialogue mapDialogue){}

    public  void onMAPProcessUnstructuredSsRequestConfirmation(Short invokeId, UssdRes ussdRes,
                                                               MAPNetworkUnstructuredSsDialogue mapDialogue,
                                                               MAPUserError userError, ProviderError providerError) {}
    public  void onMAPUnstructuredSsRequestConfirmation(Short invokeId, UssdRes ussdRes,
                                                       MAPNetworkUnstructuredSsDialogue mapDialogue,
                                                       ProviderError providerError, MAPUserError userError) {}

    public  void onMAPUnstructuredSsNotifyIndication(Short invokeID, UssdArg ussdArg,
                                                    MAPNetworkUnstructuredSsDialogue mapDialogue) {}

    public  void onMAPUnstructuredSsNotifyConfirmation(Short invokeId, MAPNetworkUnstructuredSsDialogue mapDialogue,
                                                      ProviderError providerError, MAPUserError userError) {}

}
