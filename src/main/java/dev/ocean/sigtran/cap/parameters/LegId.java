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
 * SendingSideID ::= CHOICE {sendingSideID [0] LegType} -- used to identify
 * LegID in operations sent from gsmSCF to gsmSSF
 *
 * @author eatakishiyev
 */
public abstract class LegId {

    public abstract LegType getLegType();

    public abstract void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException;

    abstract void decode(AsnInputStream ais) throws IOException, AsnException;

    public static LegId createLegId(AsnInputStream ais) throws IOException, AsnException {

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
        }
        switch (tag) {
            case 0:
                SendingSideID sendingSideID = new SendingSideID();
                sendingSideID.decode(ais);
                return sendingSideID;
            case 1:
                ReceivingSideID receivingSideID = new ReceivingSideID();
                receivingSideID.decode(ais);
                return receivingSideID;
            default:
                throw new AsnException(String.format("Unexpected tag received. Expecting Tag[0] or Tag[1] and  TagClass[2] , found TagClass[%s] Tag[%s]", ais.getTagClass(), ais.getTag()));
        }

    }

}
