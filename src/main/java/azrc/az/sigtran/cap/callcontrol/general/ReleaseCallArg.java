/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.cap.api.IReleaseCallArg;
import azrc.az.sigtran.cap.parameters.ITU.Q_850.Cause;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ReleaseCallArg ::= Cause
 *
 * @author eatakishiyev
 */
public class ReleaseCallArg implements IReleaseCallArg {

    private Cause cause;

    public ReleaseCallArg() {

    }

    public ReleaseCallArg(Cause cause) {
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL && tag != Tag.STRING_OCTET) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting Tag[4] Class[0], found Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            byte[] data = new byte[ais.readLength()];
            ais.read(data);

            this.cause = new Cause();
            cause.decode(new ByteArrayInputStream(data));
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            this.cause.encode(baos);

            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            int lenPos = aos.StartContentDefiniteLength();
            aos.write(baos.toByteArray());
            aos.FinalizeContent(lenPos);

        } catch (Exception ex) {
            throw new IncorrectSyntaxException();
        }
    }

}
