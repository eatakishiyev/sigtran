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
public class CalledINNumber extends OriginalCalledNumber {

    public CalledINNumber() {
    }

    public CalledINNumber(NatureOfAddress natureOfAddress,
            NumberingPlan numberingPlan,
            AddressPresentationRestricted addressPresentation,
            String address) {

        this.natureOfAddress = natureOfAddress;
        this.numberingPlan = numberingPlan;
        this.addressPresentationRestricted = addressPresentation;
        this.address = address;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Called IN Number [")
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
        return CALLED_IN_NUMBER;
    }
    
    

}
