/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.network.location.update;

import dev.ocean.sigtran.map.parameters.BasicServiceList;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.SSList;

/**
 *
 * @author eatakishiyev
 */
public class DeleteSubscriberDataArg  {

    private IMSI imsi;
    private BasicServiceList basicServiceList;
    private SSList ssList;
    private Boolean roamingRestrictionDueToUnsupportedFeature = Boolean.FALSE;
    private byte[] regionalSubscriptionIdentifier;
    private Boolean vbsGroupIndication = Boolean.FALSE;
    private Boolean vgcsGroupIndication = Boolean.FALSE;
    private Boolean camelSubscriptionInfoWithdraw = Boolean.FALSE;
    private byte[] extensionContainer;
    private byte[] gprsSubscriptionDataWithdraw;
    private Boolean romaingRestrictedInSgsnDueToUnsupportedFeature = Boolean.FALSE;
    private byte[] lsaInformationWithdraw;
    private Boolean gmlcListWithdraw = Boolean.FALSE;
    private Boolean istInformationWithdraw = Boolean.FALSE;
    private byte[] specificCSIWithdraw;
    private Boolean chargingCharacteristicsWithdraw = Boolean.FALSE;
    private Boolean stnSrWithdraw = Boolean.FALSE;
    private byte[] epsSubscriptionDataWithdraw;
    private Boolean apnOiReplacementWithdraw = Boolean.FALSE;
    private Boolean csgSubscriptionDeleted = Boolean.FALSE;
    private Boolean subscribedVsrvccWithdraw = Boolean.FALSE;

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the basicServiceList
     */
    public BasicServiceList getBasicServiceList() {
        return basicServiceList;
    }

    /**
     * @param basicServiceList the basicServiceList to set
     */
    public void setBasicServiceList(BasicServiceList basicServiceList) {
        this.basicServiceList = basicServiceList;
    }

    /**
     * @return the ssList
     */
    public SSList getSsList() {
        return ssList;
    }

    /**
     * @param ssList the ssList to set
     */
    public void setSsList(SSList ssList) {
        this.ssList = ssList;
    }

    /**
     * @return the roamingRestrictionDueToUnsupportedFeature
     */
    public Boolean getRoamingRestrictionDueToUnsupportedFeature() {
        return roamingRestrictionDueToUnsupportedFeature;
    }

    /**
     * @param roamingRestrictionDueToUnsupportedFeature the
     * roamingRestrictionDueToUnsupportedFeature to set
     */
    public void setRoamingRestrictionDueToUnsupportedFeature(Boolean roamingRestrictionDueToUnsupportedFeature) {
        this.roamingRestrictionDueToUnsupportedFeature = roamingRestrictionDueToUnsupportedFeature;
    }

    /**
     * @return the regionalSubscriptionIdentifier
     */
    public byte[] getRegionalSubscriptionIdentifier() {
        return regionalSubscriptionIdentifier;
    }

    /**
     * @param regionalSubscriptionIdentifier the regionalSubscriptionIdentifier
     * to set
     */
    public void setRegionalSubscriptionIdentifier(byte[] regionalSubscriptionIdentifier) {
        this.regionalSubscriptionIdentifier = regionalSubscriptionIdentifier;
    }

    /**
     * @return the vbsGroupIndication
     */
    public Boolean getVbsGroupIndication() {
        return vbsGroupIndication;
    }

    /**
     * @param vbsGroupIndication the vbsGroupIndication to set
     */
    public void setVbsGroupIndication(Boolean vbsGroupIndication) {
        this.vbsGroupIndication = vbsGroupIndication;
    }

    /**
     * @return the vgcsGroupIndication
     */
    public Boolean getVgcsGroupIndication() {
        return vgcsGroupIndication;
    }

    /**
     * @param vgcsGroupIndication the vgcsGroupIndication to set
     */
    public void setVgcsGroupIndication(Boolean vgcsGroupIndication) {
        this.vgcsGroupIndication = vgcsGroupIndication;
    }

    /**
     * @return the camelSubscriptionInfoWithdraw
     */
    public Boolean getCamelSubscriptionInfoWithdraw() {
        return camelSubscriptionInfoWithdraw;
    }

    /**
     * @param camelSubscriptionInfoWithdraw the camelSubscriptionInfoWithdraw to
     * set
     */
    public void setCamelSubscriptionInfoWithdraw(Boolean camelSubscriptionInfoWithdraw) {
        this.camelSubscriptionInfoWithdraw = camelSubscriptionInfoWithdraw;
    }

