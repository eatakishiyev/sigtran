/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

/**
 * CAP-U-ABORT-Data {itu-t(0) identified-organization(4) etsi(0) mobileDomain(0)
 * umts-network(1) modules(3) cap-u-abort-data(110) version8(7)} DEFINITIONS ::=
 * BEGIN id-CAP-U-ABORT-Reason OBJECT IDENTIFIER ::= {itu-t(0)
 * identified-organization(4) etsi(0) mobileDomain(0) umts-Network(1) as(1)
 * cap-u-abort-reason(2) version3(2)} cAP-U-ABORT-Reason-Abstract-Syntax
 * ABSTRACT-SYNTAX ::= {CAP-U-ABORT-REASON IDENTIFIED BY id-CAP-U-ABORT-Reason}
 * CAP-U-ABORT-REASON ::= ENUMERATED { no-reason-given (1),
 * application-timer-expired (2), not-allowed-procedures (3),
 * abnormal-processing (4), congestion (5), invalid-reference (6),
 * missing-reference (7), overlapping-dialogue (8) } --
 * application-timer-expired shall be set when application timer (e.g. Tssf) is
 * expired. 3GPP 61 3GPP TS 29.078 V11.1.0 (2011-12) Release 11 --
 * not-allowed-procedures shall be set when received signal is not allowed in
 * CAP -- procedures. -- For example, when a class 4 operation is received from
 * the -- gsmSCF and the operation is not allowed in gsmSSF FSM. -- (gsmSSF FSM
 * cannot continue state transition). (e.g. ReleaseCall -- operation received in
 * Waiting for End of Temporary Connection -- state.) -- abnormal-processing
 * shall be set when abnormal procedures occur at entity action. -- congestion
 * shall be set when requested resource is unavailable due to -- congestion at
 * TC user (CAP) level. -- invalid-reference shall be set if the received
 * destinationReference is unknown or -- for a known destination Reference the
 * received originationReference -- does not match with the stored
 * originationReference. -- This abort reason is used for CAP defined
 * GPRS-ReferenceNumber. -- missing-reference shall be set when the
 * destinationReference or the -- originationReference is absent in the received
 * message but is -- required to be present according to the procedures in --
 * subclause 14.1.7. -- This abort reason is used for CAP defined
 * GPRS-ReferenceNumber. -- overlapping-dialogue shall be used by the gprsSSF to
 * indicate to the gsmSCF that a -- specific instance already has a TC dialogue
 * open. This error -- cause is typically obtained when both the gsmSCF and
 * gprsSSF -- open a new dialogue at the same time. -- no-reason-given shall be
 * set when any other reasons above do not apply END –- of CAP-U-ABORT-Data
 *
 * @author eatakishiyev
 */
public enum CapUAbortReason {

    NO_REASON_GIVEN(1),
    APPLICATION_TIMER_EXPIRED(2),
    NOT_ALLOWED_PROCEDURES(3),
    ABNORMAL_PROCESSING(4),
    CONGESTION(5),
    INVALID_REFERENCE(6),
    MISSING_REFERENCE(7),
    OVERLAPPING_DIALOGUE(8);

    private int value;

    private CapUAbortReason(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CapUAbortReason getInstance(int value) {
        switch (value) {
            case 1:
                return NO_REASON_GIVEN;
            case 2:
                return APPLICATION_TIMER_EXPIRED;
            case 3:
                return NOT_ALLOWED_PROCEDURES;
            case 4:
                return ABNORMAL_PROCESSING;
            case 5:
                return CONGESTION;
            case 6:
                return INVALID_REFERENCE;
            case 7:
                return MISSING_REFERENCE;
            case 8:
                return OVERLAPPING_DIALOGUE;
            default:
                return null;
        }
    }
}
