/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import java.io.IOException;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ProblemImpl implements Problem {

    private GeneralProblem generalProblem = null;
    private InvokeProblem invokeProblem = null;
    private ReturnResultProblem returnResultProblem = null;
    private ReturnErrorProblem returnErrorProblem = null;

    public ProblemImpl() {
    }

    public ProblemImpl(GeneralProblem generalProblem) {
        this.generalProblem = generalProblem;
    }

    public ProblemImpl(InvokeProblem invokeProblem) {
        this.invokeProblem = invokeProblem;
    }

    public ProblemImpl(ReturnResultProblem returnResultProblem) {
        this.returnResultProblem = returnResultProblem;
    }

    public ProblemImpl(ReturnErrorProblem returnErrorProblem) {
        this.returnErrorProblem = returnErrorProblem;
    }
    
    public boolean isGeneralProblem(){
        return generalProblem != null;
    }
    
    public boolean isInvokeProblem(){
        return invokeProblem != null;
    }
    
    public boolean isReturnResultProblem(){
        return returnResultProblem != null;
    }

    public boolean isReturnErrorProblem(){
        return returnErrorProblem != null;
    }
    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (generalProblem != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, generalProblem.value());
                return;
            }
            if (invokeProblem != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, invokeProblem.value());
                return;
            }
            if (returnResultProblem != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x02, returnResultProblem.value());
                return;
            }
            if (returnErrorProblem != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x03, returnErrorProblem.value());
                return;
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE=%b TAG= %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        try {
            int problemType = ais.getTag();

            switch (problemType) {
                case 0x00:
                    Integer problem = ((Long) ais.readInteger()).intValue();
                    this.generalProblem = GeneralProblem.getInstance(problem);
                    break;
                case 0x01:
                    problem = ((Long) ais.readInteger()).intValue();
                    this.invokeProblem = InvokeProblem.getInstance(problem);
                    break;
                case 0x02:
                    problem = ((Long) ais.readInteger()).intValue();
                    this.returnResultProblem = ReturnResultProblem.getInstance(problem);
                    break;
                case 0x03:
                    problem = ((Long) ais.readInteger()).intValue();
                    this.returnErrorProblem = ReturnErrorProblem.getInstance(problem);
                    break;
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void setGeneralProblem(GeneralProblem generalProblem) {
        this.generalProblem = generalProblem;
        this.invokeProblem = null;
        this.returnErrorProblem = null;
        this.returnResultProblem = null;
    }

    @Override
    public GeneralProblem getGeneralProblem() {
        return this.generalProblem;
    }

    @Override
    public void setInvokeProblem(InvokeProblem invokeProblem) {
        this.invokeProblem = invokeProblem;
        this.generalProblem = null;
        this.returnErrorProblem = null;
        this.returnResultProblem = null;
    }

    @Override
    public InvokeProblem getInvokeProblem() {
        return this.invokeProblem;
    }

    @Override
    public void setReturnResultProblem(ReturnResultProblem returnResultProblem) {
        this.returnResultProblem = returnResultProblem;
        this.generalProblem = null;
        this.invokeProblem = null;
        this.returnErrorProblem = null;
    }

    @Override
    public ReturnResultProblem getReturnResultProblem() {
        return this.returnResultProblem;
    }

    @Override
    public void setReturnErrorProblem(ReturnErrorProblem returnErrorProblem) {
        this.returnErrorProblem = returnErrorProblem;
        this.generalProblem = null;
        this.invokeProblem = null;
        this.returnResultProblem = null;
    }

    @Override
    public ReturnErrorProblem getReturnErrorProblem() {
        return this.returnErrorProblem;
    }

    @Override
    public String toString() {
        return String.format("[Problem:[GeneralProblem = %s, InvokeProblem = %s, ReturnErrorProblem = %s, ReturnResultProblem = %s]]", generalProblem, invokeProblem, returnErrorProblem, returnResultProblem);
    }
}
