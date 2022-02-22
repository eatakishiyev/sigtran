/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.cap.errors.CAPUserError;
import dev.ocean.sigtran.cap.parameters.CapGPRSReferenceNumber;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.common.exceptions.UnidentifableServiceException;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;

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
