/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters.interfaces;

import dev.ocean.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface ProtocolVersion extends Encodable {

    public static final int TAG = 0x00;
    public static final int TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean TAG_PC_PRIMITIVE = true;
}
