/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import azrc.az.sigtran.cap.parameters.DestinationRoutingAddress;
import azrc.az.sigtran.cap.parameters.GenericNumbers;
import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.parameters.OriginalCalledNumber;
import azrc.az.isup.parameters.RedirectingNumber;
import azrc.az.isup.parameters.RedirectionInformation;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface ConnectArg extends CAPMessage{

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException;

    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException;

    /**
     * @return the callingPartysCategory
     */
    public CallingPartysCategory getCallingPartysCategory();

    /**
     * @return the destinationRoutingAddress
     */
    public DestinationRoutingAddress getDestinationRoutingAddress();

    /**
     * @return the extensions
     */
    public byte[] getExtensions();

    /**
     * @return the genericNumbers
     */
    public GenericNumbers getGenericNumbers();

    /**
     * @return the originalCalledPartyID
     */
    public OriginalCalledNumber getOriginalCalledPartyID();

    /**
     * @return the redirectingPartyID
     */
    public RedirectingNumber getRedirectingPartyID();

    /**
     * @return the redirectionInformation
     */
    public RedirectionInformation getRedirectionInformation();

    /**
     * @return the ocsiApplicable
     */
    public boolean isOCSIApplicable();

    /**
     * @return the suppressionOfAnnouncement
     */
    public boolean isSuppressionOfAnnouncement();

    /**
     * @param callingPartysCategory the callingPartysCategory to set
     */
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory);

    /**
     * @param destinationRoutingAddress the destinationRoutingAddress to set
     */
    public void setDestinationRoutingAddress(DestinationRoutingAddress destinationRoutingAddress);

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions);

    /**
     * @param genericNumbers the genericNumbers to set
     */
    public void setGenericNumbers(GenericNumbers genericNumbers);

    /**
     * @param ocsiApplicable the ocsiApplicable to set
     */
    public void setOCSIApplicable(boolean ocsiApplicable);

    /**
     * @return the destinationRoutingAddress
     */

    /**
     * @param originalCalledPartyID the originalCalledPartyID to set
     */
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID);

    /**
     * @param redirectingPartyID the redirectingPartyID to set
     */
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID);

    /**
     * @param redirectionInformation the redirectionInformation to set
     */
    public void setRedirectionInformation(RedirectionInformation redirectionInformation);

    /**
     * @param suppressionOfAnnouncement the suppressionOfAnnouncement to set
     */
    public void setSuppressionOfAnnouncement(boolean suppressionOfAnnouncement);

}
