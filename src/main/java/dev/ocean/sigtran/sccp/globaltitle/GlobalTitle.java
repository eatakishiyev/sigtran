/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.globaltitle;

import dev.ocean.sigtran.sccp.address.GlobalTitleIndicator;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.general.Encodable;
import dev.ocean.sigtran.sccp.general.EncodingScheme;
import dev.ocean.sigtran.sccp.general.NumberingPlan;

/**
 *
 * @author root
 */
public abstract class GlobalTitle implements Encodable {

    public abstract void setNumberingPlan(NumberingPlan numberingPlan);

    public abstract NumberingPlan getNumberingPlan();

    public abstract void setTranslationType(int translationType);

    public abstract int getTranslationType();

    public abstract void setNatureOfAddress(NatureOfAddress natureOfAddress);

    public abstract NatureOfAddress getNatureOfAddress();

    public abstract EncodingScheme getEncodingScheme();

    public abstract GlobalTitleIndicator getGlobalTitleIndicator();

    public abstract String getGlobalTitleAddressInformation();

    public abstract void setGlobalTitleAddressInformation(String globalTitleAddressInformation);
}
