/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.ericsson.mobility.management.IN;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPEricssonMobilityMngtINTriggeringContextListener extends MAPListener {

    public void onMAPEricssonMobilityMngtINTriggeringIndication(Short invokeId,
            MobilityMngtINTriggeringArg arg, MAPEricssonMobilityMngtINTriggeringDialogue dialogue);

    public void onMAPEricssonMobilityMngtINTriggeringConfirmation(Short invokeId,
            MAPEricssonMobilityMngtINTriggeringDialogue dialogue, MAPUserError userError,
            ProviderError providerError);
}
