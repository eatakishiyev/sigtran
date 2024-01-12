/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * Ext-ProtocolId ::= ENUMERATED {
 * ets-300356 (1),
 * ...
 * }
 * -- exception handling:
 * -- For Ext-ExternalSignalInfo sequences containing this parameter with any
 * -- other value than the ones listed the receiver shall ignore the whole
 * -- Ext-ExternalSignalInfo sequence.
 *
 * @author eatakishiyev
 */
public enum ExtProtocolId {

    ETS300356(1),
    UNKNOWN(-1);

    private final int value;

    private ExtProtocolId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ExtProtocolId getInstance(int value) {
        switch (value) {
            case 1:
                return ETS300356;
            default:
                return UNKNOWN;
        }
    }
}
