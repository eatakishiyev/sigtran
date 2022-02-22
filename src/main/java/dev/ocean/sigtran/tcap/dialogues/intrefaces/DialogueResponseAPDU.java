/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogues.intrefaces;

import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.AssociateResult;
import dev.ocean.sigtran.tcap.parameters.AssociateSourceDiagnostic;
import dev.ocean.sigtran.tcap.parameters.ProtocolVersionImpl;

/**
 *
 * @author root
 */
public interface DialogueResponseAPDU extends DialoguePDU {

    public ProtocolVersionImpl getProtocolVersion();

    public ApplicationContextImpl getApplicationContext();

    public void setApplicationContext(ApplicationContextImpl ac);

    public AssociateResult getResult();

    public void setResult(AssociateResult result);

    public AssociateSourceDiagnostic getResutlSourceDiagnostic();

    public void setResultSourceDiagnostic(AssociateSourceDiagnostic asd);
}
