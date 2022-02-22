/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import java.io.IOException;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.tcap.components.InvokeImpl;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;


/**
 *
 * @author eatakishiyev
 */
public class IncorrectComponent {

    private Logger logger = LogManager.getLogger(IncorrectComponent.class);
    private Short invokeId;

    public void decode(AsnInputStream ais) {
        try {
            int lengh = ais.readLength();
            int tag = ais.readTag();
            if (tag == InvokeImpl.TAG_INVOKE_ID
                    && ais.isTagPrimitive() == true
                    && ais.getTagClass() == InvokeImpl.TAG_INVOKE_ID_CLASS) {
                this.invokeId = (short) ais.readInteger();
            }
        } catch (IOException | AsnException ex) {
            logger.error("{}", ex);
        }
    }

    /**
     * @return the invokeId
     */
    public Short getInvokeId() {
        return invokeId;
    }
}
