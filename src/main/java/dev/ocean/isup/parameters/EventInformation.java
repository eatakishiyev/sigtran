/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.EventIndicator;
import dev.ocean.isup.enums.EventPresentationRestrictedIndicator;

/**
 *
 * @author eatakishiyev
 */
public class EventInformation implements IsupParameter {

    private EventIndicator eventIndicator;
    private EventPresentationRestrictedIndicator eventPresentationRestrictedIndicator;

    public EventInformation() {
    }

    public EventInformation(EventIndicator eventIndicator, EventPresentationRestrictedIndicator eventPresentationRestrictedIndicator) {
        this.eventIndicator = eventIndicator;
        this.eventPresentationRestrictedIndicator = eventPresentationRestrictedIndicator;
    }

    @Override
    public byte[] encode() throws Exception {
        return new byte[]{(byte) ((eventPresentationRestrictedIndicator.value() << 7) | eventIndicator.value())};
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.eventIndicator = EventIndicator.getInstance(data[0] & 0b01111111);
        this.eventPresentationRestrictedIndicator = EventPresentationRestrictedIndicator.getInstance((data[0] >> 7) | 0b00000001);
    }

    @Override
    public int getParameterCode() {
        return EVENT_INFORMATION;
    }

    public EventIndicator getEventIndicator() {
        return eventIndicator;
    }

    public void setEventIndicator(EventIndicator eventIndicator) {
        this.eventIndicator = eventIndicator;
    }

    public EventPresentationRestrictedIndicator getEventPresentationRestrictedIndicator() {
        return eventPresentationRestrictedIndicator;
    }

    public void setEventPresentationRestrictedIndicator(EventPresentationRestrictedIndicator eventPresentationRestrictedIndicator) {
        this.eventPresentationRestrictedIndicator = eventPresentationRestrictedIndicator;
    }
}
