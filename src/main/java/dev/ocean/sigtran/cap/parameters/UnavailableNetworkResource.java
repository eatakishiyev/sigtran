/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum UnavailableNetworkResource {

    UNAVAILABLE_RESOURCES(0),
    COMPONENT_FAILURE(1),
    BASIC_CALL_PROCESSING_EXCEPTION(2),
    RESOURCE_STATUS_FAILURE(3),
    END_USER_FAILURE(4);

    private int value;

    private UnavailableNetworkResource(int value) {
        this.value = value;
    }

    public static UnavailableNetworkResource getInstance(int value) {
        switch (value) {
            case 0:
                return UNAVAILABLE_RESOURCES;
            case 1:
                return COMPONENT_FAILURE;
            case 2:
                return BASIC_CALL_PROCESSING_EXCEPTION;
            case 3:
                return RESOURCE_STATUS_FAILURE;
            case 4:
                return END_USER_FAILURE;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
