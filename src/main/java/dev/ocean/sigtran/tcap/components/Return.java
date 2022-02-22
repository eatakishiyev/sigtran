/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import dev.ocean.sigtran.tcap.parameters.Parameter;
import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface Return extends Component {

    public static final int TAG_INVOKEID = 0x02;
    public static final int TAG_INVOKEID_CLASS = Tag.CLASS_UNIVERSAL;
    public static final boolean TAG_INVOKEID_PRIMITIVE = true;
}
