/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import dev.ocean.sigtran.tcap.parameters.ParameterImpl;

/**
 *
 * @author root
 */
public interface ReturnResult extends Return {

    /**
     * @return the parameter
     */
    public ParameterImpl getParameter();

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(ParameterImpl parameter);
}
