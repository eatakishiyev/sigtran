/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author root
 */
public interface AddressString extends ISMRPAddress, MAPParameter {

    public AddressNature getAddressNature();

    public NumberingPlan getNumberingPlan();

    public String getAddress();

    public int getMaxLength();

    public int getMinLength();
}
