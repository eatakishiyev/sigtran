/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.purging;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 *
 * @author eatakishiyev
 */
public abstract class MAPPurgeMSContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.MS_PURGING_CONTEXT};
    }

    public  void onMAPPurgeMSIndication(Short invokeId, PurgeMSArg request,
                                                MAPPurgeMSDialogue dialog){}

    public  void onMAPPurgeMSConfirmation(Short invokeId, PurgeMSRes response,
                                          MAPPurgeMSDialogue dialog, MAPUserError userError, ProviderError providerError){}
}
