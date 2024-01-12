/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages;

/**
 *
 * @author eatakishiyev
 */
public enum MessageHandling {

    NO_SPECIAL_OPTIONS(0),
    RETURN_MESSAGE_ON_ERROR(8);
    private final int value;

    private MessageHandling(int value) {
        this.value = value;
    }

    public static MessageHandling getInstance(int value) {
        switch (value) {
            case 0:
                return NO_SPECIAL_OPTIONS;
            case 8:
                return RETURN_MESSAGE_ON_ERROR;
            default:
                return NO_SPECIAL_OPTIONS;

        }
    }

    public int value() {
        return this.value;
    }
}
