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
public enum NumberIncompleteIndicator {

    NUMBER_COMPLETE,
    NUMBER_INCOMPLETE;

    public static NumberIncompleteIndicator valueOf(int value) {
        return value == 0 ? NUMBER_COMPLETE : NUMBER_INCOMPLETE;
    }
}
