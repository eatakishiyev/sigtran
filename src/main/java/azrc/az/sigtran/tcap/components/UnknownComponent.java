/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.components;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.ComponentType;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;
import azrc.az.sigtran.tcap.parameters.Parameter;
import azrc.az.sigtran.tcap.parameters.interfaces.Component;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class UnknownComponent implements Component {

    private static final Logger logger = LoggerFactory.getLogger(UnknownComponent.class);
    private Short invokeId;

    protected UnknownComponent() {
    }

    @Override
    public Short getInvokeId() {
        return this.invokeId;
    }

    @Override
    public void setInvokeId(Short invokeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComponentType getComponentType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            while (tmpAis.available() > 0) {
                int tag = tmpAis.readTag();
                if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == Invoke.TAG_INVOKE_ID) {
                    this.invokeId = (short) tmpAis.readInteger();
                } else {
                    int length = tmpAis.readLength();
                    tmpAis.read(new byte[length]);
                }
            }
        } catch (AsnException | IOException ex) {
            logger.error("Unable to parse unknown TCAP component. " + ais.toString(), ex);
        }
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
