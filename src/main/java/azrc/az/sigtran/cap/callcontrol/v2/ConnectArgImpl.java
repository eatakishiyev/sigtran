/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.parameters.OriginalCalledNumber;
import azrc.az.isup.parameters.RedirectingNumber;
import azrc.az.isup.parameters.RedirectionInformation;
import azrc.az.sigtran.cap.api.ConnectArg;
import azrc.az.sigtran.cap.parameters.DestinationRoutingAddress;
import azrc.az.sigtran.cap.parameters.GenericNumbers;
import azrc.az.sigtran.map.parameters.AlertingPattern;

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
 *
 * ConnectArg	::= SEQUENCE {
 * destinationRoutingAddress	[0] DestinationRoutingAddress,
 * alertingPattern	[1] AlertingPattern	OPTIONAL,
 * originalCalledPartyID	[6] OriginalCalledPartyID	OPTIONAL,
 * extensions	[10] SEQUENCE SIZE(1..numOfExtensions) OF
 * ExtensionField	OPTIONAL,
 * callingPartysCategory	[28] CallingPartysCategory	OPTIONAL,
 * redirectingPartyID	[29] RedirectingPartyID	OPTIONAL,
 * redirectionInformation	[30] RedirectionInformation	OPTIONAL,
 * genericNumbers	[14] GenericNumbers	OPTIONAL,
 * suppressionOfAnnouncement	[55] SuppressionOfAnnouncement	OPTIONAL,
 * oCSIApplicable	[56] OCSIApplicable	OPTIONAL,
 * ...,
 * na-Info	[57] NA-Info	OPTIONAL
 * }
 * --	na-Info is included at the discretion of the gsmSCF operator.
 *
 *
 * @author eatakishiyev
 */
public class ConnectArgImpl implements ConnectArg {

    private DestinationRoutingAddress destinationRoutingAddress;
    private AlertingPattern alertingPattern;
    private OriginalCalledNumber originalCalledPartyID;
    private byte[] extensions;
    private CallingPartysCategory callingPartysCategory;
    private RedirectingNumber redirectingPartyID;
    private RedirectionInformation redirectionInformation;
    private GenericNumbers genericNumbers;
    private boolean suppressionOfAnnouncement;
    private boolean oCSIApplicable;
    private byte[] naInfo;

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
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IOException ex) {
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
                    this.destinationRoutingAddress = new DestinationRoutingAddress();
                    this.destinationRoutingAddress.decode(ais);
                    break;
                case 1:
                    this.alertingPattern = AlertingPattern.getInstance((int) ais.readInteger());
                    break;
                case 6:
                    this.originalCalledPartyID = new OriginalCalledNumber();
                    this.originalCalledPartyID.decode(ais);
                    break;
                case 10:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(extensions);
                    break;
                case 28:
                    this.callingPartysCategory = CallingPartysCategory.getInstance((int) ais.readInteger());
                    break;
                case 29:
                    this.redirectingPartyID = new RedirectingNumber();
                    this.redirectingPartyID.decode(ais);
                    break;
                case 30:
                    this.redirectionInformation = new RedirectionInformation();
                    this.redirectionInformation.decode(ais);
                    break;
                case 14:
                    this.genericNumbers = new GenericNumbers();
                    this.genericNumbers.decode(ais);
                    break;
                case 55:
                    this.suppressionOfAnnouncement = true;
                    ais.readNull();
                    break;
                case 56:
                    this.oCSIApplicable = true;
                    ais.readNull();
                    break;
                case 57:
                    this.naInfo = new byte[ais.readLength()];
                    ais.read(naInfo);
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

            destinationRoutingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (alertingPattern != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, alertingPattern.value());
            }

            if (originalCalledPartyID != null) {
                this.originalCalledPartyID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (extensions != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 10, extensions);
            }

            if (callingPartysCategory != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 28, callingPartysCategory.value());
            }

            if (redirectingPartyID != null) {
                redirectingPartyID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 29, aos);
            }

            if (redirectionInformation != null) {
                redirectionInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 30, aos);
            }

            if (genericNumbers != null) {
                genericNumbers.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);

            }

            if (suppressionOfAnnouncement) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 55);
            }

            if (oCSIApplicable) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 56);
            }

            if (naInfo != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 57);
                int lenPos_ = aos.StartContentDefiniteLength();
                aos.write(naInfo);
                aos.FinalizeContent(lenPos_);
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

    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return this.callingPartysCategory;
    }

    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return this.destinationRoutingAddress;
    }

    @Override
    public byte[] getExtensions() {
        return this.extensions;
    }

    @Override
    public GenericNumbers getGenericNumbers() {
        return this.genericNumbers;
    }

    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return this.originalCalledPartyID;
    }

    @Override
    public RedirectingNumber getRedirectingPartyID() {
        return this.redirectingPartyID;
    }

    @Override
    public RedirectionInformation getRedirectionInformation() {
        return this.redirectionInformation;
    }

    @Override
    public boolean isOCSIApplicable() {
        return oCSIApplicable;
    }

    @Override
    public boolean isSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement;
    }

    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    @Override
    public void setDestinationRoutingAddress(DestinationRoutingAddress destinationRoutingAddress) {
        this.destinationRoutingAddress = destinationRoutingAddress;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public void setGenericNumbers(GenericNumbers genericNumbers) {
        this.genericNumbers = genericNumbers;
    }

    @Override
    public void setOCSIApplicable(boolean ocsiApplicable) {
        this.oCSIApplicable = ocsiApplicable;
    }

    @Override
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID) {
        this.originalCalledPartyID = originalCalledPartyID;
    }

    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    @Override
    public void setSuppressionOfAnnouncement(boolean suppressionOfAnnouncement) {
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
    }

    public void setNaInfo(byte[] naInfo) {
        this.naInfo = naInfo;
    }

    public byte[] getNaInfo() {
        return naInfo;
    }

}
