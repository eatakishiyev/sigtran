/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.messages.circuit.switched.call.control.bcsm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.cap.parameters.CallSegmentID;
import azrc.az.sigtran.cap.parameters.IPRoutingAddress;
import azrc.az.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ConnectToResourceArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
 * resourceAddress CHOICE { ipRoutingAddress [0] IPRoutingAddress {bound}, none
 * [3] NULL }, extensions [4] Extensions {bound} OPTIONAL,
 * serviceInteractionIndicatorsTwo [7] ServiceInteractionIndicatorsTwo OPTIONAL,
 * callSegmentID [50] CallSegmentID {bound} OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class ConnectToResourceArg {

    private IPRoutingAddress ipRoutingAddress;
    private byte[] extensions;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private CallSegmentID callSegmentID;

    public ConnectToResourceArg() {
    }

    public ConnectToResourceArg(IPRoutingAddress ipRoutingAddress) {
        this.ipRoutingAddress = ipRoutingAddress;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            if (ipRoutingAddress != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                baos.write(ipRoutingAddress.encode());

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
                int lenPos = aos.StartContentDefiniteLength();
                aos.write(baos.toByteArray());
                aos.FinalizeContent(lenPos);
            } else {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
            }

            if (extensions != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 4, extensions);
            }

            if (serviceInteractionIndicatorsTwo != null) {
                this.serviceInteractionIndicatorsTwo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (callSegmentID != null) {
                this.callSegmentID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, IllegalNumberFormatException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting TagClass[0] Tag[16], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, IncorrectSyntaxException, IllegalNumberFormatException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.ipRoutingAddress = new IPRoutingAddress();
                    byte[] data = new byte[ais.readLength()];
                    ais.read(data);
                    this.ipRoutingAddress.decode(data);
                    break;
                case 3:
                    ais.readNull();
                    break;
                case 4:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(this.extensions);
                    break;
                case 7:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwo();
                    this.serviceInteractionIndicatorsTwo.decode(ais.readSequenceStream());
                    break;
                case 50:
                    this.callSegmentID = new CallSegmentID();
                    this.callSegmentID.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

}
