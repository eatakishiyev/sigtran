/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import java.io.IOException;
import java.util.Arrays;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.*;

/*
 * 16.1.2.3 User information 
 The user information parameter of TC dialogue primitives is used to carry the MAP dialogue APDUs.
 */
/**
 *
 * @author eatakishiyev
 */
public class UserInformationImpl implements UserInformation {

    private long[] directReference;
    private Long indirectReference;
    private String objectDescriptor;
    private Encoding encoding = Encoding.SINGLE_ASN_1;
    private byte[] externalData;

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || tmpAis.isTagPrimitive() || tag != Tag.EXTERNAL) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            External external = new External();
            external.decode(tmpAis);

            this.directReference = external.getOidValue();
            this.indirectReference = external.getIndirectReference();
            this.objectDescriptor = external.getObjDescriptorValue();
            if (external.isAsn()) {
                encoding = Encoding.SINGLE_ASN_1;
            }
            if (external.isOctet()) {
                encoding = Encoding.OCTET_ALIGNED;
            }
            if (external.isArbitrary()) {
                encoding = Encoding.ARBITRARY;
            }

            this.externalData = external.getEncodeType();
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        External external = new External();
        if (directReference != null) {
            external.setOid(true);
            external.setOidValue(directReference);
        }
        if (indirectReference != null) {
            external.setInteger(true);
            external.setIndirectReference(indirectReference);
        }

        if (objectDescriptor != null) {
            external.setObjDescriptor(true);
            external.setObjDescriptorValue(objectDescriptor);
        }

        switch (this.encoding) {
            case ARBITRARY:
                external.setArbitrary(true);
                break;
            case OCTET_ALIGNED:
                external.setOctet(true);
                break;
            case SINGLE_ASN_1:
                external.setAsn(true);
                break;
        }
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x1E);
            external.setEncodeType(externalData);
            
            int position = aos.StartContentDefiniteLength();
            external.encode(aos);

            aos.FinalizeContent(position);

        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    /**
     * @return the directReference
     */
    public long[] getDirectReference() {
        return directReference;
    }

    public void setDirectReference(long[] oid) {
        this.directReference = oid;
    }

    /**
     * @return the indirectReference
     */
    public Long getIndirectReference() {
        return indirectReference;
    }

    /**
     * @param indirectReference the indirectReference to set
     */
    public void setIndirectReference(Long indirectReference) {
        this.indirectReference = indirectReference;
    }

    /**
     * @return the encoding
     */
    public Encoding getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the externalData
     */
    public byte[] getExternalData() {
        return externalData;
    }

    /**
     * @param externalData the externalData to set
     */
    public void setExternalData(byte[] externalData) {
        this.externalData = externalData;
    }

    /**
     * @return the objectDescriptor
     */
    public String getObjectDescriptor() {
        return objectDescriptor;
    }

    /**
     * @param objectDescriptor the objectDescriptor to set
     */
    public void setObjectDescriptor(String objectDescriptor) {
        this.objectDescriptor = objectDescriptor;
    }

    @Override
    public String toString() {
        return String.format("UserInformation: DirectReference = %s IndirectReference = %s "
                + "ObjectDescriptor = %s Encoding = %s ExternalData = %s",
                Arrays.toString(directReference),
                indirectReference,
                objectDescriptor,
                encoding,
                Arrays.toString(externalData));
    }

    public enum Encoding {

        SINGLE_ASN_1,
        OCTET_ALIGNED,
        ARBITRARY;
    }

}
