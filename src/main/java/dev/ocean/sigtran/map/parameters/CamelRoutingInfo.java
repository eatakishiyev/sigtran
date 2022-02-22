/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * CamelRoutingInfo::= SEQUENCE {
 * forwardingData ForwardingData OPTIONAL,
 * gmscCamelSubscriptionInfo [0] GmscCamelSubscriptionInfo,
 * extensionContainer [1] ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class CamelRoutingInfo implements MAPParameter {

    private ForwardingData forwardingData;
    private GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo;
    private ExtensionContainer extensionContainer;

    public CamelRoutingInfo() {
    }

    public CamelRoutingInfo(GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo) {
        this.gmscCamelSubscriptionInfo = gmscCamelSubscriptionInfo;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (forwardingData != null) {
                forwardingData.encode(aos);
            }

            gmscCamelSubscriptionInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.forwardingData = new ForwardingData();
                    forwardingData.decode(ais.readSequenceStream());
                } else if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.gmscCamelSubscriptionInfo = new GmscCamelSubscriptionInfo();
                    gmscCamelSubscriptionInfo.decode(ais.readSequenceStream());
                } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the forwardingData
     */
    public ForwardingData getForwardingData() {
        return forwardingData;
    }

    /**
     * @param forwardingData the forwardingData to set
     */
    public void setForwardingData(ForwardingData forwardingData) {
        this.forwardingData = forwardingData;
    }

    /**
     * @return the gmscCamelSubscriptionInfo
     */
    public GmscCamelSubscriptionInfo getGmscCamelSubscriptionInfo() {
        return gmscCamelSubscriptionInfo;
    }

    /**
     * @param gmscCamelSubscriptionInfo the gmscCamelSubscriptionInfo to set
     */
    public void setGmscCamelSubscriptionInfo(GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo) {
        this.gmscCamelSubscriptionInfo = gmscCamelSubscriptionInfo;
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
