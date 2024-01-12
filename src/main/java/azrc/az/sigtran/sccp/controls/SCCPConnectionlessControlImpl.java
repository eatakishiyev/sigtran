/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.controls;

import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SignallingPointCode;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.exception.RoutingFailureException;
import azrc.az.sigtran.sccp.general.*;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.messages.connectionless.*;
import azrc.az.sigtran.sccp.messages.management.*;
import azrc.az.sigtran.sccp.parameters.Segmentation;
import azrc.az.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author eatakishiyev
 */
public class SCCPConnectionlessControlImpl implements SCCPConnectionlessControl {

//    private static transient Logger logger = SCCPStackImpl.logger;
    protected final SCCPStackImpl sccpStack;
    private final Logger logger = LoggerFactory.getLogger(SCCPConnectionlessControlImpl.class);
    protected final Map<String, Reassembler> segments = Collections.synchronizedMap(new HashMap());
    protected final ScheduledExecutorService reassamblerTimer;

    public SCCPConnectionlessControlImpl(SCCPStackImpl sccpStack) throws Exception {
        this.sccpStack = sccpStack;
        this.reassamblerTimer = Executors.newScheduledThreadPool(10);
    }

    @Override
    public void messageForLocalSubsystem(SCCPMessage message, boolean localOriginator) throws Exception {
        SubSystemNumber ssn = message.getCalledPartyAddress().getSubSystemNumber();
        SubSystem subSystem = this.sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);



