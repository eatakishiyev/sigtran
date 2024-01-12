/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CUG-CheckInfo ::= SEQUENCE {
 * cug-Interlock CUG-Interlock,
 * cug-OutgoingAccess NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class CUGCheckInfo implements MAPParameter {

    private CugInterLock cugInterlock;
    private boolean cugOutgoingAccess;
    private ExtensionContainer extensionContainer;

    public CUGCheckInfo() {

    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            cugInterlock.encode(aos);

            if (cugOutgoingAccess) {
                aos.writeNull();
            }
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.cugInterlock = new CugInterLock();
                cugInterlock.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag Class[%s] received."
                            + "Expecting Class[UNIVERSAL]", ais.getTagClass()));
                }
                switch (tag) {
                    case Tag.NULL:
                        this.cugOutgoingAccess = true;
                        ais.readNull();
                        break;
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received."));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public CUGCheckInfo(CugInterLock cugInterlock) {
        this.cugInterlock = cugInterlock;
    }

    /**
     * @return the cugInterlock
     */
    public CugInterLock getCugInterlock() {
        return cugInterlock;
    }

    /**
     * @param cugInterlock the cugInterlock to set
     */
    public void setCugInterlock(CugInterLock cugInterlock) {
        this.cugInterlock = cugInterlock;
    }

    /**
     * @return the cugOutgoingAccess
     */
    public boolean isCugOutgoingAccess() {
        return cugOutgoingAccess;
    }

    /**
     * @param cugOutgoingAccess the cugOutgoingAccess to set
     */
    public void setCugOutgoingAccess(boolean cugOutgoingAccess) {
        this.cugOutgoingAccess = cugOutgoingAccess;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
