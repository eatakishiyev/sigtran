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
public abstract class MAPShortMessageAlertContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.SHORT_MSG_ALERT_CONTEXT};
    }

    public  void onMAPAlertScIndication(Short invokeID, AlertServiceCentreArg alertSCArg,
                                        MAPShortMessageAlertDialogue mapDialogue){}

    public  void onMAPAlertScConfirmation(Short invokeId, MAPShortMessageAlertDialogue mapDialogue,
                                          MAPUserError mapUserError, ProviderError providerError){}
}
