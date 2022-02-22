/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters.ITU.Q_850;

import dev.ocean.isup.enums.CodingStandard;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class Cause {

    private CodingStandard codingStandard;
    private Location location;
    private Recommendation recommendation;
    private CauseValue causeValue;
    private byte[] diagnostics;

    public void encode(ByteArrayOutputStream baos) throws IOException {
        int b = recommendation != null ? 0 : 1;
        b = (b << 2) | codingStandard.value();
        b = (b << 1);//spare
        b = (b << 4) | location.value();
        baos.write(b);

        if (recommendation != null) {
            b = 1;
            b = (b << 7) | recommendation.value();
            baos.write(b);
        }

        b = 1;
        b = (b << 7) | causeValue.value();
        baos.write(b);

        if (diagnostics != null) {
            baos.write(diagnostics);
        }
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        int b = bais.read() & 0xFF;
        int extensionIndicator = b >> 7;

        this.setCodingStandard(CodingStandard.getInstance((b >> 5) & 0b00000011));
        this.setLocation(Location.getInstance(b & 0b00001111));

        if (extensionIndicator == 0) {
            b = bais.read() & 0xFF;
            this.setRecommendation(Recommendation.getInstance(b & 0b01111111));
        }

        b = bais.read() & 0xFF;
        this.setCauseValue(CauseValue.getInstance(b & 0b01111111));

        if (bais.available() > 0) {
            this.setDiagnostics(new byte[bais.available()]);
            bais.read(getDiagnostics());
        }
    }

    /**
     * @return the codingStandard
     */
    public CodingStandard getCodingStandard() {
        return codingStandard;
    }

    /**
     * @param codingStandard the codingStandard to set
     */
    public void setCodingStandard(CodingStandard codingStandard) {
        this.codingStandard = codingStandard;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the recommendation
     */
    public Recommendation getRecommendation() {
        return recommendation;
    }

    /**
     * @param recommendation the recommendation to set
     */
    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    /**
     * @return the causeValue
     */
    public CauseValue getCauseValue() {
        return causeValue;
    }

    /**
     * @param causeValue the causeValue to set
     */
    public void setCauseValue(CauseValue causeValue) {
        this.causeValue = causeValue;
    }

    /**
     * @return the diagnostics
     */
    public byte[] getDiagnostics() {
        return diagnostics;
    }

    /**
     * @param diagnostics the diagnostics to set
     */
    public void setDiagnostics(byte[] diagnostics) {
        this.diagnostics = diagnostics;
    }

    public enum Location {

        U(0b00000000),
        LPN(0b00000001),
        LN(0b00000010),
        TN(0b00000011),
        RLN(0b00000100),
        RPN(0b00000101),
        INTL(0b00000111),
        BI(0b00001010),
        RESERVED_1100(0b00001100),
        RESERVED_1101(0b00001101),
        RESERVED_1110(0b00001110),
        RESERVED_1111(0b00001111),
        UNKNOWN(-1);

        private int value;

        private Location(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Location getInstance(int value) {
            switch (value) {
                case 0b00000000:
                    return U;
                case 0b00000001:
                    return LPN;
                case 0b00000010:
                    return LN;
                case 0b00000011:
                    return TN;
                case 0b00000100:
                    return RLN;
                case 0b00000101:
                    return RPN;
                case 0b00000111:
                    return INTL;
                case 0b00001010:
                    return BI;
                case 0b00001100:
                    return RESERVED_1100;
                case 0b00001101:
                    return RESERVED_1101;
                case 0b00001110:
                    return RESERVED_1110;
                case 0b00001111:
                    return RESERVED_1111;
                default:
                    return UNKNOWN;
            }
        }

    }

    public enum Recommendation {

        Q931(0b0000000),
        X21(0b00000011),
        X25(0b00000100),
        Q1031_Q1051(0b0000101),
        UNKWNOWN(-1);

        private int value;

        private Recommendation(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Recommendation getInstance(int value) {
            switch (value) {
                case 0b0000000:
                    return Q931;
                case 0b00000011:
                    return X21;
                case 0b00000100:
                    return X25;
                case 0b0000101:
                    return Q1031_Q1051;
                default:
                    return UNKWNOWN;
            }
        }
    }

    public enum CauseValue {

        UNALLOCATED_NUMBER(1),
        NO_ROUTE_TO_SPECIFIED_TRANSIT_NETWORK(2),
        NO_ROUTE_TO_DESTINATION(3),
        SEND_SPECIAL_INFORMATION_TONE(4),
        MISDIALLED_TRUNK_PREFIX(5),
        CHANNEL_UNACCEPTABLE(6),
        CALL_AWARDED_AND_BEING_DELIVERED_IN_ESTABLISHED_CHANNEL(7),
        PREEMPTION(8),
        PREEMPTION_CIRCUIT_RESERVED_FOR_REUSE(9),
        NORMAL_CALL_CLEARING(16),
        USER_BUSY(17),
        NO_USER_RESPONDING(18),
        NO_ANSWER_FROM_USER(19),
        SUBSCRIBER_ABSENT(20),
        CALL_REJECTED(21),
        NUMBER_CHANGED(22),
        REDIRECTION_TO_NEW_DESTINATION(23),
        EXCHANGE_ROUTING_ERROR(25),
        NON_SELECTED_USER_CLEARING(26),
        DESTINATION_OUT_OF_ORDER(27),
        ADDRESS_INCOMPLETE(28),
        FACILITY_REJECTED(29),
        RESPONSE_TO_STATUS_ENQUIRY(30),
        NORMAL(31),
        NO_CIRCUIT_AVAILABLE(34),
        NETWORK_OUT_OF_ORDER(38),
        PERMANENT_FRAME_MODE_CONNECTION_OUT_OF_SERVICE(39),
        PERMANENT_FRAME_MODE_CONNECTION_OPERATIONAL(40),
        TEMPORARY_FAILURE(41),
        SWITCHING_QUIPMENT_CONGESTION(42),
        ACCESS_INFORMATION_DISCARDED(43),
        REQUESTED_CIRCUIT_NOT_AVAILABLE(44),
        PRECEDENCE_CALL_BLOCKED(46),
        RESOURCE_UNAVAILABLE(47),
        QUALITY_OF_SERVICE_NOT_AVAILABLE(49),
        REQUESTED_FACILITY_NOT_SUBSCRIBED(50),
        OUTGOING_CALLS_BARRED_WITHIN_CUG(53),
        INCOMING_CALLS_BARRED_WITHIN_CUG(55),
        BEARER_CAPABILITY_NOT_AUTHORIZED(57),
        BEARER_CAPABILITY_NOT_PRESENTLY_AVAILABLE(58),
        INCONSISTENCY_IN_DESIGNATED_OUTGOING_ACCESS_INFORMATION_AND_SUBSCRIBER_CLASS(62),
        SERVICE_OR_OPTION_NOT_AVAILABLE(63),
        BEARER_CAPABILITY_NOT_IMPLEMENTED(65),
        CHANNEL_TYPE_NOT_IMPLEMENTED(66),
        REQUESTED_FACILITY_NOT_IMPLEMENTED(69),
        ONLY_RESTRICTED_DIGITAL_INFORMATION_BEARER_CAPABILITY_IS_AVAILABLE(70),
        SERVICE_OR_OPTION_NOT_IMPLEMENTED(79),
        INVALID_CALL_REFERENCE_VALUE(81),
        IDENTIFIED_CHANNEL_DOES_NOT_EXIST(82),
        SUSPENDED_CALL_EXISTS_THIS_CALL_IDENTITY_DOES_NOT_EXISTS(83),
        CALL_IDENTITY_IN_USE(84),
        NO_CALL_SUSPENDED(85),
        CALL_HAVING_REQUESTED_CALL_IDENTITY_HAS_BEEN_CLEARED(86),
        USER_NOT_MEMBER_OF_CUG(87),
        INCOMPATIBLE_DESTINATION(88),
        NON_EXISTENT_CUG(90),
        INVALID_TRANSIT_NETWORK_SELECTION(91),
        INVALID_MESSAGE(95),
        MANDATORY_INFORMATION_ELEMENT_IS_MISSING(96),
        MESSAGE_TYPE_NON_EXISTENT(97),
        MESSAGE_NOT_COMPATIBLE_WITH_CALL_STATE_OR_MESSAGE_TYPE_NON_EXISTENT(98),
        INFORMATION_ELEMENT_NON_EXISTENT(99),
        INVALID_INFORMATION_ELEMENT_CONTENTS(100),
        MESSAGE_NOT_COMPATIBLE_WITH_CALL_STATE(101),
        RECOVERY_ON_TIMER_EXPIRY(102),
        PARAMETER_NON_EXISTENT(103),
        MESSAGE_WITH_UNRECOGNIZED_PARAMETER(110),
        PROTOCOL_ERROR(111),
        INTERWORKING(127),
        UNKNOWN(-1);

        private final int value;

        public int value() {
            return this.value;
        }

        private CauseValue(int value) {
            this.value = value;
        }

        public static CauseValue getInstance(int value) {
            switch (value) {
                case 1:
                    return UNALLOCATED_NUMBER;
                case 2:
                    return NO_ROUTE_TO_SPECIFIED_TRANSIT_NETWORK;
                case 3:
                    return NO_ROUTE_TO_DESTINATION;
                case 4:
                    return SEND_SPECIAL_INFORMATION_TONE;
                case 5:
                    return MISDIALLED_TRUNK_PREFIX;
                case 6:
                    return CHANNEL_UNACCEPTABLE;
                case 7:
                    return CALL_AWARDED_AND_BEING_DELIVERED_IN_ESTABLISHED_CHANNEL;
                case 8:
                    return PREEMPTION;
                case 9:
                    return PREEMPTION_CIRCUIT_RESERVED_FOR_REUSE;
                case 16:
                    return NORMAL_CALL_CLEARING;
                case 17:
                    return USER_BUSY;
                case 18:
                    return NO_USER_RESPONDING;
                case 19:
                    return NO_ANSWER_FROM_USER;
                case 20:
                    return SUBSCRIBER_ABSENT;
                case 21:
                    return CALL_REJECTED;
                case 22:
                    return NUMBER_CHANGED;
                case 23:
                    return REDIRECTION_TO_NEW_DESTINATION;
                case 25:
                    return EXCHANGE_ROUTING_ERROR;
                case 26:
                    return NON_SELECTED_USER_CLEARING;
                case 27:
                    return DESTINATION_OUT_OF_ORDER;
                case 28:
                    return ADDRESS_INCOMPLETE;
                case 29:
                    return FACILITY_REJECTED;
                case 30:
                    return RESPONSE_TO_STATUS_ENQUIRY;
                case 31:
                    return NORMAL;
                case 34:
                    return NO_CIRCUIT_AVAILABLE;
                case 38:
                    return NETWORK_OUT_OF_ORDER;
                case 39:
                    return PERMANENT_FRAME_MODE_CONNECTION_OUT_OF_SERVICE;
                case 40:
                    return PERMANENT_FRAME_MODE_CONNECTION_OPERATIONAL;
                case 41:
                    return TEMPORARY_FAILURE;
                case 42:
                    return SWITCHING_QUIPMENT_CONGESTION;
                case 43:
                    return ACCESS_INFORMATION_DISCARDED;
                case 44:
                    return REQUESTED_CIRCUIT_NOT_AVAILABLE;
                case 46:
                    return PRECEDENCE_CALL_BLOCKED;
                case 47:
                    return RESOURCE_UNAVAILABLE;
                case 49:
                    return QUALITY_OF_SERVICE_NOT_AVAILABLE;
                case 50:
                    return REQUESTED_FACILITY_NOT_SUBSCRIBED;
                case 53:
                    return OUTGOING_CALLS_BARRED_WITHIN_CUG;
                case 55:
                    return INCOMING_CALLS_BARRED_WITHIN_CUG;
                case 57:
                    return BEARER_CAPABILITY_NOT_AUTHORIZED;
                case 58:
                    return BEARER_CAPABILITY_NOT_PRESENTLY_AVAILABLE;
                case 62:
                    return INCONSISTENCY_IN_DESIGNATED_OUTGOING_ACCESS_INFORMATION_AND_SUBSCRIBER_CLASS;
                case 63:
                    return SERVICE_OR_OPTION_NOT_AVAILABLE;
                case 65:
                    return BEARER_CAPABILITY_NOT_IMPLEMENTED;
                case 66:
                    return CHANNEL_TYPE_NOT_IMPLEMENTED;
                case 69:
                    return REQUESTED_FACILITY_NOT_IMPLEMENTED;
                case 70:
                    return ONLY_RESTRICTED_DIGITAL_INFORMATION_BEARER_CAPABILITY_IS_AVAILABLE;
                case 79:
                    return SERVICE_OR_OPTION_NOT_IMPLEMENTED;
                case 81:
                    return INVALID_CALL_REFERENCE_VALUE;
                case 82:
                    return IDENTIFIED_CHANNEL_DOES_NOT_EXIST;
                case 83:
                    return SUSPENDED_CALL_EXISTS_THIS_CALL_IDENTITY_DOES_NOT_EXISTS;
                case 84:
                    return CALL_IDENTITY_IN_USE;
                case 85:
                    return NO_CALL_SUSPENDED;
                case 86:
                    return CALL_HAVING_REQUESTED_CALL_IDENTITY_HAS_BEEN_CLEARED;
                case 87:
                    return USER_NOT_MEMBER_OF_CUG;
                case 88:
                    return INCOMPATIBLE_DESTINATION;
                case 90:
                    return NON_EXISTENT_CUG;
                case 91:
                    return INVALID_TRANSIT_NETWORK_SELECTION;
                case 95:
                    return INVALID_MESSAGE;
                case 96:
                    return MANDATORY_INFORMATION_ELEMENT_IS_MISSING;
                case 97:
                    return MESSAGE_TYPE_NON_EXISTENT;
                case 98:
                    return MESSAGE_NOT_COMPATIBLE_WITH_CALL_STATE_OR_MESSAGE_TYPE_NON_EXISTENT;
                case 99:
                    return INFORMATION_ELEMENT_NON_EXISTENT;
                case 100:
                    return INVALID_INFORMATION_ELEMENT_CONTENTS;
                case 101:
                    return MESSAGE_NOT_COMPATIBLE_WITH_CALL_STATE;
                case 102:
                    return RECOVERY_ON_TIMER_EXPIRY;
                case 103:
                    return PARAMETER_NON_EXISTENT;
                case 110:
                    return MESSAGE_WITH_UNRECOGNIZED_PARAMETER;
                case 111:
                    return PROTOCOL_ERROR;
                case 127:
                    return INTERWORKING;
                default:
                    return UNKNOWN;
            }
        }
    }

}
