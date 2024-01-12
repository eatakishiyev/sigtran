/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.isup.parameters.GenericNumber;
import azrc.az.isup.enums.NumberQualifierIndicator;

/**
 *
 * @author eatakishiyev
 */
public class AdditionalCallingPartyNumber extends GenericNumber {

    public AdditionalCallingPartyNumber() {
        super();
        super.setNumberQualifierIndicator(NumberQualifierIndicator.ADDITIONAL_CALLING_PARTY_NUMBER);
    }

    @Override
    public void setNumberQualifierIndicator(NumberQualifierIndicator numberQualifierIndicator) {
        throw new RuntimeException("Can not assign number qualifier indicator to Generic number");
    }

}
