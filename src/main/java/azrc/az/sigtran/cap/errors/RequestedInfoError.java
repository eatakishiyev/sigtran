/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.errors;

import azrc.az.sigtran.cap.CAPUserErrorCodes;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class RequestedInfoError extends CAPUserError {

    private Error error;

    public RequestedInfoError() {
    }

    public RequestedInfoError(Error error) {
        this.error = error;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IOException, AsnException {
        aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, error.value);
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
        return CAPUserErrorCodes.REQUESTED_INFO_ERROR;
    }

    public enum Error {

        UNKNOWN_REQUESTED_INFO(1),
        REQUESTED_INFO_NOT_AVAILABLE(2);

        private int value;

        private Error(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Error getInstance(int value) {
            switch (value) {
                case 1:
                    return UNKNOWN_REQUESTED_INFO;
                case 2:
                    return REQUESTED_INFO_NOT_AVAILABLE;
                default:
                    return null;
            }
        }
    }
}
