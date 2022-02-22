/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

/**
 * This element contains one of the problem codes that relate only to the invoke
 * component type.
 *
 * @author root
 */
public enum InvokeProblem {
    /**
     * The invoke ID is that of a previously invoked operation which has not
     * been completed. This code is generated by the TC-User.
     */
    DUPLICATE_INVOKE_ID(0),
    /**
     * The operation code is not one of those agreed by the two TC-User.
     */
    UNRECOGNIZED_OPERATION(1),
    /**
     * Signifies that the type of parameter in an invoke component is not that
     * agreed by the two TC-Users.
     */
    MISTYPED_PARAMETER(2),
    /**
     * Sufficient resources are not available to perform the requested
     * operation. This code is generated by the TC-User.
     */
    RESOURCE_LIMITATION(3),
    /**
     * The requested operation cannot be invoked because the dialogue is about
     * to be released. This code is generated only by the TC-User.
     */
    INITIATING_RELEASE(4),
    /**
     * The linked ID does not correspond to an active invoke operation. This
     * code is generated only by the component sublayer.
     */
    UNRECOGNIZED_LINKED_ID(5),
    /**
     * The operation referred to by the linked ID is not an operation for which
     * linked invokes are allowed. This code is generated only by the TC-User.
     */
    LINKED_RESPONSE_UNEXPECTED(6),
    /**
     * The operation referred to by the linked ID does not allow this linked
     * operation. This code is generated only by the TC-User.
     */
    UNEXPECTED_LINKED_OPERATION(7);
    private int value;

    private InvokeProblem(int value) {
        this.value = value;
    }

    public static InvokeProblem getInstance(int i) {
        switch (i) {
            case 0:
                return DUPLICATE_INVOKE_ID;
            case 1:
                return UNRECOGNIZED_OPERATION;
            case 2:
                return MISTYPED_PARAMETER;
            case 3:
                return RESOURCE_LIMITATION;
            case 4:
                return INITIATING_RELEASE;
            case 5:
                return UNRECOGNIZED_LINKED_ID;
            case 6:
                return LINKED_RESPONSE_UNEXPECTED;
            case 7:
                return UNEXPECTED_LINKED_OPERATION;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
