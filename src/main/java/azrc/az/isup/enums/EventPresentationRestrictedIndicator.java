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
public enum EventPresentationRestrictedIndicator {
    NO_INDICATION(0),
    PRESENTATION_RESTRICTED(1);

    private final int value;

    private EventPresentationRestrictedIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static EventPresentationRestrictedIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return PRESENTATION_RESTRICTED;
            default:
                return NO_INDICATION;
        }
    }
}
