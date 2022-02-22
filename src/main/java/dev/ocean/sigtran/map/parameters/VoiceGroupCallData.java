/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * VoiceGroupCallData ::= SEQUENCE {
 * groupId GroupId,
 * -- groupId shall be filled with six TBCD fillers (1111)if the longGroupId is
 * present
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * additionalSubscriptions AdditionalSubscriptions OPTIONAL,
 * additionalInfo [0] AdditionalInfo OPTIONAL,
 * longGroupId [1] Long-GroupId OPTIONAL }
 * -- VoiceGroupCallData containing a longGroupId shall not be sent to VLRs that
 * did not
 * -- indicate support of long Group IDs within the Update Location or Restore
 * Data
 * -- request message
 * @author eatakishiyev
 */
public class VoiceGroupCallData {

    private GroupId groupId;
    private ExtensionContainer extensionContainer;
    private AdditionalSubscriptions additionalSubscriptions;
    private AdditionalInfo additionalInfo;
    private LongGroupId longGroupId;

    public VoiceGroupCallData() {
    }

    public VoiceGroupCallData(GroupId groupId) {
        this.groupId = groupId;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            groupId.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (additionalSubscriptions != null) {
                additionalSubscriptions.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
            }

            if (additionalInfo != null) {
                additionalInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (longGroupId != null) {
                longGroupId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.groupId = new GroupId();
                groupId.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[OCTET] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case Tag.STRING_BIT:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.additionalSubscriptions = new AdditionalSubscriptions();
                            additionalSubscriptions.decode(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[BIT_STRING] Class[UNIVERSAL]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 0:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.additionalInfo = new AdditionalInfo();
                            additionalInfo.decode(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[0] Class[CONTEXT]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 1:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.longGroupId = new LongGroupId();
                            longGroupId.decode(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[1] Class[CONTEXT]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the groupId
     */
    public GroupId getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
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

    /**
     * @return the additionalSubscriptions
     */
    public AdditionalSubscriptions getAdditionalSubscriptions() {
        return additionalSubscriptions;
    }

    /**
     * @param additionalSubscriptions the additionalSubscriptions to set
     */
    public void setAdditionalSubscriptions(AdditionalSubscriptions additionalSubscriptions) {
        this.additionalSubscriptions = additionalSubscriptions;
    }

    /**
     * @return the additionalInfo
     */
    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return the longGroupId
     */
    public LongGroupId getLongGroupId() {
        return longGroupId;
    }

    /**
     * @param longGroupId the longGroupId to set
     */
    public void setLongGroupId(LongGroupId longGroupId) {
        this.longGroupId = longGroupId;
    }

}
