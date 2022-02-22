/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.CodingStandard;
import dev.ocean.isup.enums.InformationTransferCapability;
import dev.ocean.isup.enums.InformationTransferRate;
import dev.ocean.isup.enums.TransferMode;

/**
 *
 * @author eatakishiyev
 */
public class UserServiceInformationPrime extends UserServiceInformation {

    public UserServiceInformationPrime() {
    }

    public UserServiceInformationPrime(CodingStandard codingStandard, InformationTransferCapability informationTransferCapability,
            TransferMode transferMode, InformationTransferRate informationTransferRate) {
        this.codingStandard = codingStandard;
        this.informationTransferCapability = informationTransferCapability;
        this.transferMode = transferMode;
        this.informationTransferRate = informationTransferRate;
    }

    @Override
    public int getParameterCode() {
        return USER_SERVICE_INFORMATION_PRIME;
    }

}
