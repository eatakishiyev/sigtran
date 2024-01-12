/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.ericsson.mobility.management.IN;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPEricssonMobilityMngtINTriggeringContextListener extends MAPListener {


    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.ERI_MOBILITY_MNGT_IN};
    }

    public void onMAPEricssonMobilityManagementINTriggeringIndication(Short invokeId,
                                                                MobilityMngtINTriggeringArg arg,
                                                                MAPEricssonMobilityMngtINTriggeringDialogue dialogue) {
    }

    public void onMAPEricssonMobilityManagementINTriggeringConfirmation(Short invokeId,
                                                                        MAPEricssonMobilityMngtINTriggeringDialogue dialogue,
                                                                        MAPUserError userError, ProviderError providerError) {
    }
}
