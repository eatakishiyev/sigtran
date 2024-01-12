/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.RedirectPossibleIndicator;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class RedirectCapability implements IsupParameter {

    private byte[] redirectCapabilities;

    public RedirectCapability() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < redirectCapabilities.length; i++) {
            redirectCapabilities[i] = (byte) (redirectCapabilities[i] & 0b00000111);
        }
        redirectCapabilities[redirectCapabilities.length - 1] = (byte) (redirectCapabilities[redirectCapabilities.length - 1] | 0b10000000);
        return (redirectCapabilities);
    }

    @Override
    public void decode(byte[] data) {
        this.redirectCapabilities = data;
    }

    public void setRedirectCapabilities(byte[] redirectCapabilities) {
        this.redirectCapabilities = redirectCapabilities;
    }

    public byte[] getRedirectCapabilities() {
        return redirectCapabilities;
    }

    @Override
    public int getParameterCode() {
        return REDIRECT_CAPABILITY;
    }

    public static byte encodeRedirectCapabilityByte(RedirectPossibleIndicator redirectPossibleIndicator) {
        return (byte) (redirectPossibleIndicator.value() & 0b00000111);
    }

    public static RedirectPossibleIndicator getRedirectPossibleIndicator(byte b) {
        return RedirectPossibleIndicator.getInstance(b & 0b00000111);
    }
}
