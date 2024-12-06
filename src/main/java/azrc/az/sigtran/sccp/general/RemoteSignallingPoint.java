/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import azrc.az.sigtran.m3ua.parameters.Cause;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author eatakishiyev
 */
public class RemoteSignallingPoint implements Serializable {

    protected transient SCCPStackImpl sccpStack;
    private String name;
    private int spc;
    private boolean remoteSpProhibited = true;
    private boolean remoteSCCPProhibited = true;
    protected List<RemoteSubSystem> remoteSubSystems = new ArrayList();
    private boolean resumeReceived = false;
    private boolean upuReceived = false;
    private Cause cause;
    private int congestionLevel = 0;
    private transient Logger logger = LoggerFactory.getLogger(RemoteSignallingPoint.class);
    private boolean concerned = true;

    public RemoteSignallingPoint() {
    }

    protected RemoteSignallingPoint(String name, int spc, boolean concerned, SCCPStackImpl sccpStack) {
        this.sccpStack = sccpStack;
        this.name = name;
        this.spc = spc;
        this.concerned = concerned;
//      Create SCCP_MANAGEMENT SubSystem by default  
        RemoteSubSystem remoteSubSystem = new RemoteSubSystem(this, SubSystemNumber.SCCP_MANAGEMENT, this.sccpStack);
        this.remoteSubSystems.add(remoteSubSystem);
    }

    public boolean isConcerned() {
        return concerned;
    }

    public void setConcerned(boolean concerned) {
        this.concerned = concerned;
    }

    /**
     * @return the spc
     */
    public int getSpc() {
        return spc;
    }

    /**
     * @param spc the spc to set
     */
    public void setSpc(int spc) {
        this.spc = spc;
    }

    /**
     * @return the prohibited
     */
    public boolean isRemoteSpProhibited() {
        return this.remoteSpProhibited;
    }

    /**
     * @param prohibited the prohibited to set
     */
    public void setRemoteSpProhibited(boolean prohibited) {
        this.remoteSpProhibited = prohibited;
    }

    public boolean isRemoteSCCPProhibited() {
        return this.remoteSCCPProhibited;
    }

    public void setRemoteSCCPProhibited(boolean prohibited) {
        this.remoteSCCPProhibited = prohibited;
    }

    /**
     * @return the resumeReceived
     */
    public boolean isResumeReceived() {
        return resumeReceived;
    }

    /**
     * @param resumeReceived the resumeReceived to set
     */
    public void setResumeReceived(boolean resumeReceived) {
        this.resumeReceived = resumeReceived;
    }

    /**
     * @return the upuReceived
     */
    public boolean isUpuReceived() {
        return upuReceived;
    }

    /**
     * @param upuReceived the upuReceived to set
     */
    public void setUpuReceived(boolean upuReceived) {
        this.upuReceived = upuReceived;
    }

    /**
     * @return the congestionLevel
     */
    public int getCongestionLevel() {
        return congestionLevel;
    }

