/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.errors;

import dev.ocean.sigtran.cap.CAPUserErrorCodes;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.tcap.parameters.ErrorCodeImpl;
import dev.ocean.sigtran.tcap.parameters.Parameter;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPErrorFactory {
    
    private CAPErrorFactory(){
        
    }

    public static CAPUserError createCapUserError(ErrorCodeImpl error, Parameter parameter) throws UnknownCapErrorException, UnexpectedDataException,NoServiceParameterAvailableException, IncorrectSyntaxException {
        CAPUserErrorCodes capUserErrorCode = CAPUserErrorCodes.getInstance(error.getErrorCode());

        switch (capUserErrorCode) {
            case CANCELED:
                return new Canceled();
            case CANCEL_FAILED:
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {

                    CancelFailed cancelFailed = new CancelFailed();
                    cancelFailed.decode(new AsnInputStream(parameter.getData()));
                    return cancelFailed;
                }
                throw new NoServiceParameterAvailableException("Incorrect CANCEL_FAILED error received, expecting CANCEL_FAILD with parameters");

            case ETC_FAILED:
                return new ETCFailed();

            case IMPROPER_CALLER_RESPONSE:
                return new ImproperCallerResponse();

            case MISSING_CUSTOMER_RECORD:
                return new MissingCustomerRecord();

            case MISSING_PARAMETER:
                return new MissingParameter();

            case PARAMETER_OUT_OF_RANGE:
                return new ParameterOutOfRange();

            case REQUESTED_INFO_ERROR:
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    RequestedInfoError requestedInfoError = new RequestedInfoError();
                    requestedInfoError.decode(new AsnInputStream(parameter.getData()));
                    return requestedInfoError;
                }
                throw new NoServiceParameterAvailableException("Incorrect REQUESTED_INFO_ERROR error received, expecting CANCEL_FAILD with parameters");
            case SYSTEM_FAILURE:
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    SystemFailure systemFailure = new SystemFailure();
                    systemFailure.decode(new AsnInputStream(parameter.getData()));
                    return systemFailure;
                }
                throw new NoServiceParameterAvailableException("Incorrect SYSTEM_FAILURE error received, expecting CANCEL_FAILD with parameters");
            case TASK_REFUSED:
                if (parameter != null
                        && parameter.getData() != null
                        && parameter.getData().length > 0) {
                    TaskRefused taskRefused = new TaskRefused();
                    taskRefused.decode(new AsnInputStream(parameter.getData()));
                    return taskRefused;
                }
                throw new NoServiceParameterAvailableException("Incorrect TASK_REFUSED error received, expecting CANCEL_FAILD with parameters");

            case UNAVAILABLE_RESOURCE:
                return new UnavailableResource();

            case UNEXPECTED_COMPONENT_SEQUENCE:
                return new UnexpectedComponentSequence();

            case UNEXPECTED_DATA_VALUE:
                return new UnexpectedDataValue();

            case UNEXPECTED_PARAMETER:
                return new UnexpectedParameter();

            case UNKNOWN_LEG_ID:
                return new UnknownLegId();

            case UNKNOWN_CSID:
                return new UnknownCSID();

            case UNKNOWN_PD_PID:
                return new UnknownPDPID();

            default:
                throw new UnknownCapErrorException(String.format("Unknown CAP error code received. Code = %s", error.getErrorCode()));
        }
    }
}
