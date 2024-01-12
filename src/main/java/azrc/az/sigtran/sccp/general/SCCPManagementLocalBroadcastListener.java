/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPManagementLocalBroadcastListener {

    public void onPCState(int affectedPointCode, SignallingPointStatus pointStatus, RemoteSCCPStatus sccpStatus) throws Exception;

    public enum SignallingPointStatus {

        Inaccessible,
        Congested,
        Accessible;
    }

    public enum RemoteSCCPStatus {

        Available,
        Unavailable,
        Unequipped,
        Inaccessible,
        Congested;
    }
}
