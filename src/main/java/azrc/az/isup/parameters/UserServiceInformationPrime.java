/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.CodingStandard;
import azrc.az.isup.enums.InformationTransferCapability;
import azrc.az.isup.enums.InformationTransferRate;
import azrc.az.isup.enums.TransferMode;

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
