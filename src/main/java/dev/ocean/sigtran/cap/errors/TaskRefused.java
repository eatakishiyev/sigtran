/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.errors;

import dev.ocean.sigtran.cap.CAPUserErrorCodes;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class TaskRefused extends CAPUserError {

    private Error error;

    public TaskRefused() {
    }

    public TaskRefused(Error error) {
        this.error = error;
    }

    @Override
    public void encode(AsnOutputStream aos) throws Exception {
        aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, error.value());
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            ais.readTag();
            this.error = Error.getInstance((int) ais.readInteger());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public CAPUserErrorCodes getErrorCode() {
        return CAPUserErrorCodes.TASK_REFUSED;
    }

    public Error getError() {
        return this.error;
    }

    public enum Error {

        GENERIC(0),
        UNOBTAINABLE(1),
        CONGESTION(2);

        private int value;

        private Error(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Error getInstance(int value) {
            switch (value) {
                case 0:
                    return GENERIC;
                case 1:
                    return UNOBTAINABLE;
                case 2:
                    return CONGESTION;
                default:
                    return null;
            }
        }
    }
}
