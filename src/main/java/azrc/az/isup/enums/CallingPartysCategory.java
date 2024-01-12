/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum CallingPartysCategory {

    CALLING_PARTY_CATEGORY_UNKNOWN(0),
    OPERATOR_LANG_FRENCH(1),
    OPERATOR_LANG_ENGLISH(2),
    OPERATOR_LANG_GERMAN(3),
    OPERATOR_LANG_RUSSIAN(4),
    OPERATOR_LANG_SPANISH(5),
    NATIONAL_OPERATOR(9),
    ORDINARY_CALLING_SUBSCRIBER(10),
    CALLING_SUBSCRIBER_WITH_PRIORITY(11),
    DATA_CALL(12),
    TEST_CALL(13),
    PAYPHONE(15),
    UNKNOWN(-1);
    private final int value;

    private CallingPartysCategory(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static CallingPartysCategory getInstance(int value) {
        switch (value) {
            case 0:
                return CALLING_PARTY_CATEGORY_UNKNOWN;
            case 1:
                return OPERATOR_LANG_FRENCH;
            case 2:
                return OPERATOR_LANG_ENGLISH;
            case 3:
                return OPERATOR_LANG_GERMAN;
            case 4:
                return OPERATOR_LANG_RUSSIAN;
            case 5:
                return OPERATOR_LANG_SPANISH;
            case 9:
                return NATIONAL_OPERATOR;
            case 10:
                return ORDINARY_CALLING_SUBSCRIBER;
            case 11:
                return CALLING_SUBSCRIBER_WITH_PRIORITY;
            case 12:
                return DATA_CALL;
            case 13:
                return TEST_CALL;
            case 15:
                return PAYPHONE;
            default:
                return UNKNOWN;
        }
    }
}
