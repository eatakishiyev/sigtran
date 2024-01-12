/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 *
 * @author root
 */
public class TCAPProviderImpl implements TCAPProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCAPProviderImpl.class);
    private final transient TCAPStack tcapStack;
    protected final Statistic tcapStat;

//    
    protected TCAPProviderImpl(TCAPStack tcapStack) throws IOException {
        this.tcapStack = tcapStack;
        tcapStat = new Statistic(tcapStack.getStackName(),tcapStack);
        LOGGER.info(String.format("Initializing TCAP Provider. KeepAliveTimer => %d", tcapStack.keepAliveTime));
    }

    //Localy initiate dialogue
    @Override
    public TCAPDialogue createDialogue(QoS qos) throws ResourceLimitationException {
        qos.setSequenceNumber(this.tcapStack.getSCCPProvider().generateSequence());
        return this.tcapStack.createDialogue(true, qos);
    }

    @Override
    public void send(SCCPAddress calledParty, SCCPAddress callingParty, QoS qos,
            byte[] userData) {
        try {
            this.tcapStack.getSCCPProvider().send(calledParty, callingParty,
                    qos.isSequenceControl(), qos.getSequenceNumber(),
                    qos.getMessageHandling(), userData);
        } catch (Exception ex) {
            LOGGER.error(String.format("Error occured while send userData. "
                    + "CldPty = %s; ClngPty = %s; %s; UserData = %s", calledParty, callingParty,
                    qos, ByteUtils.bytes2Hex(userData)), ex);
        }
    }

    public TCAPDialogue getDialogue(long dialogueId) {
        return tcapStack.getDialogue(dialogueId);
    }

//    public DialogueStorage getDialogueCache() {
//        return tcapStack.dialogueStorage;
//    }
    /**
     * @return the keepAliveTime
     */
    public long getKeepAliveTime() {
        return tcapStack.keepAliveTime;
    }

    @Override
    public final void setUser(TCUser user) {
        this.tcapStack.setUser(user);
    }

    @Override
    public long getConcurrentDialogues() {
        return tcapStack.totalDialogueCounts();
    }

}
