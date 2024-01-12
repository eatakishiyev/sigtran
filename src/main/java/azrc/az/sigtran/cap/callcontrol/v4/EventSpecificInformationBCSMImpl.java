/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import java.io.IOException;

import azrc.az.sigtran.cap.api.EventSpecificInformationBCSM;
import azrc.az.sigtran.cap.parameters.AnswerSpecificInfo;
import azrc.az.sigtran.cap.parameters.CallAcceptedSpecificInfo;
import azrc.az.sigtran.cap.parameters.CalledPartyBusySpecificInfo;
import azrc.az.sigtran.cap.parameters.ChangeOfPositionSpecificInfo;
import azrc.az.sigtran.cap.parameters.DisconnectSpecificInfo;
import azrc.az.sigtran.cap.parameters.DpSpecificInfoAlt;
import azrc.az.sigtran.cap.parameters.MidCallSpecificInfo;
import azrc.az.sigtran.cap.parameters.OAbandonSpecificInfo;
import azrc.az.sigtran.cap.parameters.ONoAnswerSpecificInfo;
import azrc.az.sigtran.cap.parameters.OTermSeizedSpecificInfo;
import azrc.az.sigtran.cap.parameters.RouteSelectFailureSpecificInfo;
import azrc.az.sigtran.cap.parameters.TBusySpecificInfo;
import azrc.az.sigtran.cap.parameters.TNoAnswerSpecificInfo;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EventSpecificInformationBCSM {PARAMETERS-BOUND : bound} ::= CHOICE {
 * routeSelectFailureSpecificInfo [2] SEQUENCE {
 * failureCause [0] Cause {bound} OPTIONAL,
 * ...
 * },
 * oCalledPartyBusySpecificInfo [3] SEQUENCE {
 * busyCause [0] Cause {bound} OPTIONAL,
 * ...
 * },
 * oNoAnswerSpecificInfo [4] SEQUENCE {
 * -- no specific info defined --
 * ...
 * },
 * oAnswerSpecificInfo [5] SEQUENCE {
 * destinationAddress [50] CalledPartyNumber {bound} OPTIONAL,
 * or-Call [51] NULL OPTIONAL,
 * forwardedCall [52] NULL OPTIONAL,
 * chargeIndicator [53] ChargeIndicator OPTIONAL,
 * ext-basicServiceCode [54] Ext-BasicServiceCode OPTIONAL,
 * ext-basicServiceCode2 [55] Ext-BasicServiceCode OPTIONAL,
 * ...
 * },
 * oMidCallSpecificInfo [6] SEQUENCE {
 * midCallEvents [1] CHOICE {
 * dTMFDigitsCompleted [3] Digits {bound},
 * dTMFDigitsTimeOut [4] Digits {bound}
 * } OPTIONAL,
 * ...
 * },
 * oDisconnectSpecificInfo [7] SEQUENCE {
 * releaseCause [0] Cause {bound} OPTIONAL,
 * ...
 * },
 * tBusySpecificInfo [8] SEQUENCE {
 * busyCause [0] Cause {bound} OPTIONAL,
 * callForwarded [50] NULL OPTIONAL,
 * routeNotPermitted [51] NULL OPTIONAL,
 * forwardingDestinationNumber [52] CalledPartyNumber {bound} OPTIONAL,
 * ...
 * },
 * tNoAnswerSpecificInfo [9] SEQUENCE {
 * callForwarded [50] NULL OPTIONAL,
 * forwardingDestinationNumber [52] CalledPartyNumber {bound} OPTIONAL,
 * ...
 * },
 * tAnswerSpecificInfo [10] SEQUENCE {
 * destinationAddress [50] CalledPartyNumber {bound} OPTIONAL,
 * or-Call [51] NULL OPTIONAL,
 * forwardedCall [52] NULL OPTIONAL,
 * chargeIndicator [53] ChargeIndicator OPTIONAL,
 * ext-basicServiceCode [54] Ext-BasicServiceCode OPTIONAL,
 * ext-basicServiceCode2 [55] Ext-BasicServiceCode OPTIONAL,
 * ...
 * },
 * tMidCallSpecificInfo [11] SEQUENCE {
 * midCallEvents [1] CHOICE {
 * dTMFDigitsCompleted [3] Digits {bound},
 * dTMFDigitsTimeOut [4] Digits {bound}
 * } OPTIONAL,
 * ...
 * },
 * tDisconnectSpecificInfo [12] SEQUENCE {
 * releaseCause [0] Cause {bound} OPTIONAL,
 * ...
 * },
 * oTermSeizedSpecificInfo [13] SEQUENCE {
 * locationInformation [50] LocationInformation OPTIONAL,
 * ...
 * },
 * callAcceptedSpecificInfo [20] SEQUENCE {
 * locationInformation [50] LocationInformation OPTIONAL,
 * ...
 * },
 * oAbandonSpecificInfo [21] SEQUENCE {
 * routeNotPermitted [50] NULL OPTIONAL,
 * ...
 * },
 * oChangeOfPositionSpecificInfo [50] SEQUENCE {
 * locationInformation [50] LocationInformation OPTIONAL,
 * ...,
 * metDPCriteriaList [51] MetDPCriteriaList {bound} OPTIONAL
 * },
 * tChangeOfPositionSpecificInfo [51] SEQUENCE {
 * locationInformation [50] LocationInformation OPTIONAL,
 * ...,
 * metDPCriteriaList [51] MetDPCriteriaList {bound} OPTIONAL
 * },
 * dpSpecificInfoAlt [52] DpSpecificInfoAlt {bound}
 * }
 *
 * @author eatakishiyev
 */
