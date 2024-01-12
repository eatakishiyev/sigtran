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
 * SMS-CAMEL-TDP-Data ::= SEQUENCE {
 * sms-TriggerDetectionPoint [0] SMS-TriggerDetectionPoint,
 * serviceKey [1] ServiceKey,
 * gsmSCF-Address [2] ISDN-AddressString,
 * defaultSMS-Handling [3] DefaultSMS-Handling,
 * extensionContainer [4] ExtensionContainer OPTIONAL,
 * ...
 * }
 * @author eatakishiyev
 */
public class SMSCamelTDPData {

    private SMSTriggerDetectionPoint smsTriggerDetectionPoint;
    private Integer serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private DefaultSMSHandling defaultSMSHandling;
    private ExtensionContainer extensionContainer;

    public SMSCamelTDPData() {
    }

    public SMSCamelTDPData(SMSTriggerDetectionPoint smsTriggerDetectionPoint, Integer serviceKey, ISDNAddressString gsmSCFAddress, DefaultSMSHandling defaultSMSHandling) {
        this.smsTriggerDetectionPoint = smsTriggerDetectionPoint;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultSMSHandling = defaultSMSHandling;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, smsTriggerDetectionPoint.getValue());
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, serviceKey);
            gsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, defaultSMSHandling.getValue());
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting Class[CONTEXT] tag. Received Class[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.smsTriggerDetectionPoint = SMSTriggerDetectionPoint.getInstance((int) ais.readInteger());
                        break;
                    case 1:
                        this.serviceKey = (int) ais.readInteger();
                        break;
                    case 2:
                        this.gsmSCFAddress = new ISDNAddressString(ais);
                        break;
                    case 3:
                        this.defaultSMSHandling = DefaultSMSHandling.getInstance((int) ais.readInteger());
                        break;
                    case 4:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the smsTriggerDetectionPoint
     */
    public SMSTriggerDetectionPoint getSmsTriggerDetectionPoint() {
        return smsTriggerDetectionPoint;
    }

    /**
     * @param smsTriggerDetectionPoint the smsTriggerDetectionPoint to set
     */
    public void setSmsTriggerDetectionPoint(SMSTriggerDetectionPoint smsTriggerDetectionPoint) {
        this.smsTriggerDetectionPoint = smsTriggerDetectionPoint;
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
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    /**
     * @param gsmSCFAddress the gsmSCFAddress to set
     */
    public void setGsmSCFAddress(ISDNAddressString gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
    }

    /**
     * @return the defaultSMSHandling
     */
    public DefaultSMSHandling getDefaultSMSHandling() {
        return defaultSMSHandling;
    }

    /**
     * @param defaultSMSHandling the defaultSMSHandling to set
     */
    public void setDefaultSMSHandling(DefaultSMSHandling defaultSMSHandling) {
        this.defaultSMSHandling = defaultSMSHandling;
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
