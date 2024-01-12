/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * SMS-TriggerDetectionPoint ::= ENUMERATED {
 * sms-CollectedInfo (1),
 * ...,
 * sms-DeliveryRequest (2)
 * }
 * -- exception handling:
 * -- For SMS-CAMEL-TDP-Data and MT-smsCAMELTDP-Criteria sequences containing
 * this
 * -- parameter with any other value than the ones listed the receiver shall
 * ignore
 * -- the whole sequence.
 * --
 * -- If this
 * @author eatakishiyev
 */
public enum SMSTriggerDetectionPoint {

    smsCollectedInfo(1),
    smsDeliveryRequest(2);
    private int value;

    private SMSTriggerDetectionPoint(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static SMSTriggerDetectionPoint getInstance(int value) {
        switch (value) {
            case 1:
                return smsCollectedInfo;
            case 2:
                return smsDeliveryRequest;
            default:
                return null;
        }
    }
}
