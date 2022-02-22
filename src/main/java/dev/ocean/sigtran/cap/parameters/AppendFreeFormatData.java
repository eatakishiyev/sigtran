/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum AppendFreeFormatData {

    OVERWRITE(0),
    APPEND(1);

    private int value;

    private AppendFreeFormatData(int value) {
        this.value = value;
    }

    public static AppendFreeFormatData getInstance(int value) {
        switch (value) {
            case 0:
                return OVERWRITE;
            case 1:
                return APPEND;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
