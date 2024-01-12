/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.components;

import java.io.IOException;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.ComponentType;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;
import azrc.az.sigtran.tcap.parameters.Parameter;
import azrc.az.sigtran.tcap.parameters.ProblemImpl;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class RejectImpl implements Reject {

    private ProblemImpl problem;
    private Short invokeId;

    @Override
    public ProblemImpl getProblem() {
        return this.problem;
    }

    @Override
    public void setProblem(ProblemImpl pr) {
        this.problem = pr;
    }

    @Override
    public Short getInvokeId() {
        return this.invokeId;
    }

    @Override
    public void setInvokeId(Short invokeId) {
        this.invokeId = invokeId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.REJECT;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(REJECT_TAG_CLASS_, REJECT_TAG_PRIMITIVE, REJECT_TAG);
            int position = aos.StartContentDefiniteLength();

            if (invokeId == null) {
                aos.writeNull();
            } else {
                aos.writeInteger(invokeId);
            }

            problem.encode(aos);

            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. Class = %d Primitive =%b Tag = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        AsnInputStream _ais = ais.readSequenceStream();
        int tag = _ais.readTag();

        if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL && _ais.isTagPrimitive() && tag == TAG_INVOKE_ID) {
            this.invokeId = (short) _ais.readInteger();
            tag = _ais.readTag();
        } else if (_ais.getTagClass() == Tag.CLASS_UNIVERSAL && _ais.isTagPrimitive() && tag == Tag.NULL) {
            _ais.readNull();
            this.invokeId = null;
            tag = _ais.readTag();
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting INVOKE_ID. Found %d", tag));
        }
        if (_ais.available() > 0) {
            this.problem = new ProblemImpl();
            this.problem.decode(_ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", _ais.getTagClass(), _ais.isTagPrimitive(), _ais.getTag()));
        }
    }

    public boolean isInvokeProblem() {
        return problem != null && problem.getInvokeProblem() != null;
    }

    public boolean isGeneralProblem() {
        return problem != null && problem.getGeneralProblem() != null;
    }

    public boolean isReturnResultProblem() {
        return problem != null && problem.getReturnResultProblem() != null;
    }

    public boolean isReturnErrorProblem() {
        return problem != null && problem.getReturnErrorProblem() != null;
    }

    @Override
    public String toString() {
        return String.format("Reject[InvokeId = %d, %s]", invokeId, problem);
    }

    @Override
    public Parameter getParameter() {
        return null;
    }

    @Override
    public OperationCodeImpl getOpCode() {
        return null;
    }

    
}
