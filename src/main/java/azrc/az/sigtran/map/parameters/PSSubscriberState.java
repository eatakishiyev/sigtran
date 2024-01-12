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
 * PS-SubscriberState::= CHOICE { notProvidedFromSGSNorMME [0] NULL, ps-Detached
 * [1] NULL, ps-AttachedNotReachableForPaging [2] NULL,
 * ps-AttachedReachableForPaging [3] NULL, ps-PDP-ActiveNotReachableForPaging
 * [4] PDP-ContextInfoList, ps-PDP-ActiveReachableForPaging [5]
 * PDP-ContextInfoList, netDetNotReachable NotReachableReason }
 *
 * @author eatakishiyev
 */
public class PSSubscriberState {

    private boolean notProvisionedFromSGSNorMME;
    private boolean psDetach;
    private boolean psAttachedNotReachableForPaging;
    private boolean psAttachedReachableForPaging;
    private PDPContextInfoList psPDPActiveNotReachableForPaging;
    private PDPContextInfoList psPDPActiveReachableForPaging;
    private NotReachableReason netDetNotReachable;

    public PSSubscriberState() {
    }

    public static PSSubscriberState createNotProvisionedFromSGSNNorMME() {
        PSSubscriberState instance = new PSSubscriberState();
        instance.notProvisionedFromSGSNorMME = true;
        return instance;
    }

    public static PSSubscriberState createPsDetached() {
        PSSubscriberState instance = new PSSubscriberState();
        instance.psDetach = true;
        return instance;
    }

    public static PSSubscriberState createPsAttachedNotReachableForPaging() {
        PSSubscriberState instance = new PSSubscriberState();
        instance.psAttachedNotReachableForPaging = true;
        return instance;
    }

    public static PSSubscriberState createPsAttachedReachableForPaging() {
        PSSubscriberState instance = new PSSubscriberState();
        instance.psAttachedReachableForPaging = true;
        return instance;
    }

    public static PSSubscriberState createPsPDPActiveNotReachableForPaging(PDPContextInfoList list) {
        PSSubscriberState instance = new PSSubscriberState();
        instance.psPDPActiveNotReachableForPaging = list;
        return instance;
    }

    public static PSSubscriberState createPsPDPActiveReachableForPaging(PDPContextInfoList list) {
        PSSubscriberState instance = new PSSubscriberState();
        instance.psPDPActiveReachableForPaging = list;
        return instance;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();

            if (notProvisionedFromSGSNorMME) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            } else if (psDetach) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            } else if (psAttachedNotReachableForPaging) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
            } else if (psAttachedReachableForPaging) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
            } else if (psPDPActiveNotReachableForPaging != null) {
                psPDPActiveNotReachableForPaging.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            } else if (psPDPActiveReachableForPaging != null) {
                psPDPActiveReachableForPaging.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            } else {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, netDetNotReachable.value());
            }

            aos.FinalizeContent(pos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int length = ais.readLength();
            
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    notProvisionedFromSGSNorMME = true;
                    ais.readNull();
                    break;
                case 1:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    psDetach = true;
                    ais.readNull();
                    break;
                case 2:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    psAttachedNotReachableForPaging = true;
                    ais.readNull();
                    break;
                case 3:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    psAttachedReachableForPaging = true;
                    ais.readNull();
                    break;
                case 4:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    psPDPActiveNotReachableForPaging = new PDPContextInfoList();
                    psPDPActiveNotReachableForPaging.decode(ais);
                    break;
                case 5:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
                    }
                    psPDPActiveReachableForPaging = new PDPContextInfoList();
                    psPDPActiveReachableForPaging.decode(ais);
                    break;
                case Tag.ENUMERATED:
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d]. Expecting Class[UNIVERSAL]", tag, ais.getTagClass()));
                    }
                    netDetNotReachable = NotReachableReason.getInstance((int) ais.readInteger());
                    break;
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public boolean isNotProvisionedFromSGSNorMME() {
        return notProvisionedFromSGSNorMME;
    }

    public boolean isPsAttachedNotReachableForPaging() {
        return psAttachedNotReachableForPaging;
    }

    public boolean isPsAttachedReachableForPaging() {
        return psAttachedReachableForPaging;
    }

    public boolean isPsDetach() {
        return psDetach;
    }

    public NotReachableReason getNetDetNotReachable() {
        return netDetNotReachable;
    }

    public PDPContextInfoList getPsPDPActiveNotReachableForPaging() {
        return psPDPActiveNotReachableForPaging;
    }

    public PDPContextInfoList getPsPDPActiveReachableForPaging() {
        return psPDPActiveReachableForPaging;
    }

}
