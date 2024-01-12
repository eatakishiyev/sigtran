/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.messages;

import azrc.az.isup.parameters.CalledPartyNumber;
import azrc.az.isup.parameters.CallingPartyNumber;
import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.parameters.ClosedUserGroupInterlockCode;
import azrc.az.isup.parameters.ForwardCallIndicators;
import azrc.az.isup.parameters.GenericDigits;
import azrc.az.isup.parameters.GenericNumber;
import azrc.az.isup.parameters.LocationNumber;
import azrc.az.isup.enums.MessageType;
import azrc.az.isup.parameters.NatureOfConnectionIndicators;
import azrc.az.isup.parameters.OptionalForwardCallIndicators;
import azrc.az.isup.parameters.RedirectingNumber;
import azrc.az.isup.parameters.TransitNetworkSelection;
import azrc.az.isup.parameters.RedirectionInformation;
import azrc.az.isup.enums.CallDiversionTreatmentIndicator;
import azrc.az.isup.enums.ConferenceTreatmentIndicator;
import azrc.az.isup.parameters.AccessTransport;
import azrc.az.isup.parameters.ApplicationTransport;
import azrc.az.isup.parameters.CCSS;
import azrc.az.isup.parameters.CallOfferingTreatmentIndicators;
import azrc.az.isup.parameters.CallReference;
import azrc.az.isup.parameters.CalledDirectoryNumber;
import azrc.az.isup.parameters.CalledINNumber;
import azrc.az.isup.parameters.CallingGeodeticLocation;
import azrc.az.isup.parameters.CircuitAssignmentMap;
import azrc.az.isup.parameters.CollectCallRequest;
import azrc.az.isup.parameters.ConnectionRequest;
import azrc.az.isup.parameters.EchoControlInformation;
import azrc.az.isup.parameters.ForwardGVNS;
import azrc.az.isup.parameters.GenericNotificationIndicator;
import azrc.az.isup.parameters.HopCounter;
import azrc.az.isup.parameters.IsupParameter;
import azrc.az.isup.parameters.MLPPPrecedence;
import azrc.az.isup.parameters.NetworkManagementControls;
import azrc.az.isup.parameters.NetworkRoutingNumber;
import azrc.az.isup.parameters.NetworkSpecificFacility;
import azrc.az.isup.parameters.NumberPortabilityForwardInformation;
import azrc.az.isup.parameters.OriginalCalledINNumber;
import azrc.az.isup.parameters.OriginalCalledNumber;
import azrc.az.isup.parameters.ParameterCompatibilityInformation;
import azrc.az.isup.parameters.ParameterFactory;
import azrc.az.isup.parameters.PivotCapability;
import azrc.az.isup.parameters.PivotCounter;
import azrc.az.isup.parameters.PivotRoutingForwardInformation;
import azrc.az.isup.parameters.PropagationDelayCounter;
import azrc.az.isup.parameters.QoRCapability;
import azrc.az.isup.parameters.RedirectCapability;
import azrc.az.isup.parameters.RedirectCounter;
import azrc.az.isup.parameters.RedirectForwardInformation;
import azrc.az.isup.parameters.RedirectStatus;
import azrc.az.isup.parameters.RemoteOperations;
import azrc.az.isup.parameters.ScfId;
import azrc.az.isup.parameters.ServiceActivation;
import azrc.az.isup.parameters.SignallingPointCode;
import azrc.az.isup.enums.TransmissionMediumRequirement;
import azrc.az.isup.enums.TransmissionMediumRequirementPrime;
import azrc.az.isup.parameters.UIDCapabilityIndicators;
import azrc.az.isup.parameters.UserServiceInformation;
import azrc.az.isup.parameters.UserServiceInformationPrime;
import azrc.az.isup.parameters.UserTeleServiceInformation;
import azrc.az.isup.parameters.UserToUserIndicators;
import azrc.az.isup.parameters.UserToUserInformation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public class InitialAddress implements IsupMessage {

    //Mandatory fixed part
    public static final MessageType MESSAGE_TYPE_IAM = MessageType.INITIAL_ADDRESS;

    private NatureOfConnectionIndicators natureOfconnectionIndicators;
    private ForwardCallIndicators forwardCallIndicators;
    private CallingPartysCategory callingPartyCategory;
    private TransmissionMediumRequirement transmissionMediumRequirement;

    //Mandatory variable length part
    private CalledPartyNumber calledPartyNumber;

    //Optional part
    private TransitNetworkSelection transitNetworkSelection;
    private CallReference callReference;
    private CallingPartyNumber callingPartyNumber;
    private OptionalForwardCallIndicators optionalForwardCallIndicators;
    private RedirectingNumber redirectingNumber;
    private RedirectionInformation redirectionInformation;
    private ClosedUserGroupInterlockCode cugInterLockCode;
    private ConnectionRequest connectionRequest;
    private OriginalCalledNumber originalCalledNumber;
    private UserToUserInformation userToUserInformation;
    private AccessTransport accessTransport;
    private UserServiceInformation userServiceInformation;
    private UserToUserIndicators userToUserIndicators;
    private List<GenericNumber> genericNumbers;
    private PropagationDelayCounter propagationDelayCounter;
    private UserServiceInformationPrime userServiceInformationPrime;
    private NetworkSpecificFacility networkSpecificFacility;
    private List<GenericDigits> genericDigits;
    private SignallingPointCode originationISCPointCode;
    private UserTeleServiceInformation userTeleServiceInformation;
    private RemoteOperations remoteOperations;
    private ParameterCompatibilityInformation parameterCompatibilityInformation;
    private List<GenericNotificationIndicator> genericNotificationIndicators;
    private ServiceActivation serviceActivation;
    private MLPPPrecedence mLPPPrecedence;
    private TransmissionMediumRequirementPrime transmissionMediumRequirementPrime;
    private LocationNumber locationNumber;
    private ForwardGVNS forwardGVNS;
    private CCSS ccss;
    private NetworkManagementControls networkManagementControls;
    private CircuitAssignmentMap circuitAssignmentMap;
    private GenericDigits correlationId;//ITU-T Q.1218 Page 58. Digits
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    private CalledINNumber calledINNumber;
    private CallOfferingTreatmentIndicators callOfferingTreatmentIndicators;
    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private ScfId scfId;
    private UIDCapabilityIndicators uIDCapabilityIndicators;
    private EchoControlInformation echoControlInformation;
    private HopCounter hopCounter;
    private CollectCallRequest collectCallRequest;
    private ApplicationTransport applicationTransportParameter;
    private PivotCapability pivotCapability;
    private CalledDirectoryNumber calledDirectoryNumber;
    private OriginalCalledINNumber originalCalledINNumber;
    private CallingGeodeticLocation callingGeodeticLocation;
    private NetworkRoutingNumber networkRoutingNumber;
    private QoRCapability qoRCapability;
    private PivotCounter pivotCounter;
    private PivotRoutingForwardInformation pivotRoutingForwardInformation;
    private RedirectCapability redirectCapability;
    private RedirectCounter redirectCounter;
    private RedirectStatus redirectStatus;
    private RedirectForwardInformation redirectForwardInformation;
    private NumberPortabilityForwardInformation numberPortabilityForwardInformation;

    public InitialAddress() {

    }

    public InitialAddress(NatureOfConnectionIndicators natureOfConnectionIndicators,
            ForwardCallIndicators forwardCallIndicators, CallingPartysCategory callingPartyCategory,
            TransmissionMediumRequirement transmissionMediumRequirement,
            CalledPartyNumber calledPartyNumber) {
        this.natureOfconnectionIndicators = natureOfConnectionIndicators;
        this.forwardCallIndicators = forwardCallIndicators;
        this.callingPartyCategory = callingPartyCategory;
        this.transmissionMediumRequirement = transmissionMediumRequirement;
        this.calledPartyNumber = calledPartyNumber;
    }

    public byte[] encode() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        baos.write(MESSAGE_TYPE_IAM.value());
        baos.write(natureOfconnectionIndicators.encode());
        baos.write(forwardCallIndicators.encode());
        baos.write(callingPartyCategory.value());
        baos.write(transmissionMediumRequirement.value());
        baos.write(2);//Pointer to mandatory variable length parameter. Its fixed , because only one variable length mandatory parameter exists.
        byte[] data = calledPartyNumber.encode();

        baos.write(data.length + 1 + 1);//Point to start of optinal part
        baos.write(data.length);
        baos.write(data);

        if (transitNetworkSelection != null) {
            data = transitNetworkSelection.encode();
            baos.write(transitNetworkSelection.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (callReference != null) {
            data = callReference.encode();
            baos.write(callReference.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (callingPartyNumber != null) {
            data = callingPartyNumber.encode();
            baos.write(callingPartyNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (optionalForwardCallIndicators != null) {
            data = optionalForwardCallIndicators.encode();
            baos.write(optionalForwardCallIndicators.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectingNumber != null) {
            data = redirectingNumber.encode();
            baos.write(redirectingNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectionInformation != null) {
            data = redirectionInformation.encode();
            baos.write(redirectionInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (cugInterLockCode != null) {
            data = cugInterLockCode.encode();
            baos.write(cugInterLockCode.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (connectionRequest != null) {
            data = connectionRequest.encode();
            baos.write(connectionRequest.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (originalCalledNumber != null) {
            data = originalCalledNumber.encode();
            baos.write(originalCalledNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (userToUserInformation != null) {
            data = userToUserInformation.encode();
            baos.write(userToUserInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (accessTransport != null) {
            data = accessTransport.encode();
            baos.write(accessTransport.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (userServiceInformation != null) {
            data = userServiceInformation.encode();
            baos.write(userServiceInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (userToUserIndicators != null) {
            data = userToUserIndicators.encode();
            baos.write(userToUserIndicators.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (genericNumbers != null
                && genericNumbers.size() > 0) {
            for (GenericNumber genericNumber : genericNumbers) {
                data = genericNumber.encode();
                baos.write(genericNumber.getParameterCode());
                baos.write(data.length);
                baos.write(data);
            }
        }

        if (propagationDelayCounter != null) {
            data = propagationDelayCounter.encode();
            baos.write(propagationDelayCounter.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (userServiceInformationPrime != null) {
            data = userServiceInformationPrime.encode();
            baos.write(userServiceInformationPrime.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (networkSpecificFacility != null) {
            data = networkSpecificFacility.encode();
            baos.write(networkSpecificFacility.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (genericDigits != null
                && genericDigits.size() > 0) {
            for (GenericDigits _genericDigits : genericDigits) {
                data = _genericDigits.encode();
                baos.write(_genericDigits.getParameterCode());
                baos.write(data.length);
                baos.write(data);
            }
        }

        if (originationISCPointCode != null) {
            data = originationISCPointCode.encode();
            baos.write(originationISCPointCode.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (userTeleServiceInformation != null) {
            data = userTeleServiceInformation.encode();
            baos.write(userTeleServiceInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (remoteOperations != null) {
            data = remoteOperations.encode();
            baos.write(remoteOperations.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (parameterCompatibilityInformation != null) {
            data = parameterCompatibilityInformation.encode();
            baos.write(parameterCompatibilityInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (genericNotificationIndicators != null
                && genericNotificationIndicators.size() > 0) {
            for (GenericNotificationIndicator genericNotificationIndicator : genericNotificationIndicators) {
                data = genericNotificationIndicator.encode();
                baos.write(genericNotificationIndicator.getParameterCode());
                baos.write(data.length);
                baos.write(data);
            }
        }

        if (serviceActivation != null) {
            data = serviceActivation.encode();
            baos.write(serviceActivation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (mLPPPrecedence != null) {
            data = mLPPPrecedence.encode();
            baos.write(mLPPPrecedence.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (transmissionMediumRequirementPrime != null) {
            baos.write(IsupParameter.TRANSMISSION_MEDIUM_REQUIREMENT_PRIME);
            baos.write(1);
            baos.write(transmissionMediumRequirementPrime.value());
        }

        if (locationNumber != null) {
            data = locationNumber.encode();
            baos.write(locationNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (forwardGVNS != null) {
            data = forwardGVNS.encode();
            baos.write(forwardGVNS.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (ccss != null) {
            data = ccss.encode();
            baos.write(ccss.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (networkManagementControls != null) {
            data = networkManagementControls.encode();
            baos.write(networkManagementControls.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (circuitAssignmentMap != null) {
            data = circuitAssignmentMap.encode();
            baos.write(circuitAssignmentMap.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (correlationId != null) {
            data = correlationId.encode();
            baos.write(correlationId.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (callDiversionTreatmentIndicator != null) {
            baos.write(IsupParameter.CALL_DIVERSION_TREATMENT_INDICATORS);
            baos.write(1);
            baos.write(callDiversionTreatmentIndicator.value());
        }

        if (calledINNumber != null) {
            data = calledINNumber.encode();
            baos.write(calledINNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (callOfferingTreatmentIndicators != null) {
            data = callOfferingTreatmentIndicators.encode();
            baos.write(callOfferingTreatmentIndicators.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (conferenceTreatmentIndicator != null) {
            baos.write(IsupParameter.CONFERENCE_TREATMENT_INDICATORS);
            baos.write(1);
            baos.write(conferenceTreatmentIndicator.value());
        }

        if (scfId != null) {
            data = scfId.encode();
            baos.write(scfId.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (uIDCapabilityIndicators != null) {
            data = uIDCapabilityIndicators.encode();
            baos.write(uIDCapabilityIndicators.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (echoControlInformation != null) {
            data = echoControlInformation.encode();
            baos.write(echoControlInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (hopCounter != null) {
            data = hopCounter.encode();
            baos.write(hopCounter.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (collectCallRequest != null) {
            data = collectCallRequest.encode();
            baos.write(collectCallRequest.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (applicationTransportParameter != null) {
            data = applicationTransportParameter.encode();
            baos.write(applicationTransportParameter.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (pivotCapability != null) {
            data = pivotCapability.encode();
            baos.write(pivotCapability.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (calledDirectoryNumber != null) {
            data = calledDirectoryNumber.encode();
            baos.write(calledDirectoryNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (originalCalledINNumber != null) {
            data = originalCalledINNumber.encode();
            baos.write(originalCalledINNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (callingGeodeticLocation != null) {
            data = callingGeodeticLocation.encode();
            baos.write(callingGeodeticLocation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (networkRoutingNumber != null) {
            data = networkRoutingNumber.encode();
            baos.write(networkRoutingNumber.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (qoRCapability != null) {
            data = qoRCapability.encode();
            baos.write(qoRCapability.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (pivotCounter != null) {
            data = pivotCounter.encode();
            baos.write(pivotCounter.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (pivotRoutingForwardInformation != null) {
            data = pivotRoutingForwardInformation.encode();
            baos.write(pivotRoutingForwardInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectCapability != null) {
            data = redirectCapability.encode();
            baos.write(redirectCapability.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectCounter != null) {
            data = redirectCounter.encode();
            baos.write(redirectCounter.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectStatus != null) {
            data = redirectStatus.encode();
            baos.write(redirectStatus.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (redirectForwardInformation != null) {
            data = redirectForwardInformation.encode();
            baos.write(redirectForwardInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        if (numberPortabilityForwardInformation != null) {
            data = numberPortabilityForwardInformation.encode();
            baos.write(numberPortabilityForwardInformation.getParameterCode());
            baos.write(data.length);
            baos.write(data);
        }

        baos.write(IsupParameter.END_OF_OPTINAL_PARAMETER);
    }

    public void decode(byte[] data) throws Exception{
        this.decode(new ByteArrayInputStream(data));
    }
    
    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {

        this.natureOfconnectionIndicators = ParameterFactory.createNatureOfConnectionIndicators(bais);
        this.forwardCallIndicators = ParameterFactory.createForwardCallIndicators(bais);
        this.callingPartyCategory = ParameterFactory.createCallingPartyCategory(bais);
        this.transmissionMediumRequirement = ParameterFactory.createTransmissionMediumRequirement(bais);

        int pointerToParameter = bais.read();
        bais.mark(0);
        bais.skip(pointerToParameter - 1);
        int length = bais.read();
        byte[] tmp = new byte[length];
        bais.read(tmp);
        this.calledPartyNumber = ParameterFactory.createCalledPartyNumber(tmp);
//        bais.reset();

        if (bais.available() > 0) {
            bais.reset();
            pointerToParameter = bais.read();
            bais.mark(0);
            bais.skip(pointerToParameter - 1);
        }

        while (bais.available() > 0) {
            int parameterCode = bais.read();
            if (parameterCode == IsupParameter.END_OF_OPTINAL_PARAMETER) {
                break;//end of optional parameters
            }
            length = bais.read();
            tmp = new byte[length];
            bais.read(tmp);

            switch (parameterCode) {
                case IsupParameter.TRANSIT_NETWORK_SELECTION:
                    this.transitNetworkSelection = ParameterFactory.createTransitNetworkSelection(tmp);
                    break;
                case IsupParameter.CALL_REFERENCE:
                    this.callReference = ParameterFactory.createCallReference(tmp);
                    break;
                case IsupParameter.CALLING_PARTY_NUMBER:
                    this.callingPartyNumber = ParameterFactory.createCallingPartyNumber(tmp);
                    break;
                case IsupParameter.OPTIONAL_FORWARD_CALL_INDICATORS:
                    this.optionalForwardCallIndicators = ParameterFactory.createOptionalForwardCallIndicators(tmp);
                    break;
                case IsupParameter.REDIRECTING_NUMBER:
                    this.redirectingNumber = ParameterFactory.createRedirectingNumber(tmp);
                    break;
                case IsupParameter.REDIRECTION_INFORMATION:
                    this.redirectionInformation = ParameterFactory.createRedirectionInformation(tmp);
                    break;
                case IsupParameter.CLOSED_USER_GROUP_INTERLOCK_CODE:
                    this.cugInterLockCode = ParameterFactory.createClosedUserGroupInterlockCode(tmp);
                    break;
                case IsupParameter.CONNECTION_REQUEST:
                    this.connectionRequest = ParameterFactory.createConnectionRequest(tmp);
                    break;
                case IsupParameter.ORIGINAL_CALLED_NUMBER:
                    this.originalCalledNumber = ParameterFactory.createOriginalCalledNumber(tmp);
                    break;
                case IsupParameter.USER_TO_USER_INFORMATION:
                    this.userToUserInformation = ParameterFactory.createUserToUserInformation(tmp);
                    break;
                case IsupParameter.ACCESS_TRANSPORT:
                    this.accessTransport = ParameterFactory.createAccessTransport(tmp);
                    break;
                case IsupParameter.USER_SERVICE_INFORMATION:
                    this.userServiceInformation = ParameterFactory.createUserServiceInformation(tmp);
                    break;
                case IsupParameter.USER_TO_USER_INDICATORS:
                    this.userToUserIndicators = ParameterFactory.createUserToUserIndicators(tmp);
                    break;
                case IsupParameter.GENERIC_NUMBER:
                    if (genericNumbers == null) {
                        genericNumbers = new ArrayList<>();
                    }
                    GenericNumber genericNumber = ParameterFactory.createGenericNumber(tmp);
                    genericNumbers.add(genericNumber);
                    break;
                case IsupParameter.PROPAGATION_DELAY_COUNTER:
                    this.propagationDelayCounter = ParameterFactory.createPropagationDelayCounter(tmp);
                    break;
                case IsupParameter.USER_SERVICE_INFORMATION_PRIME:
                    this.userServiceInformationPrime = ParameterFactory.createUserServiceInformationPrime(tmp);
                    break;
                case IsupParameter.NETWORK_SPECIFIC_FACILITY:
                    this.networkSpecificFacility = ParameterFactory.createNetworkSpecificFacility(tmp);
                    break;
                case IsupParameter.GENERIC_DIGITS:
                    if (this.genericDigits == null) {
                        this.genericDigits = new ArrayList<>();
                    }
                    GenericDigits genericDigit = ParameterFactory.createGenericDigits(tmp);
                    genericDigits.add(genericDigit);
                    break;
                case IsupParameter.ORIGINATION_ISC_POINT_CODE:
                    originationISCPointCode = ParameterFactory.createOriginationISCPointCode(tmp);
                    break;
                case IsupParameter.USER_TELESERVICE_INFORMATION:
                    userTeleServiceInformation = ParameterFactory.createUserTeleServiceInformation(tmp);
                    break;
                case IsupParameter.REMOTE_OPERATIONS:
                    this.remoteOperations = ParameterFactory.createRemoteOperations(tmp);
                    break;
                case IsupParameter.PARAMETER_COMPATIBILITY_INFORMATION:
                    this.parameterCompatibilityInformation = ParameterFactory.createParameterCompatibilityInformation(tmp);
                    break;
                case IsupParameter.GENERIC_NOTIFICATION_INDICATOR:
                    if (this.genericNotificationIndicators == null) {
                        this.genericNotificationIndicators = new ArrayList<>();
                    }
                    this.genericNotificationIndicators.add(ParameterFactory.createGenericNotificationIndicator(tmp));
                    break;
                case IsupParameter.SERVICE_ACTIVATION:
                    this.serviceActivation = ParameterFactory.createServiceActivation(tmp);
                    break;
                case IsupParameter.MLPP_PRECEDENCE:
                    this.mLPPPrecedence = ParameterFactory.createMLPPPrecedence(tmp);
                    break;
                case IsupParameter.TRANSMISSION_MEDIUM_REQUIREMENT_PRIME:
                    this.transmissionMediumRequirementPrime = ParameterFactory.createTransmissionMediumRequirementPrime(tmp);
                    break;
                case IsupParameter.LOCATION_NUMBER:
                    this.locationNumber = ParameterFactory.createLocationNumber(tmp);
                    break;
                case IsupParameter.FORWARD_GVNS:
                    this.forwardGVNS = ParameterFactory.createForwardGVNS(tmp);
                    break;
                case IsupParameter.CCSS:
                    this.ccss = ParameterFactory.createCCSS(tmp);
                    break;
                case IsupParameter.NETWORK_MANAGEMENT_CONTROLS:
                    this.networkManagementControls = ParameterFactory.createNetworkManagementControls(tmp);
                    break;
                case IsupParameter.CIRCUIT_ASSIGNMENT_MAP:
                    this.circuitAssignmentMap = ParameterFactory.createCircuitAssignmentMap(tmp);
                    break;
                case IsupParameter.CORRELATION_ID:
                    this.correlationId = ParameterFactory.createCorrelationId(tmp);
                    break;
                case IsupParameter.CALL_DIVERSION_TREATMENT_INDICATORS:
                    this.callDiversionTreatmentIndicator = ParameterFactory.createCallDiversionTreatmentIndicators(tmp);
                    break;
                case IsupParameter.CALLED_IN_NUMBER:
                    this.calledINNumber = ParameterFactory.createCalledINNumber(tmp);
                    break;
                case IsupParameter.CALL_OFFERING_TREATMENT_INDICATORS:
                    this.callOfferingTreatmentIndicators = ParameterFactory.createCallOfferingTreatmentIndicators(tmp);
                    break;
                case IsupParameter.CONFERENCE_TREATMENT_INDICATORS:
                    this.conferenceTreatmentIndicator = ParameterFactory.createConferenceTreatmentIndicators(tmp);
                    break;
                case IsupParameter.SCF_ID:
                    this.scfId = ParameterFactory.createScfId(tmp);
                    break;
                case IsupParameter.UID_CAPABILITY_INDICATORS:
                    this.uIDCapabilityIndicators = ParameterFactory.createUidCapabilityIndicators(tmp);
                    break;
                case IsupParameter.ECHO_CONTROL_INFORMATION:
                    this.echoControlInformation = ParameterFactory.createEchoControlInformation(tmp);
                    break;
                case IsupParameter.HOP_COUNTER:
                    this.hopCounter = ParameterFactory.createHopCounter(tmp);
                    break;
                case IsupParameter.COLLECT_CALL_REQUEST:
                    this.collectCallRequest = ParameterFactory.createCollectCallRequest(tmp);
                    break;
                case IsupParameter.APPLICATION_TRANSPORT:
                    this.applicationTransportParameter = ParameterFactory.createApplicationTransportParameter(tmp);
                    break;
                case IsupParameter.PIVOT_CAPABILITY:
                    this.pivotCapability = ParameterFactory.createPivotCapability(tmp);
                    break;
                case IsupParameter.CALLED_DIRECTORY_NUMBER:
                    this.calledDirectoryNumber = ParameterFactory.createCalledDirectoryNumber(tmp);
                    break;
                case IsupParameter.ORIGINAL_CALLED_IN_NUMBER:
                    this.originalCalledINNumber = ParameterFactory.createOriginalCalledINNumber(tmp);
                    break;
                case IsupParameter.CALLING_GEODETIC_LOCATION:
                    this.callingGeodeticLocation = ParameterFactory.createCallingGeodeticLocation(tmp);
                    break;
                case IsupParameter.NETWORK_ROUTING_NUMBER:
                    this.networkRoutingNumber = ParameterFactory.createNetworkRoutingNumber(tmp);
                    break;
                case IsupParameter.QOR_CAPABILITY:
                    this.qoRCapability = ParameterFactory.createQoRCapability(tmp);
                    break;
                case IsupParameter.PIVOT_COUNTER:
                    this.pivotCounter = ParameterFactory.createPivotCounter(tmp);
                    break;
                case IsupParameter.PIVOT_ROUTING_FORWARD_INFORMATION:
                    this.pivotRoutingForwardInformation = ParameterFactory.createPivotRoutingForwardInformation(tmp);
                    break;
                case IsupParameter.REDIRECT_CAPABILITY:
                    this.redirectCapability = ParameterFactory.createRedirectCapability(tmp);
                    break;
                case IsupParameter.REDIRECT_COUNTER:
                    this.redirectCounter = ParameterFactory.createRedirectCounter(tmp);
                    break;
                case IsupParameter.REDIRECT_STATUS:
                    this.redirectStatus = ParameterFactory.createRedirectStatus(tmp);
                    break;
                case IsupParameter.REDIRECT_FORWARD_INFORMATION:
                    this.redirectForwardInformation = ParameterFactory.createRedirectForwardInformation(tmp);
                    break;
                case IsupParameter.NUMBER_PORTABILITY_FORWARD_INFORMATION:
                    this.numberPortabilityForwardInformation = ParameterFactory.createNumberPortabilityForwardInformation(tmp);
                    break;
            }
        }
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

    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
        return callDiversionTreatmentIndicator;
    }

    public void setCallDiversionTreatmentIndicator(CallDiversionTreatmentIndicator callDiversionTreatmentIndicator) {
        this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
    }

    public CallOfferingTreatmentIndicators getCallOfferingTreatmentIndicators() {
        return callOfferingTreatmentIndicators;
    }

    public void setCallOfferingTreatmentIndicators(CallOfferingTreatmentIndicators callOfferingTreatmentIndicators) {
        this.callOfferingTreatmentIndicators = callOfferingTreatmentIndicators;
    }

    public CallReference getCallReference() {
        return callReference;
    }

    public void setCallReference(CallReference callReference) {
        this.callReference = callReference;
    }

    public CalledDirectoryNumber getCalledDirectoryNumber() {
        return calledDirectoryNumber;
    }

    public void setCalledDirectoryNumber(CalledDirectoryNumber calledDirectoryNumber) {
        this.calledDirectoryNumber = calledDirectoryNumber;
    }

    public CalledINNumber getCalledINNumber() {
        return calledINNumber;
    }

    public void setCalledINNumber(CalledINNumber calledINNumber) {
        this.calledINNumber = calledINNumber;
    }

    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    public CallingGeodeticLocation getCallingGeodeticLocation() {
        return callingGeodeticLocation;
    }

    public void setCallingGeodeticLocation(CallingGeodeticLocation callingGeodeticLocation) {
        this.callingGeodeticLocation = callingGeodeticLocation;
    }

    public CallingPartysCategory getCallingPartyCategory() {
        return callingPartyCategory;
    }

    public void setCallingPartyCategory(CallingPartysCategory callingPartyCategory) {
        this.callingPartyCategory = callingPartyCategory;
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    public CCSS getCcss() {
        return ccss;
    }

    public void setCcss(CCSS ccss) {
        this.ccss = ccss;
    }

    public CircuitAssignmentMap getCircuitAssignmentMap() {
        return circuitAssignmentMap;
    }

    public void setCircuitAssigmentMap(CircuitAssignmentMap circuitAssignmentMap) {
        this.circuitAssignmentMap = circuitAssignmentMap;
    }

    public CollectCallRequest getCollectCallRequest() {
        return collectCallRequest;
    }

    public void setCollectCallRequest(CollectCallRequest collectCallRequest) {
        this.collectCallRequest = collectCallRequest;
    }

    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
        return conferenceTreatmentIndicator;
    }

    public void setConferenceTreatmentIndicator(ConferenceTreatmentIndicator conferenceTreatmentIndicator) {
        this.conferenceTreatmentIndicator = conferenceTreatmentIndicator;
    }

    public ConnectionRequest getConnectionRequest() {
        return connectionRequest;
    }

    public void setConnectionRequest(ConnectionRequest connectionRequest) {
        this.connectionRequest = connectionRequest;
    }

    public GenericDigits getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(GenericDigits correlationId) {
        this.correlationId = correlationId;
    }

    public ClosedUserGroupInterlockCode getCugInterLockCode() {
        return cugInterLockCode;
    }

    public void setCugInterLockCode(ClosedUserGroupInterlockCode cugInterLock) {
        this.cugInterLockCode = cugInterLock;
    }

    public EchoControlInformation getEchoControlInformation() {
        return echoControlInformation;
    }

    public void setEchoControlInformation(EchoControlInformation echoControlInformation) {
        this.echoControlInformation = echoControlInformation;
    }

    public ForwardCallIndicators getForwardCallIndicators() {
        return forwardCallIndicators;
    }

    public void setForwardCallIndicators(ForwardCallIndicators forwardCallIndicators) {
        this.forwardCallIndicators = forwardCallIndicators;
    }

    public ForwardGVNS getForwardGVNS() {
        return forwardGVNS;
    }

    public void setForwardGVNS(ForwardGVNS forwardGVNS) {
        this.forwardGVNS = forwardGVNS;
    }

    public List<GenericDigits> getGenericDigits() {
        return genericDigits;
    }

    public void setGenericDigits(List<GenericDigits> genericDigits) {
        this.genericDigits = genericDigits;
    }

    public List<GenericNotificationIndicator> getGenericNotificationIndicators() {
        return genericNotificationIndicators;
    }

    public void setGenericNotificationIndicators(List<GenericNotificationIndicator> genericNotificationIndicator) {
        this.genericNotificationIndicators = genericNotificationIndicator;
    }

    public List<GenericNumber> getGenericNumber() {
        return genericNumbers;
    }

    public void setGenericNumber(List<GenericNumber> genericNumber) {
        this.genericNumbers = genericNumber;
    }

    public HopCounter getHopCounter() {
        return hopCounter;
    }

    public void setHopCounter(HopCounter hopCounter) {
        this.hopCounter = hopCounter;
    }

    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    public NatureOfConnectionIndicators getNatureOfconnectionIndicators() {
        return natureOfconnectionIndicators;
    }

    public void setNatureOfconnectionIndicators(NatureOfConnectionIndicators natureOfconnectionIndicators) {
        this.natureOfconnectionIndicators = natureOfconnectionIndicators;
    }

    public NetworkManagementControls getNetworkManagementControls() {
        return networkManagementControls;
    }

    public void setNetworkManagementControls(NetworkManagementControls networkManagementControls) {
        this.networkManagementControls = networkManagementControls;
    }

    public NetworkRoutingNumber getNetworkRoutingNumber() {
        return networkRoutingNumber;
    }

    public void setNetworkRoutingNumber(NetworkRoutingNumber networkRoutingNumber) {
        this.networkRoutingNumber = networkRoutingNumber;
    }

    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return networkSpecificFacility;
    }

    public void setNetworkSpecificFacility(NetworkSpecificFacility networkSpecificFacility) {
        this.networkSpecificFacility = networkSpecificFacility;
    }

    public NumberPortabilityForwardInformation getNumberPortabilityForwardInformation() {
        return numberPortabilityForwardInformation;
    }

    public void setNumberPortabilityForwardInformation(NumberPortabilityForwardInformation numberPortabilityForwardInformation) {
        this.numberPortabilityForwardInformation = numberPortabilityForwardInformation;
    }

    public OptionalForwardCallIndicators getOptionalForwardCallIndicators() {
        return optionalForwardCallIndicators;
    }

    public void setOptionalForwardCallIndicators(OptionalForwardCallIndicators optionalForwardCallIndicators) {
        this.optionalForwardCallIndicators = optionalForwardCallIndicators;
    }

    public OriginalCalledINNumber getOriginalCalledINNumber() {
        return originalCalledINNumber;
    }

    public void setOriginalCalledINNumber(OriginalCalledINNumber originalCalledINNumber) {
        this.originalCalledINNumber = originalCalledINNumber;
    }

    public OriginalCalledNumber getOriginalCalledNumber() {
        return originalCalledNumber;
    }

    public void setOriginalCalledNumber(OriginalCalledNumber originalCalledNumber) {
        this.originalCalledNumber = originalCalledNumber;
    }

    public SignallingPointCode getOriginationISCPointCode() {
        return originationISCPointCode;
    }

    public void setOriginationISCPointCode(SignallingPointCode originationISCPointCode) {
        this.originationISCPointCode = originationISCPointCode;
    }

    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return parameterCompatibilityInformation;
    }

    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation parameterCompatibilityInformation) {
        this.parameterCompatibilityInformation = parameterCompatibilityInformation;
    }

    public PivotCapability getPivotCapability() {
        return pivotCapability;
    }

    public void setPivotCapability(PivotCapability pivotCapability) {
        this.pivotCapability = pivotCapability;
    }

    public PivotCounter getPivotCounter() {
        return pivotCounter;
    }

    public void setPivotCounter(PivotCounter pivotCounter) {
        this.pivotCounter = pivotCounter;
    }

    public PivotRoutingForwardInformation getPivotRoutingForwardInformation() {
        return pivotRoutingForwardInformation;
    }

    public void setPivotRoutingForwardInformation(PivotRoutingForwardInformation pivotRoutingForwardInformation) {
        this.pivotRoutingForwardInformation = pivotRoutingForwardInformation;
    }

    public PropagationDelayCounter getPropagationDelayCounter() {
        return propagationDelayCounter;
    }

    public void setPropagationDelayCounter(PropagationDelayCounter propagationDelayCounter) {
        this.propagationDelayCounter = propagationDelayCounter;
    }

    public QoRCapability getQoRCapability() {
        return qoRCapability;
    }

    public void setQoRCapability(QoRCapability qoRCapability) {
        this.qoRCapability = qoRCapability;
    }

    public RedirectCapability getRedirectCapability() {
        return redirectCapability;
    }

    public void setRedirectCapability(RedirectCapability redirectCapability) {
        this.redirectCapability = redirectCapability;
    }

    public RedirectCounter getRedirectCounter() {
        return redirectCounter;
    }

    public void setRedirectCounter(RedirectCounter redirectCounter) {
        this.redirectCounter = redirectCounter;
    }

    public RedirectForwardInformation getRedirectForwardInformation() {
        return redirectForwardInformation;
    }

    public void setRedirectForwardInformation(RedirectForwardInformation redirectForwardInformation) {
        this.redirectForwardInformation = redirectForwardInformation;
    }

    public RedirectStatus getRedirectStatus() {
        return redirectStatus;
    }

    public void setRedirectStatus(RedirectStatus redirectStatus) {
        this.redirectStatus = redirectStatus;
    }

    public RedirectingNumber getRedirectingNumber() {
        return redirectingNumber;
    }

    public void setRedirectingNumber(RedirectingNumber redirectingNumber) {
        this.redirectingNumber = redirectingNumber;
    }

    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    public RemoteOperations getRemoteOperations() {
        return remoteOperations;
    }

    public void setRemoteOperations(RemoteOperations remoteOperations) {
        this.remoteOperations = remoteOperations;
    }

    public ScfId getScfId() {
        return scfId;
    }

    public void setScfId(ScfId scfId) {
        this.scfId = scfId;
    }

    public ServiceActivation getServiceActivation() {
        return serviceActivation;
    }

    public void setServiceActivation(ServiceActivation serviceActivation) {
        this.serviceActivation = serviceActivation;
    }

    public TransitNetworkSelection getTransitNetworkSelection() {
        return transitNetworkSelection;
    }

    public void setTransitNetworkSelection(TransitNetworkSelection transitNetworkSelection) {
        this.transitNetworkSelection = transitNetworkSelection;
    }

    public TransmissionMediumRequirement getTransmissionMediumRequirement() {
        return transmissionMediumRequirement;
    }

    public void setTransmissionMediumRequirement(TransmissionMediumRequirement transmissionMediumRequirement) {
        this.transmissionMediumRequirement = transmissionMediumRequirement;
    }

    public TransmissionMediumRequirementPrime getTransmissionMediumRequirementPrime() {
        return transmissionMediumRequirementPrime;
    }

    public void setTransmissionMediumRequirementPrime(TransmissionMediumRequirementPrime transmissionMediumRequirementPrime) {
        this.transmissionMediumRequirementPrime = transmissionMediumRequirementPrime;
    }

    public UserServiceInformation getUserServiceInformation() {
        return userServiceInformation;
    }

    public void setUserServiceInformation(UserServiceInformation userServiceInformation) {
        this.userServiceInformation = userServiceInformation;
    }

    public UserServiceInformationPrime getUserServiceInformationPrime() {
        return userServiceInformationPrime;
    }

    public void setUserServiceInformationPrime(UserServiceInformationPrime userServiceInformationPrime) {
        this.userServiceInformationPrime = userServiceInformationPrime;
    }

    public UserTeleServiceInformation getUserTeleServiceInformation() {
        return userTeleServiceInformation;
    }

    public void setUserTeleServiceInformation(UserTeleServiceInformation userTeleServiceInformation) {
        this.userTeleServiceInformation = userTeleServiceInformation;
    }

    public UserToUserIndicators getUserToUserIndicators() {
        return userToUserIndicators;
    }

    public void setUserToUserIndicators(UserToUserIndicators userToUserIndicators) {
        this.userToUserIndicators = userToUserIndicators;
    }

    public UserToUserInformation getUserToUserInformation() {
        return userToUserInformation;
    }

    public void setUserToUserInformation(UserToUserInformation userToUserInformation) {
        this.userToUserInformation = userToUserInformation;
    }

    public void setmLPPPrecedence(MLPPPrecedence mLPPPrecedence) {
        this.mLPPPrecedence = mLPPPrecedence;
    }

    public MLPPPrecedence getmLPPPrecedence() {
        return mLPPPrecedence;
    }

    public void setuIDCapabilityIndicators(UIDCapabilityIndicators uIDCapabilityIndicators) {
        this.uIDCapabilityIndicators = uIDCapabilityIndicators;
    }

    public UIDCapabilityIndicators getuIDCapabilityIndicators() {
        return uIDCapabilityIndicators;
    }

    @Override
    public MessageType getMessageType() {
        return MESSAGE_TYPE_IAM;
    }

}
