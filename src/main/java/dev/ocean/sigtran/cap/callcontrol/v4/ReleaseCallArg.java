/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.cap.api.IReleaseCallArg;
import dev.ocean.sigtran.cap.parameters.AllCallSegmentsWithExtension;
import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ReleaseCallArg {PARAMETERS-BOUND : bound} ::= CHOICE {
 * allCallSegments AllCallSegments,
 * allCallSegmentsWithExtension [2] AllCallSegmentsWithExtension
 * }
 *
 * @author eatakishiyev
 */
public class ReleaseCallArg implements IReleaseCallArg {

    private Cause allCallSegments;
    private AllCallSegmentsWithExtension allCallSegmentsWithExtension;

    public ReleaseCallArg() {

    }

    public ReleaseCallArg(Cause allCallSegments) {
        this.allCallSegments = allCallSegments;
    }

    public ReleaseCallArg(AllCallSegmentsWithExtension allCallSegmentsWithExtension) {
        this.allCallSegmentsWithExtension = allCallSegmentsWithExtension;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (allCallSegments != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                this.allCallSegments.encode(baos);

                aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
                int lenPos = aos.StartContentDefiniteLength();
                aos.write(baos.toByteArray());
                aos.FinalizeContent(lenPos);
            } else {
                this.allCallSegmentsWithExtension.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException();
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC
                        && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[0] or TagClass[2], found TagClass[%s]", ais.getTagClass()));
                }

                switch (ais.getTagClass()) {
                    case Tag.CLASS_UNIVERSAL:
                        if (tag != Tag.STRING_OCTET) {
                            throw new AsnException(String.format("Received unexpected tag. Expecting Tag[4] TagClass[0], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }

                        byte[] data = new byte[ais.readLength()];
                        ais.read(data);

                        this.allCallSegments = new Cause();
                        this.allCallSegments.decode(new ByteArrayInputStream(data));
                        break;
                    case Tag.CLASS_CONTEXT_SPECIFIC:
                        if (tag != 2) {
                            throw new AsnException(String.format("Received unexpected tag. Expecting Tag[2] TagClass[2], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }

                        this.allCallSegmentsWithExtension = new AllCallSegmentsWithExtension();
                        this.allCallSegmentsWithExtension.decode(ais);
                        break;
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public Cause getCause() {
        return allCallSegments;
    }

    public AllCallSegmentsWithExtension getCauseWithExtension() {
        return allCallSegmentsWithExtension;
    }

}
