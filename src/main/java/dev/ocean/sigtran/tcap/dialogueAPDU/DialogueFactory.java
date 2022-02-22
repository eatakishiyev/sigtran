/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogueAPDU;

import java.io.IOException;
import dev.ocean.sigtran.tcap.dialogues.intrefaces.DialoguePDU;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.AbortSource;
import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.AssociateResult;
import dev.ocean.sigtran.tcap.parameters.AssociateSourceDiagnostic;
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
                DialogueResponse dialogueResponse = new DialogueResponse();
                dialogueResponse.decode(ais);
                return dialogueResponse;
            case DialoguePDU.DIALOGUE_ABORT_APDU_TAG:
                DialogueAbort dialogueAbort = new DialogueAbort();
                dialogueAbort.decode(ais);
                return dialogueAbort;
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

    public static DialogueResponse createDialogueResponse() {
        return new DialogueResponse();
    }

    public static DialogueResponse createDialogueResponse(ApplicationContextImpl applicationContext, AssociateResult result, AssociateSourceDiagnostic diagnostics) {
        return new DialogueResponse(applicationContext, result, diagnostics);
    }

    public static DialogueAbort createDialogueAbort() {
        return new DialogueAbort();
    }

    public static DialogueAbort createDialogueAbort(AbortSource abortSource) {
        return new DialogueAbort(abortSource);
    }
}
