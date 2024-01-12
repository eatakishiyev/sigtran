/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 *
 * @author eatakishiyev
 */
public class ODBGeneralData {

    private Boolean allOGCallsBarred;
    private Boolean internationalOGCallsBarred;
    private Boolean internationalOGCallsNotToHPLMNCountryBarred;
    private Boolean interzonalOGCallsBarred;
    private Boolean interzonalOGCallsNotToHPLMNCountryBarred;
    private Boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred;
    private Boolean premiumRateInformationOGCallsBarred;
    private Boolean premiumRateEntertainementOGCallsBarred;
    private Boolean ssAccessBarred;
    private Boolean allECTBarred;
    private Boolean chargeableECTBarred;
    private Boolean internationalECTBarred;
    private Boolean interzonalECTBarred;
    private Boolean doublyChargeableECTBarred;
    private Boolean multipleECTBarred;
    private Boolean allPacketOrientedServicesBarred;
    private Boolean roamerAccessToHPLMNAPBarred;
    private Boolean roamerAccessToVPLMNAPBarred;
    private Boolean roamingOutsidePLMNOGCallsBarred;
    private Boolean allICCallsBarred;
    private Boolean roamingOutsidePLMNICCallsBarred;
    private Boolean roamingOutsidePLMNICountryICCallsBarred;
    private Boolean roamingOutsidePLMNBarred;
    private Boolean roamingOutsidePLMNCountryBarred;
    private Boolean registrationAllCFBarred;
    private Boolean registrationCFNotToHPLMNBarred;
    private Boolean registrationInterzonalCFBarred;
    private Boolean registrationInterzonalCFNotToHPLMNBarred;
    private Boolean registrationInternationalCFBarred;

