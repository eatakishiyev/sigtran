/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.smscontrol.general;

import azrc.az.sigtran.cap.api.CAPMessage;
import azrc.az.sigtran.cap.parameters.CalledPartyBCDNumber;
import azrc.az.sigtran.cap.parameters.SMSAddressString;
import azrc.az.sigtran.map.parameters.ISDNAddressString;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ConnectSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { callingPartysNumber
 * [0] SMS-AddressString OPTIONAL, destinationSubscriberNumber [1]
 * CalledPartyBCDNumber {bound} OPTIONAL, sMSCAddress [2] ISDN-AddressString
 * OPTIONAL, extensions [10] Extensions {bound} OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class ConnectSMSArg implements CAPMessage {

    private SMSAddressString callingPartysNumber;
    private CalledPartyBCDNumber destinationSubscriberNumber;
    private ISDNAddressString smscAddress;
    private byte[] extensions;

    public ConnectSMSArg() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (callingPartysNumber != null) {
                callingPartysNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (destinationSubscriberNumber != null) {
                destinationSubscriberNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (smscAddress != null) {
                smscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (extensions != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 10, extensions);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting TagClass[0] Tag[4], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, UnexpectedDataException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.callingPartysNumber = new SMSAddressString();
                    this.callingPartysNumber.decode(ais);
                    break;
                case 1:
                    this.destinationSubscriberNumber = new CalledPartyBCDNumber();
                    this.destinationSubscriberNumber.decode(ais);
                    break;
                case 2:
                    this.smscAddress = new ISDNAddressString();
                    this.smscAddress.decode(ais);
                    break;
                case 10:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the callingPartysNumber
     */
    public SMSAddressString getCallingPartysNumber() {
        return callingPartysNumber;
    }

    /**
     * @param callingPartysNumber the callingPartysNumber to set
     */
    public void setCallingPartysNumber(SMSAddressString callingPartysNumber) {
        this.callingPartysNumber = callingPartysNumber;
    }

    /**
     * @return the destinationSubscriberNumber
     */
    public CalledPartyBCDNumber getDestinationSubscriberNumber() {
        return destinationSubscriberNumber;
    }

    /**
     * @param destinationSubscriberNumber the destinationSubscriberNumber to set
     */
    public void setDestinationSubscriberNumber(CalledPartyBCDNumber destinationSubscriberNumber) {
        this.destinationSubscriberNumber = destinationSubscriberNumber;
    }

    /**
     * @return the smscAddress
     */
    public ISDNAddressString getSmscAddress() {
        return smscAddress;
    }

    /**
     * @param smscAddress the smscAddress to set
     */
    public void setSmscAddress(ISDNAddressString smscAddress) {
        this.smscAddress = smscAddress;
    }

    /**
     * @return the extensions
     */
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
