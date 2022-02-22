/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import dev.ocean.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface OperationCode extends Encodable {

    public static final int TAG_LOCAL_OPCODE = 0x02;
    public static final int TAG_GLOBAL_OPCODE = 0x06;
    public static final boolean TAG_OPCODE_PRIMITIVE = true;
    public static final int TAG_OPCODE_CLASS = Tag.CLASS_UNIVERSAL;

    public OperationCodeType getOperationCodeType();

    public void setOperationCodeType(OperationCodeType operationCodeType);

    public int getLocalValue();

    public void setLocalValue(int operationCode);
}