    public void encode(AsnOutputStream aos) throws AsnException, IOException {
        BitSetStrictLength bits = new BitSetStrictLength(29);
        bits.set(0, allOGCallsBarred);
        bits.set(1, internationalOGCallsBarred);
        bits.set(2, internationalOGCallsNotToHPLMNCountryBarred);
        bits.set(3, interzonalOGCallsBarred);
        bits.set(4, interzonalOGCallsNotToHPLMNCountryBarred);
        bits.set(5, interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
        bits.set(6, premiumRateInformationOGCallsBarred);
        bits.set(7, premiumRateEntertainementOGCallsBarred);
        bits.set(8, ssAccessBarred);
        bits.set(9, allECTBarred);
        bits.set(10, chargeableECTBarred);
        bits.set(11, internationalECTBarred);
        bits.set(12, interzonalECTBarred);
        bits.set(13, doublyChargeableECTBarred);
        bits.set(14, multipleECTBarred);
        bits.set(15, allPacketOrientedServicesBarred);
        bits.set(16, roamerAccessToHPLMNAPBarred);
        bits.set(17, roamerAccessToVPLMNAPBarred);
        bits.set(18, roamingOutsidePLMNOGCallsBarred);
        bits.set(19, allICCallsBarred);
        bits.set(20, roamingOutsidePLMNICCallsBarred);
        bits.set(21, roamingOutsidePLMNICountryICCallsBarred);
        bits.set(22, roamingOutsidePLMNBarred);
        bits.set(23, roamingOutsidePLMNCountryBarred);
        bits.set(24, registrationAllCFBarred);
        bits.set(25, registrationCFNotToHPLMNBarred);
        bits.set(26, registrationInterzonalCFBarred);
        bits.set(27, registrationInterzonalCFNotToHPLMNBarred);
        bits.set(28, registrationInternationalCFBarred);

        aos.writeBitString(bits);
        AsnInputStream ais = new AsnInputStream(aos.toByteArray());
        aos.reset();
        ais.readTag();
        aos.write(ais.readBitStringData(ais.readLength()).toByteArray());

    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        BitSetStrictLength bits = new BitSetStrictLength(29);
        bits.set(ODBGeneralDatas.allOGCallsBarred.getValue(), allOGCallsBarred);
        bits.set(ODBGeneralDatas.internationalOGCallsBarred.getValue(), internationalOGCallsBarred);
        bits.set(ODBGeneralDatas.internationalOGCallsNotToHPLMNCountryBarred.getValue(), internationalOGCallsNotToHPLMNCountryBarred);
        bits.set(ODBGeneralDatas.interzonalOGCallsBarred.getValue(), interzonalOGCallsBarred);
        bits.set(ODBGeneralDatas.interzonalOGCallsNotToHPLMNCountryBarred.getValue(), interzonalOGCallsNotToHPLMNCountryBarred);
        bits.set(ODBGeneralDatas.interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred.getValue(), interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
        bits.set(ODBGeneralDatas.premiumRateInformationOGCallsBarred.getValue(), premiumRateInformationOGCallsBarred);
        bits.set(ODBGeneralDatas.premiumRateEntertainementOGCallsBarred.getValue(), premiumRateEntertainementOGCallsBarred);
        bits.set(ODBGeneralDatas.ssAccessBarred.getValue(), ssAccessBarred);
        bits.set(ODBGeneralDatas.allECTBarred.getValue(), allECTBarred);
        bits.set(ODBGeneralDatas.chargeableECTBarred.getValue(), chargeableECTBarred);
        bits.set(ODBGeneralDatas.internationalECTBarred.getValue(), internationalECTBarred);
        bits.set(ODBGeneralDatas.interzonalECTBarred.getValue(), interzonalECTBarred);
        bits.set(ODBGeneralDatas.doublyChargeableECTBarred.getValue(), doublyChargeableECTBarred);
        bits.set(ODBGeneralDatas.multipleECTBarred.getValue(), multipleECTBarred);
        bits.set(ODBGeneralDatas.allPacketOrientedServicesBarred.getValue(), allPacketOrientedServicesBarred);
        bits.set(ODBGeneralDatas.roamerAccessToHPLMNAPBarred.getValue(), roamerAccessToHPLMNAPBarred);
        bits.set(ODBGeneralDatas.roamerAccessToVPLMNAPBarred.getValue(), roamerAccessToVPLMNAPBarred);
        bits.set(ODBGeneralDatas.roamingOutsidePLMNOGCallsBarred.getValue(), roamingOutsidePLMNOGCallsBarred);
        bits.set(ODBGeneralDatas.allICCallsBarred.getValue(), allICCallsBarred);
        bits.set(ODBGeneralDatas.roamingOutsidePLMNICCallsBarred.getValue(), roamingOutsidePLMNICCallsBarred);
        bits.set(ODBGeneralDatas.roamingOutsidePLMNCountryBarred.getValue(), roamingOutsidePLMNICountryICCallsBarred);
        bits.set(ODBGeneralDatas.roamingOutsidePLMNBarred.getValue(), roamingOutsidePLMNBarred);
        bits.set(ODBGeneralDatas.roamingOutsidePLMNCountryBarred.getValue(), roamingOutsidePLMNCountryBarred);
        bits.set(ODBGeneralDatas.registrationAllCFBarred.getValue(), registrationAllCFBarred);
        bits.set(ODBGeneralDatas.registrationCFNotToHPLMNBarred.getValue(), registrationCFNotToHPLMNBarred);
        bits.set(ODBGeneralDatas.registrationInterzonalCFBarred.getValue(), registrationInterzonalCFBarred);
        bits.set(ODBGeneralDatas.registrationInterzonalCFNotToHPLMNBarred.getValue(), registrationInterzonalCFNotToHPLMNBarred);
        bits.set(ODBGeneralDatas.registrationInternationalCFBarred.getValue(), registrationInternationalCFBarred);

        aos.writeBitString(tagClass, tag, bits);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = ais.readBitString();
            this.allOGCallsBarred = bitSetStrictLength.get(0);
            this.internationalOGCallsBarred = bitSetStrictLength.get(1);
            this.internationalOGCallsNotToHPLMNCountryBarred = bitSetStrictLength.get(2);
            this.interzonalOGCallsBarred = bitSetStrictLength.get(6);
            this.interzonalOGCallsNotToHPLMNCountryBarred = bitSetStrictLength.get(7);
            this.interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = bitSetStrictLength.get(8);
            this.premiumRateInformationOGCallsBarred = bitSetStrictLength.get(3);
            this.premiumRateEntertainementOGCallsBarred = bitSetStrictLength.get(4);
            this.ssAccessBarred = bitSetStrictLength.get(5);
            this.allECTBarred = bitSetStrictLength.get(6);
            this.chargeableECTBarred = bitSetStrictLength.get(10);
            this.internationalECTBarred = bitSetStrictLength.get(11);
            this.interzonalECTBarred = bitSetStrictLength.get(12);
            this.doublyChargeableECTBarred = bitSetStrictLength.get(13);
            this.multipleECTBarred = bitSetStrictLength.get(14);
            this.allPacketOrientedServicesBarred = bitSetStrictLength.get(15);
            this.roamerAccessToHPLMNAPBarred = bitSetStrictLength.get(16);
            this.roamerAccessToVPLMNAPBarred = bitSetStrictLength.get(17);
            this.roamingOutsidePLMNOGCallsBarred = bitSetStrictLength.get(18);
            this.allICCallsBarred = bitSetStrictLength.get(19);
            this.roamingOutsidePLMNICCallsBarred = bitSetStrictLength.get(20);
            this.roamingOutsidePLMNICountryICCallsBarred = bitSetStrictLength.get(21);
            this.roamingOutsidePLMNBarred = bitSetStrictLength.get(22);
            this.roamingOutsidePLMNCountryBarred = bitSetStrictLength.get(23);
            this.registrationAllCFBarred = bitSetStrictLength.get(24);
            this.registrationCFNotToHPLMNBarred = bitSetStrictLength.get(25);
            this.registrationInterzonalCFBarred = bitSetStrictLength.get(26);
            this.registrationInterzonalCFNotToHPLMNBarred = bitSetStrictLength.get(27);
            this.registrationInternationalCFBarred = bitSetStrictLength.get(28);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the allOGCallsBarred
     */
    public Boolean getAllOGCallsBarred() {
        return allOGCallsBarred;
    }

    /**
     * @param allOGCallsBarred the allOGCallsBarred to set
     */
    public void setAllOGCallsBarred(Boolean allOGCallsBarred) {
        this.allOGCallsBarred = allOGCallsBarred;
    }

    /**
     * @return the internationalOGCallsBarred
     */
    public Boolean getInternationalOGCallsBarred() {
        return internationalOGCallsBarred;
    }

    /**
     * @param internationalOGCallsBarred the internationalOGCallsBarred to set
     */
    public void setInternationalOGCallsBarred(Boolean internationalOGCallsBarred) {
        this.internationalOGCallsBarred = internationalOGCallsBarred;
    }

    /**
     * @return the internationalOGCallsNotToHPLMNCountryBarred
     */
    public Boolean getInternationalOGCallsNotToHPLMNCountryBarred() {
        return internationalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @param internationalOGCallsNotToHPLMNCountryBarred the
     * internationalOGCallsNotToHPLMNCountryBarred to set
     */
    public void setInternationalOGCallsNotToHPLMNCountryBarred(Boolean internationalOGCallsNotToHPLMNCountryBarred) {
        this.internationalOGCallsNotToHPLMNCountryBarred = internationalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @return the interzonalOGCallsBarred
     */
    public Boolean getInterzonalOGCallsBarred() {
        return interzonalOGCallsBarred;
    }

    /**
     * @param interzonalOGCallsBarred the interzonalOGCallsBarred to set
     */
    public void setInterzonalOGCallsBarred(Boolean interzonalOGCallsBarred) {
        this.interzonalOGCallsBarred = interzonalOGCallsBarred;
    }

    /**
     * @return the interzonalOGCallsNotToHPLMNCountryBarred
     */
    public Boolean getInterzonalOGCallsNotToHPLMNCountryBarred() {
        return interzonalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @param interzonalOGCallsNotToHPLMNCountryBarred the
     * interzonalOGCallsNotToHPLMNCountryBarred to set
     */
    public void setInterzonalOGCallsNotToHPLMNCountryBarred(Boolean interzonalOGCallsNotToHPLMNCountryBarred) {
        this.interzonalOGCallsNotToHPLMNCountryBarred = interzonalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @return the
     * interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred
     */
    public Boolean getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred() {
        return interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @param interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred
     * the interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred to
     * set
     */
    public void setInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred(Boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred) {
        this.interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred;
    }

    /**
     * @return the premiumRateInformationOGCallsBarred
     */
    public Boolean getPremiumRateInformationOGCallsBarred() {
        return premiumRateInformationOGCallsBarred;
    }

    /**
     * @param premiumRateInformationOGCallsBarred the
     * premiumRateInformationOGCallsBarred to set
     */
    public void setPremiumRateInformationOGCallsBarred(Boolean premiumRateInformationOGCallsBarred) {
        this.premiumRateInformationOGCallsBarred = premiumRateInformationOGCallsBarred;
    }

    /**
     * @return the premiumRateEntertainementOGCallsBarred
     */
    public Boolean getPremiumRateEntertainementOGCallsBarred() {
        return premiumRateEntertainementOGCallsBarred;
    }

    /**
     * @param premiumRateEntertainementOGCallsBarred the
     * premiumRateEntertainementOGCallsBarred to set
     */
    public void setPremiumRateEntertainementOGCallsBarred(Boolean premiumRateEntertainementOGCallsBarred) {
        this.premiumRateEntertainementOGCallsBarred = premiumRateEntertainementOGCallsBarred;
    }

    /**
     * @return the ssAccessBarred
     */
    public Boolean getSsAccessBarred() {
        return ssAccessBarred;
    }

    /**
     * @param ssAccessBarred the ssAccessBarred to set
     */
    public void setSsAccessBarred(Boolean ssAccessBarred) {
        this.ssAccessBarred = ssAccessBarred;
    }

    /**
     * @return the allECTBarred
     */
    public Boolean getAllECTBarred() {
        return allECTBarred;
    }

    /**
     * @param allECTBarred the allECTBarred to set
     */
    public void setAllECTBarred(Boolean allECTBarred) {
        this.allECTBarred = allECTBarred;
    }

    /**
     * @return the chargeableECTBarred
     */
    public Boolean getChargeableECTBarred() {
        return chargeableECTBarred;
    }

    /**
     * @param chargeableECTBarred the chargeableECTBarred to set
     */
    public void setChargeableECTBarred(Boolean chargeableECTBarred) {
        this.chargeableECTBarred = chargeableECTBarred;
    }

    /**
     * @return the internationalECTBarred
     */
    public Boolean getInternationalECTBarred() {
        return internationalECTBarred;
    }

    /**
     * @param internationalECTBarred the internationalECTBarred to set
     */
    public void setInternationalECTBarred(Boolean internationalECTBarred) {
        this.internationalECTBarred = internationalECTBarred;
    }

    /**
     * @return the interzonalECTBarred
     */
    public Boolean getInterzonalECTBarred() {
        return interzonalECTBarred;
    }

    /**
     * @param interzonalECTBarred the interzonalECTBarred to set
     */
    public void setInterzonalECTBarred(Boolean interzonalECTBarred) {
        this.interzonalECTBarred = interzonalECTBarred;
    }

    /**
     * @return the doublyChargeableECTBarred
     */
    public Boolean getDoublyChargeableECTBarred() {
        return doublyChargeableECTBarred;
    }

    /**
     * @param doublyChargeableECTBarred the doublyChargeableECTBarred to set
     */
    public void setDoublyChargeableECTBarred(Boolean doublyChargeableECTBarred) {
        this.doublyChargeableECTBarred = doublyChargeableECTBarred;
    }

    /**
     * @return the multipleECTBarred
     */
    public Boolean getMultipleECTBarred() {
        return multipleECTBarred;
    }

    /**
     * @param multipleECTBarred the multipleECTBarred to set
     */
    public void setMultipleECTBarred(Boolean multipleECTBarred) {
        this.multipleECTBarred = multipleECTBarred;
    }

    /**
     * @return the allPacketOrientedServicesBarred
     */
    public Boolean getAllPacketOrientedServicesBarred() {
        return allPacketOrientedServicesBarred;
    }

    /**
     * @param allPacketOrientedServicesBarred the
     * allPacketOrientedServicesBarred to set
     */
    public void setAllPacketOrientedServicesBarred(Boolean allPacketOrientedServicesBarred) {
        this.allPacketOrientedServicesBarred = allPacketOrientedServicesBarred;
    }

    /**
     * @return the roamerAccessToHPLMNAPBarred
     */
    public Boolean getRoamerAccessToHPLMNAPBarred() {
        return roamerAccessToHPLMNAPBarred;
    }

    /**
     * @param roamerAccessToHPLMNAPBarred the roamerAccessToHPLMNAPBarred to set
     */
    public void setRoamerAccessToHPLMNAPBarred(Boolean roamerAccessToHPLMNAPBarred) {
        this.roamerAccessToHPLMNAPBarred = roamerAccessToHPLMNAPBarred;
    }

    /**
     * @return the roamerAccessToVPLMNAPBarred
     */
    public Boolean getRoamerAccessToVPLMNAPBarred() {
        return roamerAccessToVPLMNAPBarred;
    }

    /**
     * @param roamerAccessToVPLMNAPBarred the roamerAccessToVPLMNAPBarred to set
     */
    public void setRoamerAccessToVPLMNAPBarred(Boolean roamerAccessToVPLMNAPBarred) {
        this.roamerAccessToVPLMNAPBarred = roamerAccessToVPLMNAPBarred;
    }

    /**
     * @return the roamingOutsidePLMNOGCallsBarred
     */
    public Boolean getRoamingOutsidePLMNOGCallsBarred() {
        return roamingOutsidePLMNOGCallsBarred;
    }

    /**
     * @param roamingOutsidePLMNOGCallsBarred the
     * roamingOutsidePLMNOGCallsBarred to set
     */
    public void setRoamingOutsidePLMNOGCallsBarred(Boolean roamingOutsidePLMNOGCallsBarred) {
        this.roamingOutsidePLMNOGCallsBarred = roamingOutsidePLMNOGCallsBarred;
    }

    /**
     * @return the allICCallsBarred
     */
    public Boolean getAllICCallsBarred() {
        return allICCallsBarred;
    }

    /**
     * @param allICCallsBarred the allICCallsBarred to set
     */
    public void setAllICCallsBarred(Boolean allICCallsBarred) {
        this.allICCallsBarred = allICCallsBarred;
    }

    /**
     * @return the roamingOutsidePLMNICCallsBarred
     */
    public Boolean getRoamingOutsidePLMNICCallsBarred() {
        return roamingOutsidePLMNICCallsBarred;
    }

    /**
     * @param roamingOutsidePLMNICCallsBarred the
     * roamingOutsidePLMNICCallsBarred to set
     */
    public void setRoamingOutsidePLMNICCallsBarred(Boolean roamingOutsidePLMNICCallsBarred) {
        this.roamingOutsidePLMNICCallsBarred = roamingOutsidePLMNICCallsBarred;
    }

    /**
     * @return the roamingOutsidePLMNICountryICCallsBarred
     */
    public Boolean getRoamingOutsidePLMNICountryICCallsBarred() {
        return roamingOutsidePLMNICountryICCallsBarred;
    }

    /**
     * @param roamingOutsidePLMNICountryICCallsBarred the
     * roamingOutsidePLMNICountryICCallsBarred to set
     */
    public void setRoamingOutsidePLMNICountryICCallsBarred(Boolean roamingOutsidePLMNICountryICCallsBarred) {
        this.roamingOutsidePLMNICountryICCallsBarred = roamingOutsidePLMNICountryICCallsBarred;
    }

    /**
     * @return the roamingOutsidePLMNBarred
     */
    public Boolean getRoamingOutsidePLMNBarred() {
        return roamingOutsidePLMNBarred;
    }

    /**
     * @param roamingOutsidePLMNBarred the roamingOutsidePLMNBarred to set
     */
    public void setRoamingOutsidePLMNBarred(Boolean roamingOutsidePLMNBarred) {
        this.roamingOutsidePLMNBarred = roamingOutsidePLMNBarred;
    }

    /**
     * @return the roamingOutsidePLMNCountryBarred
     */
    public Boolean getRoamingOutsidePLMNCountryBarred() {
        return roamingOutsidePLMNCountryBarred;
    }

    /**
     * @param roamingOutsidePLMNCountryBarred the
     * roamingOutsidePLMNCountryBarred to set
     */
    public void setRoamingOutsidePLMNCountryBarred(Boolean roamingOutsidePLMNCountryBarred) {
        this.roamingOutsidePLMNCountryBarred = roamingOutsidePLMNCountryBarred;
    }

    /**
     * @return the registrationAllCFBarred
     */
    public Boolean getRegistrationAllCFBarred() {
        return registrationAllCFBarred;
    }

    /**
     * @param registrationAllCFBarred the registrationAllCFBarred to set
     */
    public void setRegistrationAllCFBarred(Boolean registrationAllCFBarred) {
        this.registrationAllCFBarred = registrationAllCFBarred;
    }

    /**
     * @return the registrationCFNotToHPLMNBarred
     */
    public Boolean getRegistrationCFNotToHPLMNBarred() {
        return registrationCFNotToHPLMNBarred;
    }

    /**
     * @param registrationCFNotToHPLMNBarred the registrationCFNotToHPLMNBarred
     * to set
     */
    public void setRegistrationCFNotToHPLMNBarred(Boolean registrationCFNotToHPLMNBarred) {
        this.registrationCFNotToHPLMNBarred = registrationCFNotToHPLMNBarred;
    }

    /**
     * @return the registrationInterzonalCFBarred
     */
    public Boolean getRegistrationInterzonalCFBarred() {
        return registrationInterzonalCFBarred;
    }

    /**
     * @param registrationInterzonalCFBarred the registrationInterzonalCFBarred
     * to set
     */
    public void setRegistrationInterzonalCFBarred(Boolean registrationInterzonalCFBarred) {
        this.registrationInterzonalCFBarred = registrationInterzonalCFBarred;
    }

    /**
     * @return the registrationInterzonalCFNotToHPLMNBarred
     */
    public Boolean getRegistrationInterzonalCFNotToHPLMNBarred() {
        return registrationInterzonalCFNotToHPLMNBarred;
    }

    /**
     * @param registrationInterzonalCFNotToHPLMNBarred the
     * registrationInterzonalCFNotToHPLMNBarred to set
     */
    public void setRegistrationInterzonalCFNotToHPLMNBarred(Boolean registrationInterzonalCFNotToHPLMNBarred) {
        this.registrationInterzonalCFNotToHPLMNBarred = registrationInterzonalCFNotToHPLMNBarred;
    }

    /**
     * @return the registrationInternationalCFBarred
     */
    public Boolean getRegistrationInternationalCFBarred() {
        return registrationInternationalCFBarred;
    }

    /**
     * @param registrationInternationalCFBarred the
     * registrationInternationalCFBarred to set
     */
    public void setRegistrationInternationalCFBarred(Boolean registrationInternationalCFBarred) {
        this.registrationInternationalCFBarred = registrationInternationalCFBarred;
    }

    public enum ODBGeneralDatas {

        allOGCallsBarred(0),
        internationalOGCallsBarred(1),
        internationalOGCallsNotToHPLMNCountryBarred(2),
        interzonalOGCallsBarred(6),
        interzonalOGCallsNotToHPLMNCountryBarred(7),
        interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred(8),
        premiumRateInformationOGCallsBarred(3),
        premiumRateEntertainementOGCallsBarred(4),
        ssAccessBarred(5),
        allECTBarred(9),
        chargeableECTBarred(10),
        internationalECTBarred(11),
        interzonalECTBarred(12),
        doublyChargeableECTBarred(13),
        multipleECTBarred(14),
        allPacketOrientedServicesBarred(15),
        roamerAccessToHPLMNAPBarred(16),
        roamerAccessToVPLMNAPBarred(17),
        roamingOutsidePLMNOGCallsBarred(18),
        allICCallsBarred(19),
        roamingOutsidePLMNICCallsBarred(20),
        roamingOutsidePLMNICountryICCallsBarred(21),
        roamingOutsidePLMNBarred(22),
        roamingOutsidePLMNCountryBarred(23),
        registrationAllCFBarred(24),
        registrationCFNotToHPLMNBarred(25),
        registrationInterzonalCFBarred(26),
        registrationInterzonalCFNotToHPLMNBarred(27),
        registrationInternationalCFBarred(28);
        private int value;

        private ODBGeneralDatas(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static ODBGeneralDatas getInstance(int value) {
            switch (value) {
                case 0:
                    return allOGCallsBarred;
                case 1:
                    return internationalOGCallsBarred;
                case 2:
                    return internationalOGCallsNotToHPLMNCountryBarred;
                case 6:
                    return interzonalOGCallsBarred;
                case 7:
                    return interzonalOGCallsNotToHPLMNCountryBarred;
                case 8:
                    return interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred;
                case 3:
                    return premiumRateInformationOGCallsBarred;
                case 4:
                    return premiumRateEntertainementOGCallsBarred;
                case 5:
                    return ssAccessBarred;
                case 9:
                    return allECTBarred;
                case 10:
                    return chargeableECTBarred;
                case 11:
                    return internationalECTBarred;
                case 12:
                    return interzonalECTBarred;
                case 13:
                    return doublyChargeableECTBarred;
                case 14:
                    return multipleECTBarred;
                case 15:
                    return allPacketOrientedServicesBarred;
                case 16:
                    return roamerAccessToHPLMNAPBarred;
                case 17:
                    return roamerAccessToVPLMNAPBarred;
                case 18:
                    return roamingOutsidePLMNOGCallsBarred;
                case 19:
                    return allICCallsBarred;
                case 20:
                    return roamingOutsidePLMNICCallsBarred;
                case 21:
                    return roamingOutsidePLMNICountryICCallsBarred;
                case 22:
                    return roamingOutsidePLMNBarred;
                case 23:
                    return roamingOutsidePLMNCountryBarred;
                case 24:
                    return registrationAllCFBarred;
                case 25:
                    return registrationCFNotToHPLMNBarred;
                case 26:
                    return registrationInterzonalCFBarred;
                case 27:
                    return registrationInterzonalCFNotToHPLMNBarred;
                case 28:
                    return registrationInternationalCFBarred;
                default:
                    return null;
            }
        }
    }
}
