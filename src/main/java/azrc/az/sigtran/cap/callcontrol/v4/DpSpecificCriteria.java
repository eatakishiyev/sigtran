/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import java.io.IOException;

import azrc.az.sigtran.cap.parameters.ApplicationTimer;
import azrc.az.sigtran.cap.parameters.DpSpecificCriteriaAlt;
import azrc.az.sigtran.cap.parameters.MidCallControlInfo;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * DpSpecificCriteria {PARAMETERS-BOUND : bound}::= CHOICE { applicationTimer
 * [1] ApplicationTimer, midCallControlInfo [2] MidCallControlInfo,
 * dpSpecificCriteriaAlt [3] DpSpecificCriteriaAlt {bound} }
 *
 * -- Exception handling: reception of DpSpecificCriteriaAlt shall be treated
 * like -- reception of no DpSpecificCriteria. -- The gsmSCF may set a timer in
 * the gsmSSF for the No_Answer event. -- If the user does not answer the call
 * within the allotted time, -- then the gsmSSF reports the event to the gsmSCF.
 * -- The gsmSCF may define a criterion for the detection of DTMF digits during
 * a call. -- The gsmSCF may define other criteria in the dpSpecificCriteriaAlt
 * alternative -- in future releases.
 *
 * @author eatakishiyev
 */
public class DpSpecificCriteria {

    private ApplicationTimer applicationTimer;
    private MidCallControlInfo midCallControlInfo;
    private DpSpecificCriteriaAlt dpSpecificCriteriaAlt;

    public DpSpecificCriteria() {
    }

    public DpSpecificCriteria(ApplicationTimer applicationTimer) {
        this.applicationTimer = applicationTimer;
    }

    public DpSpecificCriteria(MidCallControlInfo midCallControlInfo) {
        this.midCallControlInfo = midCallControlInfo;
    }

    public DpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt) {
        this.dpSpecificCriteriaAlt = dpSpecificCriteriaAlt;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws ParameterOutOfRangeException, IOException, AsnException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (this.applicationTimer != null) {
            this.applicationTimer.encode(1, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        } else if (this.midCallControlInfo != null) {
            this.midCallControlInfo.encode(2, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        } else if (this.dpSpecificCriteriaAlt != null) {
            this.dpSpecificCriteriaAlt.encode(3, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        int tag = tmpAis.readTag();

        switch (tag) {
            case 1:
                this.applicationTimer = new ApplicationTimer();
                this.applicationTimer.decode(tmpAis);
                break;
            case 2:
                this.midCallControlInfo = new MidCallControlInfo();
                this.midCallControlInfo.decode(tmpAis);
                break;
            case 3:
                this.dpSpecificCriteriaAlt = new DpSpecificCriteriaAlt();
                this.dpSpecificCriteriaAlt.decode(tmpAis);
                break;
        }
    }

    /**
     * @return the applicationTimer
     */
    public ApplicationTimer getApplicationTimer() {
        return applicationTimer;
    }

    /**
     * @return the midCallControlInfo
     */
    public MidCallControlInfo getMidCallControlInfo() {
        return midCallControlInfo;
    }

    /**
     * @return the dpSpecificCriteriaAlt
     */
    public DpSpecificCriteriaAlt getDpSpecificCriteriaAlt() {
        return dpSpecificCriteriaAlt;
    }

}
