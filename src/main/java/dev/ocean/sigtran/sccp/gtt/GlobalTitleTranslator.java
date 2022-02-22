/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.gtt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.sccp.address.GlobalTitleIndicator;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.address.RoutingIndicator;
import dev.ocean.sigtran.sccp.general.NumberingPlan;
import dev.ocean.sigtran.sccp.globaltitle.GT0001;
import dev.ocean.sigtran.sccp.globaltitle.GT0010;
import dev.ocean.sigtran.sccp.globaltitle.GT0011;
import dev.ocean.sigtran.sccp.globaltitle.GT0100;
import dev.ocean.sigtran.sccp.globaltitle.GlobalTitle;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public class GlobalTitleTranslator implements Serializable {
//  Mandatory

    private String name;
    private GlobalTitleIndicator globalTitleIndicator;
//  Optional 
    private int translationType = -1;
    private NumberingPlan numberingPlan = NumberingPlan.ANY;
    private NatureOfAddress natureOfAddress = NatureOfAddress.ANY;
    private final List<GlobalTitleTranslationRule> globalTitleTranslationRules = new ArrayList<>();
//    private transient static final Logger logger = SCCPStackImpl.logger;

    public GlobalTitleTranslator() {

    }

    public GlobalTitleTranslator(String name, NatureOfAddress nai) {
        this.name = name;
        this.globalTitleIndicator = GlobalTitleIndicator.NATURE_OF_ADDRESS_IND_ONLY;
        this.natureOfAddress = nai;
    }

    /**
     *
     * @param name
     * @param translationType -1 means that don't check translationType
     */
    public GlobalTitleTranslator(String name, int translationType) {
        this.name = name;
        this.globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_ONLY;
        this.translationType = translationType;
    }

    /**
     *
     * @param name
     * @param translationType -1 means that don't check translationType
     * @param numberingPlan
     */
    public GlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan) {
        this.name = name;
        this.globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC;
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
    }

    /**
     *
     * @param name
     * @param translationType -1 means that don't check translationType
     * @param numberingPlan
     * @param natureOfAddress
     */
    public GlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress) {
        this.name = name;
        this.globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND;
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
        this.natureOfAddress = natureOfAddress;
    }

    /**
     *
     * @param globalTitleAddressInformation
     * @return GlobalTitleTranslationRule
     */
    public GlobalTitleTranslationRule getGlobalTitleTranslationRule(String globalTitleAddressInformation) {
        for (GlobalTitleTranslationRule globalTitleTranslationRule : this.globalTitleTranslationRules) {
            if (globalTitleTranslationRule.getGtaiPattern().matcher(globalTitleAddressInformation).matches()) {
                return globalTitleTranslationRule;
            }
        }
        return null;
    }

    public GlobalTitleTranslationRule addGlobalTitleTranslationRule(String name, String pattern, RoutingIndicator ri, SccpEntitySet sccpEntitySet, Integer tt, NumberingPlan np, NatureOfAddress nai, String conversionRule) throws IOException {
        for (GlobalTitleTranslationRule globalTitleTranslationRule : this.globalTitleTranslationRules) {
            if (globalTitleTranslationRule.getName().equals(name)) {
                throw new IOException(String.format("SCCP Global Title Translation Rule already exists. Name = %s", name));
            }
        }

        GlobalTitleTranslationRule globalTitleTranslationRule = new GlobalTitleTranslationRule(name, pattern, sccpEntitySet);
        globalTitleTranslationRule.setTranslationType(tt);
        globalTitleTranslationRule.setNumberingPlan(np);
        globalTitleTranslationRule.setNatureOfAddress(nai);
        globalTitleTranslationRule.setConversionRule(conversionRule);
        globalTitleTranslationRule.setRoutingIndicator(ri);

        this.globalTitleTranslationRules.add(globalTitleTranslationRule);
        return globalTitleTranslationRule;
    }

    public void addGlobalTitleTranslationRule(GlobalTitleTranslationRule globalTitleTranslationRule) throws IOException {
        for (GlobalTitleTranslationRule rule : globalTitleTranslationRules) {
            if (rule.getName().equals(globalTitleTranslationRule.getName())) {
                throw new IOException(String.format("SCCP Global Title Translation Rule already exists. Name = %s", globalTitleTranslationRule.getName()));
            }
        }
        this.globalTitleTranslationRules.add(globalTitleTranslationRule);
    }

    public boolean matches(GlobalTitle globalTitle) {
        if (this.globalTitleIndicator != globalTitle.getGlobalTitleIndicator()) {
            return false;
        }

        switch (globalTitle.getGlobalTitleIndicator()) {
            case NO_GLOBAL_TITLE_INCLUDED:
                return false;
            case NATURE_OF_ADDRESS_IND_ONLY:
                if (this.natureOfAddress == NatureOfAddress.ANY) {
                    return true;
                } else if (((GT0001) globalTitle).getNatureOfAddress() != this.natureOfAddress) {
                    return false;
                }

                return true;
            case TRANSLATION_TYPE_ONLY:
                if (this.translationType < 0) {
                    return true;
                } else if (((GT0010) globalTitle).getTranslationType() != this.translationType) {
                    return false;
                }
                return true;
            case TRANSLATION_TYPE_NP_ENC:
                if (this.translationType >= 0
                        && ((GT0011) globalTitle).getTranslationType() != this.translationType) {
                    return false;
                }

                if (this.numberingPlan != NumberingPlan.ANY
                        && ((GT0011) globalTitle).getNumberingPlan() != this.numberingPlan) {
                    return false;
                }

                return true;
            case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                if (this.translationType >= 0
                        && ((GT0100) globalTitle).getTranslationType() != this.translationType) {
                    return false;
                }

                if (this.numberingPlan != NumberingPlan.ANY
                        && ((GT0100) globalTitle).getNumberingPlan() != this.numberingPlan) {
                    return false;
                }

                if (this.natureOfAddress != NatureOfAddress.ANY
                        && ((GT0100) globalTitle).getNatureOfAddress() != this.natureOfAddress) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public boolean removeGlobalTitleTranslationRule(String name) throws IOException {
        for (GlobalTitleTranslationRule globalTitleTranslationRule : this.globalTitleTranslationRules) {
            if (globalTitleTranslationRule.getName().equals(name)) {
                this.globalTitleTranslationRules.remove(globalTitleTranslationRule);
                return true;
            }
        }
        return false;
    }

    public GlobalTitleTranslationRule getGlobalTitleTranslationRuleByName(String name) {
        for (GlobalTitleTranslationRule globalTitleTranslationRule : this.globalTitleTranslationRules) {
            if (globalTitleTranslationRule.getName().equals(name)) {
                return globalTitleTranslationRule;
            }
        }
        return null;
    }

    /**
     * @return the tt
     */
    public int getTranslationType() {
        return translationType;
    }

    /**
     * @param translationType
     */
    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    /**
     * @return the np
     */
    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    /**
     * @param numberingPlan
     */
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @return the nai
     */
    public NatureOfAddress getNatureOfAddress() {
        return natureOfAddress;
    }

    /**
     * @param nai the nai to set
     */
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    /**
     * @return the gti
     */
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return globalTitleIndicator;
    }

    /**
     * @param gti the gti to set
     */
    public void setGlobalTitleIndicator(GlobalTitleIndicator globalTitleIndicator) {
        this.globalTitleIndicator = globalTitleIndicator;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GlobalTitleTranslator[");
        sb.append("Name = ").append(name)
                .append(";GlobalTitleIndicator = ").append(globalTitleIndicator)
                .append(";TranslationType = ").append(translationType)
                .append(";NumberingPlan = ").append(numberingPlan)
                .append(";NatureOfAddressIndicator = ").append(natureOfAddress);
        globalTitleTranslationRules.forEach((globalTitleTranslationRule) -> {
            sb.append(";").append(globalTitleTranslationRule);
        });
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the globalTitleTranslationRules
     */
    public List<GlobalTitleTranslationRule> getGlobalTitleTranslationRules() {
        return globalTitleTranslationRules;
    }

}
