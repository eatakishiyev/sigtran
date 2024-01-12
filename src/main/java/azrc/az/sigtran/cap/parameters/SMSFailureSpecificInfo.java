/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SMSFailureSpecificInfo {

    private int smsCause;

    public SMSFailureSpecificInfo() {
    }

    public SMSFailureSpecificInfo(int smsCause) {
        this.smsCause = smsCause;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeInteger(tagClass, tag, smsCause);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        int tag = ais.readTag();
        if (tag == 0) {
            this.smsCause = (int) ais.readInteger();
        }
    }

    /**
     * @return the smsCause
     */
    public int getSmsCause() {
        return smsCause;
    }

    /**
     * @param smsCause the smsCause to set
     */
    public void setSmsCause(int smsCause) {
        this.smsCause = smsCause;
    }

}
