/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import java.io.IOException;

import azrc.az.sigtran.cap.api.IPlayAnnouncementArg;
import azrc.az.sigtran.cap.parameters.CallSegmentID;
import azrc.az.sigtran.cap.parameters.InformationToSend;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class PlayAnnouncementArg implements IPlayAnnouncementArg {

    private InformationToSend informationToSend;
    private boolean disconnectFromIpForbidden = true;
    private boolean requestAnnouncementCompleteNotification = true;
    private byte[] extensions;
    private CallSegmentID callSegmentID;
    private boolean requestAnnouncementStartedNotification = true;

    public PlayAnnouncementArg() {
    }

    public void PlayAnnouncementArg(InformationToSend informationToSend) {
        this.informationToSend = informationToSend;
    }

    public void encode(AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IOException {
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, Tag.SEQUENCE);
        int lenPos = aos.StartContentDefiniteLength();

        informationToSend.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 1, disconnectFromIpForbidden);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 2, requestAnnouncementCompleteNotification);

        if (extensions != null) {
            aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 3, extensions);
        }

        if (callSegmentID != null) {
            callSegmentID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        }

        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 51, requestAnnouncementStartedNotification);

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.informationToSend = new InformationToSend();
                    this.informationToSend.decode(ais);
                    break;
                case 1:
                    this.disconnectFromIpForbidden = ais.readBoolean();
                    break;
                case 2:
                    this.requestAnnouncementCompleteNotification = ais.readBoolean();
                    break;
                case 3:
                    this.extensions = ais.readSequence();
                    break;
                case 5:
                    this.callSegmentID = new CallSegmentID();
                    this.callSegmentID.decode(ais);
                    break;
                case 51:
                    this.requestAnnouncementStartedNotification = ais.readBoolean();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the informationToSend
     */
    @Override
    public InformationToSend getInformationToSend() {
        return informationToSend;
    }

    /**
     * @param informationToSend the informationToSend to set
     */
    @Override
    public void setInformationToSend(InformationToSend informationToSend) {
        this.informationToSend = informationToSend;
    }

    /**
     * @return the disconnectFromIpForbidden
     */
    @Override
    public boolean isDisconnectFromIPForbidden() {
        return disconnectFromIpForbidden;
    }

    /**
     * @param disconnectFromIpForbidden the disconnectFromIpForbidden to set
     */
    @Override
    public void setDisconnectFromIPForbidden(boolean disconnectFromIpForbidden) {
        this.disconnectFromIpForbidden = disconnectFromIpForbidden;
    }

    /**
     * @return the requestAnnouncementCompleteNotification
     */
    @Override
    public boolean isRequestAnnouncementComplete() {
        return requestAnnouncementCompleteNotification;
    }

    /**
     * @param requestAnnouncementCompleteNotification the
     * requestAnnouncementCompleteNotification to set
     */
    @Override
    public void setRequestAnnouncementComplete(boolean requestAnnouncementCompleteNotification) {
        this.requestAnnouncementCompleteNotification = requestAnnouncementCompleteNotification;
    }

    /**
     * @return the extensions
     */
    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    /**
     * @return the callSegmentID
     */
    public CallSegmentID getCallSegmentID() {
        return callSegmentID;
    }

    /**
     * @param callSegmentID the callSegmentID to set
     */
    public void setCallSegmentID(CallSegmentID callSegmentID) {
        this.callSegmentID = callSegmentID;
    }

    /**
     * @return the requestAnnouncementStartedNotification
     */
    public boolean isRequestAnnouncementStartedNotification() {
        return requestAnnouncementStartedNotification;
    }

    /**
     * @param requestAnnouncementStartedNotification the
     * requestAnnouncementStartedNotification to set
     */
    public void setRequestAnnouncementStartedNotification(boolean requestAnnouncementStartedNotification) {
        this.requestAnnouncementStartedNotification = requestAnnouncementStartedNotification;
    }
}
