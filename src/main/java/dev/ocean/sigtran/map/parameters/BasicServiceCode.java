/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * BasicServiceCode ::= CHOICE {
 * bearerService [2] BearerServiceCode,
 * teleservice [3] TeleserviceCode}
 *
 * @author eatakishiyev
 */
public class BasicServiceCode implements MAPParameter {

    public final static int BEARER_SERVICE_TAG = 0x02;
    public final static int TELE_SERVICE_TAG = 0x03;
    private BearerServiceCode bearerService;
    private TeleServiceCode teleService;

    public BasicServiceCode() {
    }

    public BasicServiceCode(BearerServiceCode bearerService) {
        this.bearerService = bearerService;
    }

    public BasicServiceCode(TeleServiceCode teleService) {
        this.teleService = teleService;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (bearerService != null) {
            bearerService.encode(Tag.CLASS_CONTEXT_SPECIFIC, BEARER_SERVICE_TAG, aos);
        } else {
            teleService.encode(Tag.CLASS_CONTEXT_SPECIFIC, TELE_SERVICE_TAG, aos);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            this.encode(aos);
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            this.decode(tag, ais);
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(int tag, AsnInputStream ais) throws IncorrectSyntaxException {

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new IncorrectSyntaxException(String.format("Unexpected TagClass[%s] received. Expecting TagClass[CLASS_CONTEXT]", ais.getTagClass()));
        }

        switch (tag) {
            case BEARER_SERVICE_TAG:
                this.bearerService = new BearerServiceCode();
                bearerService.decode(ais);
                break;
            case TELE_SERVICE_TAG:
                this.teleService = new TeleServiceCode();
                teleService.decode(ais);
                break;
            default:
                throw new IncorrectSyntaxException("Expecting BearerService/TeleService tag. Received " + tag);
        }
    }

    /**
     * @return the extBearerService
     */
    public BearerServiceCode getBearerService() {
        return bearerService;
    }

    /**
     * @return the extTeleService
     */
    public TeleServiceCode getTeleService() {
        return teleService;
    }
}
