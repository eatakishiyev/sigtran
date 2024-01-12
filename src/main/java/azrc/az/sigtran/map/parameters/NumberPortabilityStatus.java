/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * NumberPortabilityStatus ::= ENUMERATED {
 * notKnownToBePorted (0),
 * ownNumberPortedOut (1),
 * foreignNumberPortedToForeignNetwork (2),
 * ...,
 * ownNumberNotPortedOut (4),
 * foreignNumberPortedIn (5)
 * }
 * -- exception handling:
 * -- reception of other values than the ones listed the receiver shall ignore
 * the
 * -- whole NumberPortabilityStatus;
 * -- ownNumberNotPortedOut or foreignNumberPortedIn may only be included in Any
 * Time
 * -- Interrogation message.
 *
 * @author eatakishiyev
 */
public enum NumberPortabilityStatus {

    NOT_KNOWN_TO_BE_PORTED(0),
    OWN_NUMBER_PORTED_OUT(1),
    FOREIGN_NUMBER_PORTED_TO_FOREIGN_NETWORK(2),
    OWN_NUMBER_NOT_PORTED_OUT(4),
    FOREIGN_NUMBER_PORTED_IN(5);

    private final int value;

    private NumberPortabilityStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NumberPortabilityStatus getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_KNOWN_TO_BE_PORTED;
            case 1:
                return OWN_NUMBER_PORTED_OUT;
            case 2:
                return FOREIGN_NUMBER_PORTED_TO_FOREIGN_NETWORK;
            case 4:
                return OWN_NUMBER_PORTED_OUT;
            case 5:
                return FOREIGN_NUMBER_PORTED_IN;
            default:
                return NOT_KNOWN_TO_BE_PORTED;
        }
    }
}
