/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.map.parameters.MAPApplicationContextName;
import azrc.az.sigtran.map.services.common.MapOpen;
import azrc.az.sigtran.map.services.common.MapPAbort;
import azrc.az.sigtran.map.services.common.MapUAbort;
import azrc.az.sigtran.sccp.general.ErrorReason;
import azrc.az.sigtran.tcap.TCAPProvider;
import azrc.az.sigtran.tcap.primitives.tc.*;
import azrc.az.sigtran.tcap.primitives.tr.TRNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.*;

/**
 * @author eatakishiyev
 */
public class MAPStackImpl implements MAPStack {

    private final Logger logger = LoggerFactory.getLogger(MAPStackImpl.class);
    private final MAPProvider mapProvider;
    private TCAPProvider tcapProvider;
    protected final MAPDialogueFactory mapDialogueFactory;
    private transient Map<MAPApplicationContextName, MAPListener> users = new ConcurrentHashMap<>();
    private int guardTimerValue = 0;
    protected final ConcurrentHashMap<Long, MAPDialogue> dialogues = new ConcurrentHashMap<>();
    private final ScheduledExecutorService guardTimer = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MAPGuardTimer");
        }
    });

    private ExecutorService mapWorkers[];

    public MAPStackImpl(TCAPProvider tcapProvider, int guardTimerValue, int mapWorkersCount) {
        logger.info("==========================| Starting MAP Stack |==========================");

        mapWorkers = new ExecutorService[mapWorkersCount];
        for (int i = 0; i < mapWorkersCount; i++) {
            mapWorkers[i] = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "MAP-Worker");
                }
            });
        }

        this.tcapProvider = tcapProvider;
        this.mapProvider = new MAPProviderImpl(this);
        this.mapDialogueFactory = new MAPDialogueFactory(this);
        this.tcapProvider.setUser(this);
        this.guardTimerValue = guardTimerValue;
        logger.info("==========================| MAP Stack started|==========================");
    }

    public MAPStackImpl(TCAPProvider tcapProvider, int guardTimerValue) {
        this(tcapProvider, guardTimerValue, 0);
    }

    @Override
    public MAPProvider getMAPProvider() {
        return this.mapProvider;
    }

    @Override
    public void setTCAPProvider(TCAPProvider tcapProvider) {
        this.tcapProvider = tcapProvider;
    }

    @Override
    public MAPDialogueFactory getMAPDialogueFactory() {
        return this.mapDialogueFactory;
    }

    @Override
    public void onPAbort(TCPAbort indication) {
        MAPDialogue mapDialogue = this.getMapDialogue(indication.getDialogue().getDialogueId());
        if (mapDialogue != null) {
            mapDialogue.onPAbort(indication);
        } else {
            logger.error("onPABORT: No MAP dialogue found. " + indication.getDialogue());
        }
    }

    @Override
    public void onBegin(byte[] userData, TCBegin indication) {
        MAPDialogue mapDialogue = this.mapDialogueFactory.createMAPDialogue(indication);
        mapDialogue.onTCBegin(userData, indication);
    }

    @Override
    public void onContinue(TCContinue indication) {
        if (logger.isDebugEnabled()) {
            logger.debug("TC-CONTINUE received: " + indication.getDialogue().getDialogueId());
        }

        MAPDialogue mapDialogue = this.getMapDialogue(indication.getDialogue().getDialogueId());
        if (mapDialogue != null) {
            mapDialogue.onTCContinue(indication);
        } else {
            logger.error("onCONTINUE: No MAP dialogue found. " + indication.getDialogue().getDialogueId());
        }
    }

    @Override
    public void onEnd(TCEnd indication) {
        if (logger.isDebugEnabled()) {
            logger.debug("TC-END received: " + indication.getDialogue().getDialogueId());
        }

        MAPDialogue mapDialogue = this.getMapDialogue(indication.getDialogue().getDialogueId());
        if (mapDialogue != null) {
            mapDialogue.onTCEnd(indication);
        } else {
            logger.error("onEND: No MAP dialogue found." + indication.getDialogue());
        }
    }

    @Override
    public void onUAbort(TCUAbort indication) {
        MAPDialogue mapDialogue = this.getMapDialogue(indication.getDialogueId());
        if (mapDialogue != null) {
            mapDialogue.onTCUAbort(indication);
        } else {
            logger.error("onUABORT: No MAP dialogue found." + indication.getDialogueId());
        }
    }

    @Override
    public void onTCLCancel(TCLCancel indication) {
        MAPDialogue mapDialogue = this.getMapDialogue(indication.getDialogue().getDialogueId());
        if (mapDialogue != null) {
            mapDialogue.onTCLCancel(indication);
        } else {
            logger.error("onTCLCANCEL: No MAP dialogue found. " + indication.getDialogue());
        }
    }

    @Override
    public void onNotice(TRNotice indication) {
        MAPDialogue mapDialogue = this.getMapDialogue(indication.getOrigTransactionId());
        if (mapDialogue != null) {
            mapDialogue.onNotice(indication);
        } else {
            logger.error("onNOTICE: No MAP dialogue found. " + indication.getOrigTransactionId());
        }
    }

    @Override
    public void onDialogueTimeOut(Long dialogueId) {
        MAPDialogue mapDialogue = this.getMapDialogue(dialogueId);
        if (mapDialogue != null) {
            releaseMapDialogue(dialogueId, MAPDialogue.TerminationReason.TIMEOUT);
            fireMapDialogueTimeOut(mapDialogue);
        } else {
            logger.error("onDIALOGUE_TIME_OUT: No MAP dialogue found. " + dialogueId);
        }
    }

    public ExecutorService findWorker(long dialogId) {
        int idx = (int) (dialogId % mapWorkers.length);
        return mapWorkers[idx];
    }

    public void fireMapOpenInd(MapOpen mapOpen, MAPDialogue mapDialogue) {
        {
            ExecutorService worker = findWorker(mapDialogue.getDialogueId());
            worker.submit(() -> {
                MAPApplicationContextName acName = mapDialogue.getMapApplicationContext().getMapApplicationContextName();
                MAPListener user = users.get(acName);
                if (user != null) {
                    user.onMAPOpenInd(mapOpen, mapDialogue);
                }
            });
        }
    }

    public void fireMapOpenConfirm(MapOpen mapOpen, MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPOpenConfirm(mapOpen, mapDialogue);//TODO Implement route on ssn
                });
            } else {
                logger.warn("MAPOpenConfirmation: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapCloseInd(MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPCloseInd(mapDialogue);
                });
            } else {
                logger.warn("MAPCloseIndication: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapProviderAbortInd(MapPAbort mapPAbort, MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPProviderAbortInd(mapPAbort, mapDialogue);
                });
            } else {
                logger.warn("MAPProviderAbortIndication: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapDelimiterInd(MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPDelimiterInd(mapDialogue);
                });
            } else {
                logger.warn("MAPDelimiterIndication: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapNoticeInd(ErrorReason errorReason, MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPNoticeInd(errorReason, mapDialogue);
                });
            } else {
                logger.warn("MAPNoticeIndication: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapUserAbortInd(MapUAbort mapUAbort, MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onMAPUserAbortInd(mapUAbort, mapDialogue);
                });
            } else {
                logger.warn("MAPUserAbortIndication: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public void fireMapDialogueTimeOut(MAPDialogue mapDialogue) {
        {
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                ExecutorService worker = findWorker(mapDialogue.getDialogueId());
                worker.submit(() -> {
                    listener.onDialogueTimeOut(mapDialogue);
                });
            } else {
                logger.warn("MAPDialogueTimeOut: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    @Override
    public TCAPProvider getTcapProvider() {
        return this.tcapProvider;
    }

    protected MAPDialogue getMapDialogue(Long dialogueId) {
        return dialogues.get(dialogueId);
    }

    protected void releaseMapDialogue(Long dialogueId, MAPDialogue.TerminationReason reason) {
        MAPDialogue mapDialogue = dialogues.remove(dialogueId);
        if (mapDialogue != null) {
            mapDialogue.setMapDialogueState(MAPDialogueState.CLOSING);
            mapDialogue.terminateAllPerformingSSMs();
            MAPListener listener = mapDialogue.getMAPUser();
            if (listener != null) {
                listener.onMapDialogueReleased(mapDialogue, reason);
            } else {
                logger.warn("releaseMAPDialogue: No MAP Listener found for MAPDialog  " + mapDialogue);
            }
        }
    }

    public int getGuardTimerValue() {
        return guardTimerValue;
    }

    public void setGuardTimerValue(int guardTimerValue) {
        this.guardTimerValue = guardTimerValue;
    }

    public ScheduledExecutorService getGuardTimer() {
        return guardTimer;
    }

    public Map<MAPApplicationContextName, MAPListener> getUsers() {
        return users;
    }

    public void addMapUser(MAPListener mapUser) {
        for (MAPApplicationContextName mapApplicationContextName : mapUser.getMAPApplicationContexts()) {
            logger.info("MAPUser added {}, {}", mapUser, mapApplicationContextName);
            users.put(mapApplicationContextName, mapUser);
        }
        mapUser.setMapProvider(mapProvider);
    }

}
