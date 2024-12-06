/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.gtt;

import azrc.az.sigtran.sccp.address.NatureOfAddress;
import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.EncodingScheme;
import azrc.az.sigtran.sccp.general.NumberingPlan;
import azrc.az.sigtran.sccp.globaltitle.GT0001;
import azrc.az.sigtran.sccp.globaltitle.GT0010;
import azrc.az.sigtran.sccp.globaltitle.GT0011;
import azrc.az.sigtran.sccp.globaltitle.GT0100;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 *
 * @author eatakishiyev
 */
public class GlobalTitleTranslationRule implements Serializable {
//  Mandatory

    private transient final Logger logger = LoggerFactory.getLogger(GlobalTitleTranslationRule.class);
//    private transient static final Logger logger = SCCPStackImpl.logger;

    private String name;
    //Reqular expression 
    private Pattern gtaiPattern;
//
    private RoutingIndicator routingIndicator = null;
    private SccpEntitySet sccpEntitySet = null;
    private Integer translationType = null;
    private NumberingPlan numberingPlan = null;
    private NatureOfAddress natureOfAddress = null;
    private EncodingScheme encodingScheme = null;
    /**
     * Conversion rule format is
     * rule = command *(;command)
     * command = commandName:param*(,param)
     * param = token
     * commandName = add_left, replace, delete, add_right, swap
     */
    private String conversionRule = null;

    public GlobalTitleTranslationRule() {
    }

    protected GlobalTitleTranslationRule(String name, String gtaiPattern, SccpEntitySet sccpEntitySet) {
        this.name = name;
        this.gtaiPattern = Pattern.compile(gtaiPattern);
        this.sccpEntitySet = sccpEntitySet;
    }

    @Override
    public String toString() {
        return ("GlobalTitleTranslationRule: [Name = " + name
                + "; GtaIPattern = " + gtaiPattern
                + "; RoutingIndicator = " + routingIndicator
                + "; SccpEntitySet = " + sccpEntitySet
                + "; TranslationType = " + translationType
                + "; NumberingPlan = " + numberingPlan
                + "; NatureOfAddress = " + natureOfAddress
                + "; EncodingScheme = " + encodingScheme
                + "; ConversionRule = " + conversionRule + "]");
    }

    /**
     * @return the gtDigits
     */
    public Pattern getGtaiPattern() {
        return this.gtaiPattern;
    }

    /**
     * @param gtaiPattern
     */
    public void setGtaiPattern(String gtaiPattern) {
        this.gtaiPattern = Pattern.compile(gtaiPattern);
    }

    public RoutingIndicator getRoutingIndicator() {
        return routingIndicator;
    }

    public void setRoutingIndicator(RoutingIndicator routingIndicator) {
        this.routingIndicator = routingIndicator;
    }

    public void applyGTTranslationRule(SCCPAddress calledParty) {
        switch (calledParty.getAddressIndicator().getGlobalTitleIndicator()) {
            case NATURE_OF_ADDRESS_IND_ONLY:
                if (this.getNatureOfAddress() != null
                        && this.getNatureOfAddress() != NatureOfAddress.ANY) {
                    GT0001 gt0001 = (GT0001) calledParty.getGlobalTitle();

                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0001 modifying NAI from " + gt0001.getNatureOfAddress()
                                + " to " + natureOfAddress + ". CalledParty = " + calledParty);
                    }

                    gt0001.setNatureOfAddress(getNatureOfAddress());
                }

                break;
            case TRANSLATION_TYPE_NP_ENC:
                GT0011 gt0011 = ((GT0011) calledParty.getGlobalTitle());

