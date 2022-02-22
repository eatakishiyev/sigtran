/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters.interfaces;

import java.io.Serializable;
import dev.ocean.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface ApplicationContext extends Encodable, Serializable {

    public static final int TAG = 0x01;
    public static final int TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean PRIMITIVE_PC_TAG = false;

    public long[] getOid();

    public void setOid(long[] Oid);
}
