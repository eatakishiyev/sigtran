/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * VoiceBroadcastData ::= SEQUENCE {
 * groupid GroupId,
 * -- groupId shall be filled with six TBCD fillers (1111)if the longGroupId is
 * present
 * broadcastInitEntitlement NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * longGroupId [0] Long-GroupId OPTIONAL }
 * -- VoiceBroadcastData containing a longGroupId shall not be sent to VLRs that
 * did not
 * -- indicate support of long Group IDs within the Update Location or Restore
 * Data
 * -- request message
 * @author eatakishiyev
 */
public class VoiceBroadcastData {

    private GroupId groupId;
    private Boolean broadcastInitEntitlement = null;
    private ExtensionContainer extensionContainer;
    private LongGroupId longGroupId;

    public VoiceBroadcastData() {
    }

    public VoiceBroadcastData(GroupId groupId) {
        this.groupId = groupId;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            groupId.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            if (broadcastInitEntitlement != null
                    && broadcastInitEntitlement) {
                aos.writeNull();
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (longGroupId != null) {
                longGroupId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                groupId = new GroupId();
                groupId.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[OCTET] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case Tag.NULL:
                        this.broadcastInitEntitlement = true;
                        ais.readNull();
                        break;
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer();
                        this.extensionContainer.decode(ais);
                        break;
                    case 0:
                        this.longGroupId = new LongGroupId();
                        this.longGroupId.decode(ais);
                        break;
                }
            }
        } catch (IOException | AsnException ex) {
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
     * @return the broadcastInitEntitlement
     */
    public Boolean getBroadcastInitEntitlement() {
        return broadcastInitEntitlement;
    }

    /**
     * @param broadcastInitEntitlement the broadcastInitEntitlement to set
     */
    public void setBroadcastInitEntitlement(Boolean broadcastInitEntitlement) {
        this.broadcastInitEntitlement = broadcastInitEntitlement;
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
