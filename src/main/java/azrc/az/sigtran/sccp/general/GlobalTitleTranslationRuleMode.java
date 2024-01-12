/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

/**
 *
 * @author eatakishiyev
 */
public enum GlobalTitleTranslationRuleMode {

    SOLITARY(0),
    DUPLICATED_DOMINANT(1),
    DUPLICATED_DYNAMIC_LOADSHARE(2);
    private int value;

    private GlobalTitleTranslationRuleMode(int value) {
        this.value = value;
    }

    public static GlobalTitleTranslationRuleMode getInstance(int value) {
        switch (value) {
            case 0:
                return SOLITARY;
            case 1:
                return DUPLICATED_DOMINANT;
            case 2:
                return DUPLICATED_DYNAMIC_LOADSHARE;
            default:
                return SOLITARY;
        }
    }

    public int value() {
        return this.value;
    }
}
