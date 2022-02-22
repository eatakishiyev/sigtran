/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum HighLayerCharacteristicsIdentification {

    TELEPHONY(1),
    FACSIMILE_GROUP2_3(4),
    FACSIMILE_GROUP4_CLASS1(33),
    FACSIMILE_SERVICE_GROUP_CLASS2_CLASS3(36),
    SYNTAX_BASED_VIDEO_TEX(50),
    INTERNATIONAL_VIDEO_TEX(51),
    TELEX_SERVICE(53),
    MESSAGE_HANDLING_SYSTEMS(56),
    OSI_APPLICATION(65),
    FTAM_APPLICATION(66),
    RESERVED_FOR_MAINTENANCE(94),
    RESERVED_FOR_MANAGEMENT(95),
    VIDEO_TELEPHONY(96),
    VIDEO_CONFERENCING(97),
    AUDIO_GRAPHIC_CONFERENCE(98),
    RESERVED_AUDIO_VISUAL1(99),
    RESERVED_AUDIO_VISUAL2(100),
    RESERVED_AUDIO_VISUAL3(101),
    RESERVED_AUDIO_VISUAL4(102),
    RESERVED_AUDIO_VISUAL5(103),
    MULTIMEDIA_SERVICES(104),
    UNKNOWN(-1);
    private final int value;

    private HighLayerCharacteristicsIdentification(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HighLayerCharacteristicsIdentification getInstance(int value) {
        switch (value) {
            case 1:
                return TELEPHONY;
            case 4:
                return FACSIMILE_GROUP2_3;
            case 33:
                return FACSIMILE_GROUP4_CLASS1;
            case 36:
                return FACSIMILE_SERVICE_GROUP_CLASS2_CLASS3;
            case 50:
                return SYNTAX_BASED_VIDEO_TEX;
            case 51:
                return INTERNATIONAL_VIDEO_TEX;
            case 53:
                return TELEX_SERVICE;
            case 56:
                return MESSAGE_HANDLING_SYSTEMS;
            case 65:
                return OSI_APPLICATION;
            case 66:
                return FTAM_APPLICATION;
            case 94:
                return RESERVED_FOR_MAINTENANCE;
            case 95:
                return RESERVED_FOR_MANAGEMENT;
            case 96:
                return VIDEO_TELEPHONY;
            case 97:
                return VIDEO_CONFERENCING;
            case 98:
                return AUDIO_GRAPHIC_CONFERENCE;
            case 99:
                return RESERVED_AUDIO_VISUAL1;
            case 100:
                return RESERVED_AUDIO_VISUAL2;
            case 101:
                return RESERVED_AUDIO_VISUAL3;
            case 102:
                return RESERVED_AUDIO_VISUAL4;
            case 103:
                return RESERVED_AUDIO_VISUAL5;
            case 104:
                return MULTIMEDIA_SERVICES;
            default:
                return UNKNOWN;

        }
    }
}
