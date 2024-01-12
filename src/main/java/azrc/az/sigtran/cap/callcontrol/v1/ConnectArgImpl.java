/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v1;

import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.parameters.OriginalCalledNumber;
import azrc.az.isup.parameters.RedirectingNumber;
import azrc.az.isup.parameters.RedirectionInformation;
import azrc.az.sigtran.cap.api.ConnectArg;
import azrc.az.sigtran.cap.parameters.DestinationRoutingAddress;
import azrc.az.sigtran.cap.parameters.GenericNumbers;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ConnectArg ::= SEQUENCE {
 * destinationRoutingAddress [0] DestinationRoutingAddress,
 * originalCalledPartyID [6] OriginalCalledPartyID OPTIONAL,
 * extensions [10] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField
 * OPTIONAL,
 * genericNumbers [14] GenericNumbers OPTIONAL,
 * callingPartysCategory [28] CallingPartysCategory OPTIONAL,
 * redirectingPartyID [29] RedirectingPartyID OPTIONAL,
 * redirectionInformation [30] RedirectionInformation OPTIONAL,
 * suppressionOfAnnouncement [55] SuppressionOfAnnouncement OPTIONAL,
 * oCSIApplicable [56] OCSIApplicable OPTIONAL,
 * ...
 * }
 *
 * @author eatakishiyev
 */
public class ConnectArgImpl implements ConnectArg {

    private DestinationRoutingAddress destinationRoutingAddress;
    private OriginalCalledNumber originalCalledPartyID;
    private byte[] extensions;
    private GenericNumbers genericNumbers;
    private CallingPartysCategory callingPartysCategory;
    private RedirectingNumber redirectingPartyID;
    private RedirectionInformation redirectionInformation;
    private boolean suppressionOfAnnouncement;
    private boolean ocsiApplicable;

    public ConnectArgImpl() {
    }

    public ConnectArgImpl(DestinationRoutingAddress destinationRoutingAddress) {
        this.destinationRoutingAddress = destinationRoutingAddress;
    }

    @Override
    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2] , found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.setDestinationRoutingAddress(new DestinationRoutingAddress());
                    this.getDestinationRoutingAddress().decode(ais);
                    break;
                case 6:
                    this.setOriginalCalledPartyID(new OriginalCalledNumber());
                    this.getOriginalCalledPartyID().decode(ais);
                    break;
                case 10:
                    this.setExtensions(new byte[ais.readLength()]);
                    ais.read(getExtensions());
                    break;
                case 14:
                    this.setGenericNumbers(new GenericNumbers());
                    this.getGenericNumbers().decode(ais);
                    break;
                case 28:
                    this.setCallingPartysCategory(CallingPartysCategory.getInstance((int) ais.readInteger()));
                    break;
                case 29:
                    this.setRedirectingPartyID(new RedirectingNumber());
                    this.getRedirectingPartyID().decode(ais);
                    break;
                case 30:
                    this.setRedirectionInformation(new RedirectionInformation());
                    this.getRedirectionInformation().decode(ais);
                    break;
                case 55:
                    this.setSuppressionOfAnnouncement(true);
                    ais.readNull();
                    break;
                case 56:
                    this.setOCSIApplicable(true);
                    ais.readNull();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            getDestinationRoutingAddress().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (getOriginalCalledPartyID() != null) {
                this.getOriginalCalledPartyID().encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (getExtensions() != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 10);
                aos.writeLength(getExtensions().length);
                aos.write(getExtensions());
            }

            if (getGenericNumbers() != null) {
                getGenericNumbers().encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);

            }

            if (getCallingPartysCategory() != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 28, getCallingPartysCategory().value());
            }

            if (getRedirectingPartyID() != null) {
                getRedirectingPartyID().encode(Tag.CLASS_CONTEXT_SPECIFIC, 29, aos);
            }

            if (getRedirectionInformation() != null) {
                getRedirectionInformation().encode(Tag.CLASS_CONTEXT_SPECIFIC, 30, aos);
            }

            if (isSuppressionOfAnnouncement()) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 55);
            }

            if (isOCSIApplicable()) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 56);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex.getMessage());
        }
    }

    /**
     * @return the destinationRoutingAddress
     */
    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    /**
     * @param destinationRoutingAddress the destinationRoutingAddress to set
     */
    @Override
    public void setDestinationRoutingAddress(DestinationRoutingAddress destinationRoutingAddress) {
        this.destinationRoutingAddress = destinationRoutingAddress;
    }

    /**
     * @return the originalCalledPartyID
     */
    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    /**
     * @param originalCalledPartyID the originalCalledPartyID to set
     */
    @Override
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID) {
        this.originalCalledPartyID = originalCalledPartyID;
    }

    /**
     * @return the extensions
     */
    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    /**
     * @return the genericNumbers
     */
    @Override
    public GenericNumbers getGenericNumbers() {
        return genericNumbers;
    }

    /**
     * @param genericNumbers the genericNumbers to set
     */
    @Override
    public void setGenericNumbers(GenericNumbers genericNumbers) {
        this.genericNumbers = genericNumbers;
    }

    /**
     * @return the callingPartysCategory
     */
    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return callingPartysCategory;
    }

    /**
     * @param callingPartysCategory the callingPartysCategory to set
     */
    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    /**
     * @return the redirectingPartyID
     */
    @Override
    public RedirectingNumber getRedirectingPartyID() {
        return redirectingPartyID;
    }

    /**
     * @param redirectingPartyID the redirectingPartyID to set
     */
    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    /**
     * @return the redirectionInformation
     */
    @Override
    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    /**
     * @param redirectionInformation the redirectionInformation to set
     */
    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    /**
     * @return the suppressionOfAnnouncement
     */
    @Override
    public boolean isSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement;
    }

    /**
     * @param suppressionOfAnnouncement the suppressionOfAnnouncement to set
     */
    @Override
    public void setSuppressionOfAnnouncement(boolean suppressionOfAnnouncement) {
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
    }

    /**
     * @return the ocsiApplicable
     */
    @Override
    public boolean isOCSIApplicable() {
        return ocsiApplicable;
    }

    /**
     * @param ocsiApplicable the ocsiApplicable to set
     */
    @Override
    public void setOCSIApplicable(boolean ocsiApplicable) {
        this.ocsiApplicable = ocsiApplicable;
    }

    /**
     * @return the destinationRoutingAddress
     */
}
