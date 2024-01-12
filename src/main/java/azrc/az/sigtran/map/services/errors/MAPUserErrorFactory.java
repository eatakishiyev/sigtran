/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.generic.DataMissing;
import azrc.az.sigtran.map.services.errors.generic.FacilityNotSupported;
import azrc.az.sigtran.map.services.errors.generic.SystemFailureError;
import azrc.az.sigtran.map.services.errors.generic.UnexpectedDataValue;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.tcap.parameters.ErrorCodeImpl;
import azrc.az.sigtran.tcap.parameters.Parameter;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public final class MAPUserErrorFactory {

    private MAPUserErrorFactory() {

    }

    public static MAPUserError createMAPUserError(MAPUserErrorValues errorCode) throws IncorrectSyntaxException,
            IncorrectErrorCodeException, UnexpectedDataException, NoServiceParameterAvailableException {
        return createMAPUserErrorFromIndication(new ErrorCodeImpl(errorCode.value()), null);
    }

    public static MAPUserError createMAPUserError(MAPUserErrorValues errorCode, Parameter parameter) throws IncorrectSyntaxException,
            IncorrectErrorCodeException, UnexpectedDataException, NoServiceParameterAvailableException {
        return createMAPUserErrorFromIndication(new ErrorCodeImpl(errorCode.value()), parameter);
    }

    public static MAPUserError createMAPUserErrorFromIndication(ErrorCodeImpl error, Parameter parameter) throws IncorrectSyntaxException,
            IncorrectErrorCodeException, UnexpectedDataException, NoServiceParameterAvailableException {
        MAPUserErrorValues mapUserErrorValue = MAPUserErrorValues.getInstance(error.getErrorCode());

        if (mapUserErrorValue == MAPUserErrorValues.UNKNOWN) {
            throw new IncorrectErrorCodeException(String.format("Unknown error code received. Code = %d", error.getErrorCode()));
        }

        switch (mapUserErrorValue) {
            case SYSTEM_FAILURE:
                SystemFailureError systemFailureError = new SystemFailureError();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    systemFailureError.decode(new AsnInputStream(parameter.getData()));
                }
                return systemFailureError;
            case UNEXPECTED_DATA_VALUE:
                UnexpectedDataValue unexpectedDataValueError = new UnexpectedDataValue();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    unexpectedDataValueError.decode(new AsnInputStream(parameter.getData()));
                }
                return unexpectedDataValueError;
            case DATA_MISSING:
                DataMissing dataMissingError = new DataMissing();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    dataMissingError.decode(new AsnInputStream(parameter.getData()));
                }
                return dataMissingError;
            case UNKNOWN_SUBSCRIBER:
                UnknownSubscriberError unknownSubscriberError = new UnknownSubscriberError();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    unknownSubscriberError.decode(new AsnInputStream(parameter.getData()));
                }
                return unknownSubscriberError;
            case MESSAGE_WAITING_LIST_FULL:
                MessageWaitingListFull messageWaitingListFullError = new MessageWaitingListFull();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    messageWaitingListFullError.decode(new AsnInputStream(parameter.getData()));
                }
                return messageWaitingListFullError;
            case FACILITY_NOT_SUPPORTED:
                FacilityNotSupported facilityNotSupportedError = new FacilityNotSupported();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    facilityNotSupportedError.decode(new AsnInputStream(parameter.getData()));
                }
                return facilityNotSupportedError;
            case ILLEGAL_SUBSCRIBER:
                IllegalSubscriber illegalSubscriberError = new IllegalSubscriber();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    illegalSubscriberError.decode(new AsnInputStream(parameter.getData()));
                }
                return illegalSubscriberError;
            case ILLEGAL_EQUIPMENT:
                IllegalEquipment illegalEquipmentError = new IllegalEquipment();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    illegalEquipmentError.decode(new AsnInputStream(parameter.getData()));
                }
                return illegalEquipmentError;
            case SM_DELIVERY_FAILURE:
                SMDeliveryFailure sMDeliveryFailureError = new SMDeliveryFailure();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    sMDeliveryFailureError.decode(new AsnInputStream(parameter.getData()));
                }
                return sMDeliveryFailureError;
            case ABSENT_SUBSCRIBER_SM:
                AbsentSubscriberSM absentSubscriberSMError = new AbsentSubscriberSM();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    absentSubscriberSMError.decode(new AsnInputStream(parameter.getData()));
                }
                return absentSubscriberSMError;
            case TELESERVICE_NOT_PROVISIONED:
                TeleServiceNotProvisioned teleServiceNotProvisionedError = new TeleServiceNotProvisioned();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    teleServiceNotProvisionedError.decode(new AsnInputStream(parameter.getData()));
                }
                return teleServiceNotProvisionedError;
            case CALL_BARRED:
                CallBarred callBarredError = new CallBarred();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    callBarredError.decode(new AsnInputStream(parameter.getData()));
                }
                return callBarredError;
            case ABSENT_SUBSCRIBER:
                AbsentSubscriber absentSubscriberError = new AbsentSubscriber();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    absentSubscriberError.decode(new AsnInputStream(parameter.getData()));
                }
                return absentSubscriberError;
            case SUBSCRIBER_BUSY_FOR_MT_SMS:
                SubscriberBusyForMtSMS subscriberBusyForMtSMSError = new SubscriberBusyForMtSMS();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    subscriberBusyForMtSMSError.decode(new AsnInputStream(parameter.getData()));
                }
                return subscriberBusyForMtSMSError;
            case UNIDENTIFIED_SUBSCRIBER:
                UnidentifiedSubscriber unidentifiedSubscriberError = new UnidentifiedSubscriber();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    unidentifiedSubscriberError.decode(new AsnInputStream(parameter.getData()));
                }
                return unidentifiedSubscriberError;
            case ROAMING_NOT_ALLOWED:
                RoamingNotAllowed roamingNotAllowed = new RoamingNotAllowed();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    roamingNotAllowed.decode(new AsnInputStream(parameter.getData()));
                }
                return roamingNotAllowed;
            case SS_INCOMPATIBILITY:
                SSIncompatibility sSIncompatibility = new SSIncompatibility();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    sSIncompatibility.decode(new AsnInputStream(parameter.getData()));
                }
                return sSIncompatibility;

            case FORWARDING_VIOLATION:
                ForwardingViolation forwardingViolation = new ForwardingViolation();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    forwardingViolation.decode(new AsnInputStream(parameter.getData()));
                }
                return forwardingViolation;
            case ATI_NOT_ALLOWED:
                AtiNotAllowed atiNotAllowed = new AtiNotAllowed();
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    atiNotAllowed.decode(new AsnInputStream(parameter.getData()));
                }
                return atiNotAllowed;
            default:
                return new MAPUserErrorImpl(mapUserErrorValue);
        }
    }
}
