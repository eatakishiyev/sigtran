/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.generic;

import dev.ocean.sigtran.map.services.errors.params.DataMissingParam;
import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * dataMissing ERROR ::= {
 * PARAMETER
 * DataMissingParam
 * -- optional
 * -- DataMissingParam must not be used in version <3
 * CODE local:35 }
 *
 *
 * @author eatakishiyev
 */
public class DataMissing implements MAPUserError {

    private DataMissingParam dataMissingParam;

    public DataMissing() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (dataMissingParam != null) {
            dataMissingParam.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL
                    && tag == Tag.SEQUENCE) {
                this.dataMissingParam = new DataMissingParam();
                dataMissingParam.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[CLASS_UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.DATA_MISSING;
    }

    /**
     * @return the dataMissingParam
     */
    public DataMissingParam getDataMissingParam() {
        return dataMissingParam;
    }

    /**
     * @param dataMissingParam the dataMissingParam to set
     */
    public void setDataMissingParam(DataMissingParam dataMissingParam) {
        this.dataMissingParam = dataMissingParam;
    }
}
