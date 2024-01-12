/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.CellGlobalIdOrServiceAreaIdFixedLength;
import azrc.az.sigtran.map.parameters.LAIFixedLength;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MetDPCriterion {PARAMETERS-BOUND : bound} ::= CHOICE { enteringCellGlobalId
 * [0] CellGlobalIdOrServiceAreaIdFixedLength, leavingCellGlobalId [1]
 * CellGlobalIdOrServiceAreaIdFixedLength, enteringServiceAreaId [2]
 * CellGlobalIdOrServiceAreaIdFixedLength, leavingServiceAreaId [3]
 * CellGlobalIdOrServiceAreaIdFixedLength, enteringLocationAreaId [4]
 * LAIFixedLength, leavingLocationAreaId [5] LAIFixedLength,
 * inter-SystemHandOverToUMTS [6] NULL, inter-SystemHandOverToGSM [7] NULL,
 * inter-PLMNHandOver [8] NULL, inter-MSCHandOver [9] NULL, metDPCriterionAlt
 * [10] MetDPCriterionAlt {bound} } -- The enteringCellGlobalId and
 * leavingCellGlobalId shall contain a Cell Global Identification. -- The
 * enteringServiceAreaId and leavingServiceAreaId shall contain a Service Area
 * Identification.
 *
 * @author eatakishiyev
 */
public class MetDPCriterion {

    private CellGlobalIdOrServiceAreaIdFixedLength enteringCellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength leavingCellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength enteringServiceAreaId;
    private CellGlobalIdOrServiceAreaIdFixedLength leavingServiceAreaId;
    private LAIFixedLength enteringLocationAreaId;
    private LAIFixedLength leavingLocationAreaId;
    private boolean interSystemHandOverToUMTS = false;
    private boolean interSystemHandOverToGSM = false;
    private boolean interPLMNHandOver = false;
    private boolean interMSCHandOver = false;
    private MetDPCriterionAlt metDPCriterionAlt;

    private MetDPCriterion() {

    }

    public static MetDPCriterion create() {
        MetDPCriterion instance = new MetDPCriterion();
        return instance;
    }

    public static MetDPCriterion createEnteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength enteringCellGlobalId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.enteringCellGlobalId = enteringCellGlobalId;
        return instance;
    }

    public static MetDPCriterion createLeavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength leavingCellGlobalId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.leavingCellGlobalId = leavingCellGlobalId;
        return instance;
    }

    public static MetDPCriterion createEnteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength enteringServiceAreaId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.enteringServiceAreaId = enteringServiceAreaId;
        return instance;
    }

    public static MetDPCriterion createLeavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength leavingServiceAreaId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.leavingServiceAreaId = leavingServiceAreaId;
        return instance;
    }

    public static MetDPCriterion createEnteringLocationAreaId(LAIFixedLength enteringLocationAreId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.enteringLocationAreaId = enteringLocationAreId;
        return instance;
    }

    public static MetDPCriterion createLeavingLocationAreaId(LAIFixedLength leavingLocationAreaId) {
        MetDPCriterion instance = new MetDPCriterion();
        instance.leavingLocationAreaId = leavingLocationAreaId;
        return instance;
    }

    public static MetDPCriterion createInterSystemHandOverToUMTS() {
        MetDPCriterion instance = new MetDPCriterion();
        instance.interSystemHandOverToUMTS = true;
        return instance;
    }

    public static MetDPCriterion createInterSystemHandOverToGSM() {
        MetDPCriterion instance = new MetDPCriterion();
        instance.interSystemHandOverToGSM = true;
        return instance;
    }

    public static MetDPCriterion createInterPLMNHandOver() {
        MetDPCriterion instance = new MetDPCriterion();
        instance.interPLMNHandOver = true;
        return instance;
    }

    public static MetDPCriterion createMSCHandOver() {
        MetDPCriterion instance = new MetDPCriterion();
        instance.interMSCHandOver = true;
        return instance;
    }

    public static MetDPCriterion createMetDPCriterionAlt() {
        MetDPCriterion instance = new MetDPCriterion();
        instance.metDPCriterionAlt = new MetDPCriterionAlt();
        return instance;
    }

    public void encode(AsnOutputStream aos) throws IOException, AsnException {

        if (enteringCellGlobalId != null) {
            enteringCellGlobalId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else if (leavingCellGlobalId != null) {
            leavingCellGlobalId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        } else if (enteringServiceAreaId != null) {
            enteringServiceAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        } else if (leavingServiceAreaId != null) {
            leavingServiceAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        } else if (enteringLocationAreaId != null) {
            enteringLocationAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
        } else if (leavingLocationAreaId != null) {
            leavingLocationAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        } else if (interSystemHandOverToUMTS) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 6);
        } else if (interSystemHandOverToGSM) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 7);
        } else if (interPLMNHandOver) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 8);
        } else if (interMSCHandOver) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 9);
        } else if (metDPCriterionAlt != null) {
            metDPCriterionAlt.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
        }

    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        this.encode(aos);
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.enteringCellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLength();
                    this.enteringCellGlobalId.decode(ais);
                    break;
                case 1:
                    this.leavingCellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLength();
                    this.leavingCellGlobalId.decode(ais);
                    break;
                case 2:
                    this.enteringServiceAreaId = new CellGlobalIdOrServiceAreaIdFixedLength();
                    this.enteringServiceAreaId.decode(ais);
                    break;
                case 3:
                    this.leavingServiceAreaId = new CellGlobalIdOrServiceAreaIdFixedLength();
                    this.leavingServiceAreaId.decode(ais);
                    break;
                case 4:
                    this.enteringLocationAreaId = new LAIFixedLength();
                    this.enteringLocationAreaId.decode(ais);
                    break;
                case 5:
                    this.leavingLocationAreaId = new LAIFixedLength();
                    this.leavingLocationAreaId.decode(ais);
                    break;
                case 6:
                    this.interSystemHandOverToUMTS = true;
                    ais.readNull();
                    break;
                case 7:
                    this.interSystemHandOverToGSM = true;
                    ais.readNull();
                    break;
                case 8:
                    this.interPLMNHandOver = true;
                    ais.readNull();
                    break;
                case 9:
                    this.interMSCHandOver = true;
                    ais.readNull();
                    break;
                case 10:
                    this.metDPCriterionAlt = new MetDPCriterionAlt();
                    this.metDPCriterionAlt.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Received unexpected tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));

            }
        }
    }
}