        switch (message.getType()) {
            case UDT:
                if (message.getCalledPartyAddress().getSubSystemNumber() == SubSystemNumber.SCCP_MANAGEMENT) {
                    this.processManagementMessage(message);
                } else {

                    if (subSystem == null) {
                        logger.error("Unplugged subsystem. SSN = " + message.getCalledPartyAddress().getSubSystemNumber());
                        return;
                    }
                    subSystem.onMessage(message);
                }

                break;
            case UDTS:
                subSystem.onNotice(message.getCalledPartyAddress(), message.getCallingPartyAddress(),
                        message.getData(),
                        ((UnitDataService) message).getReturnCause(), 0);
                break;
            case XUDT:
                if (message.getCalledPartyAddress().getSubSystemNumber() == SubSystemNumber.SCCP_MANAGEMENT) {
                    this.processManagementMessage(message);
                } else {

                    XUnitData xUnitData = (XUnitData) message;
                    Segmentation segmentation = xUnitData.getSegmentation();
                    if (segmentation != null) {//Segmentation parameter
                        byte[] data = xUnitData.getData();

                        if (segmentation.getRemainingSegment() == 1) {//Single segment 
                            if (message.getCalledPartyAddress().getSubSystemNumber() == SubSystemNumber.SCCP_MANAGEMENT) {
                                this.processManagementMessage(message);
                            } else {

                                if (subSystem == null) {
                                    logger.error("Unplugged subsystem. SSN = "
                                            + message.getCalledPartyAddress().getSubSystemNumber());
                                    return;
                                }
                                subSystem.onMessage(message);
                            }
                        } else {

                            String cgSegRef
                                    = message.getCallingPartyAddress().getGlobalTitle().
                                            getGlobalTitleAddressInformation() + "."
                                    + xUnitData.getSegmentation().getSegmentationLocalReference();

                            Reassembler reassembler = segments.get(cgSegRef);
                            if (reassembler == null) {//Idle
                                if (segmentation.getFirstSegmentIndication() == 1) {//First segment
                                    reassembler = new Reassembler(cgSegRef, this);
                                    reassembler.write(data);
                                    reassembler.decrementRSE();
                                } else {


                                    logger.error("Reassembly error. Unexpected "
                                            + "segment state. Expecting first segment. " + message);

                                    this.segmentationFailed(message, ErrorReason.ERROR_IN_MESSAGE_TRANSFER);
                                }
                            } else {//Busy
                                if (segmentation.getRemainingSegment() == reassembler.getRSE()) {//Correct segment order
                                    if (segmentation.getFirstSegmentIndication() == 1) {
                                        segments.remove(cgSegRef);
                                        reassembler.cancelTimer();


                                        logger.error("Reassembly error.Unexpected "
                                                + "segment state. Unexpected first "
                                                + "segment flag received" + message);

                                        this.segmentationFailed(message, ErrorReason.ERROR_IN_MESSAGE_TRANSFER);
                                    } else {
                                        reassembler.write(data);
                                        reassembler.decrementRSE();

                                        if (segmentation.getRemainingSegment() == 0) {//Last segment
                                            reassembler.cancelTimer();
                                            xUnitData.setData(reassembler.read());
                                            subSystem.onMessage(xUnitData);
                                        }
                                    }
                                } else {
                                    segments.remove(cgSegRef);
                                    reassembler.cancelTimer();


                                    logger.error("Reassembly error. Incorrect segment order."
                                            + " RSE = " + reassembler.getRSE()
                                            + " RS = " + segmentation.getRemainingSegment()
                                            + "Message = " + message);

                                    this.segmentationFailed(message, ErrorReason.ERROR_IN_MESSAGE_TRANSFER);
                                }
                            }
                        }
                    } else {
                        if (message.getCalledPartyAddress().getSubSystemNumber() == SubSystemNumber.SCCP_MANAGEMENT) {
                            this.processManagementMessage(message);
                        } else {

                            if (subSystem == null) {
                                logger.error("Unplugged SSN. Can not fire received message to the local SSN. "
                                        + message);
                                return;
                            }

                            subSystem.onMessage(message);
                        }
                    }
                }
                break;
            case XUDTS:
                XUnitDataService xudtService = ((XUnitDataService) message);

                Segmentation segmentation = xudtService.getSegmentation();
                if (segmentation != null) {//Segmentation parameter
                    byte[] data = xudtService.getData();

                    if (segmentation.getRemainingSegment() == 1) {//Single segment 
                        subSystem.onNotice(message.getCalledPartyAddress(), message.getCallingPartyAddress(),
                                data, xudtService.getReturnCause(), 0);
                    } else {

                        if (sccpStack.isReturnOnlyFirstSegment()) {
                            subSystem.onNotice(message.getCalledPartyAddress(), message.getCallingPartyAddress(),
                                    data, xudtService.getReturnCause(), 0);
                        } else {

                            String cgSegRef
                                    = message.getCallingPartyAddress().getGlobalTitle().getGlobalTitleAddressInformation() + "."
                                    + xudtService.getSegmentation().getSegmentationLocalReference();

                            Reassembler reassembler = segments.get(cgSegRef);
                            if (reassembler == null) {//Idle
                                if (segmentation.getFirstSegmentIndication() == 1) {//First segment
                                    reassembler = new Reassembler(cgSegRef, this);
                                    reassembler.write(data);
                                    reassembler.decrementRSE();
                                } else {

                                    logger.error("Reassembly error. Unexpected "
                                            + "segment state. Expecting first segment." + message);
                                }
                            } else {//Busy
                                if (segmentation.getRemainingSegment() == reassembler.getRSE()) {//Correct segment order
                                    if (segmentation.getFirstSegmentIndication() == 1) {

                                        segments.remove(cgSegRef);
                                        reassembler.cancelTimer();
                                        logger.error("Reassembly error.Unexpected "
                                                + "segment state. Unexpected first "
                                                + "segment flag received" + message);
                                    } else {
                                        reassembler.write(data);
                                        reassembler.decrementRSE();

                                        if (segmentation.getRemainingSegment() == 0) {//Last segment
                                            reassembler.cancelTimer();
                                            xudtService.setData(reassembler.read());
                                            subSystem.onMessage(message);
                                        }
                                    }
                                } else {
                                    segments.remove(cgSegRef);
                                    reassembler.cancelTimer();

                                    logger.error("Reassembly error. "
                                            + "Incorrect segment order. RSE = " + reassembler.getRSE()
                                            + "RS = " + segmentation.getRemainingSegment()
                                            + message);
                                }
                            }
                        }
                    }
                } else {//No segmentation parameter
                    subSystem.onNotice(message.getCalledPartyAddress(), message.getCallingPartyAddress(),
                            message.getData(), xudtService.getReturnCause(), 0);
                }

                break;
        }
    }

