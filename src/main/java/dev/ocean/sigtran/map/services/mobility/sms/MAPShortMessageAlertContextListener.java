/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPShortMessageAlertContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.SHORT_MSG_GATEWAY_CONTEXT};
    }

    public  void onMAPAlertScIndication(Short invokeID, AlertServiceCentreArg alertSCArg,
                                        MAPShortMessageAlertDialogue mapDialogue){}

    public  void onMAPAlertScConfirmation(Short invokeId, MAPShortMessageAlertDialogue mapDialogue,
                                                  MAPUserError mapUserError, ProviderError providerError){}
}
