/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.cap.parameters.CapGPRSReferenceNumber;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;

/**
 * @author eatakishiyev
 */
public class CAPDialogueFactory {

    private final CAPStackImpl stack;

    public CAPDialogueFactory(CAPStackImpl stack) {
        this.stack = stack;
    }

    public CAPDialogue createCAPDialogue(CAPApplicationContexts capApplicationContext,
                                         SCCPAddress destinationAddress, SCCPAddress originationAddress,
                                         CapGPRSReferenceNumber capGprsReferenceNumber, SCCPAddress respondingAddress, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        switch (capApplicationContext) {
            case CAP_V1_GSMSSF_TO_GSMSCF_AC:
                CAPDialogueV1 capDialogueV1 = new CAPDialogueV1(this.stack, destinationAddress,
                        originationAddress, messageHandling, sequenceControl);
                stack.dialogues.put(capDialogueV1.getDialogueId(), capDialogueV1);
                return capDialogueV1;
            case CAP_V2_ASSIST_GSMSSF_TO_GSMSCF_AC:
            case CAP_V2_GSMSSF_TO_GSMSCF_AC:
            case CAP_V2_GSMSRF_TO_GSMSCF_AC:
                CAPDialogueV2 capDialogueV2 = new CAPDialogueV2(stack,
                        capApplicationContext, destinationAddress, originationAddress, messageHandling, sequenceControl);
                stack.dialogues.put(capDialogueV2.getDialogueId(), capDialogueV2);
                return capDialogueV2;
            case CAP_V3_GPRSSSF_GSMSCF_AC:
            case CAP_V3_SMS_AC:
            case CAP_V3_GSMSCF_GPRSSSF_AC:
            case CAP_V3_GSMSRF_GSMSCF_AC:
            case CAP_V3_GSMSSF_SCF_ASSIST_HANDOFF_AC:
            case CAP_V3_GSMSSF_SCF_GENERIC_AC:
                CAPDialogueV3 capDialogueV3 = new CAPDialogueV3(stack,
                        capApplicationContext, destinationAddress, originationAddress, messageHandling, sequenceControl);
                stack.dialogues.put(capDialogueV3.getDialogueId(), capDialogueV3);
                return capDialogueV3;
            case CAP_V4_GSMSMS_AC:
            case CAP_V4_GSMSRF_TO_GSMSCF_AC:
            case CAP_V4_GPRSSSF_TO_GSMSCF_AC:
            case CAP_V4_GSMSCF_TO_GPRSSSF_AC:
            case CAP_V4_GSMSCF_TO_GSMSSF_GENERIC_AC:
            case CAP_V4_GSMSSF_TO_GSMSCF_GENERIC_AC:
            case CAP_V4_GSMSSF_TO_GSMSCF_ASSIST_HANDOFF_AC:
                CAPDialogueV4 capDialogueV4 = new CAPDialogueV4(this.stack, capApplicationContext,
                        destinationAddress, originationAddress,
                        capGprsReferenceNumber,
                        messageHandling, sequenceControl);
                stack.dialogues.put(capDialogueV4.getDialogueId(), capDialogueV4);
                return capDialogueV4;
            default:
                return null;
        }
    }

    public CAPDialogue createCAPDialogue(TCAPDialogue tCAPDialogue) throws ResourceLimitationException {
        CAPApplicationContexts capApplicationContext = CAPApplicationContexts.getInstance(tCAPDialogue.getAppLicationContext().getOid());
        switch (capApplicationContext) {
            case CAP_V1_GSMSSF_TO_GSMSCF_AC:
                CAPDialogueV1 capDialogueV1 = new CAPDialogueV1(tCAPDialogue, this.stack);
                stack.dialogues.put(capDialogueV1.getDialogueId(), capDialogueV1);
                return capDialogueV1;
            case CAP_V2_ASSIST_GSMSSF_TO_GSMSCF_AC:
            case CAP_V2_GSMSSF_TO_GSMSCF_AC:
            case CAP_V2_GSMSRF_TO_GSMSCF_AC:
                CAPDialogueV2 capDialogueV2 = new CAPDialogueV2(tCAPDialogue, this.stack);
                stack.dialogues.put(capDialogueV2.getDialogueId(), capDialogueV2);
                return capDialogueV2;
            case CAP_V3_SMS_AC:
                CAPDialogueV3 capDialogueV3 = new CAPDialogueV3(tCAPDialogue, this.stack);
                stack.dialogues.put(capDialogueV3.getDialogueId(), capDialogueV3);
                return capDialogueV3;
            case CAP_V4_GSMSRF_TO_GSMSCF_AC:
            case CAP_V4_GSMSCF_TO_GSMSSF_GENERIC_AC:
            case CAP_V4_GSMSSF_TO_GSMSCF_ASSIST_HANDOFF_AC:
            case CAP_V4_GSMSSF_TO_GSMSCF_GENERIC_AC:
            case CAP_V4_GSMSMS_AC:
            case CAP_V4_GPRSSSF_TO_GSMSCF_AC:
            case CAP_V4_GSMSCF_TO_GPRSSSF_AC:
                CAPDialogueV4 capDialogueV4 = new CAPDialogueV4(tCAPDialogue, this.stack);
                stack.dialogues.put(capDialogueV4.getDialogueId(), capDialogueV4);
                return capDialogueV4;
            default:
                return null;
        }
    }
}
