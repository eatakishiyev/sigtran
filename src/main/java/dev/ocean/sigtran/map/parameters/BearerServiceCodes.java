/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum BearerServiceCodes {

    ALL_BEARER_SERVICES(0),
    ALL_DATA_CDA_SERVICES(16),
    DATA_CDA300bps(17),
    DATA_CDA1200bps(18),
    DATA_CDA120075bps(19),
    DATA_CDA2400bps(20),
    DATA_CDA4800bps(21),
    DATA_CDA9600bps(22),
    GENERAL_DATA_CDA(23),
    ALL_DATA_CDS_SERVICES(24),
    DATA_CDS1200bps(26),
    DATA_CDS2400bps(28),
    DATA_CDS4800bps(29),
    DATA_CDS9600bps(30),
    GENERAL_DATA_CDS(31),
    ALL_PAD_ACCESS_CA_SERVICES(32),
    PAD_ACCESS_CA300bps(33),
    PAD_ACCESS_CA1200bps(34),
    PAD_ACCESS_CA120075bps(35),
    PAD_ACCESS_CA2400bps(36),
    PAD_ACCESS_CA4800bps(37),
    PAD_ACCESS_CA9600bps(38),
    GENERAL_PAD_ACCESS_CA(39),
    ALL_DATA_PDS_SERVICES(40),
    DATA_PDS2400bps(44),
    DATA_PDS4800bps(45),
    DATA_PDS9600bps(46),
    GENERAL_DATA_PDS(47),
    ALL_ALTERNATE_SPEECH_DATA_CDA(48),
    ALL_ATERNATE_SPEECH_DATA_CDS(56),
    ALL_SPEECH_FOLLOWED_BY_DATA_CDA(64),
    ALL_SPEECH_FOLLOWED_BY_DATA_CDS(72),
    ALL_DATA_CIRCUIT_ASYNCHRONOUS(80),
    ALL_ASYNCHRONOUS_SERVICES(96),
    ALL_DATA_CIRCUIT_SYNCHRONOUS(88),
    ALL_SYNCHRONOUS_SERVICES(104),
    ALL_PLMN_SPECIFIC_BS(208),
    PLMN_SPECIFIC_BS1(209),
    PLMN_SPECIFIC_BS2(210),
    PLMN_SPECIFIC_BS3(211),
    PLMN_SPECIFIC_BS4(212),
    PLMN_SPECIFIC_BS5(213),
    PLMN_SPECIFIC_BS6(214),
    PLMN_SPECIFIC_BS7(215),
    PLMN_SPECIFIC_BS8(216),
    PLMN_SPECIFIC_BS9(217),
    PLMN_SPECIFIC_BSA(218),
    PLMN_SPECIFIC_BSB(219),
    PLMN_SPECIFIC_BSC(220),
    PLMN_SPECIFIC_BSD(221),
    PLMN_SPECIFIC_BSE(222),
    PLMN_SPECIFIC_BSF(223);

    private final int value;

    private BearerServiceCodes(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static BearerServiceCodes getInstance(int value) {
        switch (value) {
            case 0:
                return ALL_BEARER_SERVICES;
            case 16:
                return ALL_DATA_CDA_SERVICES;
            case 17:
                return DATA_CDA300bps;
            case 18:
                return DATA_CDA1200bps;
            case 19:
                return DATA_CDA120075bps;
            case 20:
                return DATA_CDA2400bps;
            case 21:
                return DATA_CDA4800bps;
            case 22:
                return DATA_CDA9600bps;
            case 23:
                return GENERAL_DATA_CDA;
            case 24:
                return ALL_DATA_CDS_SERVICES;
            case 26:
                return DATA_CDS1200bps;
            case 28:
                return DATA_CDS2400bps;
            case 29:
                return DATA_CDS4800bps;
            case 30:
                return DATA_CDS9600bps;
            case 31:
                return GENERAL_DATA_CDS;
            case 32:
                return ALL_PAD_ACCESS_CA_SERVICES;
            case 33:
                return PAD_ACCESS_CA300bps;
            case 34:
                return PAD_ACCESS_CA1200bps;
            case 35:
                return PAD_ACCESS_CA120075bps;
            case 36:
                return PAD_ACCESS_CA2400bps;
            case 37:
                return PAD_ACCESS_CA4800bps;
            case 38:
                return PAD_ACCESS_CA9600bps;
            case 39:
                return GENERAL_PAD_ACCESS_CA;
            case 40:
                return ALL_DATA_PDS_SERVICES;
            case 44:
                return DATA_PDS2400bps;
            case 45:
                return DATA_PDS4800bps;
            case 46:
                return DATA_PDS9600bps;
            case 47:
                return GENERAL_DATA_PDS;
            case 48:
                return ALL_ALTERNATE_SPEECH_DATA_CDA;
            case 56:
                return ALL_ATERNATE_SPEECH_DATA_CDS;
            case 64:
                return ALL_SPEECH_FOLLOWED_BY_DATA_CDA;
            case 72:
                return ALL_SPEECH_FOLLOWED_BY_DATA_CDS;
            case 80:
                return ALL_DATA_CIRCUIT_ASYNCHRONOUS;
            case 96:
                return ALL_ASYNCHRONOUS_SERVICES;
            case 88:
                return ALL_DATA_CIRCUIT_SYNCHRONOUS;
            case 104:
                return ALL_SYNCHRONOUS_SERVICES;
            case 208:
                return ALL_PLMN_SPECIFIC_BS;
            case 209:
                return PLMN_SPECIFIC_BS1;
            case 210:
                return PLMN_SPECIFIC_BS2;
            case 211:
                return PLMN_SPECIFIC_BS3;
            case 212:
                return PLMN_SPECIFIC_BS4;
            case 213:
                return PLMN_SPECIFIC_BS5;
            case 214:
                return PLMN_SPECIFIC_BS6;
            case 215:
                return PLMN_SPECIFIC_BS7;
            case 216:
                return PLMN_SPECIFIC_BS8;
            case 217:
                return PLMN_SPECIFIC_BS9;
            case 218:
                return PLMN_SPECIFIC_BSA;
            case 219:
                return PLMN_SPECIFIC_BSB;
            case 220:
                return PLMN_SPECIFIC_BSC;
            case 221:
                return PLMN_SPECIFIC_BSD;
            case 222:
                return PLMN_SPECIFIC_BSE;
            case 223:
                return PLMN_SPECIFIC_BSF;
            default:
                return null;
        }
    }
}
