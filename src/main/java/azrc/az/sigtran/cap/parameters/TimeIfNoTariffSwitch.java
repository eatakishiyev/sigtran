/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class TimeIfNoTariffSwitch {

    private int value;

    public TimeIfNoTariffSwitch() {
    }

    public TimeIfNoTariffSwitch(int value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws ParameterOutOfRangeException, AsnException, IOException {
        this.doCheck();
        aos.writeInteger(tagClass, tag, value);

    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        this.value = (int) ais.readInteger();
        this.doCheck();
    }

    private void doCheck() throws ParameterOutOfRangeException {
        if (value < 0 || value > 864000) {
            throw new ParameterOutOfRangeException(String.format("Parameter is out the range [0..864000], actual value is %d", value));
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
