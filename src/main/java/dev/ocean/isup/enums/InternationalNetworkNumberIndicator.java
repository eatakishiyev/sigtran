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
public enum InternationalNetworkNumberIndicator {

    ROUTING_TO_INTERNAL_NETWORK_NUMBER_ALLOWED,
    ROUTING_TO_INTERNAL_NETWORK_NUMBER_NOT_ALLOWED;

    public static InternationalNetworkNumberIndicator valueOf(int value) {
        return value == 0 ? ROUTING_TO_INTERNAL_NETWORK_NUMBER_ALLOWED : ROUTING_TO_INTERNAL_NETWORK_NUMBER_NOT_ALLOWED;
    }
}
