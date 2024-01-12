/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum NumberQualifierIndicator {

    DIALED_DIGITS(0),
    ADDITIONAL_CALLED_NUMBER(1),
    FAILED_NETWORK_SCREENING(2),
    NOT_SCREENED(3),
    REDIRECTING_TERMINATING_NUMBER(4),
    ADDITIONAL_CONNECTED_NUMBER(5),
    ADDITIONAL_CALLING_PARTY_NUMBER(6),
    ADDITIONAL_ORIGINAL_CALLED_NUMBER(7),
    ADDITIONAL_REDIRECTING_NUMBER(8),
    ADDITIONAL_REDIRECTION_NUMBER(9),
    CALLED_FREEPHONE_NUMBER(10);
    private final int value;

    private NumberQualifierIndicator(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NumberQualifierIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return DIALED_DIGITS;
            case 1:
                return ADDITIONAL_CALLED_NUMBER;
            case 2:
                return FAILED_NETWORK_SCREENING;
            case 3:
                return NOT_SCREENED;
            case 4:
                return REDIRECTING_TERMINATING_NUMBER;
            case 5:
                return ADDITIONAL_CONNECTED_NUMBER;
            case 6:
                return ADDITIONAL_CALLING_PARTY_NUMBER;
            case 7:
                return ADDITIONAL_ORIGINAL_CALLED_NUMBER;
            case 8:
                return ADDITIONAL_REDIRECTING_NUMBER;
            case 9:
                return ADDITIONAL_REDIRECTION_NUMBER;
            case 10:
                return CALLED_FREEPHONE_NUMBER;
            default:
                return null;
        }
    }

}
