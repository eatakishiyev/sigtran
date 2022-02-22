/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.sigtran.map.parameters.AddressNature;
import dev.ocean.sigtran.map.parameters.AddressStringImpl;
import dev.ocean.sigtran.map.parameters.NumberingPlan;

/**
 *
 * @author eatakishiyev
 */
public class SMSAddressString extends AddressStringImpl {

    public SMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(addressNature, numberingPlan, address);
    }

    public SMSAddressString() {
        super();
    }
}
