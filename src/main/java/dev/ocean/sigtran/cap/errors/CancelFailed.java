/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.errors;

import dev.ocean.sigtran.cap.CAPUserErrorCodes;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * cancelFailed ERROR ::= { PARAMETER SEQUENCE { problem [0] ENUMERATED {
 * unknownOperation (0), tooLate (1), operationNotCancellable (2) }, operation
 * [1] InvokeID, ... } CODE errcode-cancelFailed } -- The operation failed to be
 * canceled.
 *
 * @author eatakishiyev
 */
public class CancelFailed extends CAPUserError {

    private Problem problem = Problem.UNKNOWN_OPERATOR;
    private int invokeId;

    public CancelFailed() {
    }

    public CancelFailed(Problem problem, int invokeId) {
        this.problem = problem;
        this.invokeId = invokeId;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IOException, AsnException {
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, problem.value);
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, invokeId);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case 0:
                        this.problem = Problem.getInstance((int) ais.readInteger());
                        break;
                    case 1:
                        this.invokeId = (int) ais.readInteger();
                        break;
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public CAPUserErrorCodes getErrorCode() {
        return CAPUserErrorCodes.CANCEL_FAILED;
    }

    /**
     * @return the problem
     */
    public Problem getProblem() {
        return problem;
    }

    /**
     * @return the invokeId
     */
    public int getInvokeId() {
        return invokeId;
    }

    public enum Problem {

        UNKNOWN_OPERATOR(0),
        TOO_LATE(1),
        OPERATION_NOT_CANCELLABLE(2);

        private int value;

        private Problem(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Problem getInstance(int value) {
            switch (value) {
                case 0:
                    return UNKNOWN_OPERATOR;
                case 1:
                    return TOO_LATE;
                case 2:
                    return OPERATION_NOT_CANCELLABLE;
                default:
                    return null;
            }
        }
    }
}
