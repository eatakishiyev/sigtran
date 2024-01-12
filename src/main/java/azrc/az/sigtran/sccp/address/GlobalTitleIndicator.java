/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.address;

/**
 *
 * @author eatakishiyev
 */
public enum GlobalTitleIndicator {

    /**
     * UNKNOWN VALUE
     */
    UNKNOWN((byte)-1),
    /**
     * NO GLOBAL TITLE INCLUDED
     */
    NO_GLOBAL_TITLE_INCLUDED((byte)0),
    /**
     * ITU-T Q.713 3.4.2.3.1 Global title indicator = 0001
     */
    NATURE_OF_ADDRESS_IND_ONLY((byte)1),//0001
    /**
     * ITU-T Q.713 3.4.2.3.2 Global title indicator = 0010
     */
    TRANSLATION_TYPE_ONLY((byte)2),//0010
    /**
     * ITU-T Q.713 3.4.2.3.3 Global title indicator = 0011
     */
    TRANSLATION_TYPE_NP_ENC((byte)3),//0011
    /**
     * ITU-T Q.713 3.4.2.3.4 Global title indicator = 0100
     */
    TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND((byte)4);//0100
    private final byte value;

    GlobalTitleIndicator(byte value) {
        this.value = value;
    }

    public static GlobalTitleIndicator getInstance(byte value) {
        switch (value) {
            case 0:
                return NO_GLOBAL_TITLE_INCLUDED;
            case 1:
                return NATURE_OF_ADDRESS_IND_ONLY;
            case 2:
                return TRANSLATION_TYPE_ONLY;
            case 3:
                return TRANSLATION_TYPE_NP_ENC;
            case 4:
                return TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND;
            default:
                return UNKNOWN;
        }
    }

    public byte value() {
        return this.value;
    }
}
