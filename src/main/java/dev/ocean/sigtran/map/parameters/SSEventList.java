/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SS-EventList ::= SEQUENCE SIZE (1..maxNumOfCamelSSEvents) OF SS-Code
 * -- Actions for the following SS-Code values are defined in CAMEL Phase 3:
 * -- ect SS-Code ::= '00110001'B
 * -- multiPTY SS-Code ::= '01010001'B
 * -- cd SS-Code ::= '00100100'B
 * -- ccbs SS-Code ::= '01000100'B
 * -- all other SS codes shall be ignored
 * -- When SS-CSI is sent to the VLR, it shall not contain a marking for ccbs.
 * -- If the VLR receives SS-CSI containing a marking for ccbs, the VLR shall
 * discard the
 * -- ccbs marking in SS-CSI.
 * @author eatakishiyev
 */
public class SSEventList {

    private final List<SSCode> events;

    public SSEventList() {
        this.events = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (events.size() < 1
                || events.size() > Constants.maxNumOfCamelSSEvents) {
            throw new UnexpectedDataException("SSEvent count must be in [1..10] range. Current count is " + events.size());
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (SSCode event : events) {
                event.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    SSCode ssCode = new SSCode();
                    ssCode.decode(ais);
                    this.events.add(ssCode);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }

            if (events.size() < 1
                    || events.size() > Constants.maxNumOfCamelSSEvents) {
                throw new UnexpectedDataException("SSEvent count must be in [1..10] range. Current count is " + events.size());
            }

        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<SSCode> getEvents() {
        return events;
    }
}
