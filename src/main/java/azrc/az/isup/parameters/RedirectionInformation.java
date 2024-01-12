/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.RedirectingReason;
import azrc.az.isup.enums.OriginalRedirectionReason;
import azrc.az.isup.enums.RedirectingIndicator;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class RedirectionInformation implements IsupParameter {

    private RedirectingIndicator redirectingIndicator;
    private OriginalRedirectionReason originalRedirectionReason;
    private int redirectionCounter = -1;
    private RedirectingReason redirectingReason;

    public RedirectionInformation() {
    }

    public RedirectionInformation(RedirectingIndicator redirectingIndicator, OriginalRedirectionReason originalRedirectionReason) {
        this.redirectingIndicator = redirectingIndicator;
        this.originalRedirectionReason = originalRedirectionReason;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.write(this.encode());
        aos.FinalizeContent(lenPos);
    }

    @Override
    public byte[] encode() throws ParameterOutOfRangeException {
        if (redirectionCounter < 1 || redirectionCounter > 5) {
            throw new ParameterOutOfRangeException("Redirection counter must be in range 1..5. Current Value = " + this.redirectionCounter);
        }

        byte[] data;
        if (redirectingReason != null && redirectionCounter >= 0) {
            data = new byte[2];

            data[1] = (byte) (redirectingReason.value() << 4);
            data[1] = (byte) (data[1] | redirectionCounter);

        } else {
            data = new byte[1];
        }

        data[0] = (byte) (originalRedirectionReason.value() << 4);
        data[0] = (byte) (data[0] | redirectingIndicator.value());

        return data;
    }

    public void decode(AsnInputStream ais) throws IOException, ParameterOutOfRangeException, AsnException {
        byte[] data = ais.readOctetString();
        this.decode(data);
    }

    @Override
    public void decode(byte[] data) throws ParameterOutOfRangeException {
        int b = data[0] & 0xFF;
        this.setRedirectingIndicator(RedirectingIndicator.getInstance(b & 0b00000111));
        this.setOriginalRedirectionReason(OriginalRedirectionReason.getInstance((b >> 4) & 0b00001111));
        if (data.length > 1) {
            b = data[1] & 0xFF;

            this.setRedirectionCounter(b & 0b00000111);

            if (redirectionCounter < 1 || redirectionCounter > 5) {
                throw new ParameterOutOfRangeException("Redirection Counter must be in range 1..5. Current Value = " + this.redirectionCounter);
            }

            this.setRedirectingReason(RedirectingReason.getInstance((b >> 4) & 0b00001111));
        }
    }

    /**
     * @return the redirectingIndicator
     */
    public RedirectingIndicator getRedirectingIndicator() {
        return redirectingIndicator;
    }

    /**
     * @return the originalRedirectionReason
     */
    public OriginalRedirectionReason getOriginalRedirectionReason() {
        return originalRedirectionReason;
    }

    /**
     * @return the redirectionCounter
     */
    public int getRedirectionCounter() {
        return redirectionCounter;
    }

    /**
     * @return the redirectingReason
     */
    public RedirectingReason getRedirectingReason() {
        return redirectingReason;
    }

    /**
     * @param redirectingIndicator the redirectingIndicator to set
     */
    public void setRedirectingIndicator(RedirectingIndicator redirectingIndicator) {
        this.redirectingIndicator = redirectingIndicator;
    }

    /**
     * @param originalRedirectionReason the originalRedirectionReason to set
     */
    public void setOriginalRedirectionReason(OriginalRedirectionReason originalRedirectionReason) {
        this.originalRedirectionReason = originalRedirectionReason;
    }

    /**
     * @param redirectionCounter the redirectionCounter to set
     */
    public void setRedirectionCounter(int redirectionCounter) {
        this.redirectionCounter = redirectionCounter;
    }

    /**
     * @param redirectingReason the redirectingReason to set
     */
    public void setRedirectingReason(RedirectingReason redirectingReason) {
        this.redirectingReason = redirectingReason;
    }

    @Override
    public int getParameterCode() {
        return REDIRECTION_INFORMATION;
    }

}
