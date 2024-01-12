/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.NotificationSubscriptionOptions;
import azrc.az.isup.enums.RedirectingReason;

/**
 *
 * @author eatakishiyev
 */
public class CallDiversionInformation implements IsupParameter {

    private NotificationSubscriptionOptions notificationSubscriptionOptions;
    private RedirectingReason redirectingReason;

    public CallDiversionInformation() {
    }

    public CallDiversionInformation(NotificationSubscriptionOptions notificationSubscriptionOptions, RedirectingReason redirectingReason) {
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
        this.redirectingReason = redirectingReason;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.notificationSubscriptionOptions = NotificationSubscriptionOptions.getInstance(data[0] & 0b00000111);
        this.redirectingReason = RedirectingReason.getInstance((data[0] >> 3) & 0b00001111);
    }

    @Override
    public byte[] encode() throws Exception {
        return new byte[]{(byte) ((redirectingReason.value() << 3) | notificationSubscriptionOptions.value())};
    }

    public RedirectingReason getRedirectingReason() {
        return redirectingReason;
    }

    public void setRedirectingReason(RedirectingReason redirectingReason) {
        this.redirectingReason = redirectingReason;
    }

    public NotificationSubscriptionOptions getNotificationSubscriptionOptions() {
        return notificationSubscriptionOptions;
    }

    public void setNotificationSubscriptionOptions(NotificationSubscriptionOptions notificationSubscriptionOptions) {
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
    }

    @Override
    public int getParameterCode() {
        return CALL_DIVERSION_INFORMATION;
    }

}
