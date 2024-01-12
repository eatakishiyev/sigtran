/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters.interfaces;

import azrc.az.sigtran.tcap.Encodable;
import azrc.az.sigtran.tcap.parameters.ComponentType;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;
import azrc.az.sigtran.tcap.parameters.Parameter;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface Component extends Encodable {

    public static final int COMPONENT_PORTION_TAG = 0x0C;
    public static final boolean IS_COMPONENT_PORITON_TAG_PRIMITIVE = false;
    public static final int COMPONENT_PORTION_TAG_CLASS = Tag.CLASS_APPLICATION;

    public Short getInvokeId();

    public void setInvokeId(Short invokeId);

    public ComponentType getComponentType();

    public Parameter getParameter();

    public OperationCodeImpl getOpCode();

}