public class EventSpecificInformationBCSMImpl implements EventSpecificInformationBCSM {

    private RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo;
    private CalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo;
    private ONoAnswerSpecificInfo oNoAnswerSpecificInfo;
    private AnswerSpecificInfo oAnswerSpecificInfo;
    private MidCallSpecificInfo oMidCallSpecificInfo;
    private DisconnectSpecificInfo oDisconnectSpecificInfo;
    private TBusySpecificInfo tBusySpecificInfo;
    private TNoAnswerSpecificInfo tNoAnswerSpecificInfo;
    private AnswerSpecificInfo tAnswerSpecificInfo;
    private MidCallSpecificInfo tMidCallSpecificInfo;
    private DisconnectSpecificInfo tDisconnectSpecificInfo;
    private OTermSeizedSpecificInfo oTermSeizedSpecificInfo;
    private CallAcceptedSpecificInfo callAcceptedSpecificInfo;
    private OAbandonSpecificInfo oAbandonSpecificInfo;
    private ChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo;
    private ChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo;
    //    --  This datatype is for extension in future releases.
    private DpSpecificInfoAlt dpSpecificInfoAlt;

    private EventSpecificInformationBCSMImpl() {
    }

    public static EventSpecificInformationBCSMImpl create() {
        return new EventSpecificInformationBCSMImpl();
    }

