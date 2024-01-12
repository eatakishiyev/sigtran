/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.parameters.Cause;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface M3UAUser extends Serializable {

    public ServiceIdentificator getServiceIdentificator();

    public void onMtpTransferIndication(MTPTransferMessage mtpTransferMessage) throws Exception;

    public void mtpTransferRequest(MTPTransferMessage mtpTransferMessage) throws Exception;

    public void onMTPPause(int dpc);

    public void onMTPResume(int dpc);

    public void onMTPStatus(int dpc, int congestionLevel);

    public void onMTPStatus(int dpc, Cause cause);
}
