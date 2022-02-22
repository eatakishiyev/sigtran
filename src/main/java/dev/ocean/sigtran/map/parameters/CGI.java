/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.nio.ByteBuffer;
import org.mobicents.protocols.asn.AsnException;

/**
 *
 * @author eatakishiyev
 */
public class CGI {

    private String mcc;
    private String mnc;
    private int lac;
    private int cellId;
    private byte[] data;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CGI[")
                .append("MCC = ").append(mcc)
                .append(";MNC = ").append(mnc)
                .append(";LAC = ").append(lac)
                .append(";CELLID = ").append(cellId)
                .append("]");
        return sb.toString();
    }

    public CGI() {
    }

    public CGI(byte[] data) throws AsnException {
        this.decode(data);
        this.data = data;
    }

    /**
     *
     * @param mcc - Mobile Country Code
     * @param mnc - Mobile Network Code
     * @param lac - Location Area Code
     * @param cellId - Cell Identity(CI) 3GPP TS 24.008 or Service Area Code
     * (SAC) 3GPP TS 23.003
     */
    public CGI(String mcc, String mnc, int lac, int cellId) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cellId = cellId;
    }

    public byte[] encode() {
        ByteBuffer bb = ByteBuffer.allocate(7);

        int octet = nextMCCDigit(0);

        int digit = nextMCCDigit(1);
        octet = octet | ((digit & 0xFF) << 4);

        bb.put((byte) octet);
        octet = 0;

        octet = nextMCCDigit(2);

        digit = nextMNCDigit(3);
        octet = octet | (((digit == -1 ? 0b1111 : digit) & 0xFF) << 4);

        bb.put((byte) octet);
        octet = 0;

        octet = nextMNCDigit(0);

        digit = nextMNCDigit(1);
        octet = octet | (digit << 4);
        bb.put((byte) octet);
        octet = 0;

        bb.put((byte) (lac >> 8));
        bb.put((byte) (lac & 0xFF));

        bb.put((byte) (cellId >> 8));
        bb.put((byte) (cellId & 0xFF));

        bb.flip();
        this.data = bb.array();
        return this.data;
    }

    public final void decode(byte[] octetData) throws AsnException {
        if (octetData.length != 7) {
            throw new AsnException("Expected 7 byte length octet string. Found " + octetData.length);
        }

        int mccFirstDigit = octetData[0] & 0x0F;
        int mccSecondDigit = ((octetData[0] >> 4) & 0x0F);
        int mccThirdDigit = ((octetData[1] & 0x0F));

        this.mcc = String.format("%s%s%s", mccFirstDigit, mccSecondDigit, mccThirdDigit);

        int mncFirstDigit = octetData[2] & 0x0F;
        int mncSecondDigit = (octetData[2] >> 4) & 0x0F;
        int mncThirdDigit = (octetData[1] >> 4) & 0x0F;

        this.mnc = String.format("%s%s%s", mncFirstDigit, mncSecondDigit, mncThirdDigit == 0b1111 ? "" : mncThirdDigit);

        this.lac = octetData[3] & 0xff;
        this.lac = (this.lac << 8) | (octetData[4] & 0xFF);

        this.cellId = octetData[5] & 0xff;
        this.cellId = (this.cellId << 8) | (octetData[6] & 0xFF);

        this.data = octetData;

    }

    private int nextMCCDigit(int i) {
        String strToken = String.valueOf(mcc.charAt(i));
        return Integer.parseInt(strToken);
    }

    private int nextMNCDigit(int i) {
        try {
            String strToken = String.valueOf(mnc.charAt(i));
            return Integer.parseInt(strToken);
        } catch (StringIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    /**
     * @return the mcc
     */
    public String getMcc() {
        return mcc;
    }

    /**
     * @param mcc the mcc to set
     */
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    /**
     * @return the mnc
     */
    public String getMnc() {
        return mnc;
    }

    /**
     * @param mnc the mnc to set
     */
    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    /**
     * @return the lac
     */
    public int getLac() {
        return lac;
    }

    /**
     * @param lac the lac to set
     */
    public void setLac(int lac) {
        this.lac = lac;
    }

    /**
     * @return the cellId
     */
    public int getCellId() {
        return cellId;
    }

    /**
     * @param cellId the cellId to set
     */
    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public byte[] getData() {
        return this.data;
    }

}
