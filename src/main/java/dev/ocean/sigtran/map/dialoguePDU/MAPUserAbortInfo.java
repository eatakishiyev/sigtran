/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/*
 * MAP-UserAbortInfo ::= SEQUENCE { 
 map-UserAbortChoice  MAP-UserAbortChoice, 
 ..., 
 extensionContainer  ExtensionContainer  OPTIONAL 
 -- extensionContainer must not be used in version 2 
 }
 */
/**
 *
 * @author eatakishiyev
 */
public class MAPUserAbortInfo implements MAPDialoguePDU {

    private boolean userSpecificReason = false;
    private boolean userResourceLimitationReason = false;
    private ResourceUnavailableReason resourceUnavailableReason;
    private ProcedureCancellationReason procedureCancellationReason;
    private ExtensionContainer extensionContainer;

    public MAPUserAbortInfo() {
    }

    public static MAPUserAbortInfo createUserSpecificAbortReason() {
        MAPUserAbortInfo instance = new MAPUserAbortInfo();
        instance.userSpecificReason = true;
        return instance;
    }

    public static MAPUserAbortInfo createUserResourceLimitationAbortReason() {
        MAPUserAbortInfo instance = new MAPUserAbortInfo();
        instance.userResourceLimitationReason = true;
        return instance;
    }

    public static MAPUserAbortInfo createResourceUnavailableAbortReason(ResourceUnavailableReason resourceUnavailableReason) {
        MAPUserAbortInfo instance = new MAPUserAbortInfo();
        instance.resourceUnavailableReason = resourceUnavailableReason;
        return instance;
    }

    public static MAPUserAbortInfo createProcedureCancellationAbortReason(ProcedureCancellationReason procedureCancellationReason) {
        MAPUserAbortInfo instance = new MAPUserAbortInfo();
        instance.procedureCancellationReason = procedureCancellationReason;
        return instance;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (userSpecificReason == false && userResourceLimitationReason == false
                && resourceUnavailableReason == null && procedureCancellationReason == null) {
            throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
        }

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x04);
            int position = aos.StartContentDefiniteLength();

            if (userSpecificReason) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x00);
            } else if (userResourceLimitationReason) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x01);
            } else if (resourceUnavailableReason != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x02, resourceUnavailableReason.value());
            } else if (procedureCancellationReason != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x03, procedureCancellationReason.value());
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tmpAis.isTagPrimitive()) {
                switch (tag) {
                    case 0x00:
                        tmpAis.readNull();
                        userSpecificReason = true;
                        break;
                    case 0x01:
                        tmpAis.readNull();
                        userResourceLimitationReason = true;
                        break;
                    case 0x02:
                        resourceUnavailableReason = ResourceUnavailableReason.getInstance(((Long) tmpAis.readInteger()).intValue());
                        if (resourceUnavailableReason == ResourceUnavailableReason.UNKNOWN) {
                            throw new UnexpectedDataException();
                        }
                        break;
                    case 0x03:
                        procedureCancellationReason = ProcedureCancellationReason.getInstance(((Long) tmpAis.readInteger()).intValue());
                        if (procedureCancellationReason == ProcedureCancellationReason.UNKNOWN) {
                            throw new UnexpectedDataException();
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException();
                }
            } else {
                throw new IncorrectSyntaxException();
            }

            if (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && !tmpAis.isTagPrimitive() && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(tmpAis);
                } else {
                    throw new IncorrectSyntaxException();
                }
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the procedureCancellationReason
     */
    public boolean getUserResourceLimitationReason() {
        return userResourceLimitationReason;
    }

    /**
     * @return the userSpecificReason
     */
    public boolean getUserSpecificReason() {
        return userSpecificReason;
    }

    /**
     * @return the resourceUnavailableReason
     */
    public ResourceUnavailableReason getResourceUnavailableReason() {
        return resourceUnavailableReason;
    }

    /**
     * @return the procedureCancellationReason
     */
    public ProcedureCancellationReason getProcedureCancellationReason() {
        return procedureCancellationReason;
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
