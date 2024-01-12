/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public class ECGI {

    private String mcc;
    private String mnc;
    private int eci;

    public ECGI() {
    }

    public ECGI(String mcc, String mnc, int eci) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.eci = eci;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ECGI[")
        .append("mcc = ").append(mcc)
                .append(" mnc = ").append(mnc)
                .append(" eci = ").append(eci)
                .append("]");
        return sb.toString();
    }

    public void decode(byte[] octetData) {
        int mccFirstDigit = octetData[0] & 0x0F;
        int mccSecondDigit = ((octetData[0] >> 4) & 0x0F);
        int mccThirdDigit = ((octetData[1] & 0x0F));

        this.mcc = String.format("%s%s%s", mccFirstDigit, mccSecondDigit, mccThirdDigit);

        int mncFirstDigit = octetData[2] & 0x0F;
        int mncSecondDigit = (octetData[2] >> 4) & 0x0F;
        int mncThirdDigit = (octetData[1] >> 4) & 0x0F;

        this.mnc = String.format("%s%s%s", mncFirstDigit, mncSecondDigit, mncThirdDigit == 0b1111 ? "" : mncThirdDigit);

        this.eci = octetData[3] & 0x0F;
        int spare = (octetData[3] >> 4) & 0x0F;
        eci = (eci << 8) | (octetData[4] & 0xFF);
        eci = (eci << 8) | (octetData[5] & 0xFF);
        eci = (eci << 8) | (octetData[6] & 0xFF);

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

    public int getEci() {
        return eci;
    }

    public void setEci(int eci) {
        this.eci = eci;
    }

}
