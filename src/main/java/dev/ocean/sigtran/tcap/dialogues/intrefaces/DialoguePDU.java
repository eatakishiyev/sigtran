/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogues.intrefaces;

import dev.ocean.sigtran.tcap.Encodable;
import dev.ocean.sigtran.tcap.parameters.UserInformationImpl;
import java.util.ArrayList;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface DialoguePDU extends Encodable {

    public static final int DIALOGUEPDU_TAG_CLASS = Tag.CLASS_APPLICATION;
    public static final boolean DIALOGUEPDU_TAG_PC = false;
    public static final int DIALOGUE_REQUEST_APDU_TAG = 0x00;
    public static final int DIALOGUE_RESPONSE_APDU_TAG = 0x01;
    public static final int DIALOGUE_ABORT_APDU_TAG = 0x04;

    public int getDialogueType();

    public UserInformationImpl getUserInformation();
    
    public void setUserInformation(UserInformationImpl userInformation);

}
