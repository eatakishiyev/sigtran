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
public class RouteSelectFailureSpecificInfo {

    private Cause failureCause;

    public RouteSelectFailureSpecificInfo() {
    }

    public RouteSelectFailureSpecificInfo(Cause failureCause) {
        this.failureCause = failureCause;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (failureCause != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            failureCause.encode(baos);

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
                
                this.failureCause = new Cause();
                this.failureCause.decode(new ByteArrayInputStream(data));
            }
        }
    }

    /**
     * @return the failureCause
     */
    public Cause getFailureCause() {
        return failureCause;
    }

    /**
     * @param failureCause the failureCause to set
     */
    public void setFailureCause(Cause failureCause) {
        this.failureCause = failureCause;
    }

}