                if (this.getTranslationType() != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0011 modifying TT from " + gt0011.getTranslationType()
                                + " to " + translationType + ". CalledParty = " + calledParty);
                    }

                    gt0011.setTranslationType(getTranslationType());
                }

                if (this.getNumberingPlan() != null
                        && this.getNumberingPlan() != NumberingPlan.ANY) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0011 modifying NP from " + gt0011.getNumberingPlan()
                                + " to " + numberingPlan + ". CalledParty = " + calledParty);
                    }

                    gt0011.setNumberingPlan(getNumberingPlan());
                }

                break;
            case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                GT0100 gt0100 = (GT0100) calledParty.getGlobalTitle();

                if (this.getTranslationType() != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0100 modifying TT from " + gt0100.getTranslationType()
                                + " to " + translationType + ". CalledParty = " + calledParty);
                    }

                    gt0100.setTranslationType(getTranslationType());
                }

                if (this.getNumberingPlan() != null
                        && this.getNumberingPlan() != NumberingPlan.ANY) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0100 modifying NP from " + gt0100.getNumberingPlan()
                                + " to " + numberingPlan + ". CalledParty = " + calledParty);
                    }

                    gt0100.setNumberingPlan(getNumberingPlan());
                }

                if (this.getEncodingScheme() != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0100 modifying EncodingScheme from " + gt0100.getEncodingScheme()
                                + " to " + encodingScheme + ". CalledParty = " + calledParty);
                    }

                    gt0100.setEncodingScheme(getEncodingScheme());
                }

                if (this.getNatureOfAddress() != null
                        && this.getNatureOfAddress() != NatureOfAddress.ANY) {

                    if (logger.isDebugEnabled()) {

                        logger.debug("GTTRule Name = " + name + ";GT0100 modifying NAI from " + gt0100.getNatureOfAddress()
                                + "to " + natureOfAddress + ". CalledParty = " + calledParty);
                    }

                    gt0100.setNatureOfAddress(getNatureOfAddress());
                }

                break;
            case TRANSLATION_TYPE_ONLY:
                GT0010 gt0010 = (GT0010) calledParty.getGlobalTitle();
                if (getTranslationType() != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("GTTRule Name = " + name + ";GT0010 modifying NAI from " + gt0010.getTranslationType()
                                + " to " + translationType + ". CalledParty = " + calledParty);

                    }

                    gt0010.setTranslationType(getTranslationType());
                }

                break;
        }
    }

    /**
     * rule = command *(;command)
     * command = commandName:param*(,param)
     * param = token
     * commandName = add_left, replace, delete, add_right, swap
     *
     * @param globalTitleAddressInformation
     * @return
     */
    public String applyGTConversionRule(String globalTitleAddressInformation) {
        String[] commands = this.conversionRule.split(";");
        for (String command : commands) {
            String[] splitted = command.trim().toLowerCase().split(":");
            String commandName = splitted[0].trim();
            String commandParameters = splitted[1].trim();
            switch (commandName) {
                case "add_left":
                    globalTitleAddressInformation = commandParameters.concat(globalTitleAddressInformation);
                    break;
                case "replace":
                    StringBuilder sb = new StringBuilder(globalTitleAddressInformation);
                    String[] replaceParam = commandParameters.split(",");
                    sb.replace(Integer.parseInt(replaceParam[0]), Integer.parseInt(replaceParam[1]), replaceParam[2]);
                    globalTitleAddressInformation = sb.toString();
                    break;
                case "delete":
                    String[] deleteParam = commandParameters.split(",");
                    sb = new StringBuilder(globalTitleAddressInformation);
                    sb.delete(Integer.parseInt(deleteParam[0]), Integer.parseInt(deleteParam[1]));
                    globalTitleAddressInformation = sb.toString();
                    break;
                case "add_right":
                    globalTitleAddressInformation = globalTitleAddressInformation.concat(commandParameters);
                    break;
                case "swap":
                    globalTitleAddressInformation = commandParameters;
                    break;

            }
        }
        return globalTitleAddressInformation;
    }

    /**
     * @return the sccpEntitySet
     */
    public SccpEntitySet getSccpEntitySet() {
        return sccpEntitySet;
    }

    /**
     * @param sccpEntitySet the sccpEntitySet to set
     */
    public void setSccpEntitySet(SccpEntitySet sccpEntitySet) {
        this.sccpEntitySet = sccpEntitySet;
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

    /**
     * @return the translationType
     */
    public Integer getTranslationType() {
        return translationType;
    }

    /**
     * @param translationType the translationType to set
     */
    public void setTranslationType(Integer translationType) {
        this.translationType = translationType;
    }

    /**
     * @return the numberingPlan
     */
    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    /**
     * @param numberingPlan the numberingPlan to set
     */
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @return the natureOfAddress
     */
    public NatureOfAddress getNatureOfAddress() {
        return natureOfAddress;
    }

    /**
     * @param natureOfAddress the natureOfAddress to set
     */
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    /**
     * @return the encodingScheme
     */
    public EncodingScheme getEncodingScheme() {
        return encodingScheme;
    }

    /**
     * @param encodingScheme the encodingScheme to set
     */
    public void setEncodingScheme(EncodingScheme encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    /**
     * @return the conversionRule
     */
    public String getConversionRule() {
        return conversionRule;
    }

    /**
     * @param conversionRule the conversionRule to set
     */
    public void setConversionRule(String conversionRule) {
        this.conversionRule = conversionRule;
    }

}
