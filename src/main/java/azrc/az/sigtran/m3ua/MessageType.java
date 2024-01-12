/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author root
 */
public enum MessageType {
    /*
     * Management Messages
     */

    ERR(0x00),
    NTFY(0x01),
    /*
     * Transfer Messages
     */
    Reserved(0x00),
    DATA(0x01),
    /*
     * SSNM Messages
     */
    DUNA(0x01),
    DAVA(0x02),
    DAUD(0x03),
    SCON(0x04),
    DUPU(0x05),
    DRST(0x06),
    /*
     * ASPSM Messages
     */
    ASPUP(0x01),
    ASPDN(0x02),
    BEAT(0x03),
    ASPUP_ACK(0x04),
    ASPDN_ACK(0x05),
    BEAT_ACK(0x06),
    /*
     * ASPTM Messages
     */
    ASPAC(0x01),
    ASPIA(0x02),
    ASPAC_ACK(0x03),
    ASPIA_ACK(0x04),
    /*
     * RKM Messages
     */
    REG_REQ(0x01),
    REG_RSP(0x02),
    DEREG_REQ(0x03),
    DEREG_RSP(0x04),
    UNKNOWN(-1);
    private int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getValue() {
        return this.type;
    }

    public static MessageType getInstance(int value, MessageClass messageClass) {
        switch (messageClass) {
            case ASPSM:
                switch (value) {
                    case 1:
                        return ASPUP;
                    case 2:
                        return ASPDN;
                    case 3:
                        return BEAT;
                    case 4:
                        return ASPUP_ACK;
                    case 5:
                        return ASPDN_ACK;
                    case 6:
                        return BEAT_ACK;
                }
            case ASPTM:
                switch (value) {
                    case 1:
                        return ASPAC;
                    case 2:
                        return ASPIA;
                    case 3:
                        return ASPAC_ACK;
                    case 4:
                        return ASPIA_ACK;
                }
            case MGMT:
                switch (value) {
                    case 0:
                        return ERR;
                    case 1:
                        return NTFY;
                }
            case TRANSFER_MESSAGE:
                switch (value) {
                    case 0:
                        return Reserved;
                    case 1:
                        return DATA;
                }
            case SSNM:
                switch (value) {
                    case 1:
                        return DUNA;
                    case 2:
                        return DAVA;
                    case 3:
                        return DAUD;
                    case 4:
                        return SCON;
                    case 5:
                        return DUPU;
                    case 6:
                        return DRST;
                }
            case RKM:
                switch (value) {
                    case 1:
                        return REG_REQ;
                    case 2:
                        return REG_RSP;
                    case 3:
                        return DEREG_REQ;
                    case 4:
                        return DEREG_RSP;
                }
        }
        return UNKNOWN;
    }
}
