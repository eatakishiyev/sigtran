/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages;

/**
 *
 * @author eatakishiyev
 */
public enum ProtocolClassEnum {

    /**
     * Connectionless message with no sequence control
     */
    CLASS0(0),
    /**
     * Connectionless message with sequence control
     */
    CLASS1(1),
    /**
     * Connection oriented message with no sequence control
     */
    CLASS2(2),
    /**
     * Connection oriented message with sequence control
     */
    CLASS3(3),
    UNKNOWN(-1);
    private final int value;

    private ProtocolClassEnum(int value) {
        this.value = value;
    }

    public static ProtocolClassEnum getInstance(int value) {
        switch (value) {
            case 0:
                return CLASS0;
            case 1:
                return CLASS1;
            case 2:
                return CLASS2;
            case 3:
                return CLASS3;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
