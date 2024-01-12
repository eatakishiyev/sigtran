/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.components;

import java.io.IOException;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.interfaces.Component;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class ComponentFactory {

    private ComponentFactory() {

    }

    public static Component createComponent(AsnInputStream ais) throws IOException, IncorrectSyntaxException, AsnException {
        int tag = ais.readTag();
        Component component = null;
        switch (tag) {
            case InvokeImpl.INVOKE_TAG:
                component = createInvokeComponent();
                component.decode(ais);
                break;
            case ReturnResultNotLastImpl.RETURN_RESULT_NOT_LAST:
                component = createReturnResultComponent();
                component.decode(ais);
                break;
            case ReturnResultLastImpl.RETURN_RESULT_LAST_TAG:
                component = createReturnResultLastComponent();
                component.decode(ais);
                break;
            case RejectImpl.REJECT_TAG:
                component = createRejectComponent();
                component.decode(ais);
                break;
            case ReturnErrorImpl.RETURN_ERROR_TAG:
                component = createReturnErrorComponent();
                component.decode(ais);
                break;
        }
        return component;
    }

    public static InvokeImpl createInvokeComponent(AsnInputStream ais) throws IncorrectSyntaxException, IOException, AsnException {
        InvokeImpl invokeImpl = new InvokeImpl();
        invokeImpl.decode(ais);
        return invokeImpl;
    }

    public static InvokeImpl createInvokeComponent() {
        return new InvokeImpl();
    }

    public static ReturnResultNotLastImpl createReturnResultComponent(AsnInputStream ais) throws IncorrectSyntaxException, IOException, AsnException {
        ReturnResultNotLastImpl returnResultNotLastImpl = new ReturnResultNotLastImpl();
        returnResultNotLastImpl.decode(ais);
        return returnResultNotLastImpl;
    }

    public static ReturnResultNotLastImpl createReturnResultComponent() {
        return new ReturnResultNotLastImpl();
    }

    public static ReturnResultLastImpl createReturnResultLastComponent(AsnInputStream ais) throws IncorrectSyntaxException, IOException, AsnException {
        ReturnResultLastImpl returnResultLastImpl = new ReturnResultLastImpl();
        returnResultLastImpl.decode(ais);
        return returnResultLastImpl;
    }

    public static ReturnResultLastImpl createReturnResultLastComponent() {
        return new ReturnResultLastImpl();
    }

    public static RejectImpl createRejectComponent(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {
        RejectImpl rejectImpl = new RejectImpl();
        rejectImpl.decode(ais);
        return rejectImpl;
    }

    public static RejectImpl createRejectComponent() {
        return new RejectImpl();
    }

    public static ReturnErrorImpl createReturnErrorComponent(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException {
        ReturnErrorImpl returnErrorImpl = new ReturnErrorImpl();
        returnErrorImpl.decode(ais);
        return returnErrorImpl;
    }

    public static UnknownComponent createUnknownComponent(AsnInputStream ais) throws IncorrectSyntaxException {
        UnknownComponent unknownComponent = new UnknownComponent();
        unknownComponent.decode(ais);
        return unknownComponent;
    }

    public static ReturnErrorImpl createReturnErrorComponent() {
        return new ReturnErrorImpl();
    }
}
