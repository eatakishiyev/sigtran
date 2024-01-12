/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * Ext-NoRepCondTime ::= INTEGER (1..100) Only values 5-30 are used. Values
 * in the ranges 1-4 and 31-100 are reserved for future use. If received:
 * values 1-4 shall be mapped on to value 5 values 31-100 shall be mapped on
 * to value 30
 *
 * @author eatakishiyev
 */
public class ExtNoRepCondTime implements MAPParameter {

    /*
     
     */
    private Integer noRepCondTime;

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeInteger(tagClass, tag, noRepCondTime);
    }

    @Override
    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.noRepCondTime = ((Long) ais.readInteger()).intValue();
    }

    public void encode(ByteArrayOutputStream baos) {
        baos.write(noRepCondTime);
    }

    /**
     * @return the noRepCondTime
     */
    public Integer getNoRepCondTime() {
        return noRepCondTime;
    }

    /**
     * @param noRepCondTime the noRepCondTime to set
     */
    public void setNoRepCondTime(Integer noRepCondTime) {
        if (noRepCondTime >= 1 && noRepCondTime <= 4) {
            this.noRepCondTime = 5;
        }
        if (noRepCondTime >= 31 && noRepCondTime <= 100) {
            this.noRepCondTime = 30;
        }
        this.noRepCondTime = noRepCondTime;
    }
}
