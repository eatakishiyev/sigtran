/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.NotificationIndicator;

/**
 *
 * @author eatakishiyev
 */
public class GenericNotificationIndicator implements IsupParameter {

    private NotificationIndicator notificationIndicator;

    public GenericNotificationIndicator() {
    }

    public GenericNotificationIndicator(NotificationIndicator notificationIndicator) {
        this.notificationIndicator = notificationIndicator;
    }

    public NotificationIndicator getNotificationIndicator() {
        return notificationIndicator;
    }

    public void setNotificationIndicator(NotificationIndicator notificationIndicator) {
        this.notificationIndicator = notificationIndicator;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (notificationIndicator.value() | 0b10000000)};
    }

    @Override
    public void decode(byte[] data) {
        this.notificationIndicator = NotificationIndicator.getInstance(data[0] & 0b01111111);
    }

    @Override
    public int getParameterCode() {
        return GENERIC_NOTIFICATION_INDICATOR;
    }

}
