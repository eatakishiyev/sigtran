/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ExtendedRoutingInfo::= CHOICE {
 * routingInfo RoutingInfo,
 * camelRoutingInfo [8] CamelRoutingInfo}
 *
 * @author eatakishiyev
 */
public class ExtendedRoutingInfo implements MAPParameter {

    private RoutingInfo routingInfo;
    private CamelRoutingInfo camelRoutingInfo;

    public ExtendedRoutingInfo() {
    }

    public ExtendedRoutingInfo(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    public ExtendedRoutingInfo(CamelRoutingInfo camelRoutingInfo) {
        this.camelRoutingInfo = camelRoutingInfo;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (routingInfo != null) {
            routingInfo.encode(aos);
        } else {
            camelRoutingInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            this.encode(aos);
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            this.routingInfo = new RoutingInfo();
            if (ais.getTag() == Tag.STRING_OCTET) {
                this.routingInfo.decodeRoamingNumber(ais);
            } else if (ais.getTag() == Tag.SEQUENCE) {
                this.routingInfo.decodeForwardingData(ais.readSequenceStream());
            } else if (ais.getTag() == 8 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.camelRoutingInfo = new CamelRoutingInfo();
                camelRoutingInfo.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", ais.getTag(), ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public RoutingInfo getRoutingInfo() {
        return routingInfo;
    }

    public CamelRoutingInfo getCamelRoutingInfo() {
        return camelRoutingInfo;
    }

}
