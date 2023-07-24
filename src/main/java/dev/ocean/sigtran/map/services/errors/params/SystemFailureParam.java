/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SystemFailureParam ::= CHOICE {
 * networkResource NetworkResource,
 * -- networkResource must not be used in version 3
 * extensibleSystemFailureParam ExtensibleSystemFailureParam
 * -- extensibleSystemFailureParam must not be used in version <3
 * }
 * @
 * author eatakishiyev
 */
public class SystemFailureParam {

    private NetworkResource networkResource;
    private ExtensibleSystemFailureParam extensibleSystemFailureParam;

    public SystemFailureParam() {
    }

    public SystemFailureParam(NetworkResource networkResource) {
        this.networkResource = networkResource;
    }

    public SystemFailureParam(ExtensibleSystemFailureParam extensibleSystemFailureParam) {
        this.extensibleSystemFailureParam = extensibleSystemFailureParam;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (networkResource != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, networkResource.value());
            } else {
                extensibleSystemFailureParam.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.ENUMERATED
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.networkResource = NetworkResource.getInstance((int) ais.readInteger());
            } else if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensibleSystemFailureParam = new ExtensibleSystemFailureParam();
                extensibleSystemFailureParam.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
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
     * @return the extensibleSystemFailureParam
     */
    public ExtensibleSystemFailureParam getExtensibleSystemFailureParam() {
        return extensibleSystemFailureParam;
    }

    /**
     * @param extensibleSystemFailureParam the extensibleSystemFailureParam to
     * set
     */
    public void setExtensibleSystemFailureParam(ExtensibleSystemFailureParam extensibleSystemFailureParam) {
        this.extensibleSystemFailureParam = extensibleSystemFailureParam;
    }

    @Override
    public String toString() {
        return "SystemFailureParam{" +
                "networkResource=" + networkResource +
                ", extensibleSystemFailureParam=" + extensibleSystemFailureParam +
                '}';
    }
}
