/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.messages;

import azrc.az.isup.enums.ConferenceTreatmentIndicator;
import azrc.az.isup.enums.MessageType;
import azrc.az.isup.enums.TransmissionMediumRequirement;
import azrc.az.isup.parameters.AccessDeliveryInformation;
import azrc.az.isup.parameters.AccessTransport;
import azrc.az.isup.parameters.ApplicationTransport;
import azrc.az.isup.parameters.BackwardCallIndicators;
import azrc.az.isup.parameters.BackwardGVNS;
import azrc.az.isup.parameters.CCNRPossibleIndicator;
import azrc.az.isup.parameters.CallDiversionInformation;
import azrc.az.isup.parameters.CallHistoryInformation;
import azrc.az.isup.parameters.CallReference;
import azrc.az.isup.parameters.CallTransferNumber;
import azrc.az.isup.parameters.CauseIndicators;
import azrc.az.isup.parameters.ConnectedNumber;
import azrc.az.isup.parameters.EchoControlInformation;
import azrc.az.isup.parameters.EventInformation;
import azrc.az.isup.parameters.GenericNotificationIndicator;
import azrc.az.isup.parameters.GenericNumber;
import azrc.az.isup.parameters.NetworkSpecificFacility;
import azrc.az.isup.parameters.OptionalBackwardCallIndicators;
import azrc.az.isup.parameters.ParameterCompatibilityInformation;
import azrc.az.isup.parameters.PivotRoutingBackwardInformation;
import azrc.az.isup.parameters.RedirectStatus;
import azrc.az.isup.parameters.RedirectionNumberRestriction;
import azrc.az.isup.parameters.RemoteOperations;
import azrc.az.isup.parameters.ServiceActivation;
import azrc.az.isup.parameters.UIDActionIndicators;
import azrc.az.isup.parameters.UserToUserIndicators;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public class CallProgress implements IsupMessage {

    public static final MessageType MESSAGE_TYPE_CPG = MessageType.CALL_PROGRESS;
    private EventInformation eventInformation;
    private CauseIndicators causeIndicators;
    private CallReference callReference;
    private BackwardCallIndicators backwardCallIndicators;
    private OptionalBackwardCallIndicators optionalBackwardCallIndicators;
    private AccessTransport accessTransport;
    private UserToUserIndicators userToUserIndicators;
    private List<GenericNotificationIndicator> genericNotificationIndicators;
    private NetworkSpecificFacility networkSpecificFacility;
    private RemoteOperations remoteOperations;
    private TransmissionMediumRequirement transmissionMediumRequirement;
    private AccessDeliveryInformation accessDeliveryInformation;
    private ParameterCompatibilityInformation parameterCompatibilityInformation;
    private CallDiversionInformation callDiversionInformation;
    private ServiceActivation serviceActivation;
    private RedirectionNumberRestriction redirectinNumberRestriction;
    private CallTransferNumber callTransferNumber;
    private EchoControlInformation echoControlInformation;
    private ConnectedNumber connectedNumber;
    private BackwardGVNS backwardGVNS;
    private List<GenericNumber> genericNumbers;
    private CallHistoryInformation callHistoryInformation;
    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private UIDActionIndicators uIDActionIndicators;
    private ApplicationTransport applicationTransportParameter;
    private CCNRPossibleIndicator cCNRPossibleIndicator;
    private PivotRoutingBackwardInformation pivotRoutingBackwardInformation;
    private RedirectStatus redirectStatus;

    public CallProgress() {
    }

    public CallProgress(EventInformation eventInformation) {
        this.eventInformation = eventInformation;
    }

    /**
     * @return the userToUserIndicators
     */
    public UserToUserIndicators getUserToUserIndicators() {
        return userToUserIndicators;
    }

    /**
     * @param userToUserIndicators the userToUserIndicators to set
     */
    public void setUserToUserIndicators(UserToUserIndicators userToUserIndicators) {
        this.userToUserIndicators = userToUserIndicators;
    }

    /**
     * @return the remoteOperations
     */
    public RemoteOperations getRemoteOperations() {
        return remoteOperations;
    }

    /**
     * @param remoteOperations the remoteOperations to set
     */
    public void setRemoteOperations(RemoteOperations remoteOperations) {
        this.remoteOperations = remoteOperations;
    }

    /**
     * @return the transmissionMediumRequirement
     */
    public TransmissionMediumRequirement getTransmissionMediumRequirement() {
        return transmissionMediumRequirement;
    }

    /**
     * @param transmissionMediumRequirement the transmissionMediumRequirement to set
     */
    public void setTransmissionMediumRequirement(TransmissionMediumRequirement transmissionMediumRequirement) {
        this.transmissionMediumRequirement = transmissionMediumRequirement;
    }

    /**
     * @return the serviceActivation
     */
    public ServiceActivation getServiceActivation() {
        return serviceActivation;
    }

    /**
     * @param serviceActivation the serviceActivation to set
     */
    public void setServiceActivation(ServiceActivation serviceActivation) {
        this.serviceActivation = serviceActivation;
    }

    /**
     * @return the uIDActionIndicators
     */
    public UIDActionIndicators getuIDActionIndicators() {
        return uIDActionIndicators;
    }

    /**
     * @param uIDActionIndicators the uIDActionIndicators to set
     */
    public void setuIDActionIndicators(UIDActionIndicators uIDActionIndicators) {
        this.uIDActionIndicators = uIDActionIndicators;
    }

    /**
     * @return the cCNRPossibleIndicator
     */
    public CCNRPossibleIndicator getcCNRPossibleIndicator() {
        return cCNRPossibleIndicator;
    }

    /**
     * @param cCNRPossibleIndicator the cCNRPossibleIndicator to set
     */
    public void setcCNRPossibleIndicator(CCNRPossibleIndicator cCNRPossibleIndicator) {
        this.cCNRPossibleIndicator = cCNRPossibleIndicator;
    }

    public AccessDeliveryInformation getAccessDeliveryInformation() {
        return accessDeliveryInformation;
    }

    public void setAccessDeliveryInformation(AccessDeliveryInformation accessDeliveryInformation) {
        this.accessDeliveryInformation = accessDeliveryInformation;
    }

    public AccessTransport getAccessTransport() {
        return accessTransport;
    }

    public void setAccessTransport(AccessTransport accessTransport) {
        this.accessTransport = accessTransport;
    }

    public ApplicationTransport getApplicationTransportParameter() {
        return applicationTransportParameter;
    }

    public void setApplicationTransportParameter(ApplicationTransport applicationTransportParameter) {
        this.applicationTransportParameter = applicationTransportParameter;
    }

    public BackwardCallIndicators getBackwardCallIndicators() {
        return backwardCallIndicators;
    }

    public void setBackwardCallIndicators(BackwardCallIndicators backwardCallIndicators) {
        this.backwardCallIndicators = backwardCallIndicators;
    }

    public BackwardGVNS getBackwardGVNS() {
        return backwardGVNS;
    }

    public void setBackwardGVNS(BackwardGVNS backwardGVNS) {
        this.backwardGVNS = backwardGVNS;
    }

    public CallDiversionInformation getCallDiversionInformation() {
        return callDiversionInformation;
    }

    public void setCallDiversionInformation(CallDiversionInformation callDiversionInformation) {
        this.callDiversionInformation = callDiversionInformation;
    }

    public CallHistoryInformation getCallHistoryInformation() {
        return callHistoryInformation;
    }

    public void setCallHistoryInformation(CallHistoryInformation callHistoryInformation) {
        this.callHistoryInformation = callHistoryInformation;
    }

    public CallReference getCallReference() {
        return callReference;
    }

    public void setCallReference(CallReference callReference) {
        this.callReference = callReference;
    }

    public CallTransferNumber getCallTransferNumber() {
        return callTransferNumber;
    }

    public void setCallTransferNumber(CallTransferNumber callTransferNumber) {
        this.callTransferNumber = callTransferNumber;
    }

    public CauseIndicators getCauseIndicators() {
        return causeIndicators;
    }

    public void setCauseIndicators(CauseIndicators causeIndicators) {
        this.causeIndicators = causeIndicators;
    }

    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
        return conferenceTreatmentIndicator;
    }

    public void setConferenceTreatmentIndicator(ConferenceTreatmentIndicator conferenceTreatmentIndicator) {
        this.conferenceTreatmentIndicator = conferenceTreatmentIndicator;
    }

    public ConnectedNumber getConnectedNumber() {
        return connectedNumber;
    }

    public void setConnectedNumber(ConnectedNumber connectedNumber) {
        this.connectedNumber = connectedNumber;
    }

    public EchoControlInformation getEchoControlInformation() {
        return echoControlInformation;
    }

    public void setEchoControlInformation(EchoControlInformation echoControlInformation) {
        this.echoControlInformation = echoControlInformation;
    }

    public EventInformation getEventInformation() {
        return eventInformation;
    }

    public void setEventInformation(EventInformation eventInformation) {
        this.eventInformation = eventInformation;
    }

    public List<GenericNotificationIndicator> getGenericNotificationIndicators() {
        return genericNotificationIndicators;
    }

    public void setGenericNotificationIndicators(List<GenericNotificationIndicator> genericNotificationIndicators) {
        this.genericNotificationIndicators = genericNotificationIndicators;
    }

    public List<GenericNumber> getGenericNumbers() {
        return genericNumbers;
    }

    public void setGenericNumbers(List<GenericNumber> genericNumbers) {
        this.genericNumbers = genericNumbers;
    }

    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return networkSpecificFacility;
    }

    public void setNetworkSpecificFacility(NetworkSpecificFacility networkSpecificFacility) {
        this.networkSpecificFacility = networkSpecificFacility;
    }

    public OptionalBackwardCallIndicators getOptionalBackwardCallIndicators() {
        return optionalBackwardCallIndicators;
    }

    public void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators optionalBackwardCallIndicators) {
        this.optionalBackwardCallIndicators = optionalBackwardCallIndicators;
    }

    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return parameterCompatibilityInformation;
    }

    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation parameterCompatibilityInformation) {
        this.parameterCompatibilityInformation = parameterCompatibilityInformation;
    }

    public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation() {
        return pivotRoutingBackwardInformation;
    }

    public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation pivotRoutingBackwardInformation) {
        this.pivotRoutingBackwardInformation = pivotRoutingBackwardInformation;
    }

    public RedirectStatus getRedirectStatus() {
        return redirectStatus;
    }

    public void setRedirectStatus(RedirectStatus redirectStatus) {
        this.redirectStatus = redirectStatus;
    }

    public RedirectionNumberRestriction getRedirectinNumberRestriction() {
        return redirectinNumberRestriction;
    }

    public void setRedirectinNumberRestriction(RedirectionNumberRestriction redirectinNumberRestriction) {
        this.redirectinNumberRestriction = redirectinNumberRestriction;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        
    }

    @Override
    public MessageType getMessageType() {
        return MESSAGE_TYPE_CPG;
    }

}
