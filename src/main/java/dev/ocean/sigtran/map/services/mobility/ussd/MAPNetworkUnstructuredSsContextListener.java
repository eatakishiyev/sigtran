/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.ussd;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPNetworkUnstructuredSsContextListener extends MAPListener {

    public void onMAPProcessUnstructuredSsRequestIndication(Short invokeID, UssdArg ussdArg, MAPNetworkUnstructuredSsDialogue mapDialogue);

    public void onMAPUnstructuredSsRequestIndication(Short invokeID, UssdArg ussdArg, MAPNetworkUnstructuredSsDialogue mapDialogue);

    public void onMAPProcessUnstructuredSsRequestConfirmation(Short invokeId, UssdRes ussdRes,
            MAPNetworkUnstructuredSsDialogue mapDialogue, MAPUserError userError, ProviderError providerError);

    public void onMAPUnstructuredSsRequestConfirmation(Short invokeId, UssdRes ussdRes,
            MAPNetworkUnstructuredSsDialogue mapDialogue, ProviderError providerError, MAPUserError userError);

    public void onMAPUnstructuredSsNotifyIndication(Short invokeID, UssdArg ussdArg, MAPNetworkUnstructuredSsDialogue mapDialogue);

    public void onMAPUnstructuredSsNotifyConfirmation(Short invokeId, MAPNetworkUnstructuredSsDialogue mapDialogue,
            ProviderError providerError, MAPUserError userError);

}
