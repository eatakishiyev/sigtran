/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.RedirectStatusIndicator;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class RedirectStatus implements IsupParameter {

    private byte[] redirectStatuses;

    public RedirectStatus() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < redirectStatuses.length; i++) {
            redirectStatuses[i] = (byte) (redirectStatuses[i] & 0b01111111);
        }
        redirectStatuses[redirectStatuses.length - 1] = (byte) (redirectStatuses[redirectStatuses.length - 1] | 0b10000000);
        return (redirectStatuses);
    }

    @Override
    public void decode(byte[] data) {
        this.redirectStatuses = data;
    }

    public byte[] getRedirectStatuses() {
        return redirectStatuses;
    }

    public void setRedirectStatuses(byte[] redirectStatuses) {
        this.redirectStatuses = redirectStatuses;
    }

    @Override
    public int getParameterCode() {
        return REDIRECT_STATUS;
    }

    public static RedirectStatusIndicator getRedirectStatusIndicator(byte b) {
        return RedirectStatusIndicator.getInstance(b & 0b00000011);
    }

    public static byte encodeRedirectStatusByte(RedirectStatusIndicator redirectStatusIndicator) {
        return (byte) (redirectStatusIndicator.value() & 0b00000011);
    }
}
