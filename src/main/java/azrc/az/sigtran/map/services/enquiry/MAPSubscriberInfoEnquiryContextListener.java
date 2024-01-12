/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPSubscriberInfoEnquiryContextListener extends MAPListener {



    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.SUBSCRIBER_DATA_MODIFICATION_NOTIFICATION_CONTEXT};
    }

    public  void onMAPProvideSubscriberInfoIndication(Short invokeId, ProvideSubscriberInfoArg provideSubscriberInfoArg,
                                                              MAPSubscriberInformationEnquiryDialogue dialogue){}

    public  void onMAPProvideSubscriberInfoConfirmation(Short invokeId, ProvideSubscriberInfoRes provideSubscriberInfoRes,
                                                        MAPSubscriberInformationEnquiryDialogue mapDialogue,
                                                        MAPUserError mapUser, ProviderError providerError){}

}
