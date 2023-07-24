/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.equipment.management;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 *
 * @author eatakishiyev
 */
public abstract class MAPEquipmentManagementContextListener extends MAPListener {


    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.EQUIPMENT_MGNT_CONTEXT};
    }

    public  void onMAPCheckIMEIIndication(Short invokeID, CheckIMEIArg arg,
                                          MAPEquipmentManagementDialogue mapDialogue){}

    public  void onMAPCheckIMEIConfirmation(Short invokeId, CheckIMEIResponse response,
            MAPEquipmentManagementDialogue dialogue, MAPUserError mapUserError,
            ProviderError providerError){}

}
