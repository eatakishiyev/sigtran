/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters.interfaces;

import azrc.az.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface AssociateSourceDiagnostic extends Encodable {

    public static final int RESULT_SOURCE_DIAGNOSTIC_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean RESULT_SOURCE_DIAGNOSTIC_TAG_PRIMITIVE = false;
    public static final int RESULT_SOURCE_DIAGNOSTIC_TAG = 0x03;
    public static final int DIALOGUE_SERVICE_USER_DIAGNOSTIC_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean DIALOGUE_SERVICE_USER_DIAGNOSTIC_TAG_PRIMITIVE = false;
    public static final int DIALOGUE_SERVICE_USER_DIAGNOSTIC_TAG = 0x01;
    public static final int DIALOGUE_SERVICE_PROVIDER_DIAGNOSTIC_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean DIALOGUE_SERVICE_PROVIDER_DIAGNOSTIC_TAG_PRIMITIVE = false;
    public static final int DIALOGUE_SERVICE_PROVIDER_DIAGNOSTIC_TAG = 0x02;

    public void setDialogueServiceUserDiagnosticValue(DialogueServiceUserDiagnostic val);

    public DialogueServiceUserDiagnostic getDialogueServiceUserDiagnosticValue();

    public void setDialogueServiceProviderDiagnosticValue(DialogueServiceProviderDiagnostic val);

    public DialogueServiceProviderDiagnostic getDialogueServiceProviderDiagnosticValue();
}
