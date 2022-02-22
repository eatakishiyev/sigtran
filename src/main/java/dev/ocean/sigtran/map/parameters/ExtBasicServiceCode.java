/*
 * To change this template, choose Tools | Templates
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
 * Ext-BasicServiceCode ::= CHOICE {
 * ext-BearerService [2] Ext-BearerServiceCode,
 * ext-Teleservice [3] Ext-TeleserviceCode}
 *
 * @author eatakishiyev
 */
public class ExtBasicServiceCode implements MAPParameter {

    public final static int EXT_BEARER_SERVICE_TAG = 0x02;
    public final static int EXT_TELE_SERVICE_TAG = 0x03;
    private ExtBearerServiceCode extBearerService;
    private ExtTeleServiceCode extTeleService;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (extBearerService != null) {
            extBearerService.encode(Tag.CLASS_CONTEXT_SPECIFIC, EXT_BEARER_SERVICE_TAG, aos);
        } else {
            extTeleService.encode(Tag.CLASS_CONTEXT_SPECIFIC, EXT_TELE_SERVICE_TAG, aos);
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
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected TagClass[%s] received. Expecting TagClass[CLASS_CONTEXT]", ais.getTagClass()));
            }

            switch (tag) {
                case EXT_BEARER_SERVICE_TAG:
                    this.extBearerService = new ExtBearerServiceCode();
                    extBearerService.decode(ais);
                    break;
                case EXT_TELE_SERVICE_TAG:
                    this.extTeleService = new ExtTeleServiceCode();
                    extTeleService.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException("Expecting ExtBearerService/ExtTeleService tag. Received " + tag);
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais, int tag) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new IncorrectSyntaxException(String.format("Unexpected TagClass[%s] received. Expecting TagClass[CLASS_CONTEXT]", ais.getTagClass()));
        }
        switch (tag) {
            case EXT_BEARER_SERVICE_TAG:
                this.extBearerService = new ExtBearerServiceCode();
                extBearerService.decode(ais);
                break;
            case EXT_TELE_SERVICE_TAG:
                this.extTeleService = new ExtTeleServiceCode();
                extTeleService.decode(ais);
                break;
            default:
                throw new IncorrectSyntaxException("Expecting ExtBearerService/ExtTeleService tag. Received " + tag);
        }
    }

    /**
     * @return the extBearerService
     */
    public ExtBearerServiceCode getExtBearerService() {
        return extBearerService;
    }

    /**
     * @param extBearerService the extBearerService to set
     */
    public void setExtBearerService(ExtBearerServiceCode extBearerService) {
        this.extBearerService = extBearerService;
        this.extTeleService = null;
    }

    /**
     * @return the extTeleService
     */
    public ExtTeleServiceCode getExtTeleService() {
        return extTeleService;
    }

    /**
     * @param extTeleService the extTeleService to set
     */
    public void setExtTeleService(ExtTeleServiceCode extTeleService) {
        this.extTeleService = extTeleService;
        this.extBearerService = null;
    }
}
