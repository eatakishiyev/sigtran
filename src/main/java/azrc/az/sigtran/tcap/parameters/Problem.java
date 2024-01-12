/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import azrc.az.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface Problem extends Encodable {

    public static final int GENERAL_PROBLEM_TAG = 0x00;
    public static final int INVOKE_PROBLEM_TAG = 0x01;
    public static final int RETURN_RESULT_PROBLEM_TAG = 0x02;
    public static final int RETURN_ERROR_PROBLEM_TAG = 0x03;
    public static final int PROBLEM_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean PROBLEM_TAG_PRIMITIVE = true;

    public void setGeneralProblem(GeneralProblem gp);

    public GeneralProblem getGeneralProblem();

    public void setInvokeProblem(InvokeProblem ip);

    public InvokeProblem getInvokeProblem();

    public void setReturnResultProblem(ReturnResultProblem rrp);

    public ReturnResultProblem getReturnResultProblem();

    public void setReturnErrorProblem(ReturnErrorProblem rep);

    public ReturnErrorProblem getReturnErrorProblem();
}
