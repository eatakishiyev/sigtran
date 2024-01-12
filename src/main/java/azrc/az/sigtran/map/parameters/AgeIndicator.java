/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * AgeIndicator ::= OCTET STRING (SIZE (1..6))
 * -- The internal structure of this parameter is implementation specific.
 *
 * @author eatakishiyev
 */
public class AgeIndicator implements MAPParameter {

    private byte[] indicator;

    public AgeIndicator() {
    }

    public AgeIndicator(byte[] indicator) throws IncorrectSyntaxException {
        if (indicator.length > 6) {
            throw new IncorrectSyntaxException("Incorrect length of parameter. Expected OCTET STRING(SIZE(1..6)), actual length is " + indicator.length);
        }
        this.indicator = indicator;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeOctetString(tagClass, tag, indicator);
    }

    @Override
    public void decode(AsnInputStream ais) throws AsnException, IOException, IncorrectSyntaxException {
        this.indicator = ais.readOctetString();
        if (indicator.length > 6) {
            throw new IncorrectSyntaxException("Incorrect length of parameter. Expected OCTET STRING(SIZE(1..6)), actual length is " + indicator.length);
        }
    }

    public byte[] getIndicator() {
        return indicator;
    }

    public void setIndicator(byte[] indicator) throws IncorrectSyntaxException {
        if (indicator.length > 6) {
            throw new IncorrectSyntaxException("Incorrect length of parameter. Expected OCTET STRING(SIZE(1..6)), actual length is " + indicator.length);
        }
        this.indicator = indicator;
    }

}
