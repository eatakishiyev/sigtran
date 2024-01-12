/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.SMDeliveryFailureCause;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * sm-DeliveryFailure ERROR ::= {
 * PARAMETER
 * SM-DeliveryFailureCause
 * CODE local:32 }
 * @author eatakishiyev
 */
public class SMDeliveryFailure implements MAPUserError {

    private SMDeliveryFailureCause sMDeliveryFailureCause;

    public SMDeliveryFailure() {
    }

    public SMDeliveryFailure(SMDeliveryFailureCause sMDeliveryFailureCause) {
        this.sMDeliveryFailureCause = sMDeliveryFailureCause;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        sMDeliveryFailureCause.encode(aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        this.sMDeliveryFailureCause = new SMDeliveryFailureCause();
        sMDeliveryFailureCause.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.SM_DELIVERY_FAILURE;
    }

    /**
     * @return the sMDeliveryFailureCause
     */
    public SMDeliveryFailureCause getsMDeliveryFailureCause() {
        return sMDeliveryFailureCause;
    }

    /**
     * @param sMDeliveryFailureCause the sMDeliveryFailureCause to set
     */
    public void setsMDeliveryFailureCause(SMDeliveryFailureCause sMDeliveryFailureCause) {
        this.sMDeliveryFailureCause = sMDeliveryFailureCause;
    }

    @Override
    public String toString() {
        return "SMDeliveryFailure{" +
                "sMDeliveryFailureCause=" + sMDeliveryFailureCause +
                '}';
    }
}
