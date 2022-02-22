/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import dev.ocean.sigtran.tcap.parameters.OperationCodeImpl;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface Invoke extends Component {

    public static final int INVOKE_TAG = 0x01;
    public static final int TAG_INVOKE_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean TAG_INVOKE_PRIMITIVE = false;
    public static final int TAG_INVOKE_ID = 0x02; //ITU-T Q.773 4.2.2.2
    public static final int TAG_INVOKE_ID_CLASS = Tag.CLASS_UNIVERSAL;
    public static final boolean TAG_INVOKE_ID_PRIMITIVE = true;
    public static final int TAG_LINKED_INVOKE_ID = 0x00;
    public static final int TAG_LINKED_INVOKE_ID_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean TAG_LINKED_ID_PRIMITIVE = true;

    public void setLinkedInvokeId(Short id);

    public Short getLinkedInvokeId();

    public void setOpCode(OperationCodeImpl opCode);

    public OperationCodeImpl getOpCode();

    public void setParameter(ParameterImpl param);

    public ParameterImpl getParameter();
}
