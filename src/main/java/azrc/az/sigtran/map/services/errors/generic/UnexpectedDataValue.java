/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.generic;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.UnexpectedDataParam;
import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * unexpectedDataValue ERROR ::= {
 * PARAMETER
 * UnexpectedDataParam
 * -- optional
 * -- UnexpectedDataParam must not be used in version <3
 * CODE local:36 }
 *
 *
 * @author eatakishiyev
 */
public class UnexpectedDataValue implements MAPUserError {

    private UnexpectedDataParam unexpectedDataParam;

    public UnexpectedDataValue() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.unexpectedDataParam != null) {
            unexpectedDataParam.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                this.unexpectedDataParam = new UnexpectedDataParam();
                unexpectedDataParam.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.UNEXPECTED_DATA_VALUE;
    }

    /**
     * @return the unexpectedDataParam
     */
    public UnexpectedDataParam getUnexpectedDataParam() {
        return unexpectedDataParam;
    }

    /**
     * @param unexpectedDataParam the unexpectedDataParam to set
     */
    public void setUnexpectedDataParam(UnexpectedDataParam unexpectedDataParam) {
        this.unexpectedDataParam = unexpectedDataParam;
    }
}
