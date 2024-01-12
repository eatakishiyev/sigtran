/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * MNPInfoRes::= SEQUENCE { routeingNumber [0] RouteingNumber OPTIONAL, imsi [1]
 * IMSI OPTIONAL, msisdn [2] ISDN-AddressString OPTIONAL,
 * numberPortabilityStatus [3] NumberPortabilityStatus OPTIONAL,
 * extensionContainer [4] ExtensionContainer OPTIONAL, ... } -- The IMSI
 * parameter contains a generic IMSI, i.e. it is not tied necessarily to the --
 * Subscriber. MCC and MNC values in this IMSI shall point to the Subscription
 * Network of -- the Subscriber. See 3GPP TS 23.066 [108].
 *
 * @author eatakishiyev
 */
public class MNPInfoRes {

    private RouteingNumber routeingNumber;
    private IMSI imsi;
    private ISDNAddressString msisdn;
    private NumberPortabilityStatus numberPortabilityStatus;
    private ExtensionContainer extensionContainer;

    public MNPInfoRes() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (routeingNumber != null) {
                routeingNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (imsi != null) {
                imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (msisdn != null) {
                msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (numberPortabilityStatus != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, numberPortabilityStatus.value());
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.routeingNumber = new RouteingNumber();
                    routeingNumber.decode(ais);
                } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.imsi = new IMSI(ais);
                } else if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.msisdn = new ISDNAddressString(ais);
                } else if (tag == 3 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.numberPortabilityStatus = NumberPortabilityStatus.getInstance((int) ais.readInteger());
                } else if (tag == 4 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received,", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the routeingNumber
     */
    public RouteingNumber getRouteingNumber() {
        return routeingNumber;
    }

    /**
     * @param routeingNumber the routeingNumber to set
     */
    public void setRouteingNumber(RouteingNumber routeingNumber) {
        this.routeingNumber = routeingNumber;
    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the msisdn
     */
    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the numberPortabilityStatus
     */
    public NumberPortabilityStatus getNumberPortabilityStatus() {
        return numberPortabilityStatus;
    }

    /**
     * @param numberPortabilityStatus the numberPortabilityStatus to set
     */
    public void setNumberPortabilityStatus(NumberPortabilityStatus numberPortabilityStatus) {
        this.numberPortabilityStatus = numberPortabilityStatus;
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
