/*
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.controls;

import azrc.az.sigtran.m3ua.MTPTransferMessage;
import azrc.az.sigtran.m3ua.NetworkIndicator;
import azrc.az.sigtran.sccp.access.point.MTPServiceAccessPoint;
import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SignallingPointCode;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.exception.RoutingFailureException;
import azrc.az.sigtran.sccp.general.*;
import azrc.az.sigtran.sccp.globaltitle.GlobalTitle;
import azrc.az.sigtran.sccp.gtt.GlobalTitleTranslationRule;
import azrc.az.sigtran.sccp.gtt.GlobalTitleTranslator;
import azrc.az.sigtran.sccp.gtt.SccpEntitySet;
import azrc.az.sigtran.sccp.messages.MessageType;
import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.messages.connectionless.*;
import azrc.az.sigtran.sccp.messages.management.SCCPManagementMessageFactory;
import azrc.az.sigtran.sccp.messages.management.SubsystemProhibited;
import azrc.az.sigtran.sccp.parameters.Segmentation;
import azrc.az.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * @author eatakishiyev
 */
public class SCCPRoutingControlImpl implements SCCPRoutingControl {

    //    private static transient Logger logger = SCCPStackImpl.logger;
    private final transient Logger logger = LoggerFactory.getLogger(SCCPRoutingControlImpl.class);
    private final SCCPStackImpl sccpStack;
    private boolean congested = false;

    public SCCPRoutingControlImpl(SCCPStackImpl sccpStack) throws Exception {
        this.sccpStack = sccpStack;
    }

