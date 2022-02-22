/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * UU-Data ::= SEQUENCE { uuIndicator [0] UUIndicator OPTIONAL, uui [1] UUI
 * OPTIONAL, uusCFInteraction [2] NULL OPTIONAL, extensionContainer [3]
 * ExtensionContainer OPTIONAL, ...}
 *
 * @author eatakishiyev
 */
public class UUData {

    private UUIndicator uUIndicator;
    private byte[] uuInformation;
    private boolean uusCFInteraction;
    private ExtensionContainer extensionContainer;

    public UUData() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IncorrectSyntaxException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (uUIndicator != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            this.uUIndicator.encode(baos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        if (uuInformation != null) {

            if (uuInformation.length < 1 || uuInformation.length > 131) {
                throw new AsnException("The parameter UUInformation length is out of the range [1..131]. " + uuInformation.length);
            }

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 1);
            aos.writeLength(uuInformation.length);
            aos.write(uuInformation);
        }

        if (uusCFInteraction) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
        }

        if (extensionContainer != null) {
            this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting TagClass CONTEXT_SPECIFIC[2]. Get " + ais.getTagClass());
            }

            switch (tag) {
                case 0:
                    int length = ais.readLength();
                    byte[] data = new byte[length];
                    ais.read(data);

                    this.uUIndicator = new UUIndicator();
                    this.uUIndicator.decode(new ByteArrayInputStream(data));
                    break;
                case 1:
                    length = ais.readLength();
                    if (length < 1 || length > 131) {
                        throw new AsnException("The parameter UUInformation length is out of the range [1..131]. " + length);
                    }

                    this.uuInformation = new byte[length];
                    ais.read(this.uuInformation);
                    break;
                case 2:
                    this.uusCFInteraction = true;
                    ais.readNull();
                    break;
                case 3:
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                    break;
            }
        }
    }

    /**
     * @return the uUIndicator
     */
    public UUIndicator getuUIndicator() {
        return uUIndicator;
    }

    /**
     * @param uUIndicator the uUIndicator to set
     */
    public void setuUIndicator(UUIndicator uUIndicator) {
        this.uUIndicator = uUIndicator;
    }

    /**
     * @return the uuInformation
     */
    public byte[] getUuInformation() {
        return uuInformation;
    }

    /**
     * @param uuInformation the uuInformation to set
     */
    public void setUuInformation(byte[] uuInformation) {
        this.uuInformation = uuInformation;
    }

    /**
     * @return the uusCFInteraction
     */
    public boolean isUusCFInteraction() {
        return uusCFInteraction;
    }

    /**
     * @param uusCFInteraction the uusCFInteraction to set
     */
    public void setUusCFInteraction(boolean uusCFInteraction) {
        this.uusCFInteraction = uusCFInteraction;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

}
