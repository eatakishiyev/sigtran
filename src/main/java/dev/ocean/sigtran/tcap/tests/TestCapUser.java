/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.tests;

import dev.ocean.sigtran.cap.CAPUser;
import dev.ocean.sigtran.map.MAPDialogue;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;
import dev.ocean.sigtran.map.services.common.MapOpen;
import dev.ocean.sigtran.map.services.common.MapPAbort;
import dev.ocean.sigtran.map.services.common.MapUAbort;
import dev.ocean.sigtran.map.services.mobility.sms.*;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import org.apache.logging.log4j.*;


/**
 *
 * @author eatakishiyev
 */
public class TestCapUser implements MAPShortMessageGatewayContextListener, MAPShortMessageAlertContextListener {

    private final Logger logger = LogManager.getLogger(TestCapUser.class);

    public TestCapUser() {
        logger.info("Starting TestCapUser logic...");
    }

    @Override
    public void onMAPSendRoutingInfoSmConfirmation(Short invokeId, SendRoutingInfoForSMRes sendRoutingInfoForSMRes, MAPShortMessageGatewayDialogue dialogue, MAPUserError mapUserError, ProviderError providerError) {

    }

    @Override
    public void onMAPSendRoutingInfoSmIndication(Short invokeId, SendRoutingInfoForSMArg arg, MAPShortMessageGatewayDialogue mapDialogue) {

    }

    @Override
    public void onMAPReportSmDeliveryStatusConfirmation(Short invokeId, ReportSMDeliveryStatusRes reportSmDeliveryStatusRes, MAPShortMessageGatewayDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError) {

    }

    @Override
    public void onMAPReportSmDeliveryStatusIndication(Short invokeID, ReportSMDeliveryStatusArg reportSMDeliveryStatusArg, MAPShortMessageGatewayDialogue mapDialogue) {

    }

    @Override
    public void onMAPInformScIndication(Short invokeID, InformServiceCentreArg informServiceCentreArg, MAPDialogue mapDialogue) {

    }

    @Override
    public void setMAPProvider(MAPProvider mapProvider) {

    }

    @Override
    public void onMAPOpenInd(MapOpen mapOpen, MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPOpenConfirm(MapOpen mapOpen, MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPCloseInd(MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPDelimiterInd(MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPNoticeInd(ErrorReason errorReason, MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPProviderAbortInd(MapPAbort mapPAbort, MAPDialogue mapDialogue) {

    }

    @Override
    public void onMAPUserAbortInd(MapUAbort mapUAbort, MAPDialogue mapDialogue) {

    }

    @Override
    public void onDialogueTimeOut(MAPDialogue mapDialogue) {

    }

    @Override
    public void onMapDialogueReleased(MAPDialogue mapDialogue, MAPDialogue.TerminationReason reason) {

    }

    @Override
    public MAPProvider getMAPProvider() {
        return null;
    }

    @Override
    public void onMAPAlertScIndication(Short invokeID, AlertServiceCentreArg alertSCArg, MAPShortMessageAlertDialogue mapDialogue) {

    }

    @Override
    public void onMAPAlertScConfirmation(Short invokeId, MAPShortMessageAlertDialogue mapDialogue, MAPUserError mapUserError, ProviderError providerError) {

    }
}
