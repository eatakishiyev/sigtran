/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.cap.api.IApplyChargingArg;
import azrc.az.sigtran.cap.api.IEstablishTemporaryConnectionArg;
import azrc.az.sigtran.cap.api.IEventReportSMSArg;
import azrc.az.sigtran.cap.api.IPlayAnnouncementArg;
import azrc.az.sigtran.cap.api.IReleaseCallArg;
import azrc.az.sigtran.cap.api.IRequestReportSMSEventArg;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.AssistRequestInstructionArg;
import azrc.az.sigtran.cap.messages.circuit.switched.call.control.bcsm.ConnectToResourceArg;
import azrc.az.sigtran.cap.smscontrol.general.ConnectSMSArg;
import azrc.az.sigtran.cap.smscontrol.general.ReleaseSMSArg;
import azrc.az.sigtran.cap.api.RequestReportBCSMEventArg;
import azrc.az.sigtran.cap.api.ConnectArg;
import azrc.az.sigtran.cap.api.InitialDpArg;
import azrc.az.sigtran.cap.api.InitialDpSMSArg;
import azrc.az.sigtran.cap.api.EventReportBCSMArg;

/**
 *
 * @author eatakishiyev
 */
public abstract class CAPUser {

    private CAPProvider capProvider;

    public void onDialogueAccepted(CAPDialogue dialogue) {

    }

    public void onDelimiter(CAPDialogue dialogue) {

    }

    public void onCloseIndication(CAPDialogue dialogue) {

    }

    public void onUserAbort(CAPGeneralAbortReasons reason, CAPDialogue dialogue) {

    }

    public void onProviderAbort(CAPGeneralAbortReasons reason, ProblemSource problemSource, CAPDialogue dialogue) {

    }

    public void onCAPInitialDpIndication(Short invokeID, InitialDpArg initialDpArg, CAPDialogue capDialogue) {

    }

    public void onCAPEventReportBCSM(Short invokeID, EventReportBCSMArg eventReportBCSM, CAPDialogue capDialogue) {

    }

    public void onCAPRequestReportBCSMEvent(Short invokeID, RequestReportBCSMEventArg requestReportBCSMEventArg, CAPDialogue capDialogue) {

    }

    public void onCAPInitialDpError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPEventReportBCSMError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPInitialDpError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPEventReportBCSMError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onTimerTimeOut(CAPDialogue dialogue) {

    }

    public void onCAPReleaseCall(Short invoke, IReleaseCallArg releaseCallArg, CAPDialogue capDialogue) {

    }

    public void onCAPContinue(Short invokeID, CAPDialogue capDialogue) {

    }

    public void onCAPApplyCharging(Short invokeId, IApplyChargingArg applyChargingArg, CAPDialogue capDialogue) {

    }

    public void onCAPInitialDpSmsError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPEventReportSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPInitialDpSmsError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPEventReportSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPInitialDpSmsIndication(Short invokeId, InitialDpSMSArg initialDpSMSArg, CAPDialogue capDialogue) {

    }

    public void onCAPEventReportSMSIndication(Short invokeId, IEventReportSMSArg eventReportSMSArg, CAPDialogue capDialogue) {

    }

    public void onCAPRequestReportSMSEvent(Short invokeId, IRequestReportSMSEventArg requestReportSMSEventArg, CAPDialogue capDialogue) {

    }

    public void onCAPContinueSms(Short invokeId, CAPDialogue capDialogue) {

    }

    public void onCAPConnectSMS(Short invokeId, ConnectSMSArg connectSMSArg, CAPDialogue capDialogue) {

    }

    public void onCAPReleaseSMS(Short invokeId, ReleaseSMSArg releaseSMSArg, CAPDialogue capDialogue) {

    }

    public void onCAPApplyChargingReportError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPApplyChargingError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPConnectError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPRequestReportBCSMEventError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPRequestReportBCSMEventError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPConnectError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPApplyChargingError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPApplyChargingReport(Short invokeId, ApplyChargingReportArg applyChargingReportArg, CAPDialogue capDialogue) {

    }

    public void onCAPApplyChargingReportError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPRequestReportSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPConnectSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPRequestReportSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPConnectSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPPlayAnnouncement(Short invokeId, IPlayAnnouncementArg playAnnouncementArg, CAPDialogue capDialogue) {

    }

    public void onPlayAnnouncementError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onPlayAnnouncementError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPConnectToResource(Short invokeId, ConnectToResourceArg connectToResourceArg, CAPDialogue capDialogue) {

    }

    public void onCAPConnectToResourceError(Short invokeId, CAPUserError userError, CAPDialogue capDialogue) {

    }

    public void onCAPConnectToResourceError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPActivityTest(Short invokeId, CAPDialogue capDialogue) {

    }

    public void onCAPActivityTestConfirmation(Short invokeId, CAPDialogue capDialogue) {

    }

    public void onCAPAssistRequestInstructions(Short invokeId, AssistRequestInstructionArg assistRequestInstructionArg, CAPDialogue capDialogue) {

    }

    public void onCAPAssistRequestInstructionsError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPAssistRequestInstructionsError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPEstablishTemporaryConnection(Short invokeId, IEstablishTemporaryConnectionArg establishTemporaryConnectionArg, CAPDialogue dialogue) {

    }

    public void onCAPEstablishTemporaryConnectionError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {

    }

    public void onCAPEstablishTemporaryConnectionError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {

    }

    public void onCAPConnect(Short invokeId, CAPDialogue capDialogue, ConnectArg connectArg) {

    }
    
    
    public void onCAPDisconnectForwardConnection(Short invokeId, CAPDialogue dialogue) {
        
    }

    public void onNotice(Notifications info, CAPDialogue dialogue) {

    }

    public final CAPProvider getCAPProvider() {
        return this.capProvider;
    }

    public final void setCAPProvider(CAPProvider capProvider) {
        this.capProvider = capProvider;
    }

}
