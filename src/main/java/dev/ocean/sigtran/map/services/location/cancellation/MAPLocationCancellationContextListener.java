/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.location.cancellation;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPLocationCancellationContextListener extends MAPListener {

    public void onMAPCancelLocationIndication(Short invokeID, CancelLocationArg cancelLocationArg,
            MAPLocationCancellationDialogue mapDialogue);

    public void onMAPCancelLocationConfirmation(Short invokeId, CancelLocationRes cancelLocationRes,
            MAPLocationCancellationDialogue mapDialogue, MAPUserError userError, ProviderError providerError);
}
