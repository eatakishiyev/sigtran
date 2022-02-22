/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import java.io.IOException;
import dev.ocean.sigtran.map.OperationCodes;
import dev.ocean.sigtran.tcap.components.InvokeImpl;
import dev.ocean.sigtran.tcap.components.RejectImpl;
import dev.ocean.sigtran.tcap.components.ReturnErrorImpl;
import dev.ocean.sigtran.tcap.components.ReturnResultLastImpl;
import dev.ocean.sigtran.tcap.components.ReturnResultNotLastImpl;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialogueAbort;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialoguePortionImpl;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialogueRequest;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialogueResponse;
import dev.ocean.sigtran.tcap.dialogues.intrefaces.DialoguePDU;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import dev.ocean.sigtran.tcap.primitives.tc.PAbortCause;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ParameterFactory {

    /**
     *
     * @author root
     */
    public static UserInformationImpl createUserInformation() {
        return new UserInformationImpl();
    }

    public static UserInformationImpl createUserInformation(AsnInputStream ais) throws IncorrectSyntaxException {
        UserInformationImpl userInformation = new UserInformationImpl();
        userInformation.decode(ais);
        return userInformation;
    }

    public static ProtocolVersionImpl createProtocolVersion(AsnInputStream ais) throws IncorrectSyntaxException {
        ProtocolVersionImpl pv = new ProtocolVersionImpl();
        pv.decode(ais);
        return pv;
    }

    public static ProtocolVersionImpl createProtocolVersion() {
        return new ProtocolVersionImpl();
    }

    public static ApplicationContextImpl createApplicationContext(AsnInputStream ais) throws IncorrectSyntaxException {
        ApplicationContextImpl ac = new ApplicationContextImpl();
        ac.decode(ais);
        return ac;
    }

    public static ApplicationContextImpl createApplicationContext() {
        return new ApplicationContextImpl();
    }

    public static ApplicationContextImpl createApplicationContet(long[] oid) {
        return new ApplicationContextImpl(oid);
    }

    public static AssociateResult createAssociateResult(AsnInputStream ais) throws IncorrectSyntaxException {
        AssociateResult associateResult = new AssociateResult();
        associateResult.decode(ais);
        return associateResult;
    }

    public static AssociateResult createAssociateResult() {
        return new AssociateResult();
    }

    public static AssociateResult createAssociateResult(Result result) {
        return new AssociateResult(result);
    }

    public static OperationCodeImpl createOperationCode(AsnInputStream ais) throws IncorrectSyntaxException {
        OperationCodeImpl operationCode = new OperationCodeImpl();
        operationCode.decode(ais);
        return operationCode;
    }

    public static OperationCodeImpl createOperationCode() {
        return new OperationCodeImpl();
    }

    public static OperationCodeImpl createOperationCode(int operationCode) {
        return new OperationCodeImpl(operationCode);
    }

    public static OperationCodeImpl createOperationCode(long[] operationCode) {
        return new OperationCodeImpl(operationCode);
    }

    public static ParameterImpl createParameter(AsnInputStream ais) throws AsnException, IOException, IncorrectSyntaxException {
        ParameterImpl parameter = new ParameterImpl();
        parameter.decode(ais);
        return parameter;
    }

    public static ParameterImpl createParameter() {
        return new ParameterImpl();
    }

    public static ErrorCodeImpl createErrorCode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        ErrorCodeImpl errCode = new ErrorCodeImpl();
        errCode.decode(ais);
        return errCode;
    }

    public static ErrorCodeImpl createErrorCode(int errorCode) {
        ErrorCodeImpl errorCodeImpl = new ErrorCodeImpl(errorCode);
        return errorCodeImpl;
    }

    public static ErrorCodeImpl createErrorCode(long[] errorCode) {
        ErrorCodeImpl errorCodeImpl = new ErrorCodeImpl(errorCode);
        return errorCodeImpl;
    }

    public static AssociateSourceDiagnostic createResultSourceDiagnostics(AsnInputStream tmpAis) throws IncorrectSyntaxException, IOException, AsnException {
        AssociateSourceDiagnostic associateSourceDiagnostic = new AssociateSourceDiagnostic();
        associateSourceDiagnostic.decode(tmpAis);
        return associateSourceDiagnostic;
    }
}
