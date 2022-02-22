/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.isup.parameters.GenericNumber;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class GenericNumbers {

    private final List<GenericNumber> genericNumbers = new ArrayList<>();

    public GenericNumbers() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        for (GenericNumber genericNumber : genericNumbers) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            genericNumber.encode(baos);

            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Unexpected tag received. Expecting Tag[4] TagClass[0], found "
                        + "Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            byte[] data = new byte[ais.readLength()];
            ais.read(data);

            GenericNumber genericNumber = new GenericNumber();
            genericNumber.decode((data));
            this.genericNumbers.add(genericNumber);
        }
    }

    /**
     * @return the genericNumbers
     */
    public List<GenericNumber> getGenericNumbers() {
        return genericNumbers;
    }

    public boolean addGenericNumber(GenericNumber genericNumber) {
        return this.genericNumbers.add(genericNumber);
    }

}
