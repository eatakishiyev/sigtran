/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.address;

/**
 *
 * @author root
 */
public enum SubSystemNumber {

    UNKNOWN(-1),
    SSN_NOT_USED(0),
    SCCP_MANAGEMENT(1),
    HLR(6),
    VLR(7),
    MSC(8),
    EIR(9),
    AUC(10),
    RESERVED_108(108),
    RANAP(142),
    RNSAP(143),
    GMLC(145),
    CAP(146),
    GSM_SCF(147),
    SIWF(148),
    SGSN(149),
    GGSN(150),
    PCAP(249),
    BSSAP_BSC(250),
    BSSAP_MSC(251),
    BSSAP_SMLC(252),
    BSS_OM(253),
    A_INTERFACE(254);
    /*
     * 15 to 31 Reserved for international use
     */
    /*
     * 32 to 254 Reserved for National networks
     */
    /*
     * 255 Reserved for expansion of national and international SSN
     */
    private  final int value;

    private SubSystemNumber(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static SubSystemNumber getInstance(int value) {
        switch (value) {
            case 0:
                return SSN_NOT_USED;
            case 1:
                return SCCP_MANAGEMENT;
            case 6:
                return HLR;
            case 7:
                return VLR;
            case 8:
                return MSC;
            case 9:
                return EIR;
            case 10:
                return AUC;
            case 108:
                return RESERVED_108;
            case 142:
                return RANAP;
            case 143:
                return RNSAP;
            case 145:
                return GMLC;
            case 146:
                return CAP;
            case 147:
                return GSM_SCF;
            case 148:
                return SIWF;
            case 149:
                return SGSN;
            case 150:
                return GGSN;
            case 249:
                return PCAP;
            case 250:
                return BSSAP_BSC;
            case 251:
                return BSSAP_MSC;
            case 252:
                return BSSAP_SMLC;
            case 253:
                return BSS_OM;
            case 254:
                return A_INTERFACE;
            default:
                return UNKNOWN;

        }
    }
}
