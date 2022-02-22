/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ServiceInteractionIndicatorsTwo ::= SEQUENCE { forwardServiceInteractionInd
 * [0] ForwardServiceInteractionInd OPTIONAL, -- applicable to operations
 * InitialDP, Connect and ContinueWithArgument. backwardServiceInteractionInd
 * [1] BackwardServiceInteractionInd OPTIONAL, -- applicable to operations
 * Connect and ContinueWithArgument. bothwayThroughConnectionInd [2]
 * BothwayThroughConnectionInd OPTIONAL, -- applicable to ConnectToResource and
 * EstablishTemporaryConnection connectedNumberTreatmentInd [4]
 * ConnectedNumberTreatmentInd OPTIONAL, -- applicable to Connect and
 * ContinueWithArgument nonCUGCall [13] NULL OPTIONAL, -- applicable to Connect
 * and ContinueWithArgument -- indicates that no parameters for CUG shall be
 * used for the call (i.e. the call shall -- be a non-CUG call). -- If not
 * present, it indicates one of three things: -- a) continue with modified CUG
 * information (when one or more of either CUG Interlock Code -- and Outgoing
 * Access Indicator are present), or -- b) continue with original CUG
 * information (when neither CUG Interlock Code or Outgoing -- Access Indicator
 * are present), i.e. no IN impact. -- c) continue with the original non-CUG
 * call. holdTreatmentIndicator [50] OCTET STRING (SIZE(1)) OPTIONAL, --
 * applicable to InitialDP, Connect and ContinueWithArgument --
 * acceptHoldRequest 'xxxx xx01'B -- rejectHoldRequest 'xxxx xx10'B -- if absent
 * from Connect or ContinueWithArgument, -- then CAMEL service does not affect
 * call hold treatment cwTreatmentIndicator [51] OCTET STRING (SIZE(1))
 * OPTIONAL, -- applicable to InitialDP, Connect and ContinueWithArgument --
 * acceptCw 'xxxx xx01'B -- rejectCw 'xxxx xx10'B -- if absent from Connect or
 * ContinueWithArgument, -- then CAMEL service does not affect call waiting
 * treatment ectTreatmentIndicator [52] OCTET STRING (SIZE(1)) OPTIONAL, --
 * applicable to InitialDP, Connect and ContinueWithArgument -- acceptEctRequest
 * 'xxxx xx01'B -- rejectEctRequest 'xxxx xx10'B -- if absent from Connect or
 * ContinueWithArgument, -- then CAMEL service does not affect explicit call
 * transfer treatment ... }
 *
 * @author eatakishiyev
 */
public class ServiceInteractionIndicatorsTwo {

    private ForwardServiceInteractionInd forwardServiceInteractionInd;
    private BackwardServiceInteractionInd backwardServiceInteractionInd;
    private BothwayThroughConnectionInd bothwayThroughConnectionInd;
    private ConnectedNumberTreatmentInd connectedNumberTreatmentInd;
    private boolean nonCUGCall;
    private HoldTreatmentIndicator holdTreatmentIndicator;
    private CallWaitingTreatmentIndicator callWaitingTreatmentIndicator;
    private ExplicitCallTransferTreatmentIndicator explicitCallTransferTreatmentIndicator;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (this.forwardServiceInteractionInd != null) {
            this.forwardServiceInteractionInd.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }

        if (this.backwardServiceInteractionInd != null) {
            this.backwardServiceInteractionInd.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        if (bothwayThroughConnectionInd != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, bothwayThroughConnectionInd.value());
        }

