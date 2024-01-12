/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.ClosedUserGroupCallIndicator;

/**
 *
 * @author eatakishiyev
 */
public class OptionalForwardCallIndicators implements IsupParameter {

    private ClosedUserGroupCallIndicator closedUserGroupCallIndicator;
    private boolean additionalInformationWillBeSent;
    private boolean connectedLineIdentityRequested;

    public OptionalForwardCallIndicators() {
    }

    public OptionalForwardCallIndicators(ClosedUserGroupCallIndicator closedUserGroupCallIndicator, boolean additionalInformationWillBeSent, boolean connectedLineIdentityRequested) {
        this.closedUserGroupCallIndicator = closedUserGroupCallIndicator;
        this.additionalInformationWillBeSent = additionalInformationWillBeSent;
        this.connectedLineIdentityRequested = connectedLineIdentityRequested;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OptionalForwardCallIndicators[")
                .append("ClosedUserGroupCallIndicator:").append(closedUserGroupCallIndicator)
                .append("; AdditionalInformationWillBeSent:").append(additionalInformationWillBeSent)
                .append("; ConnectedLineIdentityRequested:").append(connectedLineIdentityRequested)
                .append("]");
        return sb.toString();
    }

    @Override
    public byte[] encode() {
        int result = connectedLineIdentityRequested ? 1 : 0;
        result = (result << 4);//spare
        result = (result << 1) | (additionalInformationWillBeSent ? 1 : 0);
        result = (result << 2) | (closedUserGroupCallIndicator.value());
        return new byte[]{(byte) result};
    }

    @Override
    public void decode(byte[] data) {
        int value = data[0] & 0xFF;
        this.connectedLineIdentityRequested = ((value >> 7) & 0b00000001) == 1;
        this.additionalInformationWillBeSent = ((value >> 2) & 0b00000001) == 1;
        this.closedUserGroupCallIndicator = ClosedUserGroupCallIndicator.getInstance(value & 0b00000011);
    }

    public ClosedUserGroupCallIndicator getClosedUserGroupCallIndicator() {
        return closedUserGroupCallIndicator;
    }

    public boolean isAdditionalInformationWillBeSent() {
        return additionalInformationWillBeSent;
    }

    public boolean isConnectedLineIdentityRequested() {
        return connectedLineIdentityRequested;
    }

    public void setAdditionalInformationWillBeSent(boolean additionalInformationWillBeSent) {
        this.additionalInformationWillBeSent = additionalInformationWillBeSent;
    }

    public void setClosedUserGroupCallIndicator(ClosedUserGroupCallIndicator closedUserGroupCallIndicator) {
        this.closedUserGroupCallIndicator = closedUserGroupCallIndicator;
    }

    public void setConnectedLineIdentityRequested(boolean connectedLineIdentityRequested) {
        this.connectedLineIdentityRequested = connectedLineIdentityRequested;
    }

    @Override
    public int getParameterCode() {
        return OPTIONAL_FORWARD_CALL_INDICATORS;
    }

}
