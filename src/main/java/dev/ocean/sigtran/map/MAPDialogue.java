/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.dialoguePDU.*;
import dev.ocean.sigtran.map.parameters.*;
import dev.ocean.sigtran.map.services.common.Result;
import dev.ocean.sigtran.map.services.common.*;
import dev.ocean.sigtran.map.services.errors.IncorrectErrorCodeException;
import dev.ocean.sigtran.map.services.errors.MAPUserErrorFactory;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.InvocationStateMachine;
import dev.ocean.sigtran.tcap.QoS;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.*;
import dev.ocean.sigtran.tcap.primitives.AbortReason;
import dev.ocean.sigtran.tcap.primitives.Termination;
import dev.ocean.sigtran.tcap.primitives.tc.*;
import dev.ocean.sigtran.tcap.primitives.tr.TRNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eatakishiyev.
 * @version Mobile Application Part(MAP) specification (Release 11). MAP
 * Dialogue version > 1
 */
public abstract class MAPDialogue {

    private final Logger logger = LogManager.getLogger(MAPDialogue.class);
    protected MAPStackImpl stack;
    private TCAPDialogue tcapDialogue;
    private MAPDialogueState mapDialogueState = MAPDialogueState.IDLE;
    private ISDNAddressString destinationReference;
    private ISDNAddressString originatingReference;
    private ExtensionContainer specificInformation;
    private MAPApplicationContextImpl mapApplicationContext;
    private Object attachment;
    private final HashMap<Short, Future> performingSSMTable = new HashMap<>();
    private byte[] dialogData;
    private MAPListener listener;

    protected MAPDialogue(MAPStackImpl stack, MAPApplicationContextImpl mapApplicationContext,
                          SCCPAddress destinationAddress, ISDNAddressString destinationReference,
                          SCCPAddress originatingAddress, ISDNAddressString originatingReference,
                          ExtensionContainer specificInformation, MessageHandling messageHandling,
                          boolean sequenceControl) throws ResourceLimitationException {
        this(stack, stack.getTcapProvider().createDialogue(new QoS(messageHandling, sequenceControl)), mapApplicationContext);
//        
        this.tcapDialogue.setCalledParty(destinationAddress);
        this.tcapDialogue.setCallingParty(originatingAddress);
//        
        this.destinationReference = destinationReference;
        this.originatingReference = originatingReference;
        this.specificInformation = specificInformation;
        this.mapDialogueState = MAPDialogueState.WAIT_FOR_USER_REQUESTS;
    }

