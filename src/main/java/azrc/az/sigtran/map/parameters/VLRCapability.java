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
import org.mobicents.protocols.asn.Tag;

/**
 * VLR-Capability ::= SEQUENCE{
 * supportedCamelPhases [0] SupportedCamelPhases OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ... ,
 * solsaSupportIndicator [2] NULL OPTIONAL,
 * istSupportIndicator [1] IST-SupportIndicator OPTIONAL,
 * superChargerSupportedInServingNetworkEntity [3] SuperChargerInfo OPTIONAL,
 * longFTN-Supported [4] NULL OPTIONAL,
 * supportedLCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL,
 * offeredCamel4CSIs [6] OfferedCamel4CSIs OPTIONAL,
 * supportedRAT-TypesIndicator [7] SupportedRAT-Types OPTIONAL,
 * longGroupID-Supported [8] NULL OPTIONAL,
 * mtRoamingForwardingSupported [9] NULL OPTIONAL }
 *
 * @author eatakishiyev
 */
public class VLRCapability {

    private SupportedCamelPhases supportedCamelPhases;
    private ExtensionContainer extensionContainer;
    private Boolean solsaSupportIndicator = false;
    private ISTSupportIndicator iSTSupportIndicator;
    private SuperChargerInfo superChargerInfo;
    private Boolean longFTNSupported = false;
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    private OfferedCamel4CSIs offeredCamel4CSIs;
    private SupportedRATTypes supportedRATTypes;
    private Boolean longGroupIDSupported = false;
    private Boolean mtRoamingForwardingSupported = false;

    public VLRCapability() {
    }

    public VLRCapability(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        this.decode(ais);
    }

