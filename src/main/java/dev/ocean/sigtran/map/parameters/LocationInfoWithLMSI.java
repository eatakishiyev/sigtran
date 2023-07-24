/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.io.Serializable;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class LocationInfoWithLMSI implements Serializable{

    private ISDNAddressString networkNodeNumber;//M
    private LMSI lmsi;//O
    private ExtensionContainer extensionContainer;//O
    private boolean gprsNodeIndicator;//O
    private AdditionalNumber additionalNumber;

    public LocationInfoWithLMSI() {
    }

    public LocationInfoWithLMSI(ISDNAddressString networkNodeNumber) {
        this.networkNodeNumber = networkNodeNumber;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, IOException, AsnException, UnexpectedDataException {
        AsnOutputStream tmpAos = new AsnOutputStream();

        if (getNetworkNodeNumber() == null) {
            throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
        }

        getNetworkNodeNumber().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, tmpAos);

        if (this.lmsi != null) {
            this.lmsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, tmpAos);
        }

        if (this.extensionContainer != null) {
            this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, tmpAos);
        }
        try {
            if (this.gprsNodeIndicator) {
                tmpAos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x05);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

        if (this.additionalNumber != null) {
            this.additionalNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x06, tmpAos);
        }

        byte[] data = tmpAos.toByteArray();

        try {
            aos.writeSequence(tagClass, tag, data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            byte[] data = new byte[length];
            ais.read(data);
            AsnInputStream tmpAis = new AsnInputStream(data);

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !tmpAis.isTagPrimitive()
                    || tag != 0x01) {
                throw new IncorrectSyntaxException("Incorrect encoded tag");
            }
            this.setNetworkNodeNumber(new ISDNAddressString());
            this.getNetworkNodeNumber().decode(tmpAis);

            while (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == Tag.STRING_OCTET) {
                    this.lmsi = new LMSI();
                    this.lmsi.decode(tmpAis);
                } else if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && !tmpAis.isTagPrimitive() & tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(tmpAis);
                } else if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tmpAis.isTagPrimitive() && tag == 0x05) {
                    this.gprsNodeIndicator = true;
                    tmpAis.readNull();
                } else if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && !tmpAis.isTagPrimitive() && tag == 0x06) {
                    this.additionalNumber = new AdditionalNumber();
                    this.additionalNumber.decode(tmpAis);
                } else {
                    throw new IncorrectSyntaxException("Unknown parameter received");
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }


    }

    /**
     * @return the lmsi
     */
    public LMSI getLmsi() {
        return lmsi;
    }

    /**
     * @param lmsi the lmsi to set
     */
    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
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
     * @return the gprsNodeIndicator
     */
    public boolean isGprsNodeIndicator() {
        return gprsNodeIndicator;
    }

    /**
     * @param gprsNodeIndicator the gprsNodeIndicator to set
     */
    public void setGprsNodeIndicator(boolean gprsNodeIndicator) {
        this.gprsNodeIndicator = gprsNodeIndicator;
    }

    /**
     * @return the additionalNumber
     */
    public AdditionalNumber getAdditionalNumber() {
        return additionalNumber;
    }

    /**
     * @param additionalNumber the additionalNumber to set
     */
    public void setAdditionalNumber(AdditionalNumber additionalNumber) {
        this.additionalNumber = additionalNumber;
    }

    /**
     * @return the networkNodeNumber
     */
    public ISDNAddressString getNetworkNodeNumber() {
        return networkNodeNumber;
    }

    /**
     * @param networkNodeNumber the networkNodeNumber to set
     */
    public void setNetworkNodeNumber(ISDNAddressString networkNodeNumber) {
        this.networkNodeNumber = networkNodeNumber;
    }

    @Override
    public String toString() {
        return "LocationInfoWithLMSI{" +
                "networkNodeNumber=" + networkNodeNumber +
                ", lmsi=" + lmsi +
                ", extensionContainer=" + extensionContainer +
                ", gprsNodeIndicator=" + gprsNodeIndicator +
                ", additionalNumber=" + additionalNumber +
                '}';
    }
}
