/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum MessageType {
    ADDRESS_COMPLETE(0b00000110),
    ANSWER(0b00001001),
    APPLICATION_TRANSPORT(0b01000001),
    BLOCKING(0b00010011),
    BLOCKING_ACKNOWLEDGEMENT(0b00010101),
    CALL_PROGRESS(0b00101100),
    CIRCUIT_GROUP_BLOCKING(0b00011000),
    CIRCUIT_GROUP_BLOCKING_ACKNOWLEDGEMENT(0b00011010),
    CIRCUIT_GROUP_QUERY(0b00101010),
    CIRCUIT_GROUP_QUERY_RESPONSE(0b00101011),
    CIRCUIT_GROUP_RESET(0b00010111),
    CIRCUIT_GROUP_RESET_ACKNOWLEDGEMENT(0b00101001),
    CIRCUIT_GROUP_UNBLOCKING(0b00011001),
    CIRCUIT_GROUP_UNBLOCKING_ACKNOWLEDGEMENT(0b00011011),
    CHARGE_INFORMATION(0b00110001),
    CONFUSION(0b00101111),
    CONNECT(0b00000111),
    CONTINUITY(0b00000101),
    CONTINUITY_CHECK_REQUEST(0b00010001),
    FACILITY(0b00110011),
    FACILITY_ACCEPTED(0b00100000),
    FACILITY_REJECT(0b00100001),
    FACILITY_REQUEST(0b00011111),
    FORWARD_TRANSFER(0b00001000),
    IDENTIFICATION_REQUEST(0b00110110),
    IDENTIFICATION_RESPONSE(0b00110111),
    INFORMATION(0b00000100),
    INFORMATION_REQUEST(0b00000011),
    INITIAL_ADDRESS(0b00000001),
    LOOP_BACK_ACKNOWLEDGEMENT(0b00100100),
    LOOP_PREVENTION(0b01000000),
    NETWORK_RESOURCE_MANAGEMENT(0b00110010),
    OVERLOAD(0b00110000),
    PASS_ALONG(0b00101000),
    PRE_RELEASE_INFORMATION(0b01000010),
    RELEASE(0b00001100),
    RELEASE_COMPLETE(0b00010000),
    RESET_CIRCUIT(0b00010010),
    RESUME(0b00001110),
    UNKNOWN(-1);

    private final int value;

    private MessageType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MessageType getInstance(int value) {
        switch (value) {
            case 0b00000110:
                return ADDRESS_COMPLETE;
            case 0b00001001:
                return ANSWER;
            case 0b01000001:
                return APPLICATION_TRANSPORT;
            case 0b00010011:
                return BLOCKING;
            case 0b00010101:
                return BLOCKING_ACKNOWLEDGEMENT;
            case 0b00101100:
                return CALL_PROGRESS;
            case 0b00011000:
                return CIRCUIT_GROUP_BLOCKING;
            case 0b00011010:
                return CIRCUIT_GROUP_BLOCKING_ACKNOWLEDGEMENT;
            case 0b00101010:
                return CIRCUIT_GROUP_QUERY;
            case 0b00101011:
                return CIRCUIT_GROUP_QUERY_RESPONSE;
            case 0b00010111:
                return CIRCUIT_GROUP_RESET;
            case 0b00101001:
                return CIRCUIT_GROUP_RESET_ACKNOWLEDGEMENT;
            case 0b00011001:
                return CIRCUIT_GROUP_UNBLOCKING;
            case 0b00011011:
                return CIRCUIT_GROUP_UNBLOCKING_ACKNOWLEDGEMENT;
            case 0b00110001:
                return CHARGE_INFORMATION;
            case 0b00101111:
                return CONFUSION;
            case 0b00000111:
                return CONNECT;
            case 0b00000101:
                return CONTINUITY;
            case 0b00010001:
                return CONTINUITY_CHECK_REQUEST;
            case 0b00110011:
                return FACILITY;
            case 0b00100000:
                return FACILITY_ACCEPTED;
            case 0b00100001:
                return FACILITY_REJECT;
            case 0b00011111:
                return FACILITY_REQUEST;
            case 0b00001000:
                return FORWARD_TRANSFER;
            case 0b00110110:
                return IDENTIFICATION_REQUEST;
            case 0b00110111:
                return IDENTIFICATION_RESPONSE;
            case 0b00000100:
                return INFORMATION;
            case 0b00000011:
                return INFORMATION_REQUEST;
            case 0b00000001:
                return INITIAL_ADDRESS;
            case 0b00100100:
                return LOOP_BACK_ACKNOWLEDGEMENT;
            case 0b01000000:
                return LOOP_PREVENTION;
            case 0b00110010:
                return NETWORK_RESOURCE_MANAGEMENT;
            case 0b00110000:
                return OVERLOAD;
            case 0b00101000:
                return PASS_ALONG;
            case 0b01000010:
                return PRE_RELEASE_INFORMATION;
            case 0b00001100:
                return RELEASE;
            case 0b00010000:
                return RELEASE_COMPLETE;
            case 0b00010010:
                return RESET_CIRCUIT;
            case 0b00001110:
                return RESUME;
            default:
                return UNKNOWN;
        }
    }
}
