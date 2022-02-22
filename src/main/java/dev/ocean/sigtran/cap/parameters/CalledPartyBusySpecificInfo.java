/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CalledPartyBusySpecificInfo {

    private Cause busyCause;

    public CalledPartyBusySpecificInfo() {
    }

    public CalledPartyBusySpecificInfo(Cause busyCause) {
        this.busyCause = busyCause;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (busyCause != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            busyCause.encode(baos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        
        while(tmpAis.available() > 0){
            int tag = tmpAis.readTag();
            if(tag == 0){
                byte[] data = new byte[tmpAis.readLength()];
                tmpAis.read(data);
                this.busyCause = new Cause();
                this.busyCause.decode(new ByteArrayInputStream(data));
            }
        }
    }

    /**
     * @return the busyCause
     */
    public Cause getBusyCause() {
        return busyCause;
    }

    /**
     * @param busyCause the busyCause to set
     */
    public void setBusyCause(Cause busyCause) {
        this.busyCause = busyCause;
    }

}
