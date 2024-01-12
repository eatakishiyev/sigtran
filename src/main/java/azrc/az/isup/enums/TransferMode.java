/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum TransferMode {
    CIRCUIT_MODE(0),
    PACKET_MODE(2),
    UNKNOWN(-1);

    private final int value;

    private TransferMode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static TransferMode getInstance(int value) {
        switch (value) {
            case 0:
                return CIRCUIT_MODE;
            case 2:
                return PACKET_MODE;
            default:
                return UNKNOWN;
        }
    }
}
