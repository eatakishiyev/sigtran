/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogueAPDU;

import java.io.IOException;

import azrc.az.sigtran.tcap.dialogues.intrefaces.DialoguePDU;
import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogueResponsePDU;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.AbortSource;
import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sigtran.tcap.parameters.AssociateResult;
import azrc.az.sigtran.tcap.parameters.AssociateSourceDiagnosticImpl;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.AssociateSourceDiagnostic;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class DialogueFactory {

    private DialogueFactory() {

    }

    public static DialoguePDU createDialoguePDU(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        int tag = ais.readTag();

        switch (tag) {
            case DialoguePDU.DIALOGUE_REQUEST_APDU_TAG:
                DialogueRequest dialogueRequest = new DialogueRequest();
                dialogueRequest.decode(ais);
                return dialogueRequest;
            case DialoguePDU.DIALOGUE_RESPONSE_APDU_TAG:
                DialogueResponsePDUImpl dialogueResponsePDUImpl = new DialogueResponsePDUImpl();
                dialogueResponsePDUImpl.decode(ais);
                return dialogueResponsePDUImpl;
            case DialoguePDU.DIALOGUE_ABORT_APDU_TAG:
                DialogueAbortPDUImpl dialogueAbortAPDUImpl = new DialogueAbortPDUImpl();
                dialogueAbortAPDUImpl.decode(ais);
                return dialogueAbortAPDUImpl;
            default:
                throw new IncorrectSyntaxException("Unknown dialogue");
        }
    }

    public static DialoguePortionImpl createDialoguePortion(AsnInputStream ais) throws IncorrectSyntaxException {
        DialoguePortionImpl dp = new DialoguePortionImpl();
        dp.decode(ais);
        return dp;
    }

    public static DialogueRequest createDialogueRequest() {
        return new DialogueRequest();
    }

    public static DialogueRequest createDialogueRequest(ApplicationContextImpl applicationContext) {
        return new DialogueRequest(applicationContext);
    }

    public static DialogueResponsePDUImpl createDialogueResponse() {
        return new DialogueResponsePDUImpl();
    }

    public static DialogueResponsePDU createDialogueResponse(ApplicationContext applicationContext, AssociateResult result, AssociateSourceDiagnostic diagnostics) {
        return new DialogueResponsePDUImpl(applicationContext, result, diagnostics);
    }

    public static DialogueAbortPDUImpl createDialogueAbort() {
        return new DialogueAbortPDUImpl();
    }

    public static DialogueAbortPDUImpl createDialogueAbort(AbortSource abortSource) {
        return new DialogueAbortPDUImpl(abortSource);
    }
}
