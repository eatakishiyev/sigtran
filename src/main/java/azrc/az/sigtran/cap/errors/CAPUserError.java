/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.errors;

import azrc.az.sigtran.cap.CAPUserErrorCodes;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public abstract class CAPUserError {

    public void encode(AsnOutputStream aos) throws Exception {

    }

    public void decode(AsnInputStream ais) throws Exception {

    }

    public abstract CAPUserErrorCodes getErrorCode();
}
