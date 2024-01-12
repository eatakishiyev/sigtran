/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.UnidentifedSubParam;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * unidentifiedSubscriber ERROR ::= {
 * PARAMETER
 * UnidentifiedSubParam
 * -- optional
 * -- UunidentifiedSubParam must not be used in version <3
 * CODE local:5 }
 *
 * @author eatakishiyev
 */
public class UnidentifiedSubscriber implements MAPUserError {

    private UnidentifedSubParam unidentifiedSubParam;

    public UnidentifiedSubscriber() {
    }

    public UnidentifiedSubscriber(UnidentifedSubParam unidentifiedSubParam) {
        this.unidentifiedSubParam = unidentifiedSubParam;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (unidentifiedSubParam != null) {
            unidentifiedSubParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.unidentifiedSubParam = new UnidentifedSubParam();
        unidentifiedSubParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.UNIDENTIFIED_SUBSCRIBER;
    }

    /**
     * @return the unidentifiedSubParam
     */
    public UnidentifedSubParam getUnidentifiedSubParam() {
        return unidentifiedSubParam;
    }

    /**
     * @param unidentifiedSubParam the unidentifiedSubParam to set
     */
    public void setUnidentifiedSubParam(UnidentifedSubParam unidentifiedSubParam) {
        this.unidentifiedSubParam = unidentifiedSubParam;
    }

    @Override
    public String toString() {
        return "UnidentifiedSubscriber{" +
                "unidentifiedSubParam=" + unidentifiedSubParam +
                '}';
    }
}