//  From user
    @Override
    public void sendConnectionlessMessage(SCCPMessage message) throws Exception {
        if (message.getProtocolClass() == ProtocolClassEnum.CLASS0) {
            message.setSls(this.sccpStack.getSlsGenerator().generate());
        }

        this.sccpStack.getSccpRoutingControl().connectionlessMessage(message, true);
    }

    //ITU-T Q.714 Figure C.12 sheet 8 of 9
    @Override
    public void routingFailure(SCCPMessage message, ErrorReason sccpErrorReason,
            boolean localOriginator) throws RoutingFailureException {
        switch (message.getType()) {
            case UDT:
                UnitData unitData = (UnitData) message;

                //If MessageHandling is NO_SPECIAL_OPTIONS then just silently skip error 
                if (unitData.getMessageHandling() == MessageHandling.NO_SPECIAL_OPTIONS) {
                    return;
                }

                SCCPAddress calledParty = unitData.getCalledPartyAddress();
                SCCPAddress callingParty = unitData.getCallingPartyAddress();
                byte[] userData = unitData.getData();

                //Message initiated by local user. Let notify it about failure
                if (localOriginator) {
                    SubSystemNumber ssn = unitData.getCallingPartyAddress().getSubSystemNumber();
                    if (ssn == null && logger.isDebugEnabled()) {
                        logger.error("Tx: SCCP failure: No SSN included into callingParty. " + calledParty);
                        throw new RoutingFailureException("Tx: SCCP failure: No SSN included into callingParty. " + calledParty);
                    }

                    SubSystem subSystem = sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);

                    if (subSystem == null) {
                        if (logger.isDebugEnabled()) {
                            logger.error("Tx: SCCP failure: No SubSystem found. SSN = " + ssn);
                        }
                        throw new RoutingFailureException("Tx: SCCP failure: No SubSystem found. SSN = " + ssn);
                    }

                    subSystem.onNotice(calledParty, callingParty, userData, sccpErrorReason, 0);
                    return;
                }

                //Swap Calling Pty Addr and Called Pty Addr ITU-T Q.714 SCLC sheet 9 of 9
                UnitDataService unitdataService = SCCPConnectionlessMessageFactory.
                        createUnitDataService(sccpErrorReason, callingParty, calledParty, userData);
                unitdataService.setSls(unitData.getSls());

                if (unitdataService.getCalledPartyAddress().getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN) {
                    if (unitdataService.getCalledPartyAddress().getAddressIndicator().getPointCodeIndicator() == false) {
                        message.getCalledPartyAddress().setSignallingPointCode(new SignallingPointCode(message.getOpc()));
                    }
                }
                try {
                    sccpStack.getSccpRoutingControl().connectionlessMessage(unitdataService, localOriginator);
                } catch (Exception ex) {
                    logger.error("Error occured ", ex);
                }
                break;
            case XUDT:
                XUnitData xUnitData = (XUnitData) message;

                if (xUnitData.getMessageHandling() != MessageHandling.RETURN_MESSAGE_ON_ERROR) {
                    return;
                }

                calledParty = xUnitData.getCalledPartyAddress();
                callingParty = xUnitData.getCallingPartyAddress();
                userData = xUnitData.getData();

                if (localOriginator) {
                    SubSystemNumber ssn = xUnitData.getCalledPartyAddress().getSubSystemNumber();
                    if (ssn == null && logger.isDebugEnabled()) {
                        logger.error("Tx: SCCP failure: No SSN included into calledParty. " + calledParty);
                        throw new RoutingFailureException("Tx: SCCP failure: No SSN included into calledParty. " + calledParty);
                    }

                    SubSystem subSystem = sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);

                    if (subSystem == null) {
                        logger.error("Tx: SCCP failure: No SubSystem found. SSN = " + ssn);
                        throw new RoutingFailureException("Tx: SCCP failure: No SubSystem found . SSN = " + ssn);
                    }

                    subSystem.onNotice(calledParty, callingParty, userData, sccpErrorReason, 0);
                    return;
                }

                //Swap Calling Pty Addr and Called Pty Addr ITU-T Q.714 SCLC sheet 9 of 9
                XUnitDataService xUnitdataService = SCCPConnectionlessMessageFactory.
                        createExtendedUnitDataService(sccpErrorReason,
                                sccpStack.getHopCounter(), callingParty, calledParty,
                                userData);

                xUnitdataService.setSls(xUnitData.getSls());

                if (xUnitdataService.getCalledPartyAddress().getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN) {
                    if (xUnitdataService.getCalledPartyAddress().getAddressIndicator().getPointCodeIndicator() == false) {
                        message.getCalledPartyAddress().setSignallingPointCode(new SignallingPointCode(message.getOpc()));
                    }
                }
                try {
                    sccpStack.getSccpRoutingControl().connectionlessMessage(xUnitdataService, localOriginator);
                } catch (Exception ex) {
                    logger.error("Error occured ", ex);
                }
        }
    }

    private void segmentationFailed(SCCPMessage message, ErrorReason sccpErrorReason) {

        if (message.getMessageHandling() == MessageHandling.NO_SPECIAL_OPTIONS) {
            return;
        }

        switch (message.getType()) {
            case UDT:
                UnitData unitData = (UnitData) message;

                //If MessageHandling is NO_SPECIAL_OPTIONS then just silently skip error 
                SCCPAddress calledParty = unitData.getCalledPartyAddress();
                SCCPAddress callingParty = unitData.getCallingPartyAddress();
                byte[] userData = unitData.getData();

                //Swap Calling Pty Addr and Called Pty Addr ITU-T Q.714 SCLC sheet 9 of 9
                UnitDataService unitdataService = SCCPConnectionlessMessageFactory.
                        createUnitDataService(sccpErrorReason, callingParty, calledParty, userData);
                unitdataService.setSls(unitData.getSls());

                if (unitdataService.getCalledPartyAddress().getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN) {
                    if (unitdataService.getCalledPartyAddress().getAddressIndicator().getPointCodeIndicator() == false) {
                        message.getCalledPartyAddress().setSignallingPointCode(new SignallingPointCode(message.getOpc()));
                    }
                }
                try {
                    sccpStack.getSccpRoutingControl().connectionlessMessage(unitdataService, true);
                } catch (Exception ex) {
                    logger.error("Error occured ", ex);
                }
                break;
            case XUDT:
                XUnitData xUnitData = (XUnitData) message;

                calledParty = xUnitData.getCalledPartyAddress();
                callingParty = xUnitData.getCallingPartyAddress();
                userData = xUnitData.getData();

                //Swap Calling Pty Addr and Called Pty Addr ITU-T Q.714 SCLC sheet 9 of 9
                XUnitDataService xUnitdataService = SCCPConnectionlessMessageFactory.
                        createExtendedUnitDataService(sccpErrorReason,
                                sccpStack.getHopCounter(), callingParty, calledParty,
                                userData);

                xUnitdataService.setSls(xUnitData.getSls());

                if (xUnitdataService.getCalledPartyAddress().getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN) {
                    if (xUnitdataService.getCalledPartyAddress().getAddressIndicator().getPointCodeIndicator() == false) {
                        message.getCalledPartyAddress().setSignallingPointCode(new SignallingPointCode(message.getOpc()));
                    }
                }
                try {
                    sccpStack.getSccpRoutingControl().connectionlessMessage(xUnitdataService, true);
                } catch (Exception ex) {
                    logger.error("Error occured ", ex);
                }

        }
    }

    /**
     * ITU-T Q.713 , Clause 5 SCCP management (SCMG) messages are carried using
     * the connectionless service of the SCCP. When transferring SCMG messages,
     * class 0 is requested with no special option. The called and calling party
     * address parameters will refer to SSN = 1 (SCMG) and will have routing
     * indicator set to "route on SSN". SCCP management message parts are
     * provided in the "data" parameter of the unitdata or extended unitdata
     * message or "long data" of the LUDT message.
     *
     * @param callingParty
     * @param calledParty
     * @param message
     * @param localOriginator
     */
    @Override
    public void sendSccpManagementMessage(SCCPAddress callingParty, SCCPAddress calledParty,
                                          ManagementMessage message, boolean localOriginator) {
        try {

            UnitData unitData = SCCPConnectionlessMessageFactory.createUnitData();

            unitData.setCallingPartyAddress(callingParty);
            unitData.setCalledPartyAddress(calledParty);
            unitData.setMessageHandling(MessageHandling.NO_SPECIAL_OPTIONS);
            unitData.setProtocolClass(ProtocolClassEnum.CLASS0);

            unitData.setSls(this.sccpStack.getSlsGenerator().generate());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.encode(baos);

            unitData.setData(baos.toByteArray());

            this.sccpStack.getSccpRoutingControl().connectionlessMessage(unitData, localOriginator);
        } catch (Exception ex) {
            logger.error("Erro occured ", ex);
        }
    }

    private void broadcast(int informerSp, SubsystemAllowed ssa) {
//      Informer SP is not same with AffectedPointCode from SSA message. That means,
//      SSA message broadcasted to me from other node. So we do not re-broadcast it to other 
//      concerned SP.
        int affectedSpc = ssa.getAffectedPointCode().getSignallingPointCode();
        if (informerSp != affectedSpc) {
            return;
        }

        List<RemoteSignallingPoint> concernedSignallingPoints = this.sccpStack.getSccpLayerManagement().getConcernedSignallingPoints().getConcernedSignallingPoint();
        for (int i = 0; i < concernedSignallingPoints.size(); i++) {
            RemoteSignallingPoint concernedSignallingPoint = concernedSignallingPoints.get(i);

//          If sender node registered in me as concerned i don't re-broadcast SSA to its again.  
            if (concernedSignallingPoint.getSpc() == informerSp) {
                continue;
            }

            SCCPAddress callingParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, affectedSpc, null, SubSystemNumber.SCCP_MANAGEMENT);
            SCCPAddress calledParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, concernedSignallingPoint.getSpc(), null, SubSystemNumber.SCCP_MANAGEMENT);

            this.sendSccpManagementMessage(callingParty, calledParty, ssa, true);
        }
    }

    private void broadcast(int informerSp, SubsystemProhibited ssp) {
//      Informer SP is not same with AffectedPointCode from SSP message. That means,
//      SSP message broadcasted to me from other node. So we do not re-broadcast it to other 
//      concerned SP.   
        int affectedSpc = ssp.getAffectedPointCode().getSignallingPointCode();
        if (informerSp != affectedSpc) {
            return;
        }

        List<RemoteSignallingPoint> concernedSignallingPoints = this.sccpStack.getSccpLayerManagement().getConcernedSignallingPoints().getConcernedSignallingPoint();

        for (int i = 0; i < concernedSignallingPoints.size(); i++) {
            RemoteSignallingPoint concernedSignallingPoint = concernedSignallingPoints.get(i);

            //SP the same with informer SP
            if (concernedSignallingPoint.getSpc() == informerSp) {
                continue;
            }

            SCCPAddress callingParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, affectedSpc, null, SubSystemNumber.SCCP_MANAGEMENT);
            SCCPAddress calledParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, concernedSignallingPoint.getSpc(), null, SubSystemNumber.SCCP_MANAGEMENT);
            this.sendSccpManagementMessage(callingParty, calledParty, ssp, true);
        }
    }

    protected void processManagementMessage(SCCPMessage message) throws Exception {
        ManagementMessage managementMessage = SCCPManagementMessageFactory.createManagementMessage(message.getData());

        switch (managementMessage.getMessageType()) {
            case SSP:


                logger.info("Rx: SSP message received: " + managementMessage);

                SubsystemProhibited ssp = (SubsystemProhibited) managementMessage;
                RemoteSignallingPoint remoteSignallingPoint = this.sccpStack.getSccpLayerManagement().getRemoteSignallingPoint(ssp.getAffectedPointCode().getSignallingPointCode());

                if (remoteSignallingPoint == null) {
                    logger.info("Rx: SSP received, but no configured remote signalling point found. "
                            + "[DPC = " + ssp.getAffectedPointCode() + "; SSN = "
                            + ssp.getAffectedSSN() + "]");
                    return;
                }

                SubSystemNumber ssn = ssp.getAffectedSSN();
                if (ssn == SubSystemNumber.SCCP_MANAGEMENT) {
                    return;
                }

                RemoteSubSystem remoteSubSystem = remoteSignallingPoint.getRemoteSubsystem(ssn);
                if (remoteSubSystem == null) {
                    logger.info("Rx: SSP received, but no configured remote subsystem found. "
                            + "[DPC = " + remoteSignallingPoint.getSpc() + "; SSN = " + ssn + "]");
                    return;
                }

                if (remoteSubSystem.isProhibited()) {
                    if (remoteSignallingPoint.isRemoteSCCPProhibited() || remoteSignallingPoint.isRemoteSpProhibited()) {
                        this.broadcast(message.getOpc(), ssp);
                    }
                    return;
                }

                remoteSubSystem.startSst();
                remoteSubSystem.setProhibited(true);
                this.broadcast(message.getOpc(), ssp);

                break;
            case SSA:

                SubsystemAllowed ssa = (SubsystemAllowed) managementMessage;
                logger.info("Rx: SSA received: " + ssa);

                remoteSignallingPoint = this.sccpStack.getSccpLayerManagement().
                        getRemoteSignallingPoint(ssa.getAffectedPointCode().getSignallingPointCode());

                if (remoteSignallingPoint == null) {
                    logger.info("Rx: SSA received. No remote signalling point found. [DPC = "
                            + ssa.getAffectedPointCode() + "; SSN = " + ssa.getAffectedSSN() + "]");
                    return;
                }

                if (remoteSignallingPoint.isRemoteSpProhibited()) {
                    logger.info("Rx: SSA ignored: Remote signnaling"
                            + " point is prohibited. [DCP = " + ssa.getAffectedPointCode().getSignallingPointCode()
                            + ";  SSN =  " + ssa.getAffectedSSN() + "]");
                    return;
                }

                if (remoteSignallingPoint.isRemoteSCCPProhibited()) {
                    remoteSignallingPoint.setRemoteSCCPProhibited(false);
                    remoteSignallingPoint.getRemoteSubsystem(SubSystemNumber.SCCP_MANAGEMENT).stopSst();
                    return;
                }

                if (ssa.getAffectedSSN() == SubSystemNumber.SCCP_MANAGEMENT) {
                    logger.info("Rx: SSA ignored: Affected SSN is SCCP-MANAGEMENT"
                            + " [DCP = " + ssa.getAffectedPointCode().getSignallingPointCode()
                            + ";  SSN =  " + ssa.getAffectedSSN() + "]");
                    return;
                }

                remoteSubSystem = remoteSignallingPoint.getRemoteSubsystem(ssa.getAffectedSSN());

                if (remoteSubSystem == null) {
                    logger.info("Rx: SSA received: No remote subsystem found. "
                            + "[DPC = " + ssa.getAffectedPointCode().getSignallingPointCode()
                            + "; SSN = " + ssa.getAffectedSSN() + "]"
                    );
                    return;
                }

                if (!remoteSubSystem.isProhibited()) {
                    logger.info("Rx: SSA ignored: Affected SSN was not prohibited"
                            + " [DCP = " + ssa.getAffectedPointCode().getSignallingPointCode()
                            + ";  SSN =  " + ssa.getAffectedSSN() + "]");
                    return;
                }

                remoteSubSystem.stopSst();
                remoteSubSystem.setProhibited(false);

                this.broadcast(message.getOpc(), ssa);
                break;
            case SST:

                SubsystemStatusTest sst = (SubsystemStatusTest) managementMessage;
                logger.info("Rx: SST received: " + sst);

                ssn = sst.getAffectedSSN();

                SubSystem subSystem = this.sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);
                if (subSystem == null
                        || subSystem.getState() == SubSystem.State.IDLE) {
                    logger.info("Rx: SST received: Subsystem is not available. SSN = " + ssn);
                    return;
                }

                SCCPAddress callingParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN,
                        message.getDpc(), null, SubSystemNumber.SCCP_MANAGEMENT);
                SCCPAddress calledParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN,
                        message.getOpc(), null, SubSystemNumber.SCCP_MANAGEMENT);
                ssa = SCCPManagementMessageFactory.createSubsystemAllowed(ssn,
                        sst.getAffectedPointCode(), new SubsystemMultiplicityIndicator(0));
                this.sendSccpManagementMessage(callingParty, calledParty, ssa, true);

                logger.info("Rx: SSA sent: " + calledParty + "; " + callingParty + "; " + ssa);
                break;
        }
    }
}
