/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * SupportedFeatures::= BIT STRING {
 * odb-all-apn (0),
 * odb-HPLMN-APN (1),
 * odb-VPLMN-APN (2),
 * odb-all-og (3),
 * odb-all-international-og (4),
 * odb-all-int-og-not-to-HPLMN-country (5),
 * odb-all-interzonal-og (6),
 * odb-all-interzonal-og-not-to-HPLMN-country (7),
 * odb-all-interzonal-og-and-internat-og-not-to-HPLMN-country (8),
 * regSub (9),
 * trace (10),
 * lcs-all-PrivExcep (11),
 * lcs-universal (12),
 * lcs-CallSessionRelated (13),
 * lcs-CallSessionUnrelated (14),
 * lcs-PLMN-operator (15),
 * lcs-ServiceType (16),
 * lcs-all-MOLR-SS (17),
 * lcs-basicSelfLocation (18),
 * lcs-autonomousSelfLocation (19),
 * lcs-transferToThirdParty (20),
 * sm-mo-pp (21),
 * barring-OutgoingCalls (22),
 * baoc (23),
 * boic (24),
 * boicExHC (25)} (SIZE (26..40))
 * @author eatakishiyev
 */
public class SupportedFeatures {

    private boolean odbAllApn;
    private boolean odbHPLMNApn;
    private boolean odbVPLMNApn;
    private boolean odbAllOg;
    private boolean odbAllInternationalOg;
    private boolean odbAllIntOgNotToHPLMNCountry;
    private boolean odbAllInterzonalOg;
    private boolean odbAllInterzonalOgNotToHPLMNCountry;
    private boolean odbAllInterzonalOgAndInternatOgNotToHPLMNCountry;
    private boolean regSub;
    private boolean trace;
    private boolean lcsAllPrivExcep;
    private boolean lcsUniversal;
    private boolean lcsCallSessionRelated;
    private boolean lcsCallSessionUnrelated;
    private boolean lcsPLMNOperator;
    private boolean lcsServiceType;
    private boolean lcsAllMOLRSS;
    private boolean lcsBasicSelfLocation;
    private boolean lcsAutonomousSelfLocation;
    private boolean lcsTransferToThirdParty;
    private boolean smMoPP;
    private boolean barringOutgoingCalls;
    private boolean baoc;
    private boolean boic;
    private boolean boicExHC;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = new BitSetStrictLength(26);
            bitSetStrictLength.set(0, odbAllApn);
            bitSetStrictLength.set(1, odbHPLMNApn);
            bitSetStrictLength.set(2, odbVPLMNApn);
            bitSetStrictLength.set(3, odbAllOg);
            bitSetStrictLength.set(4, odbAllInternationalOg);
            bitSetStrictLength.set(5, odbAllIntOgNotToHPLMNCountry);
            bitSetStrictLength.set(6, odbAllInterzonalOg);
            bitSetStrictLength.set(7, odbAllInterzonalOgNotToHPLMNCountry);
            bitSetStrictLength.set(8, odbAllInterzonalOgAndInternatOgNotToHPLMNCountry);
            bitSetStrictLength.set(9, regSub);
            bitSetStrictLength.set(10, trace);
            bitSetStrictLength.set(11, lcsAllPrivExcep);
            bitSetStrictLength.set(12, lcsUniversal);
            bitSetStrictLength.set(13, lcsCallSessionRelated);
            bitSetStrictLength.set(14, lcsCallSessionUnrelated);
            bitSetStrictLength.set(15, lcsPLMNOperator);
            bitSetStrictLength.set(16, lcsServiceType);
            bitSetStrictLength.set(17, lcsAllMOLRSS);
            bitSetStrictLength.set(18, lcsBasicSelfLocation);
            bitSetStrictLength.set(19, lcsAutonomousSelfLocation);
            bitSetStrictLength.set(20, lcsTransferToThirdParty);
            bitSetStrictLength.set(21, smMoPP);
            bitSetStrictLength.set(22, barringOutgoingCalls);
            bitSetStrictLength.set(23, baoc);
            bitSetStrictLength.set(24, boic);
            bitSetStrictLength.set(25, boicExHC);
            aos.writeBitString(tagClass, tag, bitSetStrictLength);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try{
        BitSetStrictLength bitSetStrictLength= ais.readBitString();
        this.odbAllApn = bitSetStrictLength.get(0);
        this.odbHPLMNApn = bitSetStrictLength.get(1);
        this.odbVPLMNApn = bitSetStrictLength.get(2);
        this.odbAllOg = bitSetStrictLength.get(3);
        this.odbAllInternationalOg = bitSetStrictLength.get(4);
        this.odbAllIntOgNotToHPLMNCountry = bitSetStrictLength.get(5);
        this.odbAllInterzonalOg = bitSetStrictLength.get(6);
        this.odbAllInterzonalOgNotToHPLMNCountry = bitSetStrictLength.get(7);
        this.odbAllInterzonalOgAndInternatOgNotToHPLMNCountry = bitSetStrictLength.get(8);
        this.regSub = bitSetStrictLength.get(9);
        this.trace = bitSetStrictLength.get(10);
        this.lcsAllPrivExcep = bitSetStrictLength.get(11);
        this.lcsUniversal = bitSetStrictLength.get(12);
        this.lcsCallSessionRelated =bitSetStrictLength.get(13);
        this.lcsCallSessionUnrelated = bitSetStrictLength.get(14);
        this.lcsPLMNOperator = bitSetStrictLength.get(15);
        this.lcsServiceType = bitSetStrictLength.get(16);
        this.lcsAllMOLRSS = bitSetStrictLength.get(17);
        this.lcsBasicSelfLocation = bitSetStrictLength.get(18);
        this.lcsAutonomousSelfLocation = bitSetStrictLength.get(19);
        this.lcsTransferToThirdParty = bitSetStrictLength.get(20);
        this.smMoPP = bitSetStrictLength.get(21);
        this.barringOutgoingCalls = bitSetStrictLength.get(22);
        this.baoc = bitSetStrictLength.get(23);
        this.boic = bitSetStrictLength.get(24);
        this.boicExHC = bitSetStrictLength.get(25);
        }catch(AsnException | IOException ex){
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the odbAllApn
     */
    public boolean isOdbAllApn() {
        return odbAllApn;
    }

    /**
     * @param odbAllApn the odbAllApn to set
     */
    public void setOdbAllApn(boolean odbAllApn) {
        this.odbAllApn = odbAllApn;
    }

    /**
     * @return the odbHPLMNApn
     */
    public boolean isOdbHPLMNApn() {
        return odbHPLMNApn;
    }

    /**
     * @param odbHPLMNApn the odbHPLMNApn to set
     */
    public void setOdbHPLMNApn(boolean odbHPLMNApn) {
        this.odbHPLMNApn = odbHPLMNApn;
    }

    /**
     * @return the odbVPLMNApn
     */
    public boolean isOdbVPLMNApn() {
        return odbVPLMNApn;
    }

    /**
     * @param odbVPLMNApn the odbVPLMNApn to set
     */
    public void setOdbVPLMNApn(boolean odbVPLMNApn) {
        this.odbVPLMNApn = odbVPLMNApn;
    }

    /**
     * @return the odbAllOg
     */
    public boolean isOdbAllOg() {
        return odbAllOg;
    }

    /**
     * @param odbAllOg the odbAllOg to set
     */
    public void setOdbAllOg(boolean odbAllOg) {
        this.odbAllOg = odbAllOg;
    }

    /**
     * @return the odbAllInternationalOg
     */
    public boolean isOdbAllInternationalOg() {
        return odbAllInternationalOg;
    }

    /**
     * @param odbAllInternationalOg the odbAllInternationalOg to set
     */
    public void setOdbAllInternationalOg(boolean odbAllInternationalOg) {
        this.odbAllInternationalOg = odbAllInternationalOg;
    }

    /**
     * @return the odbAllIntOgNotToHPLMNCountry
     */
    public boolean isOdbAllIntOgNotToHPLMNCountry() {
        return odbAllIntOgNotToHPLMNCountry;
    }

    /**
     * @param odbAllIntOgNotToHPLMNCountry the odbAllIntOgNotToHPLMNCountry to
     * set
     */
    public void setOdbAllIntOgNotToHPLMNCountry(boolean odbAllIntOgNotToHPLMNCountry) {
        this.odbAllIntOgNotToHPLMNCountry = odbAllIntOgNotToHPLMNCountry;
    }

    /**
     * @return the odbAllInterzonalOg
     */
    public boolean isOdbAllInterzonalOg() {
        return odbAllInterzonalOg;
    }

    /**
     * @param odbAllInterzonalOg the odbAllInterzonalOg to set
     */
    public void setOdbAllInterzonalOg(boolean odbAllInterzonalOg) {
        this.odbAllInterzonalOg = odbAllInterzonalOg;
    }

    /**
     * @return the odbAllInterzonalOgNotToHPLMNCountry
     */
    public boolean isOdbAllInterzonalOgNotToHPLMNCountry() {
        return odbAllInterzonalOgNotToHPLMNCountry;
    }

    /**
     * @param odbAllInterzonalOgNotToHPLMNCountry the
     * odbAllInterzonalOgNotToHPLMNCountry to set
     */
    public void setOdbAllInterzonalOgNotToHPLMNCountry(boolean odbAllInterzonalOgNotToHPLMNCountry) {
        this.odbAllInterzonalOgNotToHPLMNCountry = odbAllInterzonalOgNotToHPLMNCountry;
    }

    /**
     * @return the odbAllInterzonalOgAndInternatOgNotToHPLMNCountry
     */
    public boolean isOdbAllInterzonalOgAndInternatOgNotToHPLMNCountry() {
        return odbAllInterzonalOgAndInternatOgNotToHPLMNCountry;
    }

    /**
     * @param odbAllInterzonalOgAndInternatOgNotToHPLMNCountry the
     * odbAllInterzonalOgAndInternatOgNotToHPLMNCountry to set
     */
    public void setOdbAllInterzonalOgAndInternatOgNotToHPLMNCountry(boolean odbAllInterzonalOgAndInternatOgNotToHPLMNCountry) {
        this.odbAllInterzonalOgAndInternatOgNotToHPLMNCountry = odbAllInterzonalOgAndInternatOgNotToHPLMNCountry;
    }

    /**
     * @return the regSub
     */
    public boolean isRegSub() {
        return regSub;
    }

    /**
     * @param regSub the regSub to set
     */
    public void setRegSub(boolean regSub) {
        this.regSub = regSub;
    }

    /**
     * @return the trace
     */
    public boolean isTrace() {
        return trace;
    }

    /**
     * @param trace the trace to set
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    /**
     * @return the lcsAllPrivExcep
     */
    public boolean isLcsAllPrivExcep() {
        return lcsAllPrivExcep;
    }

    /**
     * @param lcsAllPrivExcep the lcsAllPrivExcep to set
     */
    public void setLcsAllPrivExcep(boolean lcsAllPrivExcep) {
        this.lcsAllPrivExcep = lcsAllPrivExcep;
    }

    /**
     * @return the lcsUniversal
     */
    public boolean isLcsUniversal() {
        return lcsUniversal;
    }

    /**
     * @param lcsUniversal the lcsUniversal to set
     */
    public void setLcsUniversal(boolean lcsUniversal) {
        this.lcsUniversal = lcsUniversal;
    }

    /**
     * @return the lcsCallSessionRelated
     */
    public boolean isLcsCallSessionRelated() {
        return lcsCallSessionRelated;
    }

    /**
     * @param lcsCallSessionRelated the lcsCallSessionRelated to set
     */
    public void setLcsCallSessionRelated(boolean lcsCallSessionRelated) {
        this.lcsCallSessionRelated = lcsCallSessionRelated;
    }

    /**
     * @return the lcsCallSessionUnrelated
     */
    public boolean isLcsCallSessionUnrelated() {
        return lcsCallSessionUnrelated;
    }

    /**
     * @param lcsCallSessionUnrelated the lcsCallSessionUnrelated to set
     */
    public void setLcsCallSessionUnrelated(boolean lcsCallSessionUnrelated) {
        this.lcsCallSessionUnrelated = lcsCallSessionUnrelated;
    }

    /**
     * @return the lcsPLMNOperator
     */
    public boolean isLcsPLMNOperator() {
        return lcsPLMNOperator;
    }

    /**
     * @param lcsPLMNOperator the lcsPLMNOperator to set
     */
    public void setLcsPLMNOperator(boolean lcsPLMNOperator) {
        this.lcsPLMNOperator = lcsPLMNOperator;
    }

    /**
     * @return the lcsServiceType
     */
    public boolean isLcsServiceType() {
        return lcsServiceType;
    }

    /**
     * @param lcsServiceType the lcsServiceType to set
     */
    public void setLcsServiceType(boolean lcsServiceType) {
        this.lcsServiceType = lcsServiceType;
    }

    /**
     * @return the lcsAllMOLRSS
     */
    public boolean isLcsAllMOLRSS() {
        return lcsAllMOLRSS;
    }

    /**
     * @param lcsAllMOLRSS the lcsAllMOLRSS to set
     */
    public void setLcsAllMOLRSS(boolean lcsAllMOLRSS) {
        this.lcsAllMOLRSS = lcsAllMOLRSS;
    }

    /**
     * @return the lcsBasicSelfLocation
     */
    public boolean isLcsBasicSelfLocation() {
        return lcsBasicSelfLocation;
    }

    /**
     * @param lcsBasicSelfLocation the lcsBasicSelfLocation to set
     */
    public void setLcsBasicSelfLocation(boolean lcsBasicSelfLocation) {
        this.lcsBasicSelfLocation = lcsBasicSelfLocation;
    }

    /**
     * @return the lcsAutonomousSelfLocation
     */
    public boolean isLcsAutonomousSelfLocation() {
        return lcsAutonomousSelfLocation;
    }

    /**
     * @param lcsAutonomousSelfLocation the lcsAutonomousSelfLocation to set
     */
    public void setLcsAutonomousSelfLocation(boolean lcsAutonomousSelfLocation) {
        this.lcsAutonomousSelfLocation = lcsAutonomousSelfLocation;
    }

    /**
     * @return the lcsTransferToThirdParty
     */
    public boolean isLcsTransferToThirdParty() {
        return lcsTransferToThirdParty;
    }

    /**
     * @param lcsTransferToThirdParty the lcsTransferToThirdParty to set
     */
    public void setLcsTransferToThirdParty(boolean lcsTransferToThirdParty) {
        this.lcsTransferToThirdParty = lcsTransferToThirdParty;
    }

    /**
     * @return the smMoPP
     */
    public boolean isSmMoPP() {
        return smMoPP;
    }

    /**
     * @param smMoPP the smMoPP to set
     */
    public void setSmMoPP(boolean smMoPP) {
        this.smMoPP = smMoPP;
    }

    /**
     * @return the barringOutgoingCalls
     */
    public boolean isBarringOutgoingCalls() {
        return barringOutgoingCalls;
    }

    /**
     * @param barringOutgoingCalls the barringOutgoingCalls to set
     */
    public void setBarringOutgoingCalls(boolean barringOutgoingCalls) {
        this.barringOutgoingCalls = barringOutgoingCalls;
    }

    /**
     * @return the baoc
     */
    public boolean isBaoc() {
        return baoc;
    }

    /**
     * @param baoc the baoc to set
     */
    public void setBaoc(boolean baoc) {
        this.baoc = baoc;
    }

    /**
     * @return the boic
     */
    public boolean isBoic() {
        return boic;
    }

    /**
     * @param boic the boic to set
     */
    public void setBoic(boolean boic) {
        this.boic = boic;
    }

    /**
     * @return the boicExHC
     */
    public boolean isBoicExHC() {
        return boicExHC;
    }

    /**
     * @param boicExHC the boicExHC to set
     */
    public void setBoicExHC(boolean boicExHC) {
        this.boicExHC = boicExHC;
    }
}