    public final void decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();
            switch (tag) {
                case 0:
                    supportedCamelPhases = new SupportedCamelPhases();
                    supportedCamelPhases.decode(tmpAis);
                    break;
                case Tag.SEQUENCE:
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(tmpAis);
                    break;
                case 2:
                    tmpAis.readNull();
                    setSolsaSupportIndicator((Boolean) true);
                    break;
                case 1:
                    setiSTSupportIndicator(ISTSupportIndicator.getInstance((int) tmpAis.readInteger()));
                    break;
                case 3:
                    setSuperChargerInfo(new SuperChargerInfo());
                    getSuperChargerInfo().decode(tmpAis);
                    break;
                case 4:
                    tmpAis.readNull();
                    setLongFTNSupported((Boolean) true);
                    break;
                case 5:
                    setSupportedLCSCapabilitySets(new SupportedLCSCapabilitySets());
                    getSupportedLCSCapabilitySets().decode(tmpAis);
                    break;
                case 6:
                    setOfferedCamel4CSIs(new OfferedCamel4CSIs());
                    getOfferedCamel4CSIs().decode(tmpAis);
                    break;
                case 7:
                    setSupportedRATTypes(new SupportedRATTypes());
                    getSupportedRATTypes().decode(tmpAis);
                    break;
                case 8:
                    tmpAis.readNull();
                    setLongGroupIDSupported((Boolean) true);
                    break;
                case 9:
                    tmpAis.readNull();
                    setMtRoamingForwardingSupported((Boolean) true);
                    break;
            }
        }
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IncorrectSyntaxException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (supportedCamelPhases != null) {
            supportedCamelPhases.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);
        }

        if (extensionContainer != null) {
            extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
        }

        if (solsaSupportIndicator) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x02);
        }

        if (iSTSupportIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, iSTSupportIndicator.getValue());
        }

        if (getSuperChargerInfo() != null) {
            superChargerInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x03, aos);
        }

        if (longFTNSupported) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x04);
        }

        if (supportedLCSCapabilitySets != null) {
            supportedLCSCapabilitySets.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x05, aos);
        }

        if (offeredCamel4CSIs != null) {
            offeredCamel4CSIs.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x06, aos);
        }

        if (supportedRATTypes != null) {
            supportedRATTypes.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x07, aos);
        }

        if (longGroupIDSupported) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x08);
        }

        if (mtRoamingForwardingSupported) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x09);
        }
        aos.FinalizeContent(lenPos);
    }

    /**
     * @return the supportedCamelPhases
     */
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    /**
     * @param supportedCamelPhases the supportedCamelPhases to set
     */
    public void setSupportedCamelPhases(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    public void setSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        this.supportedCamelPhases = new SupportedCamelPhases(phase1, phase2, phase3, phase4);
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    /**
     * @return the solsaSupportIndicator
     */
    public Boolean getSolsaSupportIndicator() {
        return solsaSupportIndicator;
    }

    /**
     * @param solsaSupportIndicator the solsaSupportIndicator to set
     */
    public void setSolsaSupportIndicator(Boolean solsaSupportIndicator) {
        this.solsaSupportIndicator = solsaSupportIndicator;
    }

    /**
     * @return the iSTSupportIndicator
     */
    public ISTSupportIndicator getiSTSupportIndicator() {
        return iSTSupportIndicator;
    }

    /**
     * @param iSTSupportIndicator the iSTSupportIndicator to set
     */
    public void setiSTSupportIndicator(ISTSupportIndicator iSTSupportIndicator) {
        this.iSTSupportIndicator = iSTSupportIndicator;
    }

    /**
     * @return the superChargerInfo
     */
    public SuperChargerInfo getSuperChargerInfo() {
        return superChargerInfo;
    }

    /**
     * @param superChargerInfo the superChargerInfo to set
     */
    public void setSuperChargerInfo(SuperChargerInfo superChargerInfo) {
        this.superChargerInfo = superChargerInfo;
    }

    /**
     * @return the longFTNSupported
     */
    public Boolean getLongFTNSupported() {
        return longFTNSupported;
    }

    /**
     * @param longFTNSupported the longFTNSupported to set
     */
    public void setLongFTNSupported(Boolean longFTNSupported) {
        this.longFTNSupported = longFTNSupported;
    }

    /**
     * @return the supportedLCSCapabilitySets
     */
    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return supportedLCSCapabilitySets;
    }

    /**
     * @param supportedLCSCapabilitySets the supportedLCSCapabilitySets to set
     */
    public void setSupportedLCSCapabilitySets(SupportedLCSCapabilitySets supportedLCSCapabilitySets) {
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
    }

    /**
     * @return the offeredCamel4CSIs
     */
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    /**
     * @param offeredCamel4CSIs the offeredCamel4CSIs to set
     */
    public void setOfferedCamel4CSIs(OfferedCamel4CSIs offeredCamel4CSIs) {
        this.offeredCamel4CSIs = offeredCamel4CSIs;
    }

    /**
     * @return the supportedRATTypes
     */
    public SupportedRATTypes getSupportedRATTypes() {
        return supportedRATTypes;
    }

    /**
     * @param supportedRATTypes the supportedRATTypes to set
     */
    public void setSupportedRATTypes(SupportedRATTypes supportedRATTypes) {
        this.supportedRATTypes = supportedRATTypes;
    }

    /**
     * @return the longGroupIDSupported
     */
    public Boolean getLongGroupIDSupported() {
        return longGroupIDSupported;
    }

    /**
     * @param longGroupIDSupported the longGroupIDSupported to set
     */
    public void setLongGroupIDSupported(Boolean longGroupIDSupported) {
        this.longGroupIDSupported = longGroupIDSupported;
    }

    /**
     * @return the mtRoamingForwardingSupported
     */
    public Boolean getMtRoamingForwardingSupported() {
        return mtRoamingForwardingSupported;
    }

    /**
     * @param mtRoamingForwardingSupported the mtRoamingForwardingSupported to
     * set
     */
    public void setMtRoamingForwardingSupported(Boolean mtRoamingForwardingSupported) {
        this.mtRoamingForwardingSupported = mtRoamingForwardingSupported;
    }
}
