/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogues.intrefaces;

import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sigtran.tcap.parameters.AssociateResult;
import azrc.az.sigtran.tcap.parameters.AssociateSourceDiagnosticImpl;
import azrc.az.sigtran.tcap.parameters.ProtocolVersionImpl;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.AssociateSourceDiagnostic;

/**
 *
 * @author root
 */
public interface DialogueResponsePDU extends DialoguePDU {

    public ProtocolVersionImpl getProtocolVersion();

    public ApplicationContext getApplicationContext();

    public void setApplicationContext(ApplicationContext ac);

    public AssociateResult getResult();

    public void setResult(AssociateResult result);

    public AssociateSourceDiagnostic getResutlSourceDiagnostic();

    public void setResultSourceDiagnostic(AssociateSourceDiagnostic asd);
}
