/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum ExtendedAudovisiulCharacteristicsIdentification {

    CAPABILITY_SET_OF_INITIAL_CHANNEL(1),
    CAPABILITY_SET_OF_SUBSEQUENT_CHANNEL(2),
    CAPABILITY_SET_OF_INITIAL_CHANNEL_31KHZ(33),
    UNKNOWN(-1);

    private final int value;

    private ExtendedAudovisiulCharacteristicsIdentification(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ExtendedAudovisiulCharacteristicsIdentification getInstance(int value) {
        switch (value) {
            case 1:
                return CAPABILITY_SET_OF_INITIAL_CHANNEL;
            case 2:
                return CAPABILITY_SET_OF_SUBSEQUENT_CHANNEL;
            case 33:
                return CAPABILITY_SET_OF_INITIAL_CHANNEL_31KHZ;
            default:
                return UNKNOWN;
        }
    }
}
