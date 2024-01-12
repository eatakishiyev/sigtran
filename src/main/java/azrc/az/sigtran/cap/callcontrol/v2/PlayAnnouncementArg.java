/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import azrc.az.sigtran.cap.api.IPlayAnnouncementArg;
import azrc.az.sigtran.cap.parameters.InformationToSend;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * PlayAnnouncementArg ::= SEQUENCE {
 * informationToSend	[0] InformationToSend,
 * disconnectFromIPForbidden	[1] BOOLEAN	DEFAULT TRUE,
 * requestAnnouncementComplete	[2] BOOLEAN	DEFAULT TRUE,
 * extensions	[3] SEQUENCE SIZE(1..numOfExtensions) OF
 * ExtensionField	OPTIONAL,
 * ...
 * }
 *
 * @author eatakishiyev
 */
public class PlayAnnouncementArg implements IPlayAnnouncementArg {

    private InformationToSend informationToSend;
    private boolean disconnectFromIPForbidden;
    private boolean requestAnnouncementComplete;
    private byte[] extensions;

    public PlayAnnouncementArg() {
    }

    public PlayAnnouncementArg(InformationToSend informationToSend) {
        this.informationToSend = informationToSend;
    }

    public void encode(AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IOException {
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, Tag.SEQUENCE);
        int lenPos = aos.StartContentDefiniteLength();

        informationToSend.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 1, disconnectFromIPForbidden);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 2, requestAnnouncementComplete);

        if (extensions != null) {
            aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 3, extensions);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (Exception ex) {
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
                    this.disconnectFromIPForbidden = ais.readBoolean();
                    break;
                case 2:
                    this.requestAnnouncementComplete = ais.readBoolean();
                    break;
                case 3:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    @Override

    public InformationToSend getInformationToSend() {
        return this.informationToSend;
    }

    @Override
    public void setInformationToSend(InformationToSend informationToSend) {
        this.informationToSend = informationToSend;
    }

    @Override
    public boolean isDisconnectFromIPForbidden() {
        return this.disconnectFromIPForbidden;
    }

    @Override
    public void setDisconnectFromIPForbidden(boolean disconnectIPForbidden) {
        this.disconnectFromIPForbidden = disconnectIPForbidden;
    }

    @Override
    public boolean isRequestAnnouncementComplete() {
        return requestAnnouncementComplete;
    }

    @Override
    public void setRequestAnnouncementComplete(boolean requestAnnouncementComplete) {
        this.requestAnnouncementComplete = requestAnnouncementComplete;
    }

    @Override
    public byte[] getExtensions() {
        return this.extensions;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
