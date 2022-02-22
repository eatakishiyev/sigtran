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
public class TAI {

    private String mcc;
    private String mnc;
    private int tac;

    public TAI() {
    }

    public TAI(String mcc, String mnc, int tac) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.tac = tac;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public void setTac(int tac) {
        this.tac = tac;
    }

    public int getTac() {
        return tac;
    }
    
    public void decode(byte[] octetData){
        int mccFirstDigit = octetData[0] & 0x0F;
        int mccSecondDigit = ((octetData[0] >> 4) & 0x0F);
        int mccThirdDigit = ((octetData[1] & 0x0F));

        this.mcc = String.format("%s%s%s", mccFirstDigit, mccSecondDigit, mccThirdDigit);

        int mncFirstDigit = octetData[2] & 0x0F;
        int mncSecondDigit = (octetData[2] >> 4) & 0x0F;
        int mncThirdDigit = (octetData[1] >> 4) & 0x0F;

        this.mnc = String.format("%s%s%s", mncFirstDigit, mncSecondDigit, mncThirdDigit == 0b1111 ? "" : mncThirdDigit);

        this.tac = octetData[3] & 0xFF;
        tac = (tac << 8) | (octetData[4] & 0xFF);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TAI[")
                .append("mcc = ").append(mcc)
                .append(" mnc = ").append(mnc)
                .append(" eci = ").append(tac)
                .append("]");
        return sb.toString();

    }

}
