/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class OptionalBackwardCallIndicators implements IsupParameter {

    private boolean inbandIndication; //1
    private boolean callDiversionMayOccur;//1
    private boolean additionalInformationWillBeSentInSegmentationMessage;//1
    private boolean mlppUser;//1

    public OptionalBackwardCallIndicators() {
    }

    public OptionalBackwardCallIndicators(boolean inbandIndication, boolean callDiversionMayOccur, boolean additionalInformationWillBeSentInSegmentationMessage, boolean mlppUser) {
        this.inbandIndication = inbandIndication;
        this.callDiversionMayOccur = callDiversionMayOccur;
        this.additionalInformationWillBeSentInSegmentationMessage = additionalInformationWillBeSentInSegmentationMessage;
        this.mlppUser = mlppUser;
    }

    @Override
    public byte[] encode() throws Exception {
        int i = (mlppUser ? 1 : 0);
        i = (i << 1) | (additionalInformationWillBeSentInSegmentationMessage ? 1 : 0);
        i = (i << 1) | (callDiversionMayOccur ? 1 : 0);
        i = (i << 1) | (inbandIndication ? 1 : 0);

        return new byte[]{(byte) i};
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.inbandIndication = (data[0] & 0b00000001) == 1;
        this.callDiversionMayOccur = ((data[0] >> 1) & 0b00000001) == 1;
        this.additionalInformationWillBeSentInSegmentationMessage = ((data[0] >> 2) & 0b00000001) == 1;
        this.mlppUser = ((data[0] >> 3) & 0b00000001) == 1;
    }

    @Override
    public int getParameterCode() {
        return OPTIONAL_BACKWARD_CALL_INDICATORS;
    }

    public void setAdditionalInformationWillBeSentInSegmentationMessage(boolean additionalInformationWillBeSentInSegmentationMessage) {
        this.additionalInformationWillBeSentInSegmentationMessage = additionalInformationWillBeSentInSegmentationMessage;
    }

    public boolean isAdditionalInformationWillBeSentInSegmentationMessage() {
        return additionalInformationWillBeSentInSegmentationMessage;
    }

    public void setCallDiversionMayOccur(boolean callDiversionMayOccur) {
        this.callDiversionMayOccur = callDiversionMayOccur;
    }

    public boolean isCallDiversionMayOccur() {
        return callDiversionMayOccur;
    }

    public void setInbandIndication(boolean inbandIndication) {
        this.inbandIndication = inbandIndication;
    }

    public boolean isInbandIndication() {
        return inbandIndication;
    }

    public void setMlppUser(boolean mlppUser) {
        this.mlppUser = mlppUser;
    }

    public boolean isMlppUser() {
        return mlppUser;
    }

}
