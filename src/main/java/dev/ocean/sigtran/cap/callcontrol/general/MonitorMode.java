/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.general;

/**
 *
 * @author eatakishiyev
 */
public enum MonitorMode {

    INTERRUPTED(0),
    NOTIFY_AND_CONTINUE(1),
    TRANSPARENT(2);

    private int value;

    private MonitorMode(int value) {
        this.value = value;
    }

    public static MonitorMode getInstance(int value) {
        switch (value) {
            case 0:
                return INTERRUPTED;
            case 1:
                return NOTIFY_AND_CONTINUE;
            case 2:
                return TRANSPARENT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
