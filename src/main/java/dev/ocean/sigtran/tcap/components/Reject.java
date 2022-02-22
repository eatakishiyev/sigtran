/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.components;

import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import dev.ocean.sigtran.tcap.parameters.ProblemImpl;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface Reject extends Component {

    public static final int REJECT_TAG = 0x04;
    public static final int REJECT_TAG_CLASS_ = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean REJECT_TAG_PRIMITIVE = false;
    public static final int TAG_INVOKE_ID = 0x02; //ITU-T Q.773 4.2.2.2
    public static final int TAG_INVOKE_ID_CLASS = Tag.CLASS_UNIVERSAL;
    public static final boolean TAG_INVOKE_ID_PRIMITIVE = true;

    public ProblemImpl getProblem();

    public void setProblem(ProblemImpl pr);
}
