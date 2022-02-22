/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.errors;

import dev.ocean.sigtran.cap.CAPUserErrorCodes;

/**
 *
 * @author eatakishiyev
 */
public class ParameterOutOfRange extends CAPUserError {

    @Override
    public CAPUserErrorCodes getErrorCode() {
        return CAPUserErrorCodes.PARAMETER_OUT_OF_RANGE;
    }

}
