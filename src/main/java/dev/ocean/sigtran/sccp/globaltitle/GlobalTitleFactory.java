/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.globaltitle;

import java.io.Serializable;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.general.NumberingPlan;

/**
 *
 * @author eatakishiyev
 */
public class GlobalTitleFactory implements Serializable {

    public GT0001 createGlobalTitle(NatureOfAddress natureOfAddress, String GlobalTitleAddressInformation) {
        return new GT0001(natureOfAddress, GlobalTitleAddressInformation);
    }

    public GT0001 createGlobalTitle0001() {
        return new GT0001();
    }

    public GT0010 createGlobalTitle(int translationType, String GlobalTitleAddressInformation) {
        return new GT0010(translationType, GlobalTitleAddressInformation);
    }

    public GT0010 createGlobalTitle0010() {
        return new GT0010();
    }

    public GT0011 createGlobalTitle(int translationType, NumberingPlan numberingPlan, String globalTitleAddressInformation) {
        return new GT0011(translationType, numberingPlan, globalTitleAddressInformation);
    }

    public GT0011 createGlobalTitle0011() {
        return new GT0011();
    }

    public GT0100 createGlobalTitle(int translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress, String globalTitleAddressInformation) {
        return new GT0100(translationType, numberingPlan, natureOfAddress, globalTitleAddressInformation);
    }

    public GT0100 createGlobalTitle0100() {
        return new GT0100();
    }
}
