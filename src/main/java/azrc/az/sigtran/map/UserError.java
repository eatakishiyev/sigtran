/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.tcap.parameters.Parameter;

/**
 *
 * @author eatakishiyev
 */
public class UserError {

    private Integer errorCode;
    private Parameter parameter;

    public UserError() {
    }

    public UserError(Integer userErrorr) {
        this.errorCode = userErrorr;
    }

    /**
     * @return the userErrorr
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @param userErrorr the userErrorr to set
     */
    public void setErrorCode(Integer userErrorr) {
        this.errorCode = userErrorr;
    }

    /**
     * @return the parameter
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
}
