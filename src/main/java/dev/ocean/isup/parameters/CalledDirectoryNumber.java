/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.InternationalNetworkNumberIndicator;
import dev.ocean.isup.enums.NatureOfAddress;
import dev.ocean.isup.enums.NumberingPlan;

/**
 *
 * @author eatakishiyev
 */
public class CalledDirectoryNumber extends CalledPartyNumber {

    public CalledDirectoryNumber() {
    }

    public CalledDirectoryNumber(NatureOfAddress natureOfAddress, InternationalNetworkNumberIndicator internalNetworkNumberIndicator, NumberingPlan numberingPlan, String address) {
        super(natureOfAddress, internalNetworkNumberIndicator, numberingPlan, address);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CalledDirectoryNumber [ ")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; InternationalNetworkNumberIndicator:").append(internalNetworkNumberIndicator)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; Address:").append(address)
                .append("]");

        return sb.toString();
    }

    @Override
    public int getParameterCode() {
        return CALLED_DIRECTORY_NUMBER;
    }

    
}
