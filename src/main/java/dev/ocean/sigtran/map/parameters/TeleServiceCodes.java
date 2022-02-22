/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum TeleServiceCodes {

    ALL_TELESERVICES(0),
    ALL_SPEECH_TRANSMISSION_SERVICES(16),
    TELEPHONY(17),
    EMERGENCY_CALLS(18),
    ALL_SHORT_MESSAGE_SERVICES(32),
    SHORT_MESSAGE_MTPP(33),
    SHORT_MESSAGE_MOPP(34),
    ALL_FACSIMILE_TRANSMISSION_SERVICES(96),
    FACSIMILE_GROUP3_AND_ALTER_SPEECH(97),
    AUTOMATIC_FACSIMILE_GROUP3(98),
    FACSIMILE_GROUP4(99),
    ALL_DATA_TELESERVICES(112),
    ALL_TELESERVICES_EXEPT_SMS(128),
    ALL_VOICE_GROUP_CALL_SERVICES(144),
    VOICE_GROUP_CALL(145),
    VOICE_BROADCAST_CALL(146),
    ALL_PLMN_SPECIFIC_TS(208),
    PLMN_SPECIFIC_TS1(209),
    PLMN_SPECIFIC_TS2(210),
    PLMN_SPECIFIC_TS3(211),
    PLMN_SPECIFIC_TS4(212),
    PLMN_SPECIFIC_TS5(213),
    PLMN_SPECIFIC_TS6(214),
    PLMN_SPECIFIC_TS7(215),
    PLMN_SPECIFIC_TS8(216),
    PLMN_SPECIFIC_TS9(217),
    PLMN_SPECIFIC_TS_A(218),
    PLMN_SPECIFIC_TS_B(219),
    PLMN_SPECIFIC_TS_C(220),
    PLMN_SPECIFIC_TS_D(221),
    PLMN_SPECIFIC_TS_E(222),
    PLMN_SPECIFIC_TS_F(223);
    private final int value;

    private TeleServiceCodes(int value) {
        this.value = value;
    }

    public static TeleServiceCodes getInstance(int value) {
        switch (value) {
            case 0:
                return ALL_TELESERVICES;
            case 16:
                return ALL_SPEECH_TRANSMISSION_SERVICES;
            case 17:
                return TELEPHONY;
            case 18:
                return EMERGENCY_CALLS;
            case 32:
                return ALL_SHORT_MESSAGE_SERVICES;
            case 33:
                return SHORT_MESSAGE_MTPP;
            case 34:
                return SHORT_MESSAGE_MOPP;
            case 96:
                return ALL_FACSIMILE_TRANSMISSION_SERVICES;
            case 97:
                return FACSIMILE_GROUP3_AND_ALTER_SPEECH;
            case 98:
                return AUTOMATIC_FACSIMILE_GROUP3;
            case 99:
                return FACSIMILE_GROUP4;
            case 112:
                return ALL_DATA_TELESERVICES;
            case 128:
                return ALL_TELESERVICES_EXEPT_SMS;
            case 144:
                return ALL_VOICE_GROUP_CALL_SERVICES;
            case 145:
                return VOICE_GROUP_CALL;
            case 146:
                return VOICE_BROADCAST_CALL;
            case 208:
                return ALL_PLMN_SPECIFIC_TS;
            case 209:
                return PLMN_SPECIFIC_TS1;
            case 210:
                return PLMN_SPECIFIC_TS2;
            case 211:
                return PLMN_SPECIFIC_TS3;
            case 212:
                return PLMN_SPECIFIC_TS4;
            case 213:
                return PLMN_SPECIFIC_TS5;
            case 214:
                return PLMN_SPECIFIC_TS6;
            case 215:
                return PLMN_SPECIFIC_TS7;
            case 216:
                return PLMN_SPECIFIC_TS8;
            case 217:
                return PLMN_SPECIFIC_TS9;
            case 218:
                return PLMN_SPECIFIC_TS_A;
            case 219:
                return PLMN_SPECIFIC_TS_B;
            case 220:
                return PLMN_SPECIFIC_TS_C;
            case 221:
                return PLMN_SPECIFIC_TS_D;
            case 222:
                return PLMN_SPECIFIC_TS_E;
            case 223:
                return PLMN_SPECIFIC_TS_F;
            default:
                return null;

        }
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
