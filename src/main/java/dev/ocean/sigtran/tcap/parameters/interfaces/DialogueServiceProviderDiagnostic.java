/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters.interfaces;

/**
 *
 * @author root
 */
public enum DialogueServiceProviderDiagnostic {

    NULL(0),
    NO_REASON_GIVEN(1),
    NO_COMMON_DIALOGUE_PORTION(2),
    UNKNOWN(-1);
    private int type;

    private DialogueServiceProviderDiagnostic(int type) {
        this.type = type;
    }

    public static DialogueServiceProviderDiagnostic getInstance(int i) {
        switch (i) {
            case 0:
                return NULL;
            case 1:
                return NO_REASON_GIVEN;
            case 2:
                return NO_COMMON_DIALOGUE_PORTION;
            default:
                return UNKNOWN;
        }

    }

    public int value() {
        return this.type;
    }
}
