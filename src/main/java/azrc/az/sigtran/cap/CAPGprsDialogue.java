/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.cap.parameters.CapGPRSReferenceNumber;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.common.exceptions.UnidentifableServiceException;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.primitives.tc.TCInvoke;
import azrc.az.sigtran.tcap.primitives.tc.TCResult;

/**
 *
 * @author eatakishiyev
 */
public class CAPGprsDialogue extends CAPDialogue {

    public CAPGprsDialogue(CAPStackImpl stack,
            CAPApplicationContexts capApplicationContext,
            SCCPAddress destinationAddress,
            SCCPAddress originationAddress,
            CapGPRSReferenceNumber capGprsReferenceNumber,
            MessageHandling messageHandling,
            boolean sequenceControl) throws ResourceLimitationException {
        super(stack, capApplicationContext, destinationAddress, originationAddress,
                capGprsReferenceNumber, messageHandling, sequenceControl);
    }

    public CAPGprsDialogue(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        super(tcapDialogue, stack);
    }

    @Override
    public void onServiceInvocationReceived(TCInvoke invoke, OperationCodes operationCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onResultReceived(TCResult indication, int opCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNegativeResultReceived(OperationCodes operation, CAPUserError userError, Short invokeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNegativeResultReceived(OperationCodes operation, ProviderError providerError, Short invokeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