    public static EventSpecificInformationBCSMImpl createRouteSelectFailureSpecInfo(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.routeSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createCalledPartyBusySpecificInfo(CalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oCalledPartyBusySpecificInfo = oCalledPartyBusySpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl crateONoAsnwerSpecificInfo(ONoAnswerSpecificInfo oNoAnswerSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oNoAnswerSpecificInfo = oNoAnswerSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createOAnswerSpecificInfo(AnswerSpecificInfo oAnswerSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oAnswerSpecificInfo = oAnswerSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createOMidCallSpecificInfo(MidCallSpecificInfo oMidCallSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oMidCallSpecificInfo = oMidCallSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createODisconnectSpecificInf(DisconnectSpecificInfo oDisconnectSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oDisconnectSpecificInfo = oDisconnectSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTBusySpecificInfo(TBusySpecificInfo tBusySpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tBusySpecificInfo = tBusySpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTNoAnswerSpecificInfo(TNoAnswerSpecificInfo tNoAnswerSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tNoAnswerSpecificInfo = tNoAnswerSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTAnswerSpecificInfo(AnswerSpecificInfo tAnswerSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tAnswerSpecificInfo = tAnswerSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTMidCallSpecificInfo(MidCallSpecificInfo tMidCallSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tMidCallSpecificInfo = tMidCallSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTDisconnectSpecificInfo(DisconnectSpecificInfo tDisconnectSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tDisconnectSpecificInfo = tDisconnectSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createOTermSeizedSpecificInfo(OTermSeizedSpecificInfo oTermSeizedSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oTermSeizedSpecificInfo = oTermSeizedSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createCallAcceptedSpecificInfo(CallAcceptedSpecificInfo callAcceptedSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.callAcceptedSpecificInfo = callAcceptedSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createOAbandonSpecificInfo(OAbandonSpecificInfo oAbandonSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oAbandonSpecificInfo = oAbandonSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createOChangeOfPositionSpecificInfo(ChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oChangeOfPositionSpecificInfo = oChangeOfPositionSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTChangeOfPositionSpecificInfo(ChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tChangeOfPositionSpecificInfo = tChangeOfPositionSpecificInfo;
        return instance;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (routeSelectFailureSpecificInfo != null) {
            routeSelectFailureSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        } else if (oCalledPartyBusySpecificInfo != null) {
            oCalledPartyBusySpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        } else if (oNoAnswerSpecificInfo != null) {
            oNoAnswerSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
        } else if (oAnswerSpecificInfo != null) {
            oAnswerSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        } else if (oMidCallSpecificInfo != null) {
            oMidCallSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
        } else if (oDisconnectSpecificInfo != null) {
            oDisconnectSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
        } else if (tBusySpecificInfo != null) {
            tBusySpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
        } else if (tNoAnswerSpecificInfo != null) {
            tNoAnswerSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
        } else if (tAnswerSpecificInfo != null) {
            tAnswerSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
        } else if (tMidCallSpecificInfo != null) {
            tMidCallSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
        } else if (tDisconnectSpecificInfo != null) {
            tDisconnectSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
        } else if (oTermSeizedSpecificInfo != null) {
            oTermSeizedSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 13, aos);
        } else if (callAcceptedSpecificInfo != null) {
            callAcceptedSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 20, aos);
        } else if (oAbandonSpecificInfo != null) {
            oAbandonSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 21, aos);
        } else if (oChangeOfPositionSpecificInfo != null) {
            oChangeOfPositionSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 51, aos);
        } else if (dpSpecificInfoAlt != null) {
            this.dpSpecificInfoAlt.encode(Tag.CLASS_CONTEXT_SPECIFIC, 52, aos);
        }

        aos.FinalizeContent(lenPos);

    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
        }

        switch (tag) {
            case 2:
                this.routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfo();
                this.routeSelectFailureSpecificInfo.decode(ais);
                break;
            case 3:
                this.oCalledPartyBusySpecificInfo = new CalledPartyBusySpecificInfo();
                this.oCalledPartyBusySpecificInfo.decode(ais);
                break;
            case 4:
                this.oNoAnswerSpecificInfo = new ONoAnswerSpecificInfo();
                this.oNoAnswerSpecificInfo.decode(ais);
                break;
            case 5:
                this.oAnswerSpecificInfo = new AnswerSpecificInfo();
                this.oAnswerSpecificInfo.decode(ais);
                break;
            case 6:
                this.oMidCallSpecificInfo = MidCallSpecificInfo.create();
                this.oMidCallSpecificInfo.decode(ais);
                break;
            case 7:
                this.oDisconnectSpecificInfo = new DisconnectSpecificInfo();
                this.oDisconnectSpecificInfo.decode(ais);
                break;
            case 8:
                this.tBusySpecificInfo = new TBusySpecificInfo();
                this.tBusySpecificInfo.decode(ais);
                break;
            case 9:
                this.tNoAnswerSpecificInfo = new TNoAnswerSpecificInfo();
                this.tNoAnswerSpecificInfo.decode(ais);
                break;
            case 10:
                this.tAnswerSpecificInfo = new AnswerSpecificInfo();
                this.tAnswerSpecificInfo.decode(ais);
                break;
            case 11:
                this.tMidCallSpecificInfo = MidCallSpecificInfo.create();
                this.tMidCallSpecificInfo.decode(ais);
                break;
            case 12:
                this.tDisconnectSpecificInfo = new DisconnectSpecificInfo();
                this.tDisconnectSpecificInfo.decode(ais);
                break;
            case 13:
                this.oTermSeizedSpecificInfo = new OTermSeizedSpecificInfo();
                this.oTermSeizedSpecificInfo.decode(ais);
                break;
            case 20:
                this.callAcceptedSpecificInfo = new CallAcceptedSpecificInfo();
                this.callAcceptedSpecificInfo.decode(ais);
                break;
            case 21:
                this.oAbandonSpecificInfo = new OAbandonSpecificInfo();
                this.oAbandonSpecificInfo.decode(ais);
                break;
            case 50:
                this.oChangeOfPositionSpecificInfo = new ChangeOfPositionSpecificInfo();
                this.oChangeOfPositionSpecificInfo.decode(ais);
                break;
            case 51:
                this.tChangeOfPositionSpecificInfo = new ChangeOfPositionSpecificInfo();
                this.tChangeOfPositionSpecificInfo.decode(ais);
                break;
            case 52:
                this.dpSpecificInfoAlt = new DpSpecificInfoAlt();
                this.dpSpecificInfoAlt.decode(ais);
                break;
        }
    }

    /**
     * @return the routeSelectFailureSpecificInfo
     */
    @Override
    public RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo() {
        return routeSelectFailureSpecificInfo;
    }

    /**
     * @return the oCalledPartyBusySpecificInfo
     */
    @Override
    public CalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo() {
        return oCalledPartyBusySpecificInfo;
    }

    /**
     * @return the oNoAnswerSpecificInfo
     */
    @Override
    public ONoAnswerSpecificInfo getONoAnswerSpecificInfo() {
        return oNoAnswerSpecificInfo;
    }

    /**
     * @return the oAnswerSpecificInfo
     */
    @Override
    public AnswerSpecificInfo getOAnswerSpecificInfo() {
        return oAnswerSpecificInfo;
    }

    /**
     * @return the omidCallSpecificInfo
     */
    @Override
    public MidCallSpecificInfo getOMidCallSpecificInfo() {
        return oMidCallSpecificInfo;
    }

    /**
     * @return the oDisconnectSpecificInfo
     */
    @Override
    public DisconnectSpecificInfo getODisconnectSpecificInfo() {
        return oDisconnectSpecificInfo;
    }

    /**
     * @return the tBusySpecificInfo
     */
    @Override
    public TBusySpecificInfo getTBusySpecificInfo() {
        return tBusySpecificInfo;
    }

    /**
     * @return the tNoAnswerSpecificInfo
     */
    @Override
    public TNoAnswerSpecificInfo getTNoAnswerSpecificInfo() {
        return tNoAnswerSpecificInfo;
    }

    /**
     * @return the tAnswerSpecificInfo
     */
    @Override
    public AnswerSpecificInfo getTAnswerSpecificInfo() {
        return tAnswerSpecificInfo;
    }

    /**
     * @return the tMidCallSpecificInfo
     */
    @Override
    public MidCallSpecificInfo getTMidCallSpecificInfo() {
        return tMidCallSpecificInfo;
    }

    /**
     * @return the tDisconnectSpecificInfo
     */
    @Override
    public DisconnectSpecificInfo getTDisconnectSpecificInfo() {
        return tDisconnectSpecificInfo;
    }

    /**
     * @return the oTermSeizedSpecificInfo
     */
    @Override
    public OTermSeizedSpecificInfo getOTermSeizedSpecificInfo() {
        return oTermSeizedSpecificInfo;
    }

    /**
     * @return the callAcceptedSpecificInfo
     */
    @Override
    public CallAcceptedSpecificInfo getCallAcceptedSpecificInfo() {
        return callAcceptedSpecificInfo;
    }

    /**
     * @return the oAbandonSpecificInfo
     */
    @Override
    public OAbandonSpecificInfo getOAbandonSpecificInfo() {
        return oAbandonSpecificInfo;
    }

    /**
     * @return the oChangeOfPositionSpecificInfo
     */
    @Override
    public ChangeOfPositionSpecificInfo getOChangeOfPositionSpecificInfo() {
        return oChangeOfPositionSpecificInfo;
    }

    /**
     * @return the tChangeOfPositionSpecificInfo
     */
    @Override
    public ChangeOfPositionSpecificInfo getTChangeOfPositionSpecificInfo() {
        return tChangeOfPositionSpecificInfo;
    }

    /**
     * @return the dpSpecificInfoAlt
     */
    @Override
    public DpSpecificInfoAlt getDpSpecificInfoAlt() {
        return dpSpecificInfoAlt;
    }

}
