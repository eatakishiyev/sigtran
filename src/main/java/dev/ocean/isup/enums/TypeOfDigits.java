/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum TypeOfDigits {

    RESERVED_FOR_ACCOUNT_CODE,
    RESERVED_FOR_AUTHORISATION_CODE,
    RESERVED_FOR_PRIVATE_NETWORKING_TRAVELLING_CLASS_MARK,
    RESERVED_FOR_BUSINESS_COMMUNICATION_GROUP_IDENTITY;

    public static TypeOfDigits valueOf(int value) {
        switch (value) {
            case 0:
                return RESERVED_FOR_ACCOUNT_CODE;
            case 1:
                return RESERVED_FOR_AUTHORISATION_CODE;
            case 2:
                return RESERVED_FOR_PRIVATE_NETWORKING_TRAVELLING_CLASS_MARK;
            case 3:
                return RESERVED_FOR_BUSINESS_COMMUNICATION_GROUP_IDENTITY;
            default:
                return RESERVED_FOR_ACCOUNT_CODE;
        }
    }
}
