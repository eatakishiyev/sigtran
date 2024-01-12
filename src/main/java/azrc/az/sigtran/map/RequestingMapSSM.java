/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

/**
 *
 * @author root
 */
public class RequestingMapSSM {

    private final OperationCodes operation;//Do not forget to assign value to this variable on each MAP_service request

    public RequestingMapSSM(OperationCodes opCode) {
        this.operation = opCode;
    }

    /**
     * @return the operation
     */
    public OperationCodes getOperation() {
        return operation;
    }
}
