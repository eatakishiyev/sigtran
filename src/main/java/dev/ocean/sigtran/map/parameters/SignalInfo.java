/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.Arrays;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

import javax.xml.bind.DatatypeConverter;

/**
 * SignalInfo ::= OCTET STRING (SIZE (1..maxSignalInfoLength))
 *
 * @author eatakishiyev
 */
public class SignalInfo {

    private byte[] data;

    public SignalInfo() {
    }

    public SignalInfo(byte[] data) {
        this.data = data;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, getData());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length < 1 || length > 200) {
                throw new IncorrectSyntaxException("Unexpected parameter length.");
            }
            this.setData(ais.readOctetStringData(length));
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException();
        }
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SignalInfo{" +
                "data=" + DatatypeConverter.printHexBinary(data) +
                '}';
    }
}