    /**
     * @return the extensionContainer
     */
    public byte[] getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(byte[] extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    /**
     * @return the gprsSubscriptionDataWithdraw
     */
    public byte[] getGprsSubscriptionDataWithdraw() {
        return gprsSubscriptionDataWithdraw;
    }

    /**
     * @param gprsSubscriptionDataWithdraw the gprsSubscriptionDataWithdraw to
     * set
     */
    public void setGprsSubscriptionDataWithdraw(byte[] gprsSubscriptionDataWithdraw) {
        this.gprsSubscriptionDataWithdraw = gprsSubscriptionDataWithdraw;
    }

    /**
     * @return the romaingRestrictedInSgsnDueToUnsupportedFeature
     */
    public Boolean getRomaingRestrictedInSgsnDueToUnsupportedFeature() {
        return romaingRestrictedInSgsnDueToUnsupportedFeature;
    }

    /**
     * @param romaingRestrictedInSgsnDueToUnsupportedFeature the
     * romaingRestrictedInSgsnDueToUnsupportedFeature to set
     */
    public void setRoamingRestrictedInSgsnDueToUnsupportedFeature(Boolean romaingRestrictedInSgsnDueToUnsupportedFeature) {
        this.romaingRestrictedInSgsnDueToUnsupportedFeature = romaingRestrictedInSgsnDueToUnsupportedFeature;
    }

    /**
     * @return the lsaInformationWithdraw
     */
    public byte[] getLsaInformationWithdraw() {
        return lsaInformationWithdraw;
    }

    /**
     * @param lsaInformationWithdraw the lsaInformationWithdraw to set
     */
    public void setLsaInformationWithdraw(byte[] lsaInformationWithdraw) {
        this.lsaInformationWithdraw = lsaInformationWithdraw;
    }

    /**
     * @return the gmlcListWithdraw
     */
    public Boolean getGmlcListWithdraw() {
        return gmlcListWithdraw;
    }

    /**
     * @param gmlcListWithdraw the gmlcListWithdraw to set
     */
    public void setGmlcListWithdraw(Boolean gmlcListWithdraw) {
        this.gmlcListWithdraw = gmlcListWithdraw;
    }

    /**
     * @return the istInformationWithdraw
     */
    public Boolean getIstInformationWithdraw() {
        return istInformationWithdraw;
    }

    /**
     * @param istInformationWithdraw the istInformationWithdraw to set
     */
    public void setIstInformationWithdraw(Boolean istInformationWithdraw) {
        this.istInformationWithdraw = istInformationWithdraw;
    }

    /**
     * @return the specificCSIWithdraw
     */
    public byte[] getSpecificCSIWithdraw() {
        return specificCSIWithdraw;
    }

    /**
     * @param specificCSIWithdraw the specificCSIWithdraw to set
     */
    public void setSpecificCSIWithdraw(byte[] specificCSIWithdraw) {
        this.specificCSIWithdraw = specificCSIWithdraw;
    }

    /**
     * @return the chargingCharacteristicsWithdraw
     */
    public Boolean getChargingCharacteristicsWithdraw() {
        return chargingCharacteristicsWithdraw;
    }

    /**
     * @param chargingCharacteristicsWithdraw the
     * chargingCharacteristicsWithdraw to set
     */
    public void setChargingCharacteristicsWithdraw(Boolean chargingCharacteristicsWithdraw) {
        this.chargingCharacteristicsWithdraw = chargingCharacteristicsWithdraw;
    }

    /**
     * @return the stnSrWithdraw
     */
    public Boolean getStnSrWithdraw() {
        return stnSrWithdraw;
    }

    /**
     * @param stnSrWithdraw the stnSrWithdraw to set
     */
    public void setStnSrWithdraw(Boolean stnSrWithdraw) {
        this.stnSrWithdraw = stnSrWithdraw;
    }

    /**
     * @return the epsSubscriptionDataWithdraw
     */
    public byte[] getEpsSubscriptionDataWithdraw() {
        return epsSubscriptionDataWithdraw;
    }

    /**
     * @param epsSubscriptionDataWithdraw the epsSubscriptionDataWithdraw to set
     */
    public void setEpsSubscriptionDataWithdraw(byte[] epsSubscriptionDataWithdraw) {
        this.epsSubscriptionDataWithdraw = epsSubscriptionDataWithdraw;
    }

    /**
     * @return the apnOiReplacementWithdraw
     */
    public Boolean getApnOiReplacementWithdraw() {
        return apnOiReplacementWithdraw;
    }

    /**
     * @param apnOiReplacementWithdraw the apnOiReplacementWithdraw to set
     */
    public void setApnOiReplacementWithdraw(Boolean apnOiReplacementWithdraw) {
        this.apnOiReplacementWithdraw = apnOiReplacementWithdraw;
    }

    /**
     * @return the csgSubscriptionDeleted
     */
    public Boolean getCsgSubscriptionDeleted() {
        return csgSubscriptionDeleted;
    }

    /**
     * @param csgSubscriptionDeleted the csgSubscriptionDeleted to set
     */
    public void setCsgSubscriptionDeleted(Boolean csgSubscriptionDeleted) {
        this.csgSubscriptionDeleted = csgSubscriptionDeleted;
    }

    /**
     * @return the subscribedVsrvccWithdraw
     */
    public Boolean getSubscribedVsrvccWithdraw() {
        return subscribedVsrvccWithdraw;
    }

    /**
     * @param subscribedVsrvccWithdraw the subscribedVsrvccWithdraw to set
     */
    public void setSubscribedVsrvccWithdraw(Boolean subscribedVsrvccWithdraw) {
        this.subscribedVsrvccWithdraw = subscribedVsrvccWithdraw;
    }
}
