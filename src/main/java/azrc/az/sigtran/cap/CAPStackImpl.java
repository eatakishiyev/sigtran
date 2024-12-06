/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.api.*;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.AssistRequestInstructionArg;
import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.cap.messages.circuit.switched.call.control.bcsm.ConnectToResourceArg;
import azrc.az.sigtran.cap.smscontrol.general.ConnectSMSArg;
import azrc.az.sigtran.cap.smscontrol.general.ReleaseSMSArg;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPProvider;
import azrc.az.sigtran.tcap.primitives.tc.*;
import azrc.az.sigtran.tcap.primitives.tr.TRNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author eatakishiyev
 */
public class CAPStackImpl implements CAPStack {

    private final Logger logger = LoggerFactory.getLogger(CAPStackImpl.class);
    private CAPProvider capProvider;
    private TCAPProvider tcapProvider;
    private transient CAPUser user;
    private int guardTimerValue = 0;

    private final CAPDialogueFactory capDialogueFactory;
    private int workers = 10;
    private ExecutorService[] capWorkers = null;
    protected final ConcurrentHashMap<Long, CAPDialogue> dialogues = new ConcurrentHashMap<>();
    private final ScheduledExecutorService guardTimer = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "CAPGuardTimer");
        }
    });

    public ScheduledExecutorService getGuardTimer() {
        return guardTimer;
    }

    public CAPStackImpl(TCAPProvider tcapProvider, int workers) {
        logger.info("==========================| Starting CAP Stack |==========================");
        this.capWorkers = new ExecutorService[workers];
        for (int i = 0; i < workers; i++) {
            this.capWorkers[i] = Executors.newFixedThreadPool(workers, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "CAP-Worker");
                }
            });
        }

        this.tcapProvider = tcapProvider;
        this.tcapProvider.setUser(this);
        this.capProvider = new CAPProviderImpl(this);
        capDialogueFactory = new CAPDialogueFactory(this);
        logger.info("==========================| CAP Stack started |==========================");
    }

    @Override
    public void setTCAPProvider(TCAPProvider tcapProvider) {
        this.tcapProvider = tcapProvider;
    }

    @Override
    public void setCAPUser(CAPUser user) {
        this.user = user;
    }

    /**
     * @return the capProvider
     */
    @Override
    public CAPProvider getCAPProvider() {
        return capProvider;
    }

    /**
     * @param capProvider the capProvider to set
     */
    public void setCapProvider(CAPProviderImpl capProvider) {
        this.capProvider = capProvider;
    }

    /**
     * @return the tcapProvider
     */
    @Override
    public TCAPProvider getTcapProvider() {
        return tcapProvider;
    }

    @Override
    public CAPDialogueFactory getCAPDialogueFactory() {
        return this.capDialogueFactory;
    }

    /**
     * @param encodedData
     * @param indication
     */
    @Override
    public void onBegin(byte[] encodedData, TCBegin indication) {
        try {
            CAPDialogue capDialogue = this.capDialogueFactory.createCAPDialogue(indication.getDialogue());
            if (logger.isDebugEnabled()) {
                logger.debug("TCBEGIN received : " + capDialogue);
            }
            if (capDialogue != null) {
                capDialogue.onTCBegin(encodedData, indication);
            } else {
                logger.error("Not CAP dialogue found: " + indication.getDialogue());
            }
        } catch (ResourceLimitationException ex) {
            logger.error("ErrorOccured : ", ex);
        }
    }

    @Override
    public void onContinue(TCContinue indication) {
        CAPDialogue capDialogue = this.getCAPDialogue(indication.getDialogue().getDialogueId());
        if (capDialogue != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("TCContinue received : " + capDialogue);
            }
            capDialogue.onTCContinue(indication);
        } else {
            logger.error(String.format("TCContinue:CAP dialogue not found. DialogueId = %s", indication.getDialogue().getDialogueId()));
        }
    }

    @Override
    public void onEnd(TCEnd indication) {
        CAPDialogue capDialogue = this.getCAPDialogue(indication.getDialogue().getDialogueId());
        if (capDialogue != null) {
            capDialogue.onTCEnd(indication);
            if (logger.isDebugEnabled()) {
                logger.debug("TCEnd received : " + capDialogue);
            }
        } else {
            logger.error(String.format("TCEnd:CAP dialogue not found. DialogueId = %s", indication.getDialogue().getDialogueId()));
        }
    }

    @Override
    public void onPAbort(TCPAbort indication) {
        CAPDialogue capDialogue = this.getCAPDialogue(indication.getDialogue().getDialogueId());
        if (capDialogue != null) {
            capDialogue.onPAbort(indication);
            if (logger.isDebugEnabled()) {
                logger.debug("TCPAbort received : " + capDialogue.getDialogueId());
            }
        } else {
            logger.error("TCPAbort received, no dialogue found. DialogueId = " + indication.getDialogue().getDialogueId());
        }
    }

    @Override
    public void onUAbort(TCUAbort indication) {
        CAPDialogue capDialogue = this.getCAPDialogue(indication.getDialogueId());
        if (capDialogue != null) {
            capDialogue.onTCUAbort(indication);
            if (logger.isDebugEnabled()) {
                logger.debug("TCUAbort received : " + capDialogue);
            }
        } else {
            logger.error("TCUAbort received , no dialogue found. DialogueId = " + indication.getDialogueId());
        }
    }

    @Override
    public void onNotice(TRNotice notice) {
        CAPDialogue capDialogue = this.getCAPDialogue(notice.getOrigTransactionId());
        if (capDialogue != null) {
            capDialogue.onNotice(notice);
            if (logger.isDebugEnabled()) {
                logger.debug("TRNotice received : " + capDialogue);
            }
        } else {
            logger.error(String.format("TCNotice:CAP dialogue not found. DialogueId = %s", notice.getOrigTransactionId()));
        }
    }

    @Override
    public void onDialogueTimeOut(Long dialogueId) {
        CAPDialogue capDialogue = this.getCAPDialogue(dialogueId);
        if (capDialogue != null) {
            capDialogue.onDialogueTimeOut();
            logger.info("DialogueTimeOut received : " + capDialogue);
        } else {
            logger.error("DialogueTimeOut occured, no dialogue found. DialogueId = " + dialogueId);
        }
    }

    @Override
    public void onTCLCancel(TCLCancel indication) {
        CAPDialogue capDialogue = this.getCAPDialogue(indication.getDialogue().getDialogueId());
        if (capDialogue != null) {
            capDialogue.onTCLCancel(indication);
        } else {
            logger.error("TCLCancel received , no dialogue found. DialogueId = " + indication.getDialogue().getDialogueId());
        }
    }

    void fireDialogueAccepted(CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onDialogueAccepted(dialogue);
        });

    }

    void fireProviderAbort(CAPGeneralAbortReasons reason, ProblemSource problemSource, CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onProviderAbort(reason, problemSource, dialogue);
        });
    }

    void fireNotice(Notifications info, CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onNotice(info, dialogue);
        });
    }

    void fireDelimiter(CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onDelimiter(dialogue);
        });
    }

    void fireCloseIndication(CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onCloseIndication(dialogue);
        });
    }

    void fireUserAbort(CAPGeneralAbortReasons reason, CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onUserAbort(reason, dialogue);
        });
    }

    public void fireCAPDialogueTimeOut(CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(() -> {
            user.onTimerTimeOut(dialogue);
        });
    }

    public void onCAPInitialDpIndication(Short invokeID, InitialDpArg initialDpArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPInitialDpIndication(invokeID, initialDpArg, capDialogue);
        });
    }

    public void onCAPEventReportBCSM(Short invokeID, EventReportBCSMArg eventReportBCSM, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPEventReportBCSM(invokeID, eventReportBCSM, capDialogue);
        });
    }

    public void onCAPRequestReportBCSM(Short invokeID, RequestReportBCSMEventArg requestReportBCSMEventArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportBCSMEvent(invokeID, requestReportBCSMEventArg, capDialogue);
        });
    }

    public void onCAPInitialDpError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPInitialDpError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPEventReportBCSMError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPEventReportBCSMError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPInitialDpError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPInitialDpError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPEventReportBCSMError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(() -> {
            user.onCAPEventReportBCSMError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPReleaseCall(Short invokeId, IReleaseCallArg releaseCallArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPReleaseCall(invokeId, releaseCallArg, capDialogue);
        });
    }

    public void onCAPContinue(Short invokeId, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPContinue(invokeId, capDialogue);
        });
    }

    public void onCAPApplyCharging(Short invokeId, IApplyChargingArg applyChargingArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyCharging(invokeId, applyChargingArg, capDialogue);
        });
    }

    public void onCAPInitialDpSmsError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPInitialDpSmsError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPEventReportSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEventReportSMSError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPInitialDpSmsError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPInitialDpSmsError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPEventReportSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEventReportSMSError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPInitialDpSmsIndication(Short invokeID, InitialDpSMSArg initialDpSMSArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPInitialDpSmsIndication(invokeID, initialDpSMSArg, capDialogue);
        });
    }

    public void onCAPEventReportSmsIndication(Short invokeId, IEventReportSMSArg eventReportSMSArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEventReportSMSIndication(invokeId, eventReportSMSArg, capDialogue);
        });
    }

    public void onCAPRequestReportSMSEvent(Short invokeId, IRequestReportSMSEventArg requestReportSMSEventArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportSMSEvent(invokeId, requestReportSMSEventArg, capDialogue);
        });
    }

    public void onCAPContinueSms(Short invokeId, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPContinueSms(invokeId, capDialogue);
        });
    }

    public void onCAPConnectSMS(Short invokeId, ConnectSMSArg connectSMSArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectSMS(invokeId, connectSMSArg, capDialogue);
        });
    }

    public void onCAPReleaseSMS(Short invokeId, ReleaseSMSArg releaseSMSArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPReleaseSMS(invokeId, releaseSMSArg, capDialogue);
        });
    }

    public void onCAPRequestReportBCSMEventError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportBCSMEventError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPConnectError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPApplyChargingError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyChargingError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPApplyChargingReportError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyChargingReportError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPApplyChargingReportError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyChargingReportError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPApplyChargingError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyChargingError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPConnectError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPRequestReportBCSMEventError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportBCSMEventError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPRequestReportSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportSMSError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPConnectSMSError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectSMSError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPRequestReportSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPRequestReportSMSError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPConnectSMSError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectSMSError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPApplyChargingReport(Short invokeId, ApplyChargingReportArg applyChargingReportArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPApplyChargingReport(invokeId, applyChargingReportArg, capDialogue);
        });
    }

    public void onCAPPlayAnnouncement(Short invokeId, IPlayAnnouncementArg playAnnouncementArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPPlayAnnouncement(invokeId, playAnnouncementArg, capDialogue);
        });
    }

    public void onCAPPlayAnnouncementError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onPlayAnnouncementError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPPlayAnnouncementError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onPlayAnnouncementError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPConnectToResource(Short invokeId, ConnectToResourceArg connectToResourceArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectToResource(invokeId, connectToResourceArg, capDialogue);
        });
    }

    public void onCAPConnectToResourceError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectToResourceError(invokeId, userError, capDialogue);
        });
    }

    public void onCAPConnectToResourceError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnectToResourceError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPActivityTest(Short invokeId, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPActivityTest(invokeId, capDialogue);
        });
    }

    public void onCAPActivityTestConfirmation(Short invokeId, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPActivityTestConfirmation(invokeId, capDialogue);
        });
    }

    public void onCAPAssistRequestInstructions(Short invokeId, AssistRequestInstructionArg assistRequestInstructionArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPAssistRequestInstructions(invokeId, assistRequestInstructionArg, capDialogue);
        });
    }

    public void onCAPAssistRequestInstructionsError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPAssistRequestInstructionsError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPAssistRequestInstructionsError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPAssistRequestInstructionsError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPEstablishTemporaryConnection(Short invokeId, IEstablishTemporaryConnectionArg establishTemporaryConnection, CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEstablishTemporaryConnection(invokeId, establishTemporaryConnection, dialogue);
        });
    }

    public void onCAPEstablishTemporaryConnectionError(Short invokeId, CAPDialogue capDialogue, CAPUserError userError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEstablishTemporaryConnectionError(invokeId, capDialogue, userError);
        });
    }

    public void onCAPEstablishTemporaryConnectionError(Short invokeId, CAPDialogue capDialogue, ProviderError providerError) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPEstablishTemporaryConnectionError(invokeId, capDialogue, providerError);
        });
    }

    public void onCAPConnect(Short invokeId, ConnectArg connectArg, CAPDialogue capDialogue) {
        ExecutorService worker = findWorker(capDialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPConnect(invokeId, capDialogue, connectArg);
        });
    }

    public void onCAPDisconnectForwardConnection(Short invokeId, CAPDialogue dialogue) {
        ExecutorService worker = findWorker(dialogue.getDialogueId());
        worker.submit(()
                -> {
            user.onCAPDisconnectForwardConnection(invokeId, dialogue);
        });
    }

    protected void releaseCapDialogue(Long dialogueId) {
        CAPDialogue capDialogue = dialogues.remove(dialogueId);
        if (capDialogue != null) {
            capDialogue.terminateAllPerformingSSMs();
            if (user != null) {
                user.onCloseIndication(capDialogue);
            } else {
                logger.warn("releaseMAPDialogue: No MAP Listener found for MAPDialog  " + capDialogue);
            }
        }
    }

    protected CAPDialogue getCAPDialogue(long dialogueId) {
        return dialogues.get(dialogueId);
    }

    public int getGuardTimerValue() {
        return guardTimerValue;
    }

    public void setGuardTimerValue(int guardTimerValue) {
        this.guardTimerValue = guardTimerValue;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getWorkers() {
        return workers;
    }

    public ExecutorService findWorker(long dialogId) {
        int idx = (int) (dialogId % capWorkers.length);
        return capWorkers[idx];
    }

    @Override
    public ConcurrentHashMap<Long, CAPDialogue> getDialogues() {
        return dialogues;
    }
}
