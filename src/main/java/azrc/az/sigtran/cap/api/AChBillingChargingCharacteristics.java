/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * AChBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING
 * (SIZE (bound.&minAChBillingChargingLength ..
 * bound.&maxAChBillingChargingLength)) (CONSTRAINED BY {-- shall be the result
 * of the BER-encoded value of the type --
 * CAMEL-AChBillingChargingCharacteristics {bound}}) -- The
 * AChBillingChargingCharacteristics parameter specifies the charging related
 * information -- to be provided by the gsmSSF and the conditions on which this
 * information has to be reported -- back to the gsmSCF with the
 * ApplyChargingReport operation. The value of the --
 * AChBillingChargingCharacteristics of type OCTET STRING carries a value of the
 * ASN.1 data type: -- CAMEL-AChBillingChargingCharacteristics. The normal
 * encoding rules are used to encode this -- value. -- The violation of the
 * UserDefinedConstraint shall be handled as an ASN.1 syntax error.
 *
 * @author eatakishiyev
 */
public class AChBillingChargingCharacteristics {

    private CAMELAChBillingChargingCharacteristics cAMELAChBillingChargingCharacteristics;

    public AChBillingChargingCharacteristics() {
    }

    public AChBillingChargingCharacteristics(CAMELAChBillingChargingCharacteristics cAMELAChBillingChargingCharacteristics) {
        this.cAMELAChBillingChargingCharacteristics = cAMELAChBillingChargingCharacteristics;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        this.cAMELAChBillingChargingCharacteristics.encode(aos);
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);

        AsnInputStream tmpAis = new AsnInputStream(data);
        this.cAMELAChBillingChargingCharacteristics = new CAMELAChBillingChargingCharacteristics();
        this.cAMELAChBillingChargingCharacteristics.decode(tmpAis);
    }

    /**
     * @return the cAMELAChBillingChargingCharacteristics
     */
    public CAMELAChBillingChargingCharacteristics getcAMELAChBillingChargingCharacteristics() {
        return cAMELAChBillingChargingCharacteristics;
    }

}
