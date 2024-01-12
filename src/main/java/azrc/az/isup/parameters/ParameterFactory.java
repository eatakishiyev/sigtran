/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.TransmissionMediumRequirementPrime;
import azrc.az.isup.enums.TransmissionMediumRequirement;
import azrc.az.isup.enums.AddressPresentationRestricted;
import azrc.az.isup.enums.CallDiversionTreatmentIndicator;
import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.enums.ClosedUserGroupCallIndicator;
import azrc.az.isup.enums.CodingStandard;
import azrc.az.isup.enums.ConferenceTreatmentIndicator;
import azrc.az.isup.enums.ContinuityCheckIndicator;
import azrc.az.isup.enums.EchoControlDeviceIndicator;
import azrc.az.isup.enums.EndToEndInformationIndicator;
import azrc.az.isup.enums.EndToEndMethodIndicator;
import azrc.az.isup.enums.ISDNAccessIndicator;
import azrc.az.isup.enums.ISDNUserPartIndicator;
import azrc.az.isup.enums.ISDNUserPartPreferenceIndicator;
import azrc.az.isup.enums.InformationTransferCapability;
import azrc.az.isup.enums.InformationTransferRate;
import azrc.az.isup.enums.InternationalNetworkNumberIndicator;
import azrc.az.isup.enums.InterworkingIndicator;
import azrc.az.isup.enums.NationalInternationalCallIndicator;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.NetworkIdentificationPlan;
import azrc.az.isup.enums.NumberIncompleteIndicator;
import azrc.az.isup.enums.NumberQualifierIndicator;
import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.SCCPMethodIndicator;
import azrc.az.isup.enums.SatelliteIndicator;
import azrc.az.isup.enums.Screening;
import azrc.az.isup.enums.TransferMode;
import azrc.az.isup.enums.TypeOfNetworkIdentification;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class ParameterFactory {

    public static NatureOfConnectionIndicators createNatureOfConnectionIndicators(ByteArrayInputStream bais) {
        NatureOfConnectionIndicators natureOfConnectionIndicators = new NatureOfConnectionIndicators();
        natureOfConnectionIndicators.decode(bais);
        return natureOfConnectionIndicators;
    }

    public static NatureOfConnectionIndicators createNatureOfConnectionIndicators(byte[] data) {
        NatureOfConnectionIndicators natureOfConnectionIndicators = new NatureOfConnectionIndicators();
        natureOfConnectionIndicators.decode(data);
        return natureOfConnectionIndicators;
    }

    public static NatureOfConnectionIndicators createNatureOfConnectionIndicators() {
        return new NatureOfConnectionIndicators();
    }

    public static NatureOfConnectionIndicators createNatureOfConnectionIndicators(SatelliteIndicator satelliteIndicator, ContinuityCheckIndicator continuityCheckIndicator,
            EchoControlDeviceIndicator echoControlDeviceIndicator) {
        return new NatureOfConnectionIndicators(satelliteIndicator, continuityCheckIndicator, echoControlDeviceIndicator);
    }

    public static ForwardCallIndicators createForwardCallIndicators(ByteArrayInputStream bais) {
        ForwardCallIndicators forwardCallIndicators = new ForwardCallIndicators();
        forwardCallIndicators.decode(bais);
        return forwardCallIndicators;
    }

    public static ForwardCallIndicators createForwardCallIndicators(byte[] data) {
        ForwardCallIndicators forwardCallIndicators = new ForwardCallIndicators();
        forwardCallIndicators.decode(data);
        return forwardCallIndicators;
    }

    public static ForwardCallIndicators createForwardCallIndicators() {
        return new ForwardCallIndicators();
    }

    public static ForwardCallIndicators createForwardCallIndicators(NationalInternationalCallIndicator nationalInternationalCallIndicator,
            EndToEndMethodIndicator endToEndMethodIndicator, InterworkingIndicator interworkingIndicator,
            EndToEndInformationIndicator endToEndInformationIndicator, ISDNUserPartIndicator iSDNUserPartIndicator,
            ISDNUserPartPreferenceIndicator iSDNUserPartPreferenceIndicator, ISDNAccessIndicator iSDNAccessIndicator,
            SCCPMethodIndicator sCCPMethodIndicator) {
        return new ForwardCallIndicators(nationalInternationalCallIndicator, endToEndMethodIndicator,
                interworkingIndicator, endToEndInformationIndicator, iSDNUserPartIndicator,
                iSDNUserPartPreferenceIndicator, iSDNAccessIndicator, sCCPMethodIndicator);
    }

    public static CallingPartysCategory createCallingPartyCategory(ByteArrayInputStream bais) {
        return CallingPartysCategory.getInstance(bais.read());
    }

    public static TransmissionMediumRequirement createTransmissionMediumRequirement(ByteArrayInputStream bais) {
        return TransmissionMediumRequirement.getInstance(bais.read());
    }

    public static CalledPartyNumber createCalledPartyNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        CalledPartyNumber calledPartyNumber = new CalledPartyNumber();
        calledPartyNumber.decode(data);
        return calledPartyNumber;
    }

    public static CalledPartyNumber createCalledPartyNumber() {
        return new CalledPartyNumber();
    }

    public static CalledPartyNumber createCalledPartyNumber(NatureOfAddress natureOfAddress,
            InternationalNetworkNumberIndicator internationalNetworkNumberIndicator,
            NumberingPlan numberingPlan, String address) {
        return new CalledPartyNumber(natureOfAddress, internationalNetworkNumberIndicator, numberingPlan, address);
    }

    public static CallingPartyNumber createCallingPartyNumber() {
        return new CallingPartyNumber();
    }

    public static CallingPartyNumber createCallingPartyNumber(NatureOfAddress natureOfAddress, boolean numberComplete,
            NumberingPlan numberingPlan, AddressPresentationRestricted addressPresentationRestricted,
            Screening screening, String address) {
        return new CallingPartyNumber(natureOfAddress, numberComplete, numberingPlan, addressPresentationRestricted, screening, address);
    }

    public static CallingPartyNumber createCallingPartyNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        CallingPartyNumber callingPartyNumber = new CallingPartyNumber();
        callingPartyNumber.decode(data);
        return callingPartyNumber;
    }

    public static TransitNetworkSelection createTransitNetworkSelection() {
        return new TransitNetworkSelection();
    }

    public static TransitNetworkSelection createTransitNetworkSelection(TypeOfNetworkIdentification typeOfNetworkIdentification,
            NetworkIdentificationPlan networkIdentificationPlan, String networkIdentification) {
        return new TransitNetworkSelection(typeOfNetworkIdentification, networkIdentificationPlan, networkIdentification);
    }

    public static TransitNetworkSelection createTransitNetworkSelection(byte[] data) throws IllegalNumberFormatException {
        TransitNetworkSelection transitNetworkSelection = new TransitNetworkSelection();
        transitNetworkSelection.decode(data);
        return transitNetworkSelection;
    }

    public static CallReference createCallReference() {
        return new CallReference();
    }

    public static CallReference createCallReference(int callIdentity, int spc) {
        return new CallReference(callIdentity, spc);
    }

    public static CallReference createCallReference(byte[] data) {
        CallReference callReference = new CallReference();
        callReference.decode(data);
        return callReference;
    }

    public static OptionalForwardCallIndicators createOptinalForwardCallIndicators() {
        return new OptionalForwardCallIndicators();
    }

    public static OptionalForwardCallIndicators createOptionalForwardCallIndicators(ClosedUserGroupCallIndicator closedUserGroupCallIndicator,
            boolean additionalInformationWillBeSent, boolean connectedLineIdentityRequested) {
        return new OptionalForwardCallIndicators(closedUserGroupCallIndicator, additionalInformationWillBeSent, connectedLineIdentityRequested);
    }

    public static OptionalForwardCallIndicators createOptionalForwardCallIndicators(byte[] data) {
        OptionalForwardCallIndicators optionalForwardCallIndicators = new OptionalForwardCallIndicators();
        optionalForwardCallIndicators.decode(data);
        return optionalForwardCallIndicators;
    }

    public static RedirectingNumber createRedirectingNumber() {
        return new RedirectingNumber();
    }

    public static RedirectingNumber createRedirectingNumber(NatureOfAddress natureOfAddress, NumberingPlan numberingPlan,
            AddressPresentationRestricted addressPresentationRestricted, String address) {
        return new RedirectingNumber(natureOfAddress, numberingPlan, addressPresentationRestricted, address);
    }

    public static RedirectingNumber createRedirectingNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        RedirectingNumber redirectingNumber = new RedirectingNumber();
        redirectingNumber.decode(data);
        return redirectingNumber;
    }

    public static RedirectionInformation createRedirectionInformation(byte[] data) throws ParameterOutOfRangeException {
        RedirectionInformation redirectionInformation = new RedirectionInformation();
        redirectionInformation.decode(data);
        return redirectionInformation;
    }

    public static ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode() {
        return new ClosedUserGroupInterlockCode();
    }

    public static ClosedUserGroupInterlockCode createClosedGroupInterlockCode(String networkIdentity, byte[] binaryCode) {
        return new ClosedUserGroupInterlockCode(networkIdentity, binaryCode);
    }

    public static ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode(byte[] data) throws IncorrectSyntaxException, IOException {
        ClosedUserGroupInterlockCode closedUserGroupInterlockCode = new ClosedUserGroupInterlockCode();
        closedUserGroupInterlockCode.decode(data);
        return closedUserGroupInterlockCode;
    }

    public static ConnectionRequest createConnectionRequest() {
        return new ConnectionRequest();
    }

    public static ConnectionRequest createConnectionRequest(int localReference, int spc) {
        return new ConnectionRequest(localReference, spc);
    }

    public static ConnectionRequest createConnectionRequest(byte[] data) {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.decode(data);
        return connectionRequest;
    }

    public static OriginalCalledNumber createOriginalCalledNumber() {
        return new OriginalCalledNumber();
    }

    public static OriginalCalledNumber createOriginalCalledNumber(NatureOfAddress natureOfAddress,
            NumberingPlan numberingPlan, AddressPresentationRestricted addressPresentationRestricted, String address) {
        return new OriginalCalledNumber(natureOfAddress, numberingPlan, addressPresentationRestricted, address);
    }

    public static OriginalCalledNumber createOriginalCalledNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        OriginalCalledNumber originalCalledNumber = new OriginalCalledNumber();
        originalCalledNumber.decode(data);
        return originalCalledNumber;
    }

    public static UserToUserIndicators createUserToUserIndicators() {
        return new UserToUserIndicators();
    }

    public static UserToUserIndicators createUserToUserIndicators(byte[] data) {
        UserToUserIndicators userToUserIndicators = new UserToUserIndicators();
        userToUserIndicators.decode(data);
        return userToUserIndicators;
    }

    public static GenericNumber createGenericNumber() {
        return new GenericNumber();
    }

    public static GenericNumber createGenericNumber(NumberQualifierIndicator numberQualifierIndicator,
            NatureOfAddress natureOfAddress, NumberIncompleteIndicator numberIncompleteIndicator,
            NumberingPlan numberingPlan, AddressPresentationRestricted addressPresentationRestricted, Screening screening, String addres) {
        return new GenericNumber(numberQualifierIndicator, natureOfAddress,
                numberIncompleteIndicator, numberingPlan,
                addressPresentationRestricted, screening, addres);
    }

    public static GenericNumber createGenericNumber(byte[] data) throws IllegalNumberFormatException, IOException {
        GenericNumber genericNumber = new GenericNumber();
        genericNumber.decode(data);
        return genericNumber;
    }

    public static PropagationDelayCounter createPropagationDelayCounter() {
        return new PropagationDelayCounter();
    }

    public static PropagationDelayCounter createPropagationDelayCounter(int value) {
        return new PropagationDelayCounter(value);
    }

    public static PropagationDelayCounter createPropagationDelayCounter(byte[] data) {
        PropagationDelayCounter propagationDelayCounter = new PropagationDelayCounter();
        propagationDelayCounter.decode(data);
        return propagationDelayCounter;
    }

    public static UserServiceInformationPrime createUserServiceInformationPrime(byte[] data) throws Exception {
        UserServiceInformationPrime userServiceInformationPrime = new UserServiceInformationPrime();
        userServiceInformationPrime.decode(data);
        return userServiceInformationPrime;
    }

    public static UserServiceInformationPrime createUserServiceInformationPrime() {
        return new UserServiceInformationPrime();
    }

    public static UserServiceInformationPrime createUserServiceInformationPrime(CodingStandard codingStandard, InformationTransferCapability informationTransferCapability,
            TransferMode transferMode, InformationTransferRate informationTransferRate) {
        return new UserServiceInformationPrime(codingStandard, informationTransferCapability, transferMode, informationTransferRate);
    }

    public static NetworkSpecificFacility createNetworkSpecificFacility(byte[] data) {
        NetworkSpecificFacility networkSpecificFacility = new NetworkSpecificFacility();
        networkSpecificFacility.decode(data);
        return networkSpecificFacility;
    }

    public static GenericDigits createGenericDigits(byte[] data) throws IOException {
        GenericDigits genericDigits = new GenericDigits();
        genericDigits.decode(data);
        return genericDigits;
    }

    public static ParameterCompatibilityInformation createParameterCompatibilityInformation(byte[] data) {
        ParameterCompatibilityInformation parameterCompatibilityInformation = new ParameterCompatibilityInformation();
        parameterCompatibilityInformation.decode(data);
        return parameterCompatibilityInformation;
    }

    public static GenericNotificationIndicator createGenericNotificationIndicator(byte[] data) {
        GenericNotificationIndicator genericNotificationIndicator = new GenericNotificationIndicator();
        genericNotificationIndicator.decode(data);
        return genericNotificationIndicator;
    }

    public static SignallingPointCode createOriginationISCPointCode(byte[] data) {
        SignallingPointCode signallingPointCode = new SignallingPointCode();
        signallingPointCode.decode(data);
        return signallingPointCode;
    }

    public static UserTeleServiceInformation createUserTeleServiceInformation(byte[] data) {
        UserTeleServiceInformation userTeleServiceInformation = new UserTeleServiceInformation();
        userTeleServiceInformation.decode(data);
        return userTeleServiceInformation;
    }

    public static RemoteOperations createRemoteOperations(byte[] data) {
        RemoteOperations remoteOperations = new RemoteOperations();
        remoteOperations.decode(data);
        return remoteOperations;
    }

    public static ServiceActivation createServiceActivation(byte[] data) {
        ServiceActivation serviceActivation = new ServiceActivation();
        serviceActivation.decode(data);
        return serviceActivation;
    }

    public static MLPPPrecedence createMLPPPrecedence(byte[] data) throws IncorrectSyntaxException {
        MLPPPrecedence mLPPPrecedence = new MLPPPrecedence();
        mLPPPrecedence.decode(data);
        return mLPPPrecedence;
    }

    public static TransmissionMediumRequirementPrime createTransmissionMediumRequirementPrime(byte[] data) {
        return TransmissionMediumRequirementPrime.getInstance(data[0] & 0xFF);
    }

    public static LocationNumber createLocationNumber(byte[] data) throws IllegalNumberFormatException, IOException {
        LocationNumber locationNumber = new LocationNumber();
        locationNumber.decode(data);
        return locationNumber;
    }

    public static ForwardGVNS createForwardGVNS(byte[] data) {
        ForwardGVNS forwardGVNS = new ForwardGVNS();
        forwardGVNS.decode(data);
        return forwardGVNS;
    }

    public static CCSS createCCSS(byte[] data) {
        CCSS ccss = new CCSS();
        ccss.decode(data);
        return ccss;
    }

    public static NetworkManagementControls createNetworkManagementControls(byte[] data) {
        NetworkManagementControls networkManagementControls = new NetworkManagementControls();
        networkManagementControls.decode(data);
        return networkManagementControls;
    }

    public static CircuitAssignmentMap createCircuitAssignmentMap(byte[] data) {
        CircuitAssignmentMap circuitAssignmentMap = new CircuitAssignmentMap();
        circuitAssignmentMap.decode(data);
        return circuitAssignmentMap;
    }

    public static GenericDigits createCorrelationId(byte[] data) throws IOException {
        GenericDigits genericDigits = new GenericDigits();
        genericDigits.decode(data);
        return genericDigits;
    }

    public static CallDiversionTreatmentIndicator createCallDiversionTreatmentIndicators(byte[] data) {
        return CallDiversionTreatmentIndicator.getInstance(data[0]);
    }

    public static CalledINNumber createCalledINNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        CalledINNumber calledINNumber = new CalledINNumber();
        calledINNumber.decode(data);
        return calledINNumber;
    }

    public static CallOfferingTreatmentIndicators createCallOfferingTreatmentIndicators(byte[] data) {
        CallOfferingTreatmentIndicators callOfferingTreatmentIndicators = new CallOfferingTreatmentIndicators();
        callOfferingTreatmentIndicators.decode(data);
        return callOfferingTreatmentIndicators;
    }

    public static ConferenceTreatmentIndicator createConferenceTreatmentIndicators(byte[] data) {
        return ConferenceTreatmentIndicator.getInstance(data[0]);
    }

    public static ScfId createScfId(byte[] tmp) {
        ScfId scfId = new ScfId();
        scfId.decode(tmp);
        return scfId;
    }

    public static UIDCapabilityIndicators createUidCapabilityIndicators(byte[] data) {
        UIDCapabilityIndicators uIDCapabilityIndicators = new UIDCapabilityIndicators();
        uIDCapabilityIndicators.decode(data);
        return uIDCapabilityIndicators;
    }

    public static EchoControlInformation createEchoControlInformation(byte[] data) {
        EchoControlInformation echoControlInformation = new EchoControlInformation();
        echoControlInformation.decode(data);
        return echoControlInformation;
    }

    public static HopCounter createHopCounter(byte[] data) {
        HopCounter hopCounter = new HopCounter();
        hopCounter.decode(data);
        return hopCounter;
    }

    public static CollectCallRequest createCollectCallRequest(byte[] data) {
        CollectCallRequest collectCallRequest = new CollectCallRequest();
        collectCallRequest.decode(data);
        return collectCallRequest;
    }

    public static ApplicationTransport createApplicationTransportParameter(byte[] data) {
        ApplicationTransport applicationTransport = new ApplicationTransport();
        applicationTransport.decode(data);
        return applicationTransport;
    }

    public static PivotCapability createPivotCapability(byte[] data) {
        PivotCapability pivotCapability = new PivotCapability();
        pivotCapability.decode(data);
        return pivotCapability;
    }

    public static CalledDirectoryNumber createCalledDirectoryNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        CalledDirectoryNumber calledDirectoryNumber = new CalledDirectoryNumber();
        calledDirectoryNumber.decode(data);
        return calledDirectoryNumber;
    }

    public static OriginalCalledINNumber createOriginalCalledINNumber(byte[] data) throws IOException, IllegalNumberFormatException {
        OriginalCalledINNumber originalCalledINNumber = new OriginalCalledINNumber();
        originalCalledINNumber.decode(data);
        return originalCalledINNumber;
    }

    public static CallingGeodeticLocation createCallingGeodeticLocation(byte[] data) throws IncorrectSyntaxException {
        CallingGeodeticLocation callingGeodeticLocation = new CallingGeodeticLocation();
        callingGeodeticLocation.decode(data);
        return callingGeodeticLocation;
    }

    public static NetworkRoutingNumber createNetworkRoutingNumber(byte[] data) throws IllegalNumberFormatException {
        NetworkRoutingNumber networkRoutingNumber = new NetworkRoutingNumber();
        networkRoutingNumber.decode(data);
        return networkRoutingNumber;
    }

    public static QoRCapability createQoRCapability(byte[] data) {
        QoRCapability qorCapability = new QoRCapability();
        qorCapability.decode(data);
        return qorCapability;
    }

    public static PivotCounter createPivotCounter(byte[] data) {
        PivotCounter pivotCounter = new PivotCounter();
        pivotCounter.decode(data);
        return pivotCounter;
    }

    public static PivotRoutingForwardInformation createPivotRoutingForwardInformation(byte[] data) {
        PivotRoutingForwardInformation pivotRoutingForwardInformation = new PivotRoutingForwardInformation();
        pivotRoutingForwardInformation.decode(data);
        return pivotRoutingForwardInformation;
    }

    public static RedirectCapability createRedirectCapability(byte[] data) {
        RedirectCapability redirectCapability = new RedirectCapability();
        redirectCapability.decode(data);
        return redirectCapability;
    }

    public static RedirectCounter createRedirectCounter(byte[] data) {
        RedirectCounter redirectCounter = new RedirectCounter();
        redirectCounter.decode(data);
        return redirectCounter;
    }

    public static RedirectStatus createRedirectStatus(byte[] data) {
        RedirectStatus redirectStatus = new RedirectStatus();
        redirectStatus.decode(data);
        return redirectStatus;
    }

    public static RedirectForwardInformation createRedirectForwardInformation(byte[] data) {
        RedirectForwardInformation redirectForwardInformation = new RedirectForwardInformation();
        redirectForwardInformation.decode(data);
        return redirectForwardInformation;
    }

    public static NumberPortabilityForwardInformation createNumberPortabilityForwardInformation(byte[] data) {
        NumberPortabilityForwardInformation numberPortabilityForwardInformation = new NumberPortabilityForwardInformation();
        numberPortabilityForwardInformation.decode(data);
        return numberPortabilityForwardInformation;
    }

    public static UserToUserInformation createUserToUserInformation(byte[] data) {
        UserToUserInformation userToUserInformation = new UserToUserInformation();
        userToUserInformation.decode(data);
        return userToUserInformation;
    }

    public static AccessTransport createAccessTransport(byte[] data) {
        AccessTransport accessTransport = new AccessTransport();
        accessTransport.decode(data);
        return accessTransport;
    }

    public static UserServiceInformation createUserServiceInformation(byte[] data) throws Exception {
        UserServiceInformation userServiceInformation = new UserServiceInformation();
        userServiceInformation.decode(data);
        return userServiceInformation;
    }
}
