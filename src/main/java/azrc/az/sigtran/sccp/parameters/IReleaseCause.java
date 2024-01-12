/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.parameters;

/**
 *
 * @author root
 */
public interface IReleaseCause {

    public final int END_USER_ORIGINATED = 0x00;
    public final int END_USER_CONGESTION = 0x01;
    public final int END_USER_FAILURE = 0x02;
    public final int SCCP_USER_ORIGINATED = 0x03;
    public final int REMOTE_PROCEDURE_ERROR = 0x04;
    public final int INCONSISTENT_CONNECTION_DATA = 0x05;
    public final int ACCESS_FAILURE = 0x06;
    public final int ACCESS_CONGESTION = 0x07;
    public final int SUBSYSTEM_FAILURE = 0x08;
    public final int SUBSYSTEM_CONGESTION = 0x09;
    public final int MTP_FAILURE = 0xA;
    public final int NETWORK_CONGESTION = 0xB;
    public final int EXPIRATION_OF_RESET_TIMER = 0xC;
    public final int EXPIRATION_OF_RECEIVE_INACTIVITY_TIMER = 0xD;
    public final int RESERVED = 0xE;
    public final int UNQUALIFIED = 0xF;
    public final int SCCP_FAILURE = 0x10;
}
