/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 * MM-Code ::= OCTET STRING (SIZE (1))
 * -- This type is used to indicate a Mobility Management event.
 * -- Actions for the following MM-Code values are defined in CAMEL Phase 4:
 * --
 * -- CS domain MM events:
 * -- Location-update-in-same-VLR MM-Code ::= '00000000'B
 * -- Location-update-to-other-VLR MM-Code ::= '00000001'B
 * -- IMSI-Attach MM-Code ::= '00000010'B
 * -- MS-initiated-IMSI-Detach MM-Code ::= '00000011'B
 * -- Network-initiated-IMSI-Detach MM-Code ::= '00000100'B
 * --
 * -- PS domain MM events:
 * -- Routeing-Area-update-in-same-SGSN MM-Code ::= '10000000'B
 * -- Routeing-Area-update-to-other-SGSN-update-from-new-SGSN
 * -- MM-Code ::= '10000001'B
 * -- Routeing-Area-update-to-other-SGSN-disconnect-by-detach
 * -- MM-Code ::= '10000010'B
 * -- GPRS-Attach MM-Code ::= '10000011'B
 * -- MS-initiated-GPRS-Detach MM-Code ::= '10000100'B
 * -- Network-initiated-GPRS-Detach MM-Code ::= '10000101'B
 * -- Network-initiated-transfer-to-MS-not-reachable-for-paging
 * -- MM-Code ::= '10000110'B
 * --
 * -- If the MSC receives any other MM-code than the ones listed above for the
 * -- CS domain, then the MSC shall ignore that MM-code.
 * -- If the SGSN receives any other MM-code than the ones listed above for the
 * -- PS domain, then the SGSN shall ignore that MM-code.
 *
 * @author eatakishiyev
 */
public enum MMCode {

    LOCATION_UPDATE_IN_SAME_VLR(0),
    LOCATION_UPDATE_TO_OTHER_VLR(1),
    IMSI_ATTACH(2),
    MS_INITIATED_IMSI_DETACH(3),
    NETWORK_INITIATED_IMSI_DETACH(4),
    ROUTEING_AREA_UPDATE_IN_SAME_SGSN(128),
    ROUTEING_AREA_UPDATE_TO_OTHER_SGSN_UPDATE_FROM_NEW_SGSN(129),
    ROUTEING_AREA_UPDATE_TO_OTHER_SGSN_DISCONNECT_BY_DETACH(130),
    GPRS_ATTACH(131),
    MS_INITIATED_GPRS_DETACH(132),
    NETWORK_INITIATED_GPRS_DETACH(133),
    NETWORK_INITIATED_TRANSFER_TO_MS_NOT_REACHABLE_FOR_PAGING(134);

    private final int value;

    private MMCode(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static MMCode getInstance(int value) {
        switch (value) {
            case 0:
                return LOCATION_UPDATE_IN_SAME_VLR;
            case 1:
                return LOCATION_UPDATE_TO_OTHER_VLR;
            case 2:
                return IMSI_ATTACH;
            case 3:
                return MS_INITIATED_IMSI_DETACH;
            case 4:
                return NETWORK_INITIATED_IMSI_DETACH;
            case 128:
                return ROUTEING_AREA_UPDATE_IN_SAME_SGSN;
            case 129:
                return ROUTEING_AREA_UPDATE_TO_OTHER_SGSN_UPDATE_FROM_NEW_SGSN;
            case 130:
                return ROUTEING_AREA_UPDATE_TO_OTHER_SGSN_DISCONNECT_BY_DETACH;
            case 131:
                return GPRS_ATTACH;
            case 132:
                return MS_INITIATED_GPRS_DETACH;
            case 133:
                return NETWORK_INITIATED_GPRS_DETACH;
            case 134:
                return NETWORK_INITIATED_TRANSFER_TO_MS_NOT_REACHABLE_FOR_PAGING;
            default:
                return null;
        }
    }
}
