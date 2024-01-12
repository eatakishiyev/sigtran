/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ServiceKey {

    private int serviceKey;

    public ServiceKey() {
    }

    public ServiceKey(int serviceKey) {
        this.serviceKey = serviceKey;
    }

    public void setServiceKey(int serviceKey) {
        this.serviceKey = serviceKey;
    }

    public int getServiceKey() {
        return this.serviceKey;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(serviceKey);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException{
        try{
        aos.writeInteger(tagClass, tag, serviceKey);
        }catch(AsnException| IOException ex){
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        serviceKey = (int) ais.readInteger();
    }
}
