/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.isup.parameters.GenericDigits;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class MidCallSpecificInfo {

    private GenericDigits dTMFDigitsCompleted;
    private GenericDigits dTMFDigitsTimeOut;

    private MidCallSpecificInfo() {
    }

    public static MidCallSpecificInfo create() {
        return new MidCallSpecificInfo();
    }

    public static MidCallSpecificInfo createDTMFDigitsCompleted(GenericDigits dTMFDigitsCompleted) {
        MidCallSpecificInfo instance = new MidCallSpecificInfo();
        instance.dTMFDigitsCompleted = dTMFDigitsCompleted;
        return instance;
    }

    public static MidCallSpecificInfo createDTMFDigitsTimeOut(GenericDigits dTMFDigitsTimeOut) {
        MidCallSpecificInfo instance = new MidCallSpecificInfo();
        instance.dTMFDigitsTimeOut = dTMFDigitsTimeOut;
        return instance;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IllegalNumberFormatException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 1);
        int lenPos_ = aos.StartContentDefiniteLength();
        if (dTMFDigitsCompleted != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, dTMFDigitsCompleted.encode());
        } else {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, dTMFDigitsTimeOut.encode());
        }
        aos.FinalizeContent(lenPos_);
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, IllegalNumberFormatException, AsnException {
        int tag = ais.readTag();

        if (tag != 1 || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new IOException(String.format("Incorrect tag received. Expecting Tag[1] Class[2]. Got Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        byte[] data = new byte[ais.readLength()];
        ais.read(data);

        AsnInputStream tmpAis = new AsnInputStream(data);
        tag = tmpAis.readTag();
        if (tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new IOException(String.format("Incorrect tag received. Expecting Class[2]. Got Class[%s]", tmpAis.getTagClass()));
        }

        switch (tag) {
            case 3:
                this.dTMFDigitsCompleted = new GenericDigits();
                this.dTMFDigitsCompleted.decode(tmpAis.readOctetString());
                break;
            case 4:
                this.dTMFDigitsTimeOut = new GenericDigits();
                this.dTMFDigitsTimeOut.decode(tmpAis.readOctetString());
                break;
            default:
                throw new IOException(String.format("Incorrect tag received. Expecting Tag[1] Class[2]. Got Tag[%s] Class[%s]", tag, tmpAis.getTagClass()));
        }
    }

    /**
     * @return the dTMFDigitsCompleted
     */
    public GenericDigits getdTMFDigitsCompleted() {
        return dTMFDigitsCompleted;
    }

    /**
     * @return the dTMFDigitsTimeOut
     */
    public GenericDigits getdTMFDigitsTimeOut() {
        return dTMFDigitsTimeOut;
    }

}
