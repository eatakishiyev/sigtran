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
public enum IncomingEchoControlDeviceRequestIndicator {
    NO_INFORMATION(0),
    IECD_ACTIVATION_REQUEST(1),
    IECD_DEACTIVATION_REQUEST(2),
    UNKNOWN(-1);

    private final int value;

    private IncomingEchoControlDeviceRequestIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static IncomingEchoControlDeviceRequestIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INFORMATION;
            case 1:
                return IECD_ACTIVATION_REQUEST;
            case 2:
                return IECD_DEACTIVATION_REQUEST;
            default:
                return UNKNOWN;
        }
    }
}
