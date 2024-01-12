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
public enum NotificationSubscriptionOptions {
    UNKNOWN(0b00000000),
    PRESENTATION_NOT_ALLOWED(0b00000001),
    PRESENTATION_ALLOWED_WITH_REDIRECTION_NUMBER(0b00000010),
    PRESENTATION_ALLOWED_WITHOUT_REDIRECTION_NUMBER(0b00000011);

    private final int value;

    private NotificationSubscriptionOptions(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NotificationSubscriptionOptions getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return UNKNOWN;
            case 0b00000001:
                return PRESENTATION_NOT_ALLOWED;
            case 0b00000010:
                return PRESENTATION_ALLOWED_WITH_REDIRECTION_NUMBER;
            case 0b00000011:
                return PRESENTATION_ALLOWED_WITHOUT_REDIRECTION_NUMBER;
            default:
                return UNKNOWN;
        }
    }
}
