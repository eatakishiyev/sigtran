/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.BasicServiceCode;
import azrc.az.sigtran.map.parameters.SSCode;
import azrc.az.sigtran.map.parameters.SSStatus;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SS-IncompatibilityCause ::= SEQUENCE {
 * ss-Code [1] SS-Code OPTIONAL,
 * basicService BasicServiceCode OPTIONAL,
 * ss-Status [4] SS-Status OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class SSIncompatibilityCause {

    private SSCode ssCode;
    private BasicServiceCode basicService;
    private SSStatus ssStatus;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            if (ssCode != null) {
                ssCode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (basicService != null) {
                basicService.encode(aos);
            }
            if (ssStatus != null) {
                ssStatus.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL
                    || tag != Tag.SEQUENCE) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();
            while (_ais.available() > 0) {
                tag = _ais.readTag();
                if (tag == 1 && _ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.ssCode = new SSCode();
                    ssCode.decode(_ais);
                } else if ((tag == 2 || tag == 3) && _ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.basicService = new BasicServiceCode();
                    basicService.decode(tag, _ais);
                } else if (tag == 4 && _ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.ssStatus = new SSStatus();
                    ssStatus.decode(_ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, _ais.getTagClass()));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
}
