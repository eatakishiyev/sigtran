/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.subscriber.information.enquiry;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPSubscriberInfoEnquiryContextListener extends MAPListener {

    public void onMAPProvideSubscriberInfoIndication(Short invokeId, ProvideSubscriberInfoArg provideSubscriberInfoArg,
            MAPSubscriberInformationEnquiryDialogue dialogue);

    public void onMAPProvideSubscriberInfoConfirmation(Short invokeId, ProvideSubscriberInfoRes provideSubscriberInfoRes,
            MAPSubscriberInformationEnquiryDialogue mapDialogue, MAPUserError mapUser, ProviderError providerError);

}
