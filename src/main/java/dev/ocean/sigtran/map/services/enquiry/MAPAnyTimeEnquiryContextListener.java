/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.enquiry;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;

/**
 *
 * @author eatakishiyev
 */
public abstract class MAPAnyTimeEnquiryContextListener extends MAPListener {

    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.ANY_TIME_ENQUIRY_CONTEXT};
    }



    public  void onMAPAnyTimeInterrogationIndication(Short invokeID, AnyTimeInterrogationArg anyTimeInterrogationArg,
                                                     MAPAnyTimeInterrogationDialogue mapDialogue){}

    public  void onMAPAnyTimeInterrogationConfirmation(Short invokeId, AnyTimeInterrogationRes anyTimeInterrogationRes,
            MAPAnyTimeInterrogationDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError){}
}
