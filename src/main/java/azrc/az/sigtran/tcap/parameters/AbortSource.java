/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public enum AbortSource {

    DIALOGUE_SERVICE_USER(0),
    DIALOGUE_SERVICE_PROVIDER(1),
    UNKNOWN(-1);
    private int value;
    public static final int ABORT_SOURCE_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean ABORT_SOURCE_PC = true;
    public static final int ABORT_SOURCE_TAG = 0x00;

    AbortSource(int type) {
        this.value = type;
    }

    public int value() {
        return value;
    }

    public static AbortSource getInstance(int value) {
        switch (value) {
            case 0:
                return DIALOGUE_SERVICE_USER;
            case 1:
                return DIALOGUE_SERVICE_PROVIDER;
            default:
                return UNKNOWN;
        }
    }
}
