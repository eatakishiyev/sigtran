/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.parameters;

/**
 *
 * @author root
 */
public interface IReturnCause {

    public final int NO_TRANSLATION_FOR_AN_ADDRESS_OF_SUCH_NATURE = 0x01;
    public final int NO_TRANSLATION_FOR_THIS_SPECIFIC_ADDRESS = 0x02;
    public final int SUBSYSTEM_CONGESTION = 0x03;
    public final int SUBSYSTEM_FAILURE = 0x04;
    public final int UNEQUIPPED_USER = 0x05;
    public final int MTP_FAILURE = 0x06;
    public final int NETWORK_CONGESTION = 0x07;
    public final int UNQUALIFIED = 0x08;
    public final int ERROR_IN_MESSAGE_TRANSPORT = 0x08;
    public final int ERROR_IN_LOCAL_PROCESSING = 0x09;
    public final int DESTINATION_CANNOT_PERFORM_REASSEMBLY = 0xA;
    public final int SCCP_FAILURE = 0xB;
    public final int HOP_COUNTER_VIOLATION = 0xC;
    public final int SEGMENTATION_NOT_SUPPORTED = 0xD;
    public final int SEGMENTATION_FAILURE = 0xE;
}
