/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum LocationScreeningIndicator {

    USER_PROVIDED_NOT_VERIFIED(0),
    USER_PROVIDED_VERIFIED_PASSED(1),
    USER_PROVIDED_VERIFIED_FAILED(2),
    NETWORK_PROVIDED(3),
    UNKNOWN(-1);
    private int value;

    private LocationScreeningIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LocationScreeningIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return USER_PROVIDED_NOT_VERIFIED;
            case 1:
                return USER_PROVIDED_VERIFIED_PASSED;
            case 2:
                return USER_PROVIDED_VERIFIED_FAILED;
            case 3:
                return NETWORK_PROVIDED;
            default:
                return UNKNOWN;
        }
    }
}
