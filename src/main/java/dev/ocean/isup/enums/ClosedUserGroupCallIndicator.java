/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum ClosedUserGroupCallIndicator {
    NO_CUG_CALL(0),
    CUG_CALL_OUTGOING_ACCESS_ALLOWED(2),
    CUG_CALL_OUTGOING_ACCESS_NOT_ALLOWED(3);

    private final int value;

    private ClosedUserGroupCallIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
    
    public static ClosedUserGroupCallIndicator getInstance(int value){
        switch(value){
            case 0:
                return NO_CUG_CALL;
            case 2:
                return CUG_CALL_OUTGOING_ACCESS_ALLOWED;
            case 3:
                return CUG_CALL_OUTGOING_ACCESS_NOT_ALLOWED;
            default:
                return NO_CUG_CALL;
        }
    }
}