        if (connectedNumberTreatmentInd != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, connectedNumberTreatmentInd.value());
        }

        if (nonCUGCall) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 13);
        }

        if (holdTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 50, holdTreatmentIndicator.value());
        }

        if (callWaitingTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 51, callWaitingTreatmentIndicator.value());
        }

        if (explicitCallTransferTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 52, explicitCallTransferTreatmentIndicator.value());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting Context specific[2] tag. Get" + ais.getTagClass());
            }

            switch (tag) {
                case 0:
                    this.forwardServiceInteractionInd = new ForwardServiceInteractionInd();
                    this.forwardServiceInteractionInd.decode(ais.readSequenceStream());
                    break;
                case 1:
                    this.backwardServiceInteractionInd = new BackwardServiceInteractionInd();
                    this.backwardServiceInteractionInd.decode(ais.readSequenceStream());
                    break;
                case 2:
                    this.bothwayThroughConnectionInd = BothwayThroughConnectionInd.getInstance((int) ais.readInteger());
                    break;
                case 4:
                    this.connectedNumberTreatmentInd = ConnectedNumberTreatmentInd.getInstance((int) ais.readInteger());
                    break;
                case 13:
                    this.nonCUGCall = true;
                    ais.readNull();
                    break;
                case 50:
                    this.holdTreatmentIndicator = HoldTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
                case 51:
                    this.callWaitingTreatmentIndicator = CallWaitingTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
                case 52:
                    this.explicitCallTransferTreatmentIndicator = ExplicitCallTransferTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
            }
        }
    }

    /**
     * @return the forwardServiceInteractionInd
     */
    public ForwardServiceInteractionInd getForwardServiceInteractionInd() {
        return forwardServiceInteractionInd;
    }

    /**
     * @param forwardServiceInteractionInd the forwardServiceInteractionInd to
     * set
     */
    public void setForwardServiceInteractionInd(ForwardServiceInteractionInd forwardServiceInteractionInd) {
        this.forwardServiceInteractionInd = forwardServiceInteractionInd;
    }

    /**
     * @return the backwardServiceInteractionInd
     */
    public BackwardServiceInteractionInd getBackwardServiceInteractionInd() {
        return backwardServiceInteractionInd;
    }

    /**
     * @param backwardServiceInteractionInd the backwardServiceInteractionInd to
     * set
     */
    public void setBackwardServiceInteractionInd(BackwardServiceInteractionInd backwardServiceInteractionInd) {
        this.backwardServiceInteractionInd = backwardServiceInteractionInd;
    }

    /**
     * @return the bothwayThroughConnectionInd
     */
    public BothwayThroughConnectionInd getBothwayThroughConnectionInd() {
        return bothwayThroughConnectionInd;
    }

    /**
     * @param bothwayThroughConnectionInd the bothwayThroughConnectionInd to set
     */
    public void setBothwayThroughConnectionInd(BothwayThroughConnectionInd bothwayThroughConnectionInd) {
        this.bothwayThroughConnectionInd = bothwayThroughConnectionInd;
    }

    /**
     * @return the connectedNumberTreatmentInd
     */
    public ConnectedNumberTreatmentInd getConnectedNumberTreatmentInd() {
        return connectedNumberTreatmentInd;
    }

    /**
     * @param connectedNumberTreatmentInd the connectedNumberTreatmentInd to set
     */
    public void setConnectedNumberTreatmentInd(ConnectedNumberTreatmentInd connectedNumberTreatmentInd) {
        this.connectedNumberTreatmentInd = connectedNumberTreatmentInd;
    }

    /**
     * @return the nonCUGCall
     */
    public boolean getNonCUGCall() {
        return nonCUGCall;
    }

    /**
     * @param nonCUGCall the nonCUGCall to set
     */
    public void setNonCUGCall(boolean nonCUGCall) {
        this.nonCUGCall = nonCUGCall;
    }

    /**
     * @return the holdTreatmentIndicator
     */
    public HoldTreatmentIndicator getHoldTreatmentIndicator() {
        return holdTreatmentIndicator;
    }

    /**
     * @param holdTreatmentIndicator the holdTreatmentIndicator to set
     */
    public void setHoldTreatmentIndicator(HoldTreatmentIndicator holdTreatmentIndicator) {
        this.holdTreatmentIndicator = holdTreatmentIndicator;
    }

    /**
     * @return the callWaitingTreatmentIndicator
     */
    public CallWaitingTreatmentIndicator getCallWaitingTreatmentIndicator() {
        return callWaitingTreatmentIndicator;
    }

    /**
     * @param callWaitingTreatmentIndicator the callWaitingTreatmentIndicator to
     * set
     */
    public void setCallWaitingTreatmentIndicator(CallWaitingTreatmentIndicator callWaitingTreatmentIndicator) {
        this.callWaitingTreatmentIndicator = callWaitingTreatmentIndicator;
    }

    /**
     * @return the explicitCallTransferTreatmentIndicator
     */
    public ExplicitCallTransferTreatmentIndicator getExplicitCallTransferTreatmentIndicator() {
        return explicitCallTransferTreatmentIndicator;
    }

    /**
     * @param explicitCallTransferTreatmentIndicator the
     * explicitCallTransferTreatmentIndicator to set
     */
    public void setExplicitCallTransferTreatmentIndicator(ExplicitCallTransferTreatmentIndicator explicitCallTransferTreatmentIndicator) {
        this.explicitCallTransferTreatmentIndicator = explicitCallTransferTreatmentIndicator;
    }

    public enum HoldTreatmentIndicator {

        ACCEPT_HOLD_REQUEST(0b00000001),
        REJECT_HOLD_REQUEST(0b00000010),
        UNKNOWN(-1);

        private int value;

        private HoldTreatmentIndicator(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static HoldTreatmentIndicator getInstance(int value) {
            switch (value) {
                case 0b00000001:
                    return ACCEPT_HOLD_REQUEST;
                case 0b00000010:
                    return REJECT_HOLD_REQUEST;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum CallWaitingTreatmentIndicator {

        ACCEPT_CALL_WAITING(0b00000001),
        REJECT_CALL_WAITING(0b00000010),
        UNKNOWN(-1);

        private int value;

        private CallWaitingTreatmentIndicator(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static CallWaitingTreatmentIndicator getInstance(int value) {
            switch (value) {
                case 0b00000001:
                    return ACCEPT_CALL_WAITING;
                case 0b00000010:
                    return REJECT_CALL_WAITING;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum ExplicitCallTransferTreatmentIndicator {

        ACCEPT_ECT_REQUEST(0b00000001),
        REJECT_ECT_REQUEST(0b00000010),
        UNKNOWN(-1);

        private int value;

        public int value() {
            return this.value;
        }

        private ExplicitCallTransferTreatmentIndicator(int value) {
            this.value = value;
        }

        public static ExplicitCallTransferTreatmentIndicator getInstance(int value) {
            switch (value) {
                case 0b00000001:
                    return ACCEPT_ECT_REQUEST;
                case 0b00000010:
                    return REJECT_ECT_REQUEST;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum BothwayThroughConnectionInd {

        BOTH_WAY_PATH_REQUIRED(0),
        BOTH_WAY_PATH_NOT_REQUIRED(1),
        UNKNOWN(-1);

        private final int value;

        private BothwayThroughConnectionInd(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static BothwayThroughConnectionInd getInstance(int value) {
            switch (value) {
                case 0:
                    return BOTH_WAY_PATH_REQUIRED;
                case 1:
                    return BOTH_WAY_PATH_NOT_REQUIRED;
                default:
                    return UNKNOWN;
            }
        }
    }
}
