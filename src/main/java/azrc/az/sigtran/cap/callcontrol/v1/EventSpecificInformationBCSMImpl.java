/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v1;

import azrc.az.sigtran.cap.api.EventSpecificInformationBCSM;

import java.io.IOException;
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
 * EventSpecificInformationBCSM ::= CHOICE {
 * oDisconnectSpecificInfo [7] SEQUENCE {
 * releaseCause [0] Cause OPTIONAL
 * --...--
 * },
 * tDisconnectSpecificInfo [12] SEQUENCE {
 * releaseCause [0] Cause OPTIONAL
 * --...--
 * }
 * }
 * Indicates the call related information specific to the event
 *
 * @author eatakishiyev
 */
public class EventSpecificInformationBCSMImpl implements EventSpecificInformationBCSM {

    private DisconnectSpecificInfo oDisconnectSpecificInfo;
    private DisconnectSpecificInfo tDisconnectSpecificInfo;

    private EventSpecificInformationBCSMImpl() {

    }

    public static EventSpecificInformationBCSMImpl create() {
        return new EventSpecificInformationBCSMImpl();
    }

    public static EventSpecificInformationBCSMImpl createODisconnectSpecificInfo(DisconnectSpecificInfo oDisconnectSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.oDisconnectSpecificInfo = oDisconnectSpecificInfo;
        return instance;
    }

    public static EventSpecificInformationBCSMImpl createTDisconnectSpecificInfo(DisconnectSpecificInfo tDisconnectSpecificInfo) {
        EventSpecificInformationBCSMImpl instance = new EventSpecificInformationBCSMImpl();
        instance.tDisconnectSpecificInfo = tDisconnectSpecificInfo;
        return instance;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (oDisconnectSpecificInfo != null) {
            oDisconnectSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
        }
        if (tDisconnectSpecificInfo != null) {
            tDisconnectSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
        }

        aos.FinalizeContent(lenPos);

    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
        }

        switch (tag) {

            case 7:
                this.oDisconnectSpecificInfo = new DisconnectSpecificInfo();
                this.oDisconnectSpecificInfo.decode(ais);
                break;
            case 12:
                this.tDisconnectSpecificInfo = new DisconnectSpecificInfo();
                this.tDisconnectSpecificInfo.decode(ais);
                break;
        }
    }

    public DisconnectSpecificInfo getoDisconnectSpecificInfo() {
        return oDisconnectSpecificInfo;
    }

    public DisconnectSpecificInfo gettDisconnectSpecificInfo() {
        return tDisconnectSpecificInfo;
    }

    @Override
    public CallAcceptedSpecificInfo getCallAcceptedSpecificInfo() {
        return null;
    }

    @Override
    public DpSpecificInfoAlt getDpSpecificInfoAlt() {
        return null;
    }

    @Override
    public OAbandonSpecificInfo getOAbandonSpecificInfo() {
        return null;
    }

    @Override
    public AnswerSpecificInfo getOAnswerSpecificInfo() {
        return null;
    }

    @Override
    public CalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo() {
        return null;
    }

    @Override
    public ChangeOfPositionSpecificInfo getOChangeOfPositionSpecificInfo() {
        return null;
    }

    @Override
    public DisconnectSpecificInfo getODisconnectSpecificInfo() {
        return null;
    }

    @Override
    public MidCallSpecificInfo getOMidCallSpecificInfo() {
        return null;
    }

    @Override
    public ONoAnswerSpecificInfo getONoAnswerSpecificInfo() {
        return null;
    }

    @Override
    public OTermSeizedSpecificInfo getOTermSeizedSpecificInfo() {
        return null;
    }

    @Override
    public RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo() {
        return null;
    }

    @Override
    public AnswerSpecificInfo getTAnswerSpecificInfo() {
        return null;
    }

    @Override
    public TBusySpecificInfo getTBusySpecificInfo() {
        return null;
    }

    @Override
    public ChangeOfPositionSpecificInfo getTChangeOfPositionSpecificInfo() {
        return null;
    }

    @Override
    public DisconnectSpecificInfo getTDisconnectSpecificInfo() {
        return null;
    }

    @Override
    public MidCallSpecificInfo getTMidCallSpecificInfo() {
        return null;
    }

    @Override
    public TNoAnswerSpecificInfo getTNoAnswerSpecificInfo() {
        return null;
    }

}
