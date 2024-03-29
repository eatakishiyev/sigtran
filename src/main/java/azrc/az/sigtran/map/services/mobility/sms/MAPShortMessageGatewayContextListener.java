/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import azrc.az.sigtran.map.MAPDialogue;
import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 * @author eatakishiyev
 */
public abstract class MAPShortMessageGatewayContextListener extends MAPListener {


    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.SHORT_MSG_GATEWAY_CONTEXT};
    }

    public  void onMAPSendRoutingInfoSmConfirmation(Short invokeId, SendRoutingInfoForSMRes sendRoutingInfoForSMRes,
                                                            MAPShortMessageGatewayDialogue dialogue, MAPUserError mapUserError,
                                                            ProviderError providerError){}

    public  void onMAPSendRoutingInfoSmIndication(Short invokeId, SendRoutingInfoForSMArg arg,
                                                          MAPShortMessageGatewayDialogue mapDialogue){}

    public  void onMAPReportSmDeliveryStatusConfirmation(Short invokeId, ReportSMDeliveryStatusRes reportSmDeliveryStatusRes,
                                                                 MAPShortMessageGatewayDialogue mapDialogue, MAPUserError mapUserError,
                                                                 ProviderError providerError){}

    public  void onMAPReportSmDeliveryStatusIndication(Short invokeID, ReportSMDeliveryStatusArg reportSMDeliveryStatusArg,
                                                               MAPShortMessageGatewayDialogue mapDialogue){}

    public  void onMAPInformScIndication(Short invokeID, InformServiceCentreArg informServiceCentreArg, MAPDialogue mapDialogue){}

}
