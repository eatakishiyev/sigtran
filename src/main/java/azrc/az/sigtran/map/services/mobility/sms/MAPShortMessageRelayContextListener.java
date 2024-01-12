/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPShortMessageRelayContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.SHORT_MSG_MT_RELAY_CONTEXT,
                MAPApplicationContextName.SHORT_MSG_MO_RELAY_CONTEXT};
    }

    public  void onMAPMtForwardSmIndication(Short invokeId, MTForwardSMArg arg,
                                           MAPShortMessageRelayDialogue mapDialogue) {}

    public  void onMAPForwardSmIndication(Short invokeId, MAPForwardSmArg arg,
                                         MAPShortMessageRelayDialogue mapDialogue) {}

    public  void onMAPForwardSmConfirmation(Short invokeId, MAPForwardSMResponse response,
                                           MAPShortMessageRelayDialogue dialogue, MAPUserError mapUserError,
                                           ProviderError providerError) {}
}
