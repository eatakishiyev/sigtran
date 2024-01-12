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
public enum LocationPresentationRestrictedIndicator {
    PRESENTATION_ALLOWED(0),
    PRESENTATION_RESTRICTED(1),
    LOCATION_NOT_AVAILABLE(2);

    private final int value;

    private LocationPresentationRestrictedIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LocationPresentationRestrictedIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return PRESENTATION_ALLOWED;
            case 1:
                return PRESENTATION_RESTRICTED;
            case 2:
                return LOCATION_NOT_AVAILABLE;
            default:
                return LOCATION_NOT_AVAILABLE;
        }
    }
}
