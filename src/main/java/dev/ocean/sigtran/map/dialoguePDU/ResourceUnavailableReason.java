/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

/**
 *
 * @author root
 */
public enum ResourceUnavailableReason {

    SHORT_TERM_RESOURCE_LIMITATION(0),
    LONG_TERM_RESOURCE_LIMITATION(1),
    UNKNOWN(-1);
    private int value;

    private ResourceUnavailableReason(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ResourceUnavailableReason getInstance(int value) {
        switch (value) {
            case 0:
                return SHORT_TERM_RESOURCE_LIMITATION;
            case 1:
                return LONG_TERM_RESOURCE_LIMITATION;
            default:
                return UNKNOWN;
        }
    }
}
