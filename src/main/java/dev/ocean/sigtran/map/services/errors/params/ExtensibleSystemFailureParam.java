/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.services.errors.params.AdditionalNetworkResource;
import dev.ocean.sigtran.map.services.errors.params.FailureCause;
import dev.ocean.sigtran.map.services.errors.params.NetworkResource;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ExtensibleSystemFailureParam ::= SEQUENCE {
 * networkResource NetworkResource OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * additionalNetworkResource [0] AdditionalNetworkResource OPTIONAL,
 * failureCauseParam [1] FailureCauseParam OPTIONAL }
 * @author eatakishiyev
 */
public class ExtensibleSystemFailureParam {

    private NetworkResource networkResource;
    private ExtensionContainer extensionContainer;
    private AdditionalNetworkResource additionalNetworkResource;
    private FailureCause failureCause;

    public ExtensibleSystemFailureParam() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (networkResource != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, networkResource.value());
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (additionalNetworkResource != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, additionalNetworkResource.value());
            }

            if (failureCause != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, failureCause.value());
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case Tag.ENUMERATED:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.networkResource = NetworkResource.getInstance((int) ais.readInteger());
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case Tag.SEQUENCE:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 0:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.additionalNetworkResource = AdditionalNetworkResource.getInstance((int) ais.readInteger());
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 1:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.failureCause = FailureCause.getInstance((int) ais.readInteger());
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the networkResource
     */
    public NetworkResource getNetworkResource() {
        return networkResource;
    }

    /**
     * @param networkResource the networkResource to set
     */
    public void setNetworkResource(NetworkResource networkResource) {
        this.networkResource = networkResource;
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
     * @return the additionalNetworkResource
     */
    public AdditionalNetworkResource getAdditionalNetworkResource() {
        return additionalNetworkResource;
    }

    /**
     * @param additionalNetworkResource the additionalNetworkResource to set
     */
    public void setAdditionalNetworkResource(AdditionalNetworkResource additionalNetworkResource) {
        this.additionalNetworkResource = additionalNetworkResource;
    }

    /**
     * @return the failureCause
     */
    public FailureCause getFailureCause() {
        return failureCause;
    }

    /**
     * @param failureCause the failureCause to set
     */
    public void setFailureCause(FailureCause failureCause) {
        this.failureCause = failureCause;
    }

    @Override
    public String toString() {
        return "ExtensibleSystemFailureParam{" +
                "networkResource=" + networkResource +
                ", extensionContainer=" + extensionContainer +
                ", additionalNetworkResource=" + additionalNetworkResource +
                ", failureCause=" + failureCause +
                '}';
    }
}
