/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.ussd;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPResponse;

/**
 *
 * @author eatakishiyev
 */
public class UssdRes implements MAPResponse {

    private Integer ussdDataCodingScheme;
    private byte[] ussdString;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public UssdRes() {
    }

    public UssdRes(int ussdDataCodingScheme, byte[] ussdString) {
        this.ussdDataCodingScheme = ussdDataCodingScheme;
        this.ussdString = ussdString;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.ussdDataCodingScheme == null) {
                throw new IncorrectSyntaxException("Expecting mandatory parameter: UssdDataCodingScheme");
            }

            if (this.ussdString == null) {
                throw new IncorrectSyntaxException("Expecting mandatory parameter: UssdString");
            }
            //ussdDataCodingScheme
            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            aos.writeLength(1);
            aos.write(this.ussdDataCodingScheme);

            //ussdString
            aos.writeOctetString(ussdString);
            aos.FinalizeContent(lenPos);

        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {

            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());

        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, IncorrectSyntaxException {
        int tag = ais.readTag();
        if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException(String.format("UssdDataCodingScheme: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                    tag, ais.isTagPrimitive(), ais.getTagClass()));
        }

        int length = ais.readLength();
        this.ussdDataCodingScheme = ais.read();

        tag = ais.readTag();
        if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException(String.format("UssdDataCodingScheme: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                    tag, ais.isTagPrimitive(), ais.getTagClass()));
        }

        length = ais.readLength();
        this.ussdString = new byte[length];
        ais.read(ussdString);
    }

    /**
     * @return the ussdDataCodingScheme
     */
    public int getUssdDataCodingScheme() {
        return ussdDataCodingScheme;
    }

    /**
     * @param ussdDataCodingScheme the ussdDataCodingScheme to set
     */
    public void setUssdDataCodingScheme(int ussdDataCodingScheme) {
        this.ussdDataCodingScheme = ussdDataCodingScheme;
    }

    /**
     * @return the ussdString
     */
    public byte[] getUssdString() {
        return ussdString;
    }

    /**
     * @param ussdString the ussdString to set
     */
    public void setUssdString(byte[] ussdString) {
        this.ussdString = ussdString;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