    @Override
    public void onMtpTransferIndication(MTPTransferMessage mtpTransferMessage) {


        if (logger.isDebugEnabled()) {
            logger.debug("Rx: MtpTransferIndication received:" + mtpTransferMessage);
        }

        SCCPMessage message = SCCPConnectionlessMessageFactory.createMessage(mtpTransferMessage.getUserData());

        try {
            if (message == null) {
                //Impossible create SCCPMessage from received bytes. Incorrect syntax

                return;
            }

            message.setDpc(mtpTransferMessage.getDpc());
            message.setOpc(mtpTransferMessage.getOpc());
            message.setNi(mtpTransferMessage.getNi());

            //Figure C.1/Q.714 page 2(sheet 2 of 12)
            ProtocolClassEnum protocolClass = message.getProtocolClass();
            boolean isProtocolClass1 = (message.getType() == MessageType.XUDTS
                    || protocolClass == ProtocolClassEnum.CLASS1);

            if (isProtocolClass1) {
                message.setSls(mtpTransferMessage.getSls());
            } else {
                message.setSls(this.sccpStack.getSlsGenerator().generate());
            }

            switch (message.getCalledPartyAddress().getAddressIndicator().getRoutingIndicator()) {
                case ROUTE_ON_SSN:
                    this.processRouteOnSSN(message, false);
                    break;
                case ROUTE_ON_GT:
                    if (message.getType() == MessageType.XUDT
                            || message.getType() == MessageType.XUDTS) {
                        int hopCounter = message.getHopCounter().getHopCounter() - 1;

                        if (hopCounter <= 0) {
                            logger.warn(("Rx: HopCounterViolation. "
                                    + "Hop counter reached to 0"));
                            sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.HOP_COUNTER_VIOLATION, false);
                            return;
                        }
                    }

                    if (message.isConnectionless()) {
                        this.processRouteOnGt(message, false);
                    } else {
                        logger.error("Rx: Not implemented message type received. MessageType = "
                                + message.getType().name());

                    }
                    break;
            }

        } catch (Exception ex) {
            logger.error("Rx: Error occured", ex);
        }
    }

    //ITU-T Q.714 Figure C.1/Sheet 1 of 12
    //SCLC -> SCRC
    //Message from upper layer/SCMG/RoutingFailure
    @Override
    public void connectionlessMessage(SCCPMessage message, boolean localOriginator) throws Exception {
        SCCPAddress calledParty = message.getCalledPartyAddress();
        SCCPAddress callingParty = message.getCallingPartyAddress();

        boolean isTranslated = calledParty.isTranslated();
        switch (message.getType()) {
            case UDT:
            case XUDT:
                if (isTranslated) {
                    MTPServiceAccessPoint mtpSap = calledParty.getMtpSap();
                    if (mtpSap == null) {
                        logger.error("SCCP delivering fails: No MTP-SAP found. " + calledParty);
                        return;
                    }
                    if (localOriginator) {
                        this.treatCallingPartyAddressOnLocalOriginatorCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, mtpSap);
                    } else {
                        this.treatCallingPartyAddressOnTransitCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, message.getOpc());
                    }
                    this.mtpTransferRequest(mtpSap, message);
                } else { //not translated yet
                    this.routeConnectionlessMessage(message, localOriginator);
                }
                break;
            case UDTS:
            case XUDTS:
                if (calledParty.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_GT) {//RI == 0
                    this.routeConnectionlessMessage(message, localOriginator);
                } else {
                    this.fromSCLCToMTP(message, localOriginator);
                }
                break;
        }
    }

    private void processRouteOnSSN(SCCPMessage message, boolean localOriginator) throws Exception {
        if (!message.getCalledPartyAddress().getAddressIndicator().getSSNIndicator()) {

            String log = ": SCCP RouteOnSSN failure: There is no"
                    + " SSN included in the CalledParty." + message;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        SubSystemNumber ssn = message.getCalledPartyAddress().getSubSystemNumber();

        if (ssn == SubSystemNumber.SCCP_MANAGEMENT) {
            //fire management message to the user
            sccpStack.getSccpConnectionlessControl().processManagementMessage(message);
        } else {//Subsystem is not SCCP_MANAGEMENT

            SubSystem subsystem = sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);

            if (subsystem == null) {

                sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.UNEQUIPPED_USER, localOriginator);
                String log = ": Error occured during route on SSN. UNEQUIPPED_USER." + message;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return;
            }

            if (subsystem.getState() == SubSystem.State.IDLE) {
                messageForUnavailableSubsystem(localOriginator, message);
                sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.SUBSYSTEM_FAILURE, localOriginator);
                String log = ": Error occured during route on SSN. SUBSYSTEM_FAILURE." + message;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return;
            }

            sccpStack.getSccpConnectionlessControl().messageForLocalSubsystem(message, localOriginator);
        }
    }

    /**
     * @return the congested
     */
    public boolean isCongested() {
        return congested;
    }

    /**
     * @param congested the congested to set
     */
    public void setCongested(boolean congested) {
        this.congested = congested;
    }

    private void processRouteOnGt(SCCPMessage message, boolean localOriginator) throws Exception {

        //Start Translation
        SCCPAddress calledParty = message.getCalledPartyAddress();
        SCCPAddress callingParty = message.getCallingPartyAddress();

        GlobalTitleTranslator globalTitleTranslator = this.sccpStack.getSccpLayerManagement().getGlobalTitleTranslator(calledParty.getGlobalTitle());

        if (globalTitleTranslator == null) {

            String log = ": SCCP GTT failure: NO_TRANSLATION_FOR_ADDRESS_SUCH_NATURE. " + calledParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);

            sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.NO_TRANSLATION_FOR_ADDRESS_SUCH_NATURE, localOriginator);
            return;
        }

        GlobalTitleTranslationRule rule = globalTitleTranslator.getGlobalTitleTranslationRule(calledParty.getGlobalTitle().getGlobalTitleAddressInformation());

        if (rule == null) {
            String log = ": GTT[" + globalTitleTranslator.getName() + "] SCCP GTT failure: NO_TRANSLATION_FOR_SPECIFIC_ADDRESS. CalledParty = " + calledParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);

            sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.NO_TRANSLATION_FOR_SPECIFIC_ADDRESS, localOriginator);
            return;
        }

        //Obtain new TT, NP, NAI
        rule.applyGTTranslationRule(calledParty);

        SccpEntitySet entitySet = rule.getSccpEntitySet();

        if (entitySet == null) {

            String log = ": GTT[" + globalTitleTranslator.getName() + "] "
                    + "GTT-RULE[" + rule.getName() + "] SCCP GTT failure: No SCCPEntitySet defined." + calledParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        SubSystemNumber ssn = entitySet.getSsn();

        if (ssn == null) {
            ssn = calledParty.getSubSystemNumber();
        }

        MTPServiceAccessPoint mtpSap = selectEntity(message, entitySet, ssn, rule.getRoutingIndicator(), localOriginator);

        if (mtpSap == null) {

            String log = ": GTT[" + globalTitleTranslator.getName() + "] "
                    + "GTT-RULE [" + rule.getName() + "]. SCCP GTT fail: No MTP-SAP found. " + calledParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        if (logger.isDebugEnabled()) {
            String log = ": GTT Done. GTT[" + globalTitleTranslator.getName() + "] "
                    + "GTT-RULE[" + rule.getName() + "]. MTP-SAP[" + mtpSap.getName() + "]." + calledParty;
            logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
        }

        //Obtain new GTAI
        if (rule.getConversionRule() != null && !rule.getConversionRule().trim().equals("")) {
            String newGTAI = rule.applyGTConversionRule(calledParty.getGlobalTitle().getGlobalTitleAddressInformation());
            calledParty.getGlobalTitle().setGlobalTitleAddressInformation(newGTAI);
        }

        RoutingIndicator newRoutingIndicator = rule.getRoutingIndicator() == null
                ? calledParty.getAddressIndicator().getRoutingIndicator() : rule.getRoutingIndicator();

        SCCPAddress newCalledParty = new SCCPAddress(newRoutingIndicator,
                mtpSap.getDpc(), calledParty.getGlobalTitle(), ssn);

        newCalledParty.setTranslated(true);
        newCalledParty.setMtpSap(mtpSap);

        //Remove SPC from calledParty if parameter set to true and RI = ROUTE_ON_GT
        if (sccpStack.isRemoveSpc() && newRoutingIndicator == RoutingIndicator.ROUTE_ON_GT) {
            newCalledParty.setSignallingPointCode(null);
        }

        message.setCalledPartyAddress(newCalledParty);

        //ITU-T Q.714 Figure C.1 Sheet 3 of 12
        //GTT result is ROUTE ON SSN and PC is local SPC.
        if (newRoutingIndicator == RoutingIndicator.ROUTE_ON_SSN
                && this.sccpStack.getSccpLayerManagement().isLocalSpc(mtpSap.getDpc(), mtpSap.getNi())) {
            this.sccpStack.getSccpConnectionlessControl().messageForLocalSubsystem(message, localOriginator);
        } else {// GTT result is not ROUTE ON SSN or PC is not local SPC. We have to forward message to the next hop.

            //message change action requested
            if (mtpSap.getTargetMessageType() != null && !message.isModified()) {
                SCCPMessage modifiedMessage = messageChange(message, mtpSap.getTargetMessageType());
                if (logger.isDebugEnabled()) {
                    String log = ": Message Modification done: \n From = " + message.getType()
                            + "\n to = " + modifiedMessage.getType() + "\n New Message = " + modifiedMessage;
                    logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                }
                //message already translated
                connectionlessMessage(modifiedMessage, localOriginator);

            } //segmentation needed
            else if (message.getData() != null
                    && message.getData().length > sccpStack.getMinNotSegmentedMessageSize()
                    && !message.isSegmented()) {

                byte[] userData = message.getData();
                int position = 0;
                int fBit = 1;
                int segmentationLocalReference = generateSegmentationLocalReference();
                int rs = userData.length / sccpStack.getMinNotSegmentedMessageSize()
                        + (userData.length % sccpStack.getMinNotSegmentedMessageSize() > 0 ? 1 : 0);

                if (logger.isDebugEnabled()) {
                    String log = ": Message need to be segmented."
                            + " Actual UserData length is " + message.getData().length
                            + " configured MinNotSegmentedMessageSize is " + sccpStack.getMinNotSegmentedMessageSize()
                            + " Generated SLR = " + segmentationLocalReference
                            + " RS = " + rs + calledParty;
                    logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                }

                while (rs > 0) {
                    int length = position + sccpStack.getMinNotSegmentedMessageSize() < userData.length
                            ? sccpStack.getMinNotSegmentedMessageSize() : (userData.length - position);

                    //select segment of user data
                    byte[] segmentedData = new byte[length];
                    System.arraycopy(userData, position, segmentedData, 0, length);
                    position = position + length;

                    //UDT or XUDT should be segmented
                    if (message.getType() == MessageType.UDT
                            || message.getType() == MessageType.XUDT) {
                        XUnitData xudt = (XUnitData) messageChange(message, MessageType.XUDT);

                        Segmentation segmentation = new Segmentation(fBit,
                                ProtocolClassEnum.CLASS1, (short) rs,
                                segmentationLocalReference);

                        //As message segmented then segments should be delivered to the destination in sequence.
                        //Because of this i select Protocol class 1 for segmented message
                        xudt.setProtocolClass(ProtocolClassEnum.CLASS1);
                        xudt.setSegmentation(segmentation);
                        xudt.setData(segmentedData);
                        xudt.setSegmented(true);
                        if (logger.isDebugEnabled()) {
                            String log = ": Sending segmented message "
                                    + " Actial UserData length is " + message.getData().length
                                    + " configured MinNotSegmentedMessageSize is " + sccpStack.getMinNotSegmentedMessageSize()
                                    + " Generated SLR = " + segmentationLocalReference
                                    + " RS = " + rs
                                    + xudt;
                            logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                        }
                        connectionlessMessage(xudt, localOriginator);
                    }//UDTS or XUDTS should be segmented
                    else if (message.getType() == MessageType.UDTS
                            || message.getType() == MessageType.XUDTS) {
                        XUnitDataService xudts = (XUnitDataService) messageChange(message,
                                MessageType.XUDTS);

                        Segmentation segmentation = new Segmentation(fBit,
                                ProtocolClassEnum.CLASS1, (short) rs,
                                segmentationLocalReference);

                        //As message segmented then segments should be delivered to the destination in sequence.
                        //Because of this i select Protocol class 1 for segmented message
                        xudts.setProtocolClass(ProtocolClassEnum.CLASS1);
                        xudts.setSegmentation(segmentation);
                        xudts.setData(segmentedData);
                        xudts.setSegmented(true);

                        if (logger.isDebugEnabled()) {
                            String log = ": Sending segmented message "
                                    + " Actial UserData length is " + message.getData().length
                                    + " configured MinNotSegmentedMessageSize is " + sccpStack.getMinNotSegmentedMessageSize()
                                    + " Generated SLR = " + segmentationLocalReference
                                    + " RS = " + rs + xudts;
                            logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                        }
                        connectionlessMessage(xudts, localOriginator);
                    }

                    fBit = 0;
                    rs = rs - 1;

                }
            } else {

                //Treat calling party address
                if (localOriginator) {//local originated
                    this.treatCallingPartyAddressOnLocalOriginatorCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, mtpSap);
                    if (logger.isDebugEnabled()) {
                        String log = ": Calling party treating done. "
                                + "Treated calling party = " + callingParty;
                        logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                    }
                } else {// transit
                    this.treatCallingPartyAddressOnTransitCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, message.getOpc());
                    if (logger.isDebugEnabled()) {
                        String log = ": Transit message"
                                + " calling party treating done. Treated calling party = " + callingParty;
                        logger.debug(localOriginator ? "Tx" + log : "Rx" + log);
                    }
                }

                this.mtpTransferRequest(mtpSap, message);
            }
        }
    }

    private int generateSegmentationLocalReference() {
        int rnd = 0 + (int) (Math.random() * (16777215 - 0 + 1));
        return rnd;
    }

    //ITU-T Rec. Q.715 6.3 Compatibility test and message change procedures
    private SCCPMessage messageChange(SCCPMessage message, MessageType targetType) {
        SCCPMessage targetMessage = message;

        //Convert from XUDT to UDT.Destination only supports UDT(S) or use of 
        //UDT(S) is mandatory in the network
        if (targetType == MessageType.UDT) {
            if (message.getType() == MessageType.XUDT) {
                if (((XUnitData) message).getSegmentation() == null) {
                    targetMessage = SCCPConnectionlessMessageFactory.
                            createUnitData(message.getCalledPartyAddress(),
                                    message.getCalledPartyAddress(),
                                    message.getData());
                    targetMessage.setDpc(message.getDpc());
                    targetMessage.setMessageHandling(message.getMessageHandling());
                    targetMessage.setNi(message.getNi());
                    targetMessage.setOpc(message.getOpc());
                    targetMessage.setSls(message.getSls());
                    targetMessage.setProtocolClass(message.getProtocolClass());
                } else {
                    logger.error("Message type change impossible from XUDT to UDT. "
                            + "Because source message containse from two or more segments." + message);
                }
            } else if (message.getType() == MessageType.XUDTS) {
                XUnitDataService xudts = (XUnitDataService) message;
                if (xudts.getSegmentation() == null) {
                    targetMessage = SCCPConnectionlessMessageFactory.createUnitDataService(xudts.getReturnCause(),
                            message.getCalledPartyAddress(),
                            message.getCalledPartyAddress(), message.getData());
                    targetMessage.setDpc(message.getDpc());
                    targetMessage.setMessageHandling(message.getMessageHandling());
                    targetMessage.setNi(message.getNi());
                    targetMessage.setOpc(message.getOpc());
                    targetMessage.setSls(message.getSls());
                    targetMessage.setProtocolClass(message.getProtocolClass());
                } else {
                    logger.error("Message type change impossible from XUDTS to UDTS. "
                            + "Because source message containse from two or more segments." + message);
                }
            }
        } //Convert from UDT to XUDT.Destination only supports XUDT(S) or use of 
        //XUDT(S) is mandatory in the network
        else if (targetType == MessageType.XUDT) {
            if (message.getType() == MessageType.UDT) {
                targetMessage = SCCPConnectionlessMessageFactory.createExtendedUnitData(message.getCalledPartyAddress(),
                        message.getCallingPartyAddress(), sccpStack.getHopCounter(), message.getData());
                targetMessage.setDpc(message.getDpc());
                targetMessage.setMessageHandling(message.getMessageHandling());
                targetMessage.setNi(message.getNi());
                targetMessage.setOpc(message.getOpc());
                targetMessage.setSls(message.getSls());
                targetMessage.setProtocolClass(message.getProtocolClass());
            } else if (message.getType() == MessageType.UDTS) {
                targetMessage = SCCPConnectionlessMessageFactory.
                        createExtendedUnitDataService(((UnitDataService) message).getReturnCause(),
                                sccpStack.getHopCounter(), message.getCalledPartyAddress(),
                                message.getCallingPartyAddress(), message.getData());
                targetMessage.setDpc(message.getDpc());
                targetMessage.setMessageHandling(message.getMessageHandling());
                targetMessage.setNi(message.getNi());
                targetMessage.setOpc(message.getOpc());
                targetMessage.setSls(message.getSls());
                targetMessage.setProtocolClass(message.getProtocolClass());
            }
        }
        targetMessage.getCalledPartyAddress().setTranslated(true);
        targetMessage.setModified(true);

        if (logger.isDebugEnabled()) {
            logger.debug("Message modification done."
                    + "\nOriginalMessage = " + message
                    + "\n ModifiedMessage = " + targetMessage);
        }

        return targetMessage;
    }

    private MTPServiceAccessPoint selectEntity(SCCPMessage message, SccpEntitySet entitySet,
                                               SubSystemNumber ssn, RoutingIndicator ri, boolean localOriginator) throws RoutingFailureException {
        MTPServiceAccessPoint mtpSap = null;
        switch (entitySet.getMode()) {
            case SOLITARY:
                mtpSap = entitySet.getMasterSap();

                ErrorReason reason = checkAvailability(mtpSap, ssn, ri, message, localOriginator);
                if (reason != ErrorReason.NONE) {
                    sccpStack.getSccpConnectionlessControl().routingFailure(message, reason, localOriginator);
                    String log = ": SCCP GTT failure: SOLITARY EntitySet Mode."
                            + " Master MTP-SAP " + mtpSap + " is  unavailable. Reason = " + reason;
                    logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                    return null;
                }

                return mtpSap;

            case DOMINANT:
                mtpSap = entitySet.getMasterSap();

                reason = checkAvailability(mtpSap, ssn, ri, message, localOriginator);
                if (reason != ErrorReason.NONE) {

                    String log = ": DOMINANT EntitySet Mode. "
                            + "Master MTP-SAP " + mtpSap + "is not available .Reason = " + reason
                            + ". Trying to select Slave MTP-SAP from entitySet and checkAvailability.";
                    logger.warn(localOriginator ? "Tx" + log : "Rx" + log);

                    mtpSap = entitySet.getSlaveSap();
                    reason = checkAvailability(mtpSap, ssn, ri, message, localOriginator);
                    if (reason != ErrorReason.NONE) {
                        sccpStack.getSccpConnectionlessControl().routingFailure(message, reason, localOriginator);
                        log = ": SCCP GTT failure: SOLITARY EntitySet Mode."
                                + "Slave MTP-SAP = " + mtpSap + "is also unavailable. Reason = " + reason;
                        logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                        return null;
                    }

                    return mtpSap;

                } else {
                    return mtpSap;
                }

            case LOADSHARING:
                /**
                 * Loadsharing among SCCP entities will be achieved on the basis
                 * of SLS for class 1 traffic and on round-robin fashion for
                 * class 0 traffic.
                 */

                int entityId;
//                  ITU-T Q.705 Appendix A clause 5  
                if (message.getProtocolClass() == ProtocolClassEnum.CLASS0) {
                    Random randomizer = new Random();
                    entityId = randomizer.nextInt(2);
                } else {
                    entityId = message.getSls() & 0x01;

                }

                boolean isMaster = false;
                switch (entityId) {
                    case 0:
                        mtpSap = entitySet.getMasterSap();
                        isMaster = true;

                        break;
                    case 1:
                        mtpSap = entitySet.getSlaveSap();
                        break;
                }

                reason = checkAvailability(mtpSap, ssn, ri, message, localOriginator);
                if (reason != ErrorReason.NONE) {
                    mtpSap = isMaster ? entitySet.getSlaveSap() : entitySet.getMasterSap();
                    reason = checkAvailability(mtpSap, ssn, ri, message, localOriginator);
                    if (reason != ErrorReason.NONE) {
                        sccpStack.getSccpConnectionlessControl().routingFailure(message, reason, localOriginator);
                        String log = ": SCCP GTT failure: LOADSHARING EntitySet Mode. "
                                + "MTP-SAP = " + mtpSap + " is unavailable. Reason = " + reason;
                        logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                        return null;
                    } else {
                        return mtpSap;
                    }
                } else {
                    return mtpSap;
                }
            default:
                return null;
        }
    }

    private ErrorReason checkAvailability(MTPServiceAccessPoint mtpSap,
                                          SubSystemNumber ssn, RoutingIndicator ri, SCCPMessage message, boolean localOriginator) {
        if (mtpSap == null) {

            String log = ": SCCP is not available : "
                    + "MtpSap =  " + mtpSap
                    + ";SSN = " + ssn
                    + ";RI = " + ri;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return ErrorReason.SCCP_FAILURE;
        }
        if (!this.sccpStack.getSccpLayerManagement().isRemoteSpcAccessible(mtpSap.getDpc(), mtpSap.getNi())) {

            String log = ": Remote SPC is not Accessible :"
                    + " MtpSap = " + mtpSap
                    + ";SSN = " + ssn
                    + ";RI = " + ri;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return ErrorReason.MTP_FAILURE;
        }

        if (!this.isSccpAvailable(mtpSap.getDpc(), mtpSap.getNi())) {

            String log = ": SCCP is not available : "
                    + "MtpSap = " + mtpSap
                    + ";SSN = " + ssn
                    + ";RI = " + ri;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return ErrorReason.SCCP_FAILURE;
        }

        if (ri == RoutingIndicator.ROUTE_ON_SSN) {
            if (ssn == null) {

                String log = ": UnEquippedUser : "
                        + "MtpSap = " + mtpSap
                        + ";SSN = " + ssn
                        + ";RI = " + ri;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return ErrorReason.UNEQUIPPED_USER;
            } else if (ssn == SubSystemNumber.SCCP_MANAGEMENT) {

                String log = ": UnEquippedUser : "
                        + "MtpSap = " + mtpSap
                        + ";SSN = " + ssn
                        + ";RI = " + ri;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return ErrorReason.UNEQUIPPED_USER;
            }

            SubSystem subSystem = sccpStack.getSccpLayerManagement().getLocalSubsystem(ssn);
            if (subSystem == null) {

                String log = ": UnEquippedUser : "
                        + "MtpSap = " + mtpSap
                        + ";SSN = " + ssn
                        + ";RI = " + ri;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return ErrorReason.UNEQUIPPED_USER;
            }
            if (subSystem.getState() == SubSystem.State.IDLE) {

                String log = ": SubSystemFailure : "
                        + "MtpSap = " + mtpSap
                        + ";SSN = " + ssn
                        + ";RI = " + ri;
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return ErrorReason.SUBSYSTEM_FAILURE;
            }
        }
        return ErrorReason.NONE;
    }

    //ITU-T Q.714 Clause 2.2.2.1
    private void routeConnectionlessMessage(SCCPMessage message, boolean localOriginator) throws Exception {

        SubSystemNumber ssn = message.getCalledPartyAddress().getSubSystemNumber();
        GlobalTitle gt = message.getCalledPartyAddress().getGlobalTitle();

        //Called address does not include DPC
        if (!message.getCalledPartyAddress().getAddressIndicator().getPointCodeIndicator()) {
            if (gt == null) {//gt present?

                this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.UNQUALIFIED, localOriginator);
                String log = ":SCCP failure:. "
                        + "ITU-T Q.714[2.2.2.1] didn't meet. CalledPartyAddress don't contains PC and GT: " + message.getCalledPartyAddress();
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return;
            }
            this.processRouteOnGt(message, localOriginator);
            return;
        }

        int spc = message.getCalledPartyAddress().getSignallingPointCode().getSignallingPointCode();

        //DPC from called address is not this node
        if (!(this.sccpStack.getSccpLayerManagement().isLocalSpc(spc))) {
            fromSCLCToMTP(message, localOriginator);
            return;
        }

        //DPC from called address is this node and SSN is null or equals to Zero. 
        //See ITU-T Q.714 2.2.2.1
        if (ssn == null || ssn == SubSystemNumber.SSN_NOT_USED) {
            if (gt == null) {

                this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.UNQUALIFIED, localOriginator);
                String log = ":SCCP Failure:"
                        + " No SSN and no GT exists in called party. CalledParty = " + message.getCalledPartyAddress();
                logger.error(localOriginator ? "Tx" + log : "Rx" + log);
                return;
            }
            this.processRouteOnGt(message, localOriginator);//routeOnGT
            return;
        }

        if (gt == null) {
            this.processRouteOnSSN(message, localOriginator);
            return;
        }

        this.processRouteOnGt(message, localOriginator);
    }

    private boolean isSccpAvailable(int dpc, NetworkIndicator ni) {
        if (this.sccpStack.getSccpLayerManagement().isRemoteSpcAccessible(dpc, ni)) {
            return true;
        }

        RemoteSignallingPoint remoteSignallingPoint = this.sccpStack.getSccpLayerManagement().getRemoteSignallingPoint(dpc);

        if (remoteSignallingPoint == null) {
            return false;
        }

        if (!remoteSignallingPoint.isRemoteSCCPProhibited()) {
            return true;
        }
        return false;
    }

    private void messageForUnavailableSubsystem(boolean localOriginator, SCCPMessage message) {
        if (!localOriginator) {
            SubsystemProhibited ssp = SCCPManagementMessageFactory.createSubsystemProhibited();
            ssp.setAffectedPointCode(new SignallingPointCode(message.getDpc()));
            ssp.setAffectedSSN(message.getCalledPartyAddress().getSubSystemNumber());
            ssp.setSubsystemMultiplicityIndicator(new SubsystemMultiplicityIndicator(0));
            SCCPAddress callingParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, message.getDpc(), null, SubSystemNumber.SCCP_MANAGEMENT);
            SCCPAddress calledParty = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN, message.getOpc(), null, SubSystemNumber.SCCP_MANAGEMENT);
            this.sccpStack.getSccpConnectionlessControl().sendSccpManagementMessage(callingParty, calledParty, ssp, localOriginator);
        }
    }

    private void treatCallingPartyAddressOnLocalOriginatorCase(RoutingIndicator calledRi,
                                                               SCCPAddress callingParty, MTPServiceAccessPoint mtpSap) {
        if (calledRi == RoutingIndicator.ROUTE_ON_GT
                && callingParty.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN) {
            callingParty.setSignallingPointCode(new SignallingPointCode(mtpSap.getOpc()));
        }
    }

    private void treatCallingPartyAddressOnTransitCase(RoutingIndicator calledRi,
                                                       SCCPAddress callingParty, int opc) {
        //Calling party address treatment. ITU-T Q.714 2.7.5 clause

        if (callingParty.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTE_ON_SSN
                && callingParty.getAddressIndicator().getPointCodeIndicator() == false) {
            callingParty.setSignallingPointCode(new SignallingPointCode(opc));
        }
    }

    //ITU -Q.714 Figure C.1 sheet 4 of 12
    private void fromSCLCToMTP(SCCPMessage message, boolean localOriginator) throws Exception {
        SCCPAddress calledParty = message.getCalledPartyAddress();
        SCCPAddress callingParty = message.getCallingPartyAddress();

        int spc = calledParty.getSignallingPointCode().getSignallingPointCode();

        if (logger.isDebugEnabled()) {
            String log = ": Trying to transfer message to the MTP. "
                    + "CalledParty = " + calledParty + "; CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
        }

        MTPServiceAccessPoint mtpSap = this.sccpStack.getSccpLayerManagement().getMtpSap(spc);

        if (mtpSap == null) {
            String log = ": SCCP Routing failure: "
                    + "No MTP-SAP found. SPC = " + spc + "; CalledParty = " + calledParty
                    + "; CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        SubSystemNumber ssn = message.getCalledPartyAddress().getSubSystemNumber();

        RemoteSignallingPoint remoteSignallingPoint = this.sccpStack.getSccpLayerManagement().getRemoteSignallingPoint(spc);

        if (remoteSignallingPoint == null || remoteSignallingPoint.isRemoteSpProhibited()) {//DPC accessible ?
            this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.MTP_FAILURE, localOriginator);
            String log = ": SCCP failure: RemoteSignallingPoint is not available. SPC = " + spc
                    + "; CalledParty = " + calledParty + "; CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        if (remoteSignallingPoint.isRemoteSCCPProhibited()) {//SCCP available?
            this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.SCCP_FAILURE, localOriginator);
            String log = ": SCCP failure: RemoteSCCP is not available. SPC = " + spc
                    + ";CalledParty = " + calledParty + "; CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        //Called address include SSN?
        if (ssn == null || ssn == SubSystemNumber.SSN_NOT_USED) {

            if (localOriginator) {
                this.treatCallingPartyAddressOnLocalOriginatorCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, mtpSap);
            } else {
                this.treatCallingPartyAddressOnTransitCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, message.getOpc());
            }

            this.mtpTransferRequest(mtpSap, message);
            return;
        }

        RemoteSubSystem remoteSubSystem = remoteSignallingPoint.getRemoteSubsystem(ssn);

        if (remoteSubSystem == null) {
            this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.UNEQUIPPED_USER, localOriginator);
            String log = ": SCCP failure: RemoteSubsystem is unequipped. "
                    + "SPC = " + spc + ";SSN = " + ssn + ";CalledParty = " + calledParty + ";CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }
        if (remoteSubSystem.isProhibited()) {
            this.sccpStack.getSccpConnectionlessControl().routingFailure(message, ErrorReason.SUBSYSTEM_FAILURE, localOriginator);
            String log = ": SCCP failure: RemoteSubsystm is unavailable."
                    + "SPC = " + spc + ";SSN = " + ssn + ";CalledParty = " + calledParty
                    + ";CallingParty = " + callingParty;
            logger.error(localOriginator ? "Tx" + log : "Rx" + log);
            return;
        }

        if (localOriginator) {
            this.treatCallingPartyAddressOnLocalOriginatorCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, mtpSap);
        } else {
            this.treatCallingPartyAddressOnTransitCase(calledParty.getAddressIndicator().getRoutingIndicator(), callingParty, message.getOpc());
        }

        this.mtpTransferRequest(mtpSap, message);
    }

    private void mtpTransferRequest(MTPServiceAccessPoint mtpSap, SCCPMessage message) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        message.encode(baos);

        MTPTransferMessage mtpTransferMessage = new MTPTransferMessage(mtpSap.getOpc(),
                mtpSap.getDpc(), message.getSls(), sccpStack.getServiceIdentificator(),
                0, mtpSap.getNi(), baos.toByteArray());

        if (logger.isDebugEnabled()) {
            logger.debug("Sending message to the MTP destination. MTP-SAP = "
                    + mtpSap + ";" + mtpTransferMessage);
        }
        this.sccpStack.mtpTransferRequest(mtpTransferMessage);
    }
}
