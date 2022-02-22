/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface IsupParameter extends Serializable {

    public static final int NATURE_OF_CONNECTIN_INDICATORS = 0b00000110;
    public static final int FORWARD_CALL_INDICATORS = 0b00000111;
    public static final int CALLING_PARTYS_CATEGORY = 0b00001001;
    public static final int TRANSMISSION_MEDIUM_REQUIREMENT = 0b00000010;
    public static final int CALLED_PARTY_NUMBER = 0b00000100;
    public static final int CALLING_PARTY_NUMBER = 0b00001010;
    public static final int TRANSIT_NETWORK_SELECTION = 0b00100011;
    public static final int CALL_REFERENCE = 0b00000001;
    public static final int OPTIONAL_FORWARD_CALL_INDICATORS = 0b00001000;
    public static final int ORIGINAL_CALLED_NUMBER = 0b00101000;
    public static final int USER_TO_USER_INFORMATION = 0b00101101;
    public static final int REDIRECTING_NUMBER = 0b00001011;
    public static final int REDIRECTION_INFORMATION = 0b00010011;
    public static final int CLOSED_USER_GROUP_INTERLOCK_CODE = 0b00011010;
    public static final int CONNECTION_REQUEST = 0b00001101;
    public static final int USER_TO_USER_INDICATORS = 0b00101010;
    public static final int GENERIC_NUMBER = 0b11000000;
    public static final int PROPAGATION_DELAY_COUNTER = 0b00110001;
    public static final int USER_SERVICE_INFORMATION = 0b00011101;
    public static final int USER_SERVICE_INFORMATION_PRIME = 0b00110000;
    public static final int NETWORK_SPECIFIC_FACILITY = 0b00101111;
    public static final int GENERIC_DIGITS = 0b11000001;
    public static final int PARAMETER_COMPATIBILITY_INFORMATION = 0b00111001;
    public static final int GENERIC_NOTIFICATION_INDICATOR = 0b00101100;
    public static final int ORIGINATION_ISC_POINT_CODE = 0b00011110;
    public static final int USER_TELESERVICE_INFORMATION = 0b00110100;
    public static final int REMOTE_OPERATIONS = 0b00110010;
    public static final int SERVICE_ACTIVATION = 0b00110011;
    public static final int MLPP_PRECEDENCE = 0b00111010;
    public static final int TRANSMISSION_MEDIUM_REQUIREMENT_PRIME = 0b00111110;
    public static final int LOCATION_NUMBER = 0b00111111;
    public static final int FORWARD_GVNS = 0b01001100;
    public static final int CCSS = 0b01001011;
    public static final int NETWORK_MANAGEMENT_CONTROLS = 0b01011011;
    public static final int CIRCUIT_ASSIGNMENT_MAP = 0b00100101;
    public static final int CORRELATION_ID = 0b01100101;
    public static final int CALL_DIVERSION_TREATMENT_INDICATORS = 0b01101110;
    public static final int CALLED_IN_NUMBER = 0b01101111;
    public static final int CALL_OFFERING_TREATMENT_INDICATORS = 0b01110000;
    public static final int CONFERENCE_TREATMENT_INDICATORS = 0b01110010;
    public static final int SCF_ID = 0b01100110;
    public static final int UID_CAPABILITY_INDICATORS = 0b01110101;
    public static final int ECHO_CONTROL_INFORMATION = 0b00110111;
    public static final int HOP_COUNTER = 0b00111101;
    public static final int COLLECT_CALL_REQUEST = 0b01111001;
    public static final int APPLICATION_TRANSPORT = 0b01000001;
    public static final int PIVOT_CAPABILITY = 0b01111011;
    public static final int CALLED_DIRECTORY_NUMBER = 0b01111101;
    public static final int ORIGINAL_CALLED_IN_NUMBER = 0b01111111;
    public static final int CALLING_GEODETIC_LOCATION = 0b10000001;
    public static final int NETWORK_ROUTING_NUMBER = 0b10000100;
    public static final int QOR_CAPABILITY = 0b10000101;
    public static final int PIVOT_COUNTER = 0b10000111;
    public static final int PIVOT_ROUTING_FORWARD_INFORMATION = 0b10001000;
    public static final int REDIRECT_CAPABILITY = 0b01001110;
    public static final int REDIRECT_COUNTER = 0b01110111;
    public static final int REDIRECT_STATUS = 0b10001010;
    public static final int REDIRECT_FORWARD_INFORMATION = 0b10001011;
    public static final int NUMBER_PORTABILITY_FORWARD_INFORMATION = 0b10001101;
    public static final int ACCESS_TRANSPORT = 0b00000011;
    public static final int EVENT_INFORMATION = 0b00100100;
    public static final int CAUSE_INDICATORS = 0b00010010;
    public static final int ACCESS_DELIVERY_INFORMATION = 0b00101110;
    public static final int CALL_DIVERSION_INFORMATION = 0b00110110;
    public static final int REDIRECTION_NUMBER_RESTRICTION = 0b01000000;
    public static final int CALL_TRANSFER_NUMBER = 0b01000101;
    public static final int CONNECTED_NUMBER = 0b00100001;
    public static final int BACKWARD_GVNS = 0b01001101;
    public static final int CALL_HISTORY_INFORMATION = 0b00101101;
    public static final int PIVOT_ROUTING_BACKWARD_INFORMATION = 0b10001001;
    public static final int CCNR_POSSIBLE_INDICATOR = 0b01111010;
    public static final int UID_ACTION_INDICATORS = 0b01110100;
    public static final int BACKWARD_CALL_INDICATORS = 0b00010001;
    public static final int OPTIONAL_BACKWARD_CALL_INDICATORS = 0b00101001;
    public static final int END_OF_OPTINAL_PARAMETER = 0b00000000;

    public byte[] encode() throws Exception;

    public void decode(byte[] data) throws Exception;

    public int getParameterCode();
}
