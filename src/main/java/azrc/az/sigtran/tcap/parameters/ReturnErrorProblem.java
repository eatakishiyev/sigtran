/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

/**
 * This element contains one of the problem codes that relate only to the return
 * error component type.
 *
 * @author root
 */
public enum ReturnErrorProblem {
    /**
     * No operation with the specified invoke ID is in progress. This code is
     * generated by the component sublayer.
     */
    UNRECOGNIZED_INVOKE_ID(0),
    /**
     * The invoked operation does not report failure. This code is generated by
     * the component sublayer.
     */
    RETURN_ERROR_UNEXPECTED(1),
    /**
     * The error code is not one of those agreed by the two TC-User.
     */
    UNRECOGNIZED_ERROR(2),
    /**
     * The received error is not one of those that the invoked operation may
     * report. This code is generated by the TC-User.
     */
    UNEXPECTED_ERROR(3),
    /**
     * Signifies that the type parameter in a Return Error component is not that
     * agreed by the two TC-Users.
     */
    MISTYPED_PARAMETER(4);
    private int value;

    private ReturnErrorProblem(int value) {
        this.value = value;
    }

    public static ReturnErrorProblem getInstance(int value) {
        switch (value) {
            case 0:
                return UNRECOGNIZED_INVOKE_ID;
            case 1:
                return RETURN_ERROR_UNEXPECTED;
            case 2:
                return UNRECOGNIZED_ERROR;
            case 3:
                return UNEXPECTED_ERROR;
            case 4:
                return MISTYPED_PARAMETER;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
