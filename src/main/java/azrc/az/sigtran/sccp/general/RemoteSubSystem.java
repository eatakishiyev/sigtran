/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import azrc.az.sigtran.m3ua.parameters.Cause;
import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SignallingPointCode;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.messages.management.SCCPManagementMessageFactory;
import azrc.az.sigtran.sccp.messages.management.SubsystemStatusTest;
import azrc.az.sigtran.sccp.parameters.SubsystemMultiplicityIndicator;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author eatakishiyev
 */
public class RemoteSubSystem implements Runnable, Serializable {

    private transient final Logger logger = LoggerFactory.getLogger(RemoteSubSystem.class);
    protected transient SCCPStackImpl sccpStack;
    private SubSystemNumber remoteSSN;
    private boolean prohibited;
    private transient Future task;
    private TesterState state = TesterState.IDLE;
    protected RemoteSignallingPoint remoteSignallingPoint;
    protected long delay;

    public RemoteSubSystem() {

    }

    protected RemoteSubSystem(RemoteSignallingPoint remoteSignallingPoint, SubSystemNumber remoteSSN, SCCPStackImpl sccpStack) {
        this();
        this.sccpStack = sccpStack;
        this.remoteSSN = remoteSSN;
        this.remoteSignallingPoint = remoteSignallingPoint;
        this.delay = sccpStack.getSstTimerMin();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RemoteSubSystem[");
        sb.append("Remote SP = ").append(remoteSignallingPoint)
                .append(", RemoteSSN = ").append(remoteSSN)
                .append("]");
        return sb.toString();
    }

    /**
     * @return the remoteSSN
     */
    public SubSystemNumber getRemoteSSN() {
        return remoteSSN;
    }

    /**
     * @param remoteSSN the remoteSSN to set
     */
    public void setRemoteSSN(SubSystemNumber remoteSSN) {
        this.remoteSSN = remoteSSN;
    }

    /**
     * @return the prohibited
     */
    public boolean isProhibited() {
        return prohibited;
    }

    /**
     * @param prohibited the prohibited to set
     */
    public void setProhibited(boolean prohibited) {
        this.prohibited = prohibited;
    }

    public void stopSst() {
        logger.info("SST stopped. SSN = " + remoteSSN + ";" + this.remoteSignallingPoint);
        if (this.task != null) {
            this.task.cancel(true);
            this.task = null;
        }
    }

    public void startSst() {
        if (this.state == TesterState.TEST_IN_PROGRESS) {
            return;
        }

        logger.info("Starting SST.SSN = " + remoteSSN + ";" + remoteSignallingPoint);
        this.task = sccpStack.sstScheduler.scheduleAtFixedRate(this, this.delay,
                this.delay, TimeUnit.MILLISECONDS);
        this.state = TesterState.TEST_IN_PROGRESS;
    }

    public void restartSst() {
        if (this.delay >= sccpStack.getSstTimerMax()) {
            this.delay = sccpStack.getSstTimerMin();
        }

        this.stopSst();
        this.startSst();

        this.delay += sccpStack.getSstTimerIncreaseBy();
    }

    @Override
    public void run() {
        //SSN = SCMG
        if (this.remoteSSN == SubSystemNumber.SCCP_MANAGEMENT) {
            if (this.remoteSignallingPoint.isUpuReceived()) {
                this.remoteSignallingPoint.setUpuReceived(false);
                if (this.remoteSignallingPoint.getCause() != Cause.UNEQUIPPED_REMOTE_USER) {
                    this.sendSst(this.remoteSignallingPoint);
                }

            } else {
                this.prohibited = false;
                this.remoteSignallingPoint.onRemoteSCCPAccessible();
                this.state = TesterState.IDLE;
            }
        } //SSN != SCMG
        else {
            this.sendSst(this.remoteSignallingPoint);
            this.restartSst();
        }
    }

    private void sendSst(RemoteSignallingPoint remoteSignallingPoint) {
        SubsystemStatusTest sst = SCCPManagementMessageFactory.
                createSubsystemStatusTest(SubSystemNumber.SCCP_MANAGEMENT,
                        new SignallingPointCode(remoteSignallingPoint.getSpc()),
                        new SubsystemMultiplicityIndicator(0));

        int dpc = remoteSignallingPoint.getSpc();
        int opc = this.sccpStack.getSccpLayerManagement().getMtpSap(dpc).getOpc();

        SCCPAddress callingAddress = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN,
                opc, null, SubSystemNumber.SCCP_MANAGEMENT);//SPC will be added after calling party treatment
        SCCPAddress calledAddress = new SCCPAddress(RoutingIndicator.ROUTE_ON_SSN,
                dpc, null, SubSystemNumber.SCCP_MANAGEMENT);

        sccpStack.getSccpConnectionlessControl().sendSccpManagementMessage(callingAddress,
                calledAddress, sst, true);
    }

    public boolean isSstRunning() {
        return !(task == null);
    }
}
