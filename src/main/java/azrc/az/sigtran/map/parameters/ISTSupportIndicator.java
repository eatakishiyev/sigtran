/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * This parameter is used to indicate to the HLR that the VMSC supports basic IST
 * functionality, that is, the VMSC is able
 * to terminate the Subscriber Call Activity that originated the IST Alert when it
 * receives the IST alert response indicating that the call(s) shall be terminated.
 * If this parameter is not included in the Update Location indication and the
 * Subscriber is marked as an IST Subscriber, then the HLR may limit the service
 * for the subscriber (by inducing an Operator Determined barring of Roaming,
 * Incoming or Outgoing calls), or allow service assuming the associated risk of
 * not having the basic IST mechanism available.
 * This parameter can also indicate that the VMSC supports the IST Command service,
 * including the ability to terminate all calls being carried for the identified
 * subscriber by using the IMSI as a key. If this additional capability is not
 * included in the Update Location indication and the HLR supports the IST Command
 * capability, then the HLR may limit the service for the subscriber (by inducing an
 * Operator Determined barring of Roaming, Incoming or Outgoing calls), or allow
 * service assuming the associated risk of not having the IST Command mechanism available.
 *
 * @author eatakishiyev
 */
public enum ISTSupportIndicator {

    BASIC_IST_SUPPORTED(0),
    IST_COMMAND_SUPPORTED(1);

    private final int value;

    private ISTSupportIndicator(int value) {
        this.value = value;
    }

    public static ISTSupportIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return BASIC_IST_SUPPORTED;
            case 1:
                return IST_COMMAND_SUPPORTED;
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
