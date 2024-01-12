/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters.interfaces;

/**
 *
 * @author root
 */
public enum DialogueServiceUserDiagnostic {

    NULL(0),
    NO_REASON_GIVEN(1),
    APPLICATION_CONTEXT_NAME_NOT_SUPPORTED(2);
    private final int type;

    private DialogueServiceUserDiagnostic(int type) {
        this.type = type;
    }

    public static DialogueServiceUserDiagnostic getInstance(int i) {
        switch (i) {
            case 0:
                return NULL;
            case 1:
                return NO_REASON_GIVEN;
            case 2:
                return APPLICATION_CONTEXT_NAME_NOT_SUPPORTED;
            default:
                return NULL;
        }
    }

    public int value() {
        return this.type;
    }
}
