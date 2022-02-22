/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum NotificationIndicator {
    USER_SUSPENDED(0b00000000),
    USER_RESUMED(0b00000001),
    BEARER_SERVICE_CHANGE(0b00000010),
    DISCRIMINATOR_FOR_EXTENSION_TO_ASN1_ENCODED_COMPONENT(0b00000011),
    CALL_COMPLETION_DELAY(0b00000100),
    CONFERENCE_ESTABLISHED(0b10000010),
    CONFERENCE_DISCONNECTED(0b10000011),
    OTHER_PARTY_ADDED(0b01000100),
    ISOLATED(0b01000101),
    REATTACHED(0b01000110),
    OTHER_PARTY_ISOLATED(0b01000111),
    OTHER_PARTY_REATTACHED(0b01001000),
    OTHER_PARTY_SPLIT(0b01001001),
    OTHER_PARTY_DISCONNECTED(0b01001010),
    CONFERENCE_FLOATING(0b01001011),
    CALL_IS_WAITING_CALL(0b01100000),
    DIVERSION_ACTIVATED(0b01101000),
    CALL_TRANSFER_ALERTING(0b01101001),
    CALL_TRANSFER_ACTIVE(0b01101010),
    REMOTE_HOLD(0b01111001),
    REMOTE_RETRIEVAL(0b01111010),
    CALL_IS_DIVERTING(0b01111011),
    UNKNOWN(-1);

    private final int value;

    private NotificationIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NotificationIndicator getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return USER_SUSPENDED;
            case 0b00000001:
                return USER_RESUMED;
            case 0b00000010:
                return BEARER_SERVICE_CHANGE;
            case 0b00000011:
                return DISCRIMINATOR_FOR_EXTENSION_TO_ASN1_ENCODED_COMPONENT;
            case 0b00000100:
                return CALL_COMPLETION_DELAY;
            case 0b10000010:
                return CONFERENCE_ESTABLISHED;
            case 0b10000011:
                return CONFERENCE_DISCONNECTED;
            case 0b01000100:
                return OTHER_PARTY_ADDED;
            case 0b01000101:
                return ISOLATED;
            case 0b01000110:
                return REATTACHED;
            case 0b01000111:
                return OTHER_PARTY_ISOLATED;
            case 0b01001000:
                return OTHER_PARTY_REATTACHED;
            case 0b01001001:
                return OTHER_PARTY_SPLIT;
            case 0b01001010:
                return OTHER_PARTY_DISCONNECTED;
            case 0b01001011:
                return CONFERENCE_FLOATING;
            case 0b01100000:
                return CALL_IS_WAITING_CALL;
            case 0b01101000:
                return DIVERSION_ACTIVATED;
            case 0b01101001:
                return CALL_TRANSFER_ALERTING;
            case 0b01101010:
                return CALL_TRANSFER_ACTIVE;
            case 0b01111001:
                return REMOTE_HOLD;
            case 0b01111010:
                return REMOTE_RETRIEVAL;
            case 0b01111011:
                return CALL_IS_DIVERTING;
            default:
                return UNKNOWN;
        }
    }
}
