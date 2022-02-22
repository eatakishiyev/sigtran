/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import dev.ocean.sigtran.map.MAPDialogue;
import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPShortMessageGatewayContextListener extends MAPListener {

    public void onMAPSendRoutingInfoSmConfirmation(Short invokeId, SendRoutingInfoForSMRes sendRoutingInfoForSMRes,
            MAPShortMessageGatewayDialogue dialogue, MAPUserError mapUserError,
            ProviderError providerError);

    public void onMAPSendRoutingInfoSmIndication(Short invokeId, SendRoutingInfoForSMArg arg,
            MAPShortMessageGatewayDialogue mapDialogue);

    public void onMAPReportSmDeliveryStatusConfirmation(Short invokeId, ReportSMDeliveryStatusRes reportSmDeliveryStatusRes,
            MAPShortMessageGatewayDialogue mapDialogue, MAPUserError mapUserError,
            ProviderError providerError);

    public void onMAPReportSmDeliveryStatusIndication(Short invokeID, ReportSMDeliveryStatusArg reportSMDeliveryStatusArg,
            MAPShortMessageGatewayDialogue mapDialogue);

    public void onMAPInformScIndication(Short invokeID, InformServiceCentreArg informServiceCentreArg, MAPDialogue mapDialogue);

}
