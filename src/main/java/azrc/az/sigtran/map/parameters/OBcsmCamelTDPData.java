/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * O-BcsmCamelTDPData ::= SEQUENCE {
 * o-BcsmTriggerDetectionPoint O-BcsmTriggerDetectionPoint,
 * serviceKey ServiceKey,
 * gsmSCF-Address [0] ISDN-AddressString,
 * defaultCallHandling [1] DefaultCallHandling,
 * extensionContainer [2] ExtensionContainer OPTIONAL,
 * ...
 * }
 * @author eatakishiyev
 */
public class OBcsmCamelTDPData {

    private OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    private int serviceKey;
    private AddressStringImpl gsmSCFAddress;
    private DefaultCallHandling defaultCallHandling;
    private ExtensionContainer extensionContaier;

    public OBcsmCamelTDPData() {
    }

    public OBcsmCamelTDPData(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, int serviceKey, AddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling) {
        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultCallHandling = defaultCallHandling;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, oBcsmTriggerDetectionPoint.getValue());
            aos.writeInteger(serviceKey);
            gsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, defaultCallHandling.getValue());

            if (extensionContaier != null) {
                extensionContaier.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.ENUMERATED
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[ENUMERATED] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.serviceKey = (int) ais.readInteger();
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[INTEGER] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 0
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.gsmSCFAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[0] Class[CONTEXT]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 1
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.defaultCallHandling = DefaultCallHandling.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[1] Class[CONTEXT]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 2
                        && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContaier = new ExtensionContainer();
                    extensionContaier.decode(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[2] Class[CONTEXT]."
                            + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the oBcsmTriggerDetectionPoint
     */
    public OBcsmTriggerDetectionPoint getoBcsmTriggerDetectionPoint() {
        return oBcsmTriggerDetectionPoint;
    }

    /**
     * @param oBcsmTriggerDetectionPoint the oBcsmTriggerDetectionPoint to set
     */
    public void setoBcsmTriggerDetectionPoint(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint) {
        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
    }

    /**
     * @return the serviceKey
     */
    public int getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(int serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @return the gsmSCFAddress
     */
    public AddressStringImpl getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    /**
     * @param gsmSCFAddress the gsmSCFAddress to set
     */
    public void setGsmSCFAddress(AddressStringImpl gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
    }

    /**
     * @return the defaultCallHandling
     */
    public DefaultCallHandling getDefaultCallHandling() {
        return defaultCallHandling;
    }

    /**
     * @param defaultCallHandling the defaultCallHandling to set
     */
    public void setDefaultCallHandling(DefaultCallHandling defaultCallHandling) {
        this.defaultCallHandling = defaultCallHandling;
    }

    /**
     * @return the extensionContaier
     */
    public ExtensionContainer getExtensionContaier() {
        return extensionContaier;
    }

    /**
     * @param extensionContaier the extensionContaier to set
     */
    public void setExtensionContaier(ExtensionContainer extensionContaier) {
        this.extensionContaier = extensionContaier;
    }
}
