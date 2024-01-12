/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CUG-SubscriptionList ::= SEQUENCE SIZE (0..maxNumOfCUG) OF CUG-Subscription
 *
 * @author eatakishiyev
 */
public class CUGSubscriptionList implements MAPParameter {

    private final List<CUGSubscription> cUGSubscriptions;

    public CUGSubscriptionList() {
        this.cUGSubscriptions = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (cUGSubscriptions.size() > Constants.maxNumOfCUG) {
                throw new UnexpectedDataException("CUGSubscription count must be in [0..10] range. Current count is " + cUGSubscriptions.size());
            }

            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (CUGSubscription cugSubscription : cUGSubscriptions) {
                cugSubscription.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    CUGSubscription cUGSubscription = new CUGSubscription();
                    cUGSubscription.decode(ais.readSequenceStream());
                    cUGSubscriptions.add(cUGSubscription);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                            + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (cUGSubscriptions.size() > Constants.maxNumOfCUG) {
                throw new UnexpectedDataException("CUGSubscription count must be in [0..10] range. Current count is " + cUGSubscriptions.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the cUGSubscriptions
     */
    public List<CUGSubscription> getcUGSubscriptions() {
        return cUGSubscriptions;
    }
}
