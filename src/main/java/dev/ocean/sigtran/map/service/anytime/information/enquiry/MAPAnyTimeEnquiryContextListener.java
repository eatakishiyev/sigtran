/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.anytime.information.enquiry;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPAnyTimeEnquiryContextListener extends MAPListener {

    public void onMAPAnyTimeInterrogationIndication(Short invokeID, AnyTimeInterrogationArg anyTimeInterrogationArg,
            MAPAnyTimeInterrogationDialogue mapDialogue);

    public void onMAPAnyTimeInterrogationConfirmation(Short invokeId, AnyTimeInterrogationRes anyTimeInterrogationRes,
            MAPAnyTimeInterrogationDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError);

}
