/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPShortMessageRelayContextListener extends MAPListener {

    public void onMAPMtForwardSmIndication(Short invokeId, MTForwardSMArg arg,
            MAPShortMessageRelayDialogue mapDialogue);

    public void onMAPForwardSmIndication(Short invokeId, MAPForwardSmArg arg,
            MAPShortMessageRelayDialogue mapDialogue);

    public void onMAPForwardSmConfirmation(Short invokeId, MAPForwardSMResponse response,
            MAPShortMessageRelayDialogue dialogue, MAPUserError mapUserError,
            ProviderError providerError);
}