    /**
     * @param congestionLevel the congestionLevel to set
     */
    public void setCongestionLevel(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public RemoteSubSystem getRemoteSubsystem(SubSystemNumber ssn) {
        for (int i = 0; i < this.remoteSubSystems.size(); i++) {
            RemoteSubSystem remoteSubSystem = this.remoteSubSystems.get(i);
            if (remoteSubSystem.getRemoteSSN() == ssn) {
                return remoteSubSystem;
            }
        }
        return null;
    }

    protected boolean isRemoteSubsystemAvailable(SubSystemNumber ssn) {
        for (int i = 0; i < this.remoteSubSystems.size(); i++) {
            RemoteSubSystem remoteSubSystem = this.remoteSubSystems.get(i);
            if (remoteSubSystem.getRemoteSSN() == ssn
                    && !remoteSubSystem.isProhibited()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    public void onMTPPause(int dpc) {
        if (this.remoteSpProhibited) {
            logger.warn(String.format("MTP-PAUSE received: RemoteSp already prohibited. DPC = %d", dpc));
            return;
        }

        this.remoteSpProhibited = true;

        logger.info(String.format("MTPPause received. Remote SP is prohibited now. DPC = %s", dpc));// local broadcast

        //Remote SCCP is already marked as prohibited
        //May be MTP-STATUS already received and marked SCCP prohibited
        if (this.remoteSCCPProhibited) {
            if (logger.isDebugEnabled()) {
                logger.debug("Remote SCCP is prohibited. Stopping SST[SCMG]");
            }

            //ITU Q.714 clause 5.2.2(3)
            //Discontinues all subsystem status tests(including SSN=1) if an MTP-PAUSE or
            //MTP-STATUS indication primitive is received with a cause of "unequipped SCCP".
            //The SCCP discontinues all subsystem status tests, expect for SSN =1, if an MTP-STATUS
            //indication primitive is received with a clause of either "unknown" or "inaccessible".
            RemoteSubSystem remoteSubSystem = this.getRemoteSubsystem(SubSystemNumber.SCCP_MANAGEMENT);
            if (remoteSubSystem == null) {
                logger.error(String.format("MTP-PAUSE received: Unequipped SCCP_MANAGEMENT subsystem. DPC = %d SSN = SCCP_MANAGEMENT", dpc));
                return;
            }

            remoteSubSystem.stopSst();
        } //Remote SCCP is not marked prohibited
        else {

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Remote SCCP is not marked as prohibited: DPC = %s", dpc));
            }

            this.remoteSCCPProhibited = true;

            for (int i = 0; i < this.remoteSubSystems.size(); i++) {
                RemoteSubSystem remoteSubSystem = this.remoteSubSystems.get(i);
                if (remoteSubSystem.isProhibited()) {
                    remoteSubSystem.stopSst();
                }
                remoteSubSystem.setProhibited(true);
//                if (logger.isDebugEnabled()) {
                logger.warn(String.format("MTPPause received: Remote Subsystem is"
                        + " prohibited now. DPC = %s SSN = %s", dpc,
                        remoteSubSystem.getRemoteSSN()));
//                }
            }
        }

    }

    //Called from SCCP restart control
    public void onSpAccessible() {
        this.resumeReceived = true;

        //Mark SP and remote SCCP allowed
        this.remoteSpProhibited = false;
        this.remoteSCCPProhibited = false;

        RemoteSubSystem remoteSubSystem = this.getRemoteSubsystem(SubSystemNumber.SCCP_MANAGEMENT);

        //If sp accessible then we assume that SCMG alse stand available on node
        remoteSubSystem.setProhibited(false);
        remoteSubSystem.stopSst();

        this.congestionLevel = 0;

        for (int i = 0; i < this.remoteSubSystems.size(); i++) {
            remoteSubSystem = this.remoteSubSystems.get(i);

            if (remoteSubSystem.getRemoteSSN() == SubSystemNumber.SCCP_MANAGEMENT) {
                continue;
            }

            if (remoteSubSystem.isProhibited() && this.resumeReceived) {
                remoteSubSystem.startSst();
            } else {
                remoteSubSystem.setProhibited(false);
                remoteSubSystem.stopSst();

                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Remote Subsystem is accessible now. SP = %s SSN = %s(%d)", this.spc, remoteSubSystem.getRemoteSSN(), remoteSubSystem.getRemoteSSN().value()));
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("SCCP is accessible now. SP = %s", this.spc));
        }
        this.resumeReceived = false;
    }

    public void onMTPStatus(int dpc, int congestionLevel) {
        logger.error(String.format("MTP Status received. Congestion level changed. DPC = %d CONGESTION_LEVEL = %d", dpc, congestionLevel));
    }

    public void onMTPStatus(int dpc, Cause cause) {
        this.upuReceived = true;
        this.cause = cause;

        if (this.remoteSCCPProhibited) {
            return;
        }

        this.remoteSCCPProhibited = true;

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("MTP Status received: DPC = %s CAUSE = %s", dpc, cause));
        }

        if (cause == Cause.INACCESSIBLE_REMOTE_USER
                || cause == Cause.UNKNOWN) {
            RemoteSubSystem remoteSubSystem = this.getRemoteSubsystem(SubSystemNumber.SCCP_MANAGEMENT);
            remoteSubSystem.startSst();
        }
    }

    public void onMTPResume() {
        this.onSpAccessible();
    }

    public void onRemoteSCCPAccessible() {
        this.remoteSpProhibited = false;
        this.remoteSCCPProhibited = false;

        for (int i = 0; i < this.remoteSubSystems.size(); i++) {
            RemoteSubSystem remoteSubSystem = this.remoteSubSystems.get(i);
            if (remoteSubSystem.getRemoteSSN() == SubSystemNumber.SCCP_MANAGEMENT) {
                continue;
            }

            if (remoteSubSystem.isProhibited()
                    && resumeReceived) {
                remoteSubSystem.startSst();
            } else {
                remoteSubSystem.setProhibited(false);
                remoteSubSystem.stopSst();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("SpAccessible received: Remot Subsystem is accessible now. SP = %s SSN = %s", this.spc, remoteSubSystem.getRemoteSSN()));
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("SpAccessible received: SCCP is accessible now. SP = %s", this.spc));
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the remoteSubSystems
     */
    public List<RemoteSubSystem> getRemoteSubSystems() {
        return remoteSubSystems;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RemoteSignallingPoint) {
            return ((RemoteSignallingPoint) obj).name.equals(this.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + this.spc;
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RemoteSP:[")
                .append("Name = ").append(name)
                .append("; Spc = ").append(spc)
                .append("; RemoteSpProhibited = ").append(remoteSpProhibited)
                .append("; RemoteSCCPProhobite = ").append(remoteSCCPProhibited)
                .append("; ResumeReceived = ").append(resumeReceived)
                .append("; UpuReceived = ").append(upuReceived)
                .append("; Cause = ").append(cause)
                .append("; Concerned = ").append(concerned)
                .append("]");
        return sb.toString();
    }

}
