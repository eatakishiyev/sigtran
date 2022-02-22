/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.purging;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPPurgeMSContextListener extends MAPListener {

    public void onMAPPurgeMSIndication(Short invokeId, PurgeMSArg request,
            MAPPurgeMSDialogue dialog);

    public void onMAPPurgeMSConfirmation(Short invokeId, PurgeMSRes response,
            MAPPurgeMSDialogue dialog, MAPUserError userError, ProviderError providerError);
}
