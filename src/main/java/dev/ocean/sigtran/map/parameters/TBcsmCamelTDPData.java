/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * T-BcsmCamelTDPData ::= SEQUENCE {
 * t-BcsmTriggerDetectionPoint T-BcsmTriggerDetectionPoint,
 * serviceKey ServiceKey,
 * gsmSCF-Address [0] ISDN-AddressString,
 * defaultCallHandling [1] DefaultCallHandling,
 * extensionContainer [2] ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class TBcsmCamelTDPData {

    private TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    private Integer serviceKey;
    private AddressStringImpl gsmSCFAddress;
    private DefaultCallHandling defaultCallHandling;
    private ExtensionContainer extensionContainer;

    public TBcsmCamelTDPData() {
    }

    public TBcsmCamelTDPData(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, Integer serviceKey, AddressStringImpl gsmSCFAddress, DefaultCallHandling defaultCallHandling) {
        this.tBcsmTriggerDetectionPoint = tBcsmTriggerDetectionPoint;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultCallHandling = defaultCallHandling;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, tBcsmTriggerDetectionPoint.getValue());
            aos.writeInteger(serviceKey);
            gsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, defaultCallHandling.getValue());
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
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
                this.tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.serviceKey = (int) ais.readInteger();
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[INTEGER] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 0
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.gsmSCFAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 1
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.defaultCallHandling = DefaultCallHandling.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 2
                        && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[2] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the tBcsmTriggerDetectionPoint
     */
    public TBcsmTriggerDetectionPoint gettBcsmTriggerDetectionPoint() {
        return tBcsmTriggerDetectionPoint;
    }

    /**
     * @param tBcsmTriggerDetectionPoint the tBcsmTriggerDetectionPoint to set
     */
    public void settBcsmTriggerDetectionPoint(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint) {
        this.tBcsmTriggerDetectionPoint = tBcsmTriggerDetectionPoint;
    }

    /**
     * @return the serviceKey
     */
    public Integer getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(Integer serviceKey) {
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

}