    /**
     * @param stack
     * @param tcapDialogue
     * @param mapApplicationContext
     */
    protected MAPDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        this.stack = stack;
        this.tcapDialogue = tcapDialogue;
        this.mapApplicationContext = mapApplicationContext;
        this.listener = stack.getUsers().get(mapApplicationContext.getMapApplicationContextName());
        if (listener == null) {
            logger.warn("[MAPDialogue]: Constructor. MapListener not found for AC " + mapApplicationContext
                    + ". DialogueId = " + tcapDialogue.getDialogueId());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[MAPDialogue]:Constructor: Creating MAPDialogue. %s", this));
        }
        stack.dialogues.put(tcapDialogue.getDialogueId(), this);
    }

    public Long getDialogueId() {
        return this.tcapDialogue.getDialogueId();
    }

    /**
     * @param mapDialogueState the mapDialogueState to set
     */
    private void setMAPDialogueState(MAPDialogueState mapDialogueState) {
        this.mapDialogueState = mapDialogueState;
    }

    /**
     * @return the mapDialogueState
     */
    public MAPDialogueState getMAPDialogueState() {
        return this.mapDialogueState;
    }

    public void MAPCancelRequest(Short invokeId) throws Exception {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        tcapDialogue.getLock().lock();
        try {
            switch (mapDialogueState) {
                case WAIT_FOR_USER_REQUESTS:
                case DIALOGUE_ACCEPTED:
                case DIALOGUE_ESTABLISHED:
                case DIALOGUE_INITIATED:
                case DIALOGUE_PENDING:
                    this.tcapDialogue.userCancel(invokeId);
                    if (!this.isPerformingSSMExists(invokeId)) {
                        logger.error(String.format("DialogueId = %s. LocalCancel of invocation. "
                                + "No PSSM found. invokeId = %s", getDialogueId(), invokeId));
                    } else {
                        this.terminatePerformingSSM(invokeId);
                    }
                    break;
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }


    /*
     * MAP_DELIMITER_req
     */

    /**
     * @throws java.io.IOException
     * @throws dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException
     */
    public void MAPDelimiterRequest() throws IOException, IncorrectSyntaxException {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        tcapDialogue.getLock().lock();
        try {
            switch (this.mapDialogueState) {
                case WAIT_FOR_USER_REQUESTS:
                    TCBegin tcBegin = new TCBegin(this.tcapDialogue);
                    //If selected map-application-context version 1, then no application-context-must be included
                    // to begin message.Look amt 3GPP TS 29.002 V11.1.0 (2011-12) MAP_DSM9(17)
                    boolean isMAPVersion1Used = this.mapApplicationContext.getMapApplicationContextVersion().value() == 1;
                    if (!isMAPVersion1Used) {
                        tcBegin.setApplicationContext(new ApplicationContextImpl(this.mapApplicationContext.getOid()));
                    }

                    if (this.destinationReference != null
                            || this.originatingReference != null
                            || this.specificInformation != null) {
                        MAPOpenInfo mAPOpenInfo = new MAPOpenInfo();
                        mAPOpenInfo.setDestinationReference(destinationReference);
                        mAPOpenInfo.setOriginationReference(originatingReference);
                        mAPOpenInfo.setExtensionContainer(specificInformation);
                        AsnOutputStream aos = new AsnOutputStream();
                        mAPOpenInfo.encode(aos);

                        UserInformationImpl userInformation = new UserInformationImpl();
                        userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                        userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                        userInformation.setExternalData(aos.toByteArray());
                        tcBegin.setUserInformation(userInformation);
                    }

                    //MAP VERSION 1 logic
                    if (isMAPVersion1Used) {
                        this.mapDialogueState = MAPDialogueState.WAIT_FOR_RESULT;
                    } else {
                        this.mapDialogueState = MAPDialogueState.DIALOGUE_INITIATED;
                    }

                    this.tcapDialogue.sendBegin(tcBegin);
                    break;

                case DIALOGUE_ESTABLISHED:
                case DIALOGUE_ACCEPTED:
                    TCContinue tcContinue = new TCContinue();
                    tcContinue.setApplicationContextName(new ApplicationContextImpl(this.mapApplicationContext.getOid()));
                    tcContinue.setDialogue(this.tcapDialogue);
                    this.mapDialogueState = MAPDialogueState.DIALOGUE_ESTABLISHED;
                    tcapDialogue.sendContinue(tcContinue);
//                    stack.mapCache.storeDialogue(this);
                    break;
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    public void MAPUAbortRequest(AbortReason abortReason) {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        try {
            tcapDialogue.getLock().lock();
            if (this.mapDialogueState == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_INITIATED
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_PENDING
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_ACCEPTED
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                //int TC-U-ABORT request MAP Application Context shall be present 
                //if and only if the abort reason is "application-context-not-supported"
                TCUAbort tcUAbortReq = new TCUAbort();
                tcUAbortReq.setAbortReason(abortReason);
//            tcUAbortReq.setDialogue(this.tcapDialogue);

                //No needing to terminate all RSSM and PSSM separatelly. Dialogue termination will raise 
                //termination all of them
                this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                this.tcapDialogue.sendUAbort(tcUAbortReq);
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param mapUserAbortInfoPDU
     * @throws Exception
     */
    public void MAPUAbortRequest(MAPUserAbortInfo mapUserAbortInfoPDU) throws Exception {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        try {
            tcapDialogue.getLock().lock();
            if (this.mapDialogueState == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_INITIATED
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_PENDING
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_ACCEPTED
                    || this.mapDialogueState == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                //in TC-U-ABORT request MAP Application Context shall be present 
                //if and only if the abort reason is "application-context-not-supported"
                TCUAbort tcUAbortReq = new TCUAbort();
                tcUAbortReq.setAbortReason(AbortReason.USER_SPECIFIC);
//            tcUAbortReq.setDialogue(this.tcapDialogue);
                if (mapUserAbortInfoPDU != null) {
                    AsnOutputStream aos = new AsnOutputStream();
                    mapUserAbortInfoPDU.encode(aos);

                    UserInformationImpl userInformation = new UserInformationImpl();
                    userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                    userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                    userInformation.setExternalData(aos.toByteArray());

                    tcUAbortReq.setUserInformation(userInformation);
                }

                //No needing to terminate all RSSM and PSSM separatelly. Dialogue termination will raise 
                //termination all of them
                this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                this.tcapDialogue.sendUAbort(tcUAbortReq);
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param type normal release; in this case the primitive is mapped onto the protocol
     *             and sent to the peer;
     *             <p>
     *             prearranged end; in this case the primitive is not mapped onto the
     *             protocol. Prearranged end is managed independently by the two users, i.e.
     *             only the request type primitive is required in this case.
     */
    public void MAPCloseRequest(Termination type) {
        this.MAPCloseRequest(type, null);
    }

    /**
     * @param type           normal release; in this case the primitive is mapped onto the protocol
     *                       and sent to the peer;
     *                       <p>
     *                       prearranged end; in this case the primitive is not mapped onto the
     *                       protocol. Prearranged end is managed independently by the two users, i.e.
     *                       only the request type primitive is required in this case.
     * @param mapDialoguePdu One of the dialog PDUs listed below:
     *                       <b>MAPAcceptInfo</b>, <b>MAPCloseInfo</b>, <b>MAPOpenInfo</b>,
     *                       <b>MAPProviderAbortInfo</b>, <b> MAPRefuseInfo</b>,
     *                       <b>MAPUserAbortInfo</b>
     */
    public void MAPCloseRequest(Termination type, MAPDialoguePDU mapDialoguePdu) {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        tcapDialogue.getLock().lock();
        try {
            switch (this.mapDialogueState) {
                case DIALOGUE_INITIATED:
                    TCEnd tcEndReq = new TCEnd();
                    tcEndReq.setDialogue(this.tcapDialogue);
                    tcEndReq.setTermination(Termination.PREARRANGED);

                    if (this.mapApplicationContext.getMapApplicationContextVersion() != MAPApplicationContextVersion.VERSION_1) {
                        tcEndReq.setApplicationContextName(new ApplicationContextImpl(this.mapApplicationContext.getOid()));
                    }

                    //No needing to terminate all RSSM and PSSM separatelly. Dialogue termination will raise 
                    //termination all of them
                    tcapDialogue.sendEnd(tcEndReq);

                    break;
                case DIALOGUE_ACCEPTED:
                    tcEndReq = new TCEnd();
                    tcEndReq.setDialogue(this.tcapDialogue);
                    tcEndReq.setTermination(type);

                    if (this.mapApplicationContext.getMapApplicationContextVersion() != MAPApplicationContextVersion.VERSION_1) {
                        tcEndReq.setApplicationContextName(new ApplicationContextImpl(this.mapApplicationContext.getOid()));
                    }

                    //No needing to terminate all RSSM and PSSM separatelly. Dialogue termination will raise 
                    //termination all of them
                    tcapDialogue.sendEnd(tcEndReq);
                    break;
                case WAIT_FOR_INIT_DATA:
                case DIALOGUE_PENDING:
                case DIALOGUE_ESTABLISHED:
                    tcEndReq = new TCEnd();
                    tcEndReq.setTermination(type);
                    tcEndReq.setDialogue(this.tcapDialogue);

                    //No needing to terminate all RSSM and PSSM separatelly. Dialogue termination will raise 
                    //termination all of them
                    tcapDialogue.sendEnd(tcEndReq);
                    break;
            }
        } finally {
            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.NORMAL);
            tcapDialogue.getLock().unlock();
        }
    }

    protected void onNotice(TRNotice notice) {
        try {
            tcapDialogue.getLock().lock();
            switch (mapDialogueState) {
                case DIALOGUE_INITIATED:
                    MapOpen mapOpen = new MapOpen();
                    mapOpen.setResult(Result.REJECT);
                    mapOpen.setDestinationAddress(notice.getCalledParty());
                    mapOpen.setOriginatingAddress(notice.getCallingParty());
                    mapOpen.setRefuseReason(RefuseReason.REMOTE_NODE_NOT_REACHABLE);
                    stack.fireMapOpenConfirm(mapOpen, this);
                    //I'm closing MAP-Dialogue here, becaus in all diagrams(SMS/LU...) 
                    //on MAP_NOTICE_ind MAP_CLOSE_req called
                    stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                    break;
                case DIALOGUE_ESTABLISHED:
                    stack.fireMapNoticeInd(notice.getErrorReason(), this);
                    //I'm closing MAP-Dialogue here, becaus in all diagrams(SMS/LU...) 
                    //on MAP_NOTICE_ind MAP_CLOSE_req called
                    MAPCloseRequest(Termination.BASIC);
                    break;
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param indication
     */
    protected void onPAbort(TCPAbort indication) {
        try {
//            tcapDialogue.getLock().lock();
            switch (getMAPDialogueState()) {
                case WAIT_FOR_INIT_DATA:
                    TCUAbort tCUAbort = new TCUAbort();
//                tCUAbort.setDialogue(this.tcapDialogue);
                    tCUAbort.setAbortReason(AbortReason.DIALOGUE_REFUSED);
                    this.tcapDialogue.sendUAbort(tCUAbort);
                    return;
                case WAIT_FOR_RESULT:
                case DIALOGUE_INITIATED:
                    switch (indication.getpAbort()) {
                        case NO_COMMON_DIALOGUE_PORTION:
                            MapPAbort mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.VERSION_INCOMPATIBILTY);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            this.stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case UNRECOGNIZED_MESSAGE_TYPE:
                        case INCORRECT_TRANSACTION_PORTION:
                        case BADDLY_FORMATTED_TRANSACTION_PORTION:
                        case ABNORMAL_DIALOGUE:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.PROVIDER_MALFUNCTION);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case UNRECOGNIZED_TRANSACTION_ID:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.SUPPORTING_DIALOGUE_RELEASED);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case RESOURCE_LIMITATION:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.RESOURCE_LIMITATION);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                    }
                    break;
                case DIALOGUE_ESTABLISHED:
                    MapPAbort mapPAbortInd;
                    switch (indication.getpAbort()) {
                        case INCORRECT_TRANSACTION_PORTION:
                            MapOpen mapOpenCnf = new MapOpen();
                            mapOpenCnf.setResult(Result.REJECT);
                            mapOpenCnf.setRefuseReason(RefuseReason.POTENTIAL_VERSION_INCOMPABILITY);

                            this.stack.fireMapOpenConfirm(mapOpenCnf, this);
                            break;
                        case NO_COMMON_DIALOGUE_PORTION:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.VERSION_INCOMPATIBILTY);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            this.stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case UNRECOGNIZED_MESSAGE_TYPE:
                        case BADDLY_FORMATTED_TRANSACTION_PORTION:
                        case ABNORMAL_DIALOGUE:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.PROVIDER_MALFUNCTION);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case UNRECOGNIZED_TRANSACTION_ID:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.SUPPORTING_DIALOGUE_RELEASED);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                        case RESOURCE_LIMITATION:
                            mapPAbortInd = new MapPAbort();
                            mapPAbortInd.setProviderReason(MapPAbort.ProviderReason.RESOURCE_LIMITATION);
                            mapPAbortInd.setSource(MapPAbort.Source.TC_PROBLEM);

                            stack.fireMapProviderAbortInd(mapPAbortInd, this);
                            break;
                    }
                    break;
            }
        } finally {
            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
//            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param indication
     */
    protected void onTCUAbort(TCUAbort indication) {
        try {
//            tcapDialogue.getLock().lock();
            switch (getMAPDialogueState()) {
                case WAIT_FOR_INIT_DATA:
                    TCUAbort tCUAbort = new TCUAbort();
//                tCUAbort.setDialogue(this.tcapDialogue);
                    tCUAbort.setAbortReason(AbortReason.DIALOGUE_REFUSED);
                    tcapDialogue.sendUAbort(tCUAbort);
                    break;
                case WAIT_FOR_RESULT:
                case DIALOGUE_INITIATED:
                    switch (indication.getAbortReason()) {
                        case APPLICATION_CONTEXT_NAME_NOT_SUPPORTED:
                            MapOpen mapOpenCnf = new MapOpen();
                            mapOpenCnf.setResult(Result.REJECT);
                            mapOpenCnf.setRefuseReason(RefuseReason.APPLICATION_CONTEXT_NOT_SUPPORTED);
                            if (indication.getApplicationContextName() != null) {
                                MAPApplicationContextImpl supportedMapApplicationContext
                                        = new MAPApplicationContextImpl(indication.getApplicationContextName().getOid());
                                mapOpenCnf.setMapApplicationContext(supportedMapApplicationContext);
                            }

                            stack.fireMapOpenConfirm(mapOpenCnf, this);

                            break;
                        case USER_SPECIFIC:
                            if (indication.getUserInformation() != null) {
                                AsnInputStream ais = new AsnInputStream(indication.getUserInformation().getExternalData());
                                try {
                                    MAPDialoguePDU mapDialoguePDU = MAPDialoguePDUFactory.createMAPDialoguePDU(ais);

                                    if (mapDialoguePDU != null
                                            && (mapDialoguePDU instanceof MAPUserAbortInfo
                                            || mapDialoguePDU instanceof MAPProviderAbortInfo)) {
                                        MapPAbort mapPAbort = new MapPAbort();
                                        mapPAbort.setProviderReason(MapPAbort.ProviderReason.ABNORMAL_MAP_DIALOGUE);
                                        mapPAbort.setSource(MapPAbort.Source.MAP_PROBLEM);

                                        this.stack.fireMapProviderAbortInd(mapPAbort, this);
                                    }

                                    if (mapDialoguePDU instanceof MAPRefuseInfo) {
                                        MAPRefuseInfo mapRefuseInfo = (MAPRefuseInfo) mapDialoguePDU;

                                        MapOpen mapOpen = new MapOpen();
                                        mapOpen.setResult(Result.REJECT);

                                        if (mapRefuseInfo.getReason() == Reason.INVALID_DESTIONATION_REFERENCE) {
                                            mapOpen.setRefuseReason(RefuseReason.INVALID_DESTINATION_REFERENCE);
                                        }

                                        if (mapRefuseInfo.getReason() == Reason.INVALID_ORIGINATING_REFERENCE) {
                                            mapOpen.setRefuseReason(RefuseReason.INVALID_ORIGINATING_REFERENCE);
                                        }

                                        if (mapRefuseInfo.getReason() == Reason.NO_REASON_GIVEN) {
                                            mapOpen.setRefuseReason(RefuseReason.NO_REASON_GIVEN);
                                        }

                                        this.stack.fireMapOpenConfirm(mapOpen, this);
                                    }
                                } catch (IncorrectSyntaxException | UnexpectedDataException ex) {
                                    logger.error("{}", ex);
                                    //Fire UAbort to MAPUser anyway
                                    MapOpen mapOpen = new MapOpen();
                                    mapOpen.setResult(Result.REJECT);
                                    mapOpen.setRefuseReason(RefuseReason.NO_REASON_GIVEN);

                                    this.stack.fireMapOpenConfirm(mapOpen, this);
                                }
                            } else {
                                mapOpenCnf = new MapOpen();
                                mapOpenCnf.setResult(Result.REJECT);
                                mapOpenCnf.setRefuseReason(RefuseReason.POTENTIAL_VERSION_INCOMPABILITY);
                                this.stack.fireMapOpenConfirm(mapOpenCnf, this);
                            }
                            break;
                    }
                    break;
                case DIALOGUE_ESTABLISHED:
                    if (indication.getUserInformation() != null) {
                        AsnInputStream ais = new AsnInputStream(indication.getUserInformation().getExternalData());
                        try {
                            MAPDialoguePDU mapDialoguePDU = MAPDialoguePDUFactory.createMAPDialoguePDU(ais);
                            if (mapDialoguePDU instanceof MAPProviderAbortInfo) {
                                MapPAbort mapPAbort = new MapPAbort();
                                mapPAbort.setSource(MapPAbort.Source.TC_PROBLEM);
                                mapPAbort.setProviderReason(MapPAbort.ProviderReason.PROVIDER_MALFUNCTION);

                                this.stack.fireMapProviderAbortInd(mapPAbort, this);
                            }
                            if (mapDialoguePDU instanceof MAPUserAbortInfo) {
                                MapUAbort mapUAbort = new MapUAbort();
                                mapUAbort.setUserReason(MapUAbort.UserReason.PROCEDURE_ERROR);

                                this.stack.fireMapUserAbortInd(mapUAbort, this);

                            }
                        } catch (IncorrectSyntaxException | UnexpectedDataException ex) {
                            logger.error("{}", ex);
                            MapPAbort mapPAbort = new MapPAbort();
                            mapPAbort.setSource(MapPAbort.Source.MAP_PROBLEM);
                            mapPAbort.setProviderReason(MapPAbort.ProviderReason.ABNORMAL_MAP_DIALOGUE);

                            this.stack.fireMapProviderAbortInd(mapPAbort, this);
                        }
                    } else {
                        logger.error("Error expecting UserInformation. Dialog " + this);
                    }
                    break;
            }
        } finally {
            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
//            tcapDialogue.getLock().unlock();
        }

    }

    /**
     * @param indication
     */
    protected void onTCLCancel(TCLCancel indication) {
        try {
            tcapDialogue.getLock().lock();
            switch (getMAPDialogueState()) {
                case WAIT_FOR_INIT_DATA:
                    TCUAbort tCUAbort = new TCUAbort();
//                tCUAbort.setDialogue(this.tcapDialogue);
                    tCUAbort.setAbortReason(AbortReason.DIALOGUE_REFUSED);
                    tcapDialogue.sendUAbort(tCUAbort);
                    stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                    break;
                case WAIT_FOR_RESULT:
                case DIALOGUE_INITIATED:
                    MapOpen mapOpenCnf = new MapOpen();
                    mapOpenCnf.setResult(Result.ACCEPTED);
                    this.stack.fireMapOpenConfirm(mapOpenCnf, this);
                    this.onTimerExpiry(indication.getInvokeId(), indication.getOperationCode());
                    break;
                case DIALOGUE_ESTABLISHED:
                    this.onTimerExpiry(indication.getInvokeId(), indication.getOperationCode());
                    break;
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * TC_BEGIN Indication from TCAP layer
     *
     * @param encodedData
     * @param indication
     */
    protected void onTCBegin(byte[] encodedData, TCBegin indication) {
//        mapDialogueLock.lock();
        try {
//            tcapDialogue.getLock().lock();
            this.dialogData = encodedData;
            if (this.getMAPDialogueState() == MAPDialogueState.IDLE) {
                if (indication.isApplicationContextIncluded()) {

                    this.mapApplicationContext = new MAPApplicationContextImpl(indication.getApplicationContextName().getOid());

                    //ApplicationContext should be null in case AC V1
                    if (this.mapApplicationContext.getMapApplicationContextVersion() == MAPApplicationContextVersion.VERSION_1) {
                        logger.error("Version1 dialogue received, with included AC:" + this + ". Abort dialogue");

                        this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);

                        AsnOutputStream aos = new AsnOutputStream();

                        MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
                        mapProviderAbortInfo.setMapProviderAbortReason(MapProviderAbortReason.ABNORMAL_DIALOGUE);
                        mapProviderAbortInfo.encode(aos);

                        UserInformationImpl userInformation = new UserInformationImpl();
                        userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                        userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                        userInformation.setExternalData(aos.toByteArray());

                        TCUAbort tcUAbort = new TCUAbort();
//                        tcUAbort.setDialogue(indication.getDialogue());
                        tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                        tcUAbort.setUserInformation(userInformation);

                        this.tcapDialogue.sendUAbort(tcUAbort);

                    } else if (indication.getUserInformation() != null) {
                        MAPDialoguePDU mapDialoguePDU;
                        try {
                            mapDialoguePDU = MAPDialoguePDUFactory.createMAPDialoguePDU(new AsnInputStream(indication.getUserInformation().getExternalData()));
                        } catch (IncorrectSyntaxException ex) {
                            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);

                            logger.error("Abort dialogue" + this, ex);

                            AsnOutputStream aos = new AsnOutputStream();

                            MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
                            mapProviderAbortInfo.setMapProviderAbortReason(MapProviderAbortReason.ABNORMAL_DIALOGUE);
                            mapProviderAbortInfo.encode(aos);

                            UserInformationImpl userInformation = new UserInformationImpl();
                            userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                            userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                            userInformation.setExternalData(aos.toByteArray());

                            TCUAbort tcUAbort = new TCUAbort();
                            tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                            tcUAbort.setUserInformation(userInformation);

                            this.tcapDialogue.sendUAbort(tcUAbort);

                            return;
                        }

                        if (mapDialoguePDU != null &&
                                mapDialoguePDU.getClass() == MAPOpenInfo.class) {

                            MAPOpenInfo mapOpenInfo = (MAPOpenInfo) mapDialoguePDU;

//                            this.isACSupporting(this.mapApplicationContext);//Select Service on base of AC
                            MapOpen mapOpen = new MapOpen();
                            mapOpen.setMapApplicationContext(this.mapApplicationContext);

                            mapOpen.setDestinationAddress(tcapDialogue.getCalledParty());
                            mapOpen.setOriginatingAddress(tcapDialogue.getCallingParty());
                            mapOpen.setDestinationReference(mapOpenInfo.getDestinationReference());
                            mapOpen.setOriginatingReference(mapOpenInfo.getOriginationReference());
                            mapOpen.setSpecificInformation(mapOpenInfo.getExtensionContainer());
                            mapOpen.setResult(Result.ACCEPTED);

                            this.destinationReference = mapOpenInfo.getDestinationReference();
                            this.originatingReference = mapOpenInfo.getOriginationReference();

                            this.setMAPDialogueState(MAPDialogueState.DIALOGUE_ACCEPTED);
                            this.stack.fireMapOpenInd(mapOpen, this);

//                                stack.mapCache.storeDialogue(this);
                            if (indication.getComponents() != null
                                    && indication.getComponents().size() > 0) {

                                List<Operation> componets = indication.getComponents();
                                for (Operation operation : componets) {
                                    if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                        logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                                + this);
                                        return;
                                    }
                                    this.onOperation(operation);
                                }

                            }
                            this.stack.fireMapDelimiterInd(this);

                        } else {
                            logger.error("Expecting MAPOpenInfo MAPDialoguePDU, found " + mapDialoguePDU.getClass() + ":Abort dialogue " + this);

                            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);

                            AsnOutputStream aos = new AsnOutputStream();

                            MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
                            mapProviderAbortInfo.setMapProviderAbortReason(MapProviderAbortReason.ABNORMAL_DIALOGUE);
                            mapProviderAbortInfo.encode(aos);

                            UserInformationImpl userInformation = new UserInformationImpl();
                            userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                            userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                            userInformation.setExternalData(aos.toByteArray());

                            TCUAbort tcUAbort = new TCUAbort();
//                            tcUAbort.setDialogue(indication.getDialogue());
                            tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                            tcUAbort.setUserInformation(userInformation);

                            this.tcapDialogue.sendUAbort(tcUAbort);

                        }
                    } else if (this.mapApplicationContext.getMapApplicationContextName().isUserInformationRequired()) {
                        logger.error("AC required UserInformation but not found. Abort dialogue" + this);

                        this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                        AsnOutputStream aos = new AsnOutputStream();

                        MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
                        mapProviderAbortInfo.setMapProviderAbortReason(MapProviderAbortReason.ABNORMAL_DIALOGUE);
                        mapProviderAbortInfo.encode(aos);

                        UserInformationImpl userInformation = new UserInformationImpl();
                        userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                        userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
                        userInformation.setExternalData(aos.toByteArray());

                        TCUAbort tcUAbort = new TCUAbort();
//                        tcUAbort.setDialogue(indication.getDialogue());
                        tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                        tcUAbort.setUserInformation(userInformation);

                        this.tcapDialogue.sendUAbort(tcUAbort);

                    } else {

//                            this.isACSupporting(this.mapApplicationContext);//Select Service on base of AC
                        MapOpen mapOpen = new MapOpen();
                        mapOpen.setMapApplicationContext(this.mapApplicationContext);

                        mapOpen.setDestinationAddress(tcapDialogue.getCalledParty());
                        mapOpen.setOriginatingAddress(tcapDialogue.getCallingParty());
                        mapOpen.setResult(Result.ACCEPTED);

                        this.stack.fireMapOpenInd(mapOpen, this);

                        this.setMAPDialogueState(MAPDialogueState.DIALOGUE_ACCEPTED);
//                            stack.mapCache.storeDialogue(this);

                        if (indication.getComponents() != null
                                && indication.getComponents().size() > 0) {
                            List<Operation> components = indication.getComponents();

                            for (Operation operation : components) {
                                if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                    logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                            + this);
                                    return;
                                }

                                this.onOperation(operation);
                            }
                        }
                        this.stack.fireMapDelimiterInd(this);

                    }
                } else {
                    //ApplicationContext not included that is means, Mobile Application Part Version 1.0 used

                    List<Operation> components = indication.getComponents();
                    MapOpen mapOpen = new MapOpen();

//                        this.isACSupporting(this.mapApplicationContext);
                    mapOpen.setMapApplicationContext(this.mapApplicationContext);

                    mapOpen.setDestinationAddress(tcapDialogue.getCalledParty());
                    mapOpen.setOriginatingAddress(tcapDialogue.getCallingParty());
                    mapOpen.setResult(Result.ACCEPTED);

                    this.stack.fireMapOpenInd(mapOpen, this);
                    for (Operation operation : components) {
                        if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                            logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                    + this);
                            return;
                        }

                        this.onOperation(operation);
                    }

                    this.setMAPDialogueState(MAPDialogueState.WAIT_FOR_INIT_DATA);
//                        stack.mapCache.storeDialogue(this);

                }
            }
        } catch (UnexpectedDataException | IncorrectSyntaxException exception) {
            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
            logger.error("ErrorOccured. Abort dialogue " + this, exception);

            TCUAbort tcUAbort = new TCUAbort();
//            tcUAbort.setDialogue(indication.getDialogue());
            tcUAbort.setAbortReason(AbortReason.NULL);
            this.tcapDialogue.sendUAbort(tcUAbort);
        } finally {
//            mapDialogueLock.unlock();
//            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param indication
     */
    protected void onTCEnd(TCEnd indication) {
//        mapDialogueLock.lock();
        try {
            tcapDialogue.getLock().lock();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("TC_END received. State = %s. %s", this.getMAPDialogueState(), indication));
            }
            switch (getMAPDialogueState()) {
                case WAIT_FOR_INIT_DATA:

                    TCUAbort tCUAbort = new TCUAbort();
//                tCUAbort.setDialogue(this.tcapDialogue);
                    tCUAbort.setAbortReason(AbortReason.DIALOGUE_REFUSED);
                    try {
                        this.tcapDialogue.sendUAbort(tCUAbort);
                    } catch (Exception ex) {
                        logger.error("{}", ex);
                        return;
                    }
                    return;
                case DIALOGUE_INITIATED:
                    if (indication.getApplicationContextName() != null
                            && Arrays.equals(this.mapApplicationContext.getOid(), indication.getApplicationContextName().getOid())) {

                        MapOpen mapOpen = new MapOpen();
                        mapOpen.setResult(Result.ACCEPTED);
                        mapOpen.setMapApplicationContext(this.mapApplicationContext);

                        mapOpen.setDestinationAddress(tcapDialogue.getCalledParty());
                        mapOpen.setOriginatingAddress(tcapDialogue.getCallingParty());

                        if (logger.isDebugEnabled()) {
                            logger.debug("FireMapOpenConfigrmation");
                        }

                        this.stack.fireMapOpenConfirm(mapOpen, this);

                        if (indication.getComponents() != null
                                && indication.getComponents().size() > 0) {

                            List<Operation> componets = indication.getComponents();
                            componets.forEach((operation) -> {
                                if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                    logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                            + this);
                                    return;
                                }

                                this.onOperation(operation);
                            });

                        }

                        this.stack.fireMapCloseInd(this);

                    } else {
                        MapPAbort mapPAbort = new MapPAbort();

                        mapPAbort.setProviderReason(MapPAbort.ProviderReason.ABNORMAL_MAP_DIALOGUE);
                        mapPAbort.setSource(MapPAbort.Source.MAP_PROBLEM);
                        this.stack.fireMapProviderAbortInd(mapPAbort, this);
                    }
                    break;
                case DIALOGUE_ESTABLISHED:

                    if (indication.getComponents() != null
                            && indication.getComponents().size() > 0) {

                        List<Operation> componets = indication.getComponents();
                        componets.forEach((operation) -> {
                            if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                        + this);
                                return;
                            }

                            this.onOperation(operation);
                        });
                    }

                    this.stack.fireMapCloseInd(this);

                    break;
                //MAP VERSION 1 Logic
                case WAIT_FOR_RESULT:
                    if (indication.getComponents() == null
                            || indication.getComponents().size() <= 0) {

                        MapPAbort mapPAbort = new MapPAbort();
                        mapPAbort.setProviderReason(MapPAbort.ProviderReason.ABNORMAL_MAP_DIALOGUE);
                        mapPAbort.setSource(MapPAbort.Source.MAP_PROBLEM);
                        this.stack.fireMapProviderAbortInd(mapPAbort, this);
                    } else {

                        MapOpen mapOpen = new MapOpen();
                        mapOpen.setResult(Result.ACCEPTED);
                        mapOpen.setMapApplicationContext(this.mapApplicationContext);
                        mapOpen.setDestinationAddress(tcapDialogue.getCalledParty());
                        mapOpen.setOriginatingAddress(tcapDialogue.getCallingParty());

                        this.stack.fireMapOpenConfirm(mapOpen, this);

                        List<Operation> componets = indication.getComponents();
                        for (Operation operation : componets) {
                            if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                        + this);
                                return;
                            }

                            this.onOperation(operation);
                        }

                        this.stack.fireMapCloseInd(this);
                    }
                    break;
            }
        } finally {
            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.NORMAL);
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @param indication
     */
    protected void onTCContinue(TCContinue indication) {
//        mapDialogueLock.lock();
        try {
            tcapDialogue.getLock().lock();
            switch (getMAPDialogueState()) {
                case WAIT_FOR_INIT_DATA:
                    stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);

                    TCUAbort tCUAbort = new TCUAbort();
//                tCUAbort.setDialogue(this.tcapDialogue);
                    tCUAbort.setAbortReason(AbortReason.DIALOGUE_REFUSED);

                    tcapDialogue.sendUAbort(tCUAbort);
                    break;
                case DIALOGUE_INITIATED:
                    if (indication.getApplicationContextName() != null
                            && Arrays.equals(indication.getApplicationContextName().getOid(), this.mapApplicationContext.getOid())) {
                        MapOpen mapOpenCnf = new MapOpen();
                        mapOpenCnf.setResult(Result.ACCEPTED);

                        if (indication.getApplicationContextName() != null) {
                            mapOpenCnf.setMapApplicationContext(new MAPApplicationContextImpl(indication.getApplicationContextName().getOid()));
                        }

                        this.setMAPDialogueState(MAPDialogueState.DIALOGUE_ESTABLISHED);
//                        stack.mapCache.storeDialogue(this);

                        stack.fireMapOpenConfirm(mapOpenCnf, this);

                        if (indication.getComponents() != null
                                && indication.getComponents().size() > 0) {
                            List<Operation> componets = indication.getComponents();
                            for (Operation operation : componets) {
                                if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                    logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                            + this);
                                    return;
                                }

                                this.onOperation(operation);
                            }
                            stack.fireMapDelimiterInd(this);
                        }

                    } else {
                        try {
                            MapPAbort mapPAbort = new MapPAbort();
                            mapPAbort.setProviderReason(MapPAbort.ProviderReason.ABNORMAL_MAP_DIALOGUE);
                            mapPAbort.setSource(MapPAbort.Source.MAP_PROBLEM);
                            stack.fireMapProviderAbortInd(mapPAbort, this);

                            AsnOutputStream aos = new AsnOutputStream();

                            MAPProviderAbortInfo mapProviderAbortInfoPDU = new MAPProviderAbortInfo();
                            mapProviderAbortInfoPDU.setMapProviderAbortReason(MapProviderAbortReason.ABNORMAL_DIALOGUE);
                            mapProviderAbortInfoPDU.encode(aos);

                            UserInformationImpl userInformation = new UserInformationImpl();
                            userInformation.setDirectReference(ObjectIdentifiers.MAP_DIALOGUE_AS.value());
                            userInformation.setExternalData(aos.toByteArray());

                            TCUAbort tcUAbortReq = new TCUAbort();
                            tcUAbortReq.setAbortReason(AbortReason.USER_SPECIFIC);
                            tcUAbortReq.setApplicationContextName(indication.getApplicationContextName());
                            tcUAbortReq.setUserInformation(userInformation);

                            tcapDialogue.sendUAbort(tcUAbortReq);
                        } catch (IncorrectSyntaxException ex) {
                            logger.error("ErrorOccured:", ex);
                        } finally {
                            this.stack.releaseMapDialogue(this.getDialogueId(), TerminationReason.ABNORMAL);
                        }
                    }
                    break;
                case DIALOGUE_ESTABLISHED:
                    if (indication.getComponents() != null
                            && indication.getComponents().size() > 0) {
                        List<Operation> componets = indication.getComponents();
                        for (Operation operation : componets) {
                            if (getMAPDialogueState() == MAPDialogueState.CLOSING) {
                                logger.error("MAPDialogue is in CLOSING state. Skipping all pending invocations: "
                                        + this);
                                return;
                            }

                            this.onOperation(operation);
                        }
                        stack.fireMapDelimiterInd(this);
                    }
                    break;
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    /**
     * @return the tcapDialogue
     */
    public final TCAPDialogue getTcapDialogue() {
        return tcapDialogue;
    }

    /**
     * @param indication
     */
    protected void onTCInvoke(TCInvoke indication) {
        OperationCodes opCode = OperationCodes.getInstance(indication.getOperationCode());
        if (performingSSMTable.containsKey(indication.getInvokeID())) {//Invoke id already assigned
            logger.error(String.format("TCInvoke received."
                    + "InvokeId already assigned. Generate Reject. DialogueId[%d]: %s", this.getDialogueId(), indication));
            this.tcapDialogue.tcURejectRequest(indication.getInvokeID(), InvokeProblem.DUPLICATE_INVOKE_ID);

            this.stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        } else if (isOperationExists(mapApplicationContext, opCode)) {
            //Not linked operation or linked operation and operation class is not 4
            try {
//                if (indication.getLinkedId() == null) {
                this.processInvocation(opCode, indication);
//                } else {
//                    this.processLinkedInvocation(opCode, indication);
//                }
            } catch (IncorrectSyntaxException ex) {

                logger.error("MistypedParameterException. " + this, ex);
                tcapDialogue.tcURejectRequest(indication.getInvokeID(), InvokeProblem.MISTYPED_PARAMETER);
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (UnexpectedDataException ex) {

                logger.error("UnidentifableServiceException." + this, ex);

                OperationClass operationClass = opCode.operationClass();

                if (operationClass == OperationClass.CLASS3 || operationClass == OperationClass.CLASS4) {
                    return;
                }
                tcapDialogue.sendUserError(indication.getInvokeID(), MAPUserErrorValues.UNEXPECTED_DATA_VALUE.value());
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (NoServiceParameterAvailableException ex) {
                logger.error("DataMissingException." + this, ex);

                OperationClass operationClass = OperationCodes.getInstance(indication.getOperationCode()).operationClass();
                if (operationClass == OperationClass.CLASS3 || operationClass == OperationClass.CLASS4) {
                    return;
                }

                tcapDialogue.sendUserError(indication.getInvokeID(), MAPUserErrorValues.DATA_MISSING.value());
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            }
        } else {
            logger.error(String.format("TCInvoke received. "
                    + "Cannot select correct MapService. Generate Reject"
                    + "DialogueId[%d]: %s", this.getDialogueId(), indication));
            tcapDialogue.tcURejectRequest(indication.getInvokeID(), InvokeProblem.UNRECOGNIZED_OPERATION);

            if (this.mapApplicationContext.getMapApplicationContextVersion().value() < 3) {
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            }
        }
    }

    private void processInvocation(OperationCodes operationCode, TCInvoke indication) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {

        Short invokeId = indication.getInvokeID();

        //Confirmed opcode. We have to store this PSSM, as we will send response and terminate timer.
        if (operationCode.operationClass() != OperationClass.CLASS4) {
            Future future = stack.getGuardTimer().schedule(new GuardTimerTask(this, invokeId), stack.getGuardTimerValue(), TimeUnit.SECONDS);
            performingSSMTable.put(invokeId, future);
        }
        this.onServiceInvocationReceived(indication);
    }

//    private void processLinkedInvocation(OperationCodes opCode, TCInvoke indication) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
//        RequestingMapSSM requestingSsm = stack.mapCache.getRequestingSSM(getDialogueId(), indication.getLinkedId());
//        if (requestingSsm == null) {
//            tcapDialogue.tcURejectRequest(indication.getInvokeID(), InvokeProblem.LINKED_RESPONSE_UNEXPECTED);
//            stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
//            logger.error(String.format("LinkedId not assigned. %s , %s", indication, this));
//        } else if (indication.getOperationClass() == OperationClass.CLASS4) {
//            stack.mapCache.linkedOperationInvoked(getDialogueId(), indication.getInvokeID());
//            if (requestingSsm.getOperation().isLinkedOperationAllowed(opCode)) {
//                this.onLinkedRequestReceived(indication);
//            } else {
//                this.onNegativeResultReceived(opCode.value(), ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeID());
//                this.getTcapDialogue().tcURejectRequest(indication.getInvokeID(), ReturnResultProblem.MISTYPED_PARAMETER);
//            }
//        } else {
//            stack.mapCache.linkedOperationInvoked(getDialogueId(), indication.getLinkedId());
//            stack.mapCache.implicitConfirmation(getDialogueId(), indication.getLinkedId());
//
//            this.onLinkedServiceInvoked(indication);
//            stack.mapCache.initiatePssm(this.getDialogueId(), indication.getInvokeID(), stack.getGuardTimerValue());
//            this.onServiceInvocationReceived(indication);
//        }
//    }

    /**
     * @param indication
     */
    protected void onTCResultLast(TCResult indication) {
        //Assume that if TCResultLast came then MAP-Service is confirmable
        InvocationStateMachine requestingMAPSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

        if (requestingMAPSSM == null) {
            logger.error("onTCResultLast: RequestingSSM = null");
            tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);

            stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            return;
        }

        this.onResultReceived(indication, requestingMAPSSM.getOperationCode());
    }

    /**
     * @param indication
     */
    protected void onTCLReject(TCLReject indication) {

        if (!indication.isInvokeIdPresent()) {
            this.stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            return;
        }

        ProblemImpl problem = indication.getProblem();

        if (problem.isInvokeProblem()) {
            InvocationStateMachine requestingMapSSM
                    = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

            if (requestingMapSSM == null) {
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } else {
                this.onNegativeResultReceived(requestingMapSSM.getOperationCode(), ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
            }
        } else {
            stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        }
    }

    /**
     * @param indication
     */
    protected void onTCRReject(TCRReject indication) {
        if (indication.getProblem().isInvokeProblem()) {
            InvocationStateMachine requestingMapSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());
            if (requestingMapSSM == null) {
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            } else {
                this.onNegativeResultReceived(requestingMapSSM.getOperationCode(), ProviderError.SERVICE_COMPLETION_FAILURE, indication.getInvokeId());
            }
        } else {
            ErrorReason mapDiagnostics = ErrorReason.RESPONSE_REJECTED_BY_PEER;
            if (indication.getProblem().isGeneralProblem()) {
                mapDiagnostics = ErrorReason.ABNORMAL_EVENT_DETECTED_BY_PEER;
            }
            stack.fireMapNoticeInd(mapDiagnostics, this);
        }
    }

    /**
     * @param indication
     */
    private void onTCUReject(TCUReject indication) {

        if (indication.getProblem().isInvokeProblem()) {
            InvocationStateMachine requestingMapSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());
            if (requestingMapSSM == null) {
                stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            } else {
                this.onNegativeResultReceived(requestingMapSSM.getOperationCode(), ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
            }
        } else {
            stack.fireMapNoticeInd(ErrorReason.RESPONSE_REJECTED_BY_PEER, this);
        }
    }

    /**
     * @param indication
     */
    protected void onTCUError(TCUError indication) {
        InvocationStateMachine requestingMapSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

        if (requestingMapSSM == null) {
            tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnErrorProblem.UNRECOGNIZED_INVOKE_ID);
            stack.fireMapNoticeInd(ErrorReason.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        } else {

            int operation = requestingMapSSM.getOperationCode();

            try {
                MAPUserError mapUserError = MAPUserErrorFactory.createMAPUserErrorFromIndication(indication.getError(), indication.getParameter());
                this.onNegativeResultReceived(operation, mapUserError, indication.getInvokeId());
            } catch (IncorrectErrorCodeException | IncorrectSyntaxException | NoServiceParameterAvailableException | UnexpectedDataException ex) {
                logger.error("ErrorOccured:", ex);
                this.onNegativeResultReceived(operation, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
                this.getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnErrorProblem.MISTYPED_PARAMETER);
            }

//            stack.mapCache.terminateRssm(getDialogueId(), indication.getInvokeId());
        }
    }

    /**
     * @return the destinationReference
     */
    public ISDNAddressString getDestinationReference() {
        return destinationReference;
    }

    /**
     * @param destinationReference the destinationReference to set
     */
    public void setDestinationReference(ISDNAddressString destinationReference) {
        this.destinationReference = destinationReference;
    }

    /**
     * @return the originatingReference
     */
    public ISDNAddressString getOriginatingReference() {
        return originatingReference;
    }

    /**
     * @param originatingReference the originatingReference to set
     */
    public void setOriginatingReference(ISDNAddressString originatingReference) {
        this.originatingReference = originatingReference;
    }

    /**
     * @param tcapDialogue the tcapDialogue to set
     */
    protected void setTcapDialogue(TCAPDialogue tcapDialogue) {
        this.tcapDialogue = tcapDialogue;
    }

    public void setMapDialogueState(MAPDialogueState mapDialogueState) {
        this.mapDialogueState = mapDialogueState;
    }

    public void setSpecificInformation(ExtensionContainer specificInformation) {
        this.specificInformation = specificInformation;
    }

    public void setMapProvider(MAPStackImpl stack) {
        this.stack = stack;
    }

    public void setMapApplicationContext(MAPApplicationContextImpl mapApplicationContext) {
        this.mapApplicationContext = mapApplicationContext;
    }

    protected void onOperation(Operation indication) {
        switch (indication.getOperation()) {
            case TCINVOKE:
                this.onTCInvoke((TCInvoke) indication);
                break;
            case TCLREJECT:
                this.onTCLReject((TCLReject) indication);
                break;
            case TCRETURNRESULTLAST:
                this.onTCResultLast((TCResult) indication);
                break;
            case TCRREJECT:
                this.onTCRReject((TCRReject) indication);
                break;
            case TCUERROR:
                this.onTCUError((TCUError) indication);
                break;
            case TCUREJECT:
                this.onTCUReject((TCUReject) indication);
                break;
        }
    }

    public SCCPAddress getCallingAddress() {
        return this.tcapDialogue.getCallingParty();
    }

    public SCCPAddress getCalledAddress() {
        return this.tcapDialogue.getCalledParty();
    }

    public MAPApplicationContextImpl getMapApplicationContext() {
        return mapApplicationContext;
    }

    public void setAttachment(Object attachment) {
        ReentrantLock lock = tcapDialogue.getLock();
        lock.lock();
        try {
            this.attachment = attachment;
        } finally {
            lock.unlock();
        }
    }

    public Object getAttachment() {
        ReentrantLock lock = tcapDialogue.getLock();
        lock.lock();
        try {
            return attachment;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MapDialogue[");
        sb.append("DialogueId=").append(this.getDialogueId()).append(";");
        sb.append("State=").append(this.mapDialogueState).append(";");
        sb.append("CallingAddress=").append(tcapDialogue.getCallingParty()).append(";");
        sb.append("CalledAddress=").append(tcapDialogue.getCalledParty()).append(";");
        sb.append("OriginatingReference=").append(this.originatingReference).append(";");
        sb.append("DestinatingReference=").append(this.destinationReference).append(";");
        sb.append("ApplicationContext=").append(this.mapApplicationContext).append(";");
        sb.append("Qos=").append(this.getQos());
        sb.append("]");
        return sb.toString();
    }

    boolean isOperationExists(MAPApplicationContextImpl mapApplicationContext, OperationCodes operationCode) {
        MAPApplicationContextName mapApplicationContextName = mapApplicationContext.getMapApplicationContextName();
        MAPApplicationContextVersion mapApplicationContextVersion = mapApplicationContext.getMapApplicationContextVersion();
        return mapApplicationContextName.isOperationSupported(operationCode, mapApplicationContextVersion);
    }

    /**
     * @param respondingAddress the respondingAddress to set
     */
    public void setRespondingAddress(SCCPAddress respondingAddress) {
        try {
            tcapDialogue.getLock().lock();
            if (mapDialogueState == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || mapDialogueState == MAPDialogueState.WAIT_FOR_INIT_DATA
                    || mapDialogueState == MAPDialogueState.IDLE
                    || mapDialogueState == MAPDialogueState.DIALOGUE_ACCEPTED) {
                tcapDialogue.setCalledParty(respondingAddress);
                logger.info("Responding address set:" + respondingAddress);
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    protected final void onTimerExpiry(Short invokeId, int operation) {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode.operationClass()) {
            case CLASS1:
            case CLASS2:
            case CLASS3:
                this.onNegativeResultReceived(operation, ProviderError.NO_RESPONSE_FROM_PEER, invokeId);
                break;
        }
    }

    public void sendNegativeResponse(Short invokeId, MAPUserError userError) throws Exception {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        try {
            tcapDialogue.getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.performingSSMTable.containsKey(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                AsnOutputStream aos = new AsnOutputStream();
                userError.encode(aos);

                ParameterImpl parameter = null;

                if (aos.size() > 0) {
                    parameter = new ParameterImpl(aos.toByteArray());
                }

                this.terminatePerformingSSM(invokeId);
                tcapDialogue.sendUserError(invokeId, userError.getMAPUserErrorValue().value(), parameter);
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    public final void sendNegativeResponse(Short invokeId, MAPUserErrorValues userError, ParameterImpl parameter) throws Exception {
        ThreadContext.put("correlation_id", String.valueOf(getDialogueId()));
        try {
            tcapDialogue.getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.performingSSMTable.containsKey(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }
                this.terminatePerformingSSM(invokeId);
                tcapDialogue.sendUserError(invokeId, userError.value(), parameter);
            }
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    public void onLinkedServiceInvoked(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {

    }

    public void onLinkedRequestReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {

    }

    protected abstract void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException;

    protected abstract void onResultReceived(TCResult indication, int operationCode);

    protected abstract void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId);

    protected abstract void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId);

    protected byte[] serialize() {
        AsnOutputStream aos = new AsnOutputStream();
        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, mapDialogueState.value());

            if (destinationReference != null) {
                destinationReference.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (originatingReference != null) {
                originatingReference.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (specificInformation != null) {
                specificInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            aos.writeObjectIdentifier(Tag.CLASS_CONTEXT_SPECIFIC, 4, mapApplicationContext.getOid());

            if (attachment != null) {
//                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 5, attachment);
            }

        } catch (AsnException | IOException | IncorrectSyntaxException ex) {
            logger.error(String.format("Error occured during serialize MAP dialogue. Dialogue = [%s]", this), ex);
        }
        return aos.toByteArray();
    }

    protected boolean isPerformingSSMExists(Short invokeId) {
        return performingSSMTable.containsKey(invokeId);
    }

    protected void terminatePerformingSSM(Short invokeId) {
        Future future = performingSSMTable.remove(invokeId);
        if (future != null) {
            future.cancel(false);
        }
    }

    protected void terminateAllPerformingSSMs() {
        tcapDialogue.getLock().lock();
        try {
            performingSSMTable.values().forEach((future) -> {
                future.cancel(false);
            });

            performingSSMTable.clear();
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    public byte[] getDialogData() {
        return dialogData;
    }

    public QoS getQos() {
        return tcapDialogue.getQos();
    }

    public MAPListener getMAPUser() {
        return listener;
    }

    public enum TerminationReason {
        NORMAL,
        TIMEOUT,
        ABNORMAL,
    }
}
