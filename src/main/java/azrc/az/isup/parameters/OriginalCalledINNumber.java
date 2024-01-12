/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.AddressPresentationRestricted;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.NumberingPlan;

/**
 *
 * @author eatakishiyev
 */
public class OriginalCalledINNumber extends OriginalCalledNumber {

    public OriginalCalledINNumber() {
    }

    public OriginalCalledINNumber(NatureOfAddress natureOfAddress,
            NumberingPlan numberingPlan,
            AddressPresentationRestricted addressPresentation,
            String address) {
        super(natureOfAddress, numberingPlan, addressPresentation, address);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OriginalCalledINNumber [")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; AddressPresentationRestricted:").append(addressPresentationRestricted)
                .append("; Address:").append(address)
                .append("]");
        return sb.toString();
    }

    @Override
    public int getParameterCode() {
        return ORIGINAL_CALLED_IN_NUMBER;
    }

}
