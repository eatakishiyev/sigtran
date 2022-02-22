/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import org.apache.logging.log4j.*;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;


/**
 *
 * @author eatakishiyev
 */
public class TCAPCache {

    private static final Logger logger = LogManager.getLogger(TCAPCache.class);
    private static boolean b = false;

    public TCAPCache(SubSystemNumber ssn, TCAPStack stack) {
        try {
            logger.info(String.format("Loading %s", "/usr/lib/libtcap_cache" + ssn.value() + ".so"));
            System.load("/usr/lib/libtcap_cache" + ssn.value() + ".so");
            this.setProvider(stack);
            b = true;
        } catch (Throwable th) {
            logger.error("error: ", th);
        }
    }

    //Dialogue cache Part
    public native void init(long start_tid, long end_tid);

    public native void storeDialogue(long dialogueId, byte[] data, long keepAliveTimer);

    public native void eraseDialogue(long dialogueId);

    public native byte[] getDialogue(long dialogueId);

    public native boolean isDialogueExists(long dialogueId);

    public native short allocateInvokeId(long dialogueId);

    public native void freeInvokeId(long dialogueId, short invokeId);

    public native long allocateDialogueId();

    public native void freeDialogueId(long dialogueId);

    private native long consumeExpiredDialogues();

    // ISM Cache Part
    public native boolean isIsmExists(long dialogueId, short invokeId);

    public native void terminateIsm(long dialogueId, short invokeId);

    public native void terminateIsm(long dialogueId);

    public native void initIsm(long dialogueId, short invokeId, int operationClass, long timeOutInSeconds);

    public native int getInvocationStateMachine(long dialogueId, short invokeId);

    private native void setProvider(TCAPStack stack);

    public native void stopInvocationTimer(long dialogueId, short invokeId);

    public native void startInvocationTimer(long dialogueId, short invokeId);

    public native void stopRejectTimer(long dialogueId, short invokeId);

    public native void startRejectTimer(long dialogueId, short invokeId);

    private native long[] consumeTCLCancels();

}
