/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import java.util.Arrays;

/**
 *
 * @author eatakishiyev
 */
public enum CAPApplicationContexts {

    CAP_V1_GSMSSF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 0, 50, 0}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP:
                case CONNECT:
                case RELEASE_CALL:
                case EVENT_REPORT_BCSM:
                case REQUEST_REPORT_BCSM_EVENT:
                case CONTINUE:
                case ACTIVITY_TEST:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 1;
        }
    },
    /**
     * CAP-v2-gsmSRF-to-gsmSCF-AC	APPLICATION-CONTEXT -- dialogue initiated by
     * gsmSRF with AssistRequestInstructions INITIATOR CONSUMER OF {
     * GSM-SCF-GSM-SRF-activation-of-assist-ASE,
     * Specialized-resource-control-ASE, Cancel-ASE, Activity-test-ASE } ::=
     * {ccitt(0) identified-organization(4) etsi(0) mobileDomain(0)
     * gsm-Network(1) ac(0)cap-gsmSRF-to-gsmscf(52) version2(1)}; CAP-Classes
     * {ccitt(0) identified-organization(4) etsi(0) mobileDomain(0)
     * gsm-Network(1) modules(3) cap--classes(54) version2(1)}
     */
    CAP_V2_GSMSRF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 0, 52, 1}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ASSIST_REQUEST_INSTRUCTIONS:
                case SPECIALISED_RESOURCE_REPORT:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case CANCEL:
                case ACTIVITY_TEST:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 2;
        }
    },
    /**
     * CAP-v2-assist-gsmSSF-to-gsmSCF-AC	APPLICATION-CONTEXT -- dialogue
     * initiated by gsmSSF with AssistRequestInstructions INITIATOR CONSUMER OF
     * { GSM-SCF-GSM-SRF-activation-of-assist-ASE,
     * Generic-disconnect-resource-ASE,
     * Non-assisted-connection-establishment-ASE, Timer-ASE,
     * Specialized-resource-control-ASE, Cancel-ASE, Activity-test-ASE } ::=
     * {ccitt(0) identified-organization(4) etsi(0) mobileDomain(0)
     * gsm-Network(1) ac(0) cap-assist-handoff-gsmssf-to-gsmscf(51)
     * version2(1)};
     *
     */
    CAP_V2_ASSIST_GSMSSF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 0, 51, 1}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ASSIST_REQUEST_INSTRUCTIONS:
                case DISCONNECT_FORWARD_CONNECTION:
                case CONNECT_TO_RESOURCE:
                case RESET_TIMER:
                case SPECIALISED_RESOURCE_REPORT:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case CANCEL:
                case ACTIVITY_TEST:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 2;
        }
    },
    /**
     * CAP-v2-gsmSSF-to-gsmSCF-AC APPLICATION-CONTEXT -- dialogue initiated by
     * gsmSSF with InitialDP INITIATOR CONSUMER OF { GSM-SCF-activation-ASE,
     * Assist-connection-establishment-ASE,
     * Non-assisted-connection-establishment-ASE,
     * Generic-disconnect-resource-ASE, Connect-ASE, Call-handling-ASE,
     * BCSM-event-handling-ASE, Charging-ASE, GSM-SSF-call-processing-ASE,
     * Timer-ASE, Billing-ASE, Call-report-ASE, Signalling-control-ASE,
     * Specialized-resource-control-ASE, Cancel-ASE, Activity-test-ASE } ::=
     * {ccitt(0) identified-organization(4) etsi(0) mobileDomain(0)
     * gsm-Network(1) ac(0) cap-gsmssf-to-gsmscf(50) version2(1)};
     */
    CAP_V2_GSMSSF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 0, 50, 1}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case APPLY_CHARGING:
                case REQUEST_REPORT_BCSM_EVENT:
                case EVENT_REPORT_BCSM:
                case APPLY_CHARGING_REPORT:
                case INITIAL_DP:
                case ESTABLISH_TEMPORARY_CONNECTION:
                case CONNECT_TO_RESOURCE:
                case DISCONNECT_FORWARD_CONNECTION:
                case CONNECT:
                case RELEASE_CALL:
                case CONTINUE:
                case RESET_TIMER:
                case FURNISH_CHARGING_INFORMATION:
                case CALL_INFORMATION_REPORT:
                case SEND_CHARGING_INFORMATION:
                case SPECIALISED_RESOURCE_REPORT:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case CANCEL:
                case ACTIVITY_TEST:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 2;
        }
    },
    CAP_V3_GSMSSF_SCF_GENERIC_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 4}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ACTIVITY_TEST:
                case APPLY_CHARGING:
                case APPLY_CHARGING_REPORT:
                case CALL_INFORMATION_REPORT:
                case CALL_INFORMATION_REQUEST:
                case CANCEL:
                case CONNECT:
                case CONTINUE_WITH_ARGUMENT:
                case CONNECT_TO_RESOURCE:
                case DISCONNECT_FORWARD_CONNECTION:
                case ESTABLISH_TEMPORARY_CONNECTION:
                case EVENT_REPORT_BCSM:
                case FURNISH_CHARGING_INFORMATION:
                case INITIAL_DP:
                case RELEASE_CALL:
                case REQUEST_REPORT_BCSM_EVENT:
                case RESET_TIMER:
                case SEND_CHARGING_INFORMATION:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case SPECIALISED_RESOURCE_REPORT:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V3_GSMSSF_SCF_ASSIST_HANDOFF_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 6}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ACTIVITY_TEST:
                case ASSIST_REQUEST_INSTRUCTIONS:
                case CANCEL:
                case CONNECT_TO_RESOURCE:
                case DISCONNECT_FORWARD_CONNECTION:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case RESET_TIMER:
                case SPECIALISED_RESOURCE_REPORT:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V3_GSMSRF_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 20, 3, 14}) {

        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ACTIVITY_TEST:
                case ASSIST_REQUEST_INSTRUCTIONS:
                case CANCEL:
                case PLAY_ANNOUNCEMENT:
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case SPECIALISED_RESOURCE_REPORT:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V3_GPRSSSF_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 50}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ACTIVITY_TEST_GPRS:
                case APPLY_CHARGING_GPRS:
                case APPLY_CHARGING_REPORT_GPRS:
                case CANCEL_GPRS:
                case CONNECT_GPRS:
                case ENTITY_RELEASED_GPRS:
                case EVENT_REPORT_GPRS:
                case FURNISH_CHARGING_INFORMATION_GPRS:
                case INITIAL_DP_GPRS:
                case RELEASE_GPRS:
                case REQUEST_REPORT_GPRS_EVENT:
                case RESET_TIMER_GPRS:
                case SEND_CHARGING_INFORMATION_GPRS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V3_GSMSCF_GPRSSSF_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 51}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case ACTIVITY_TEST_GPRS:
                case APPLY_CHARGING_GPRS:
                case CANCEL_GPRS:
                case FURNISH_CHARGING_INFORMATION_GPRS:
                case INITIAL_DP_GPRS:
                case RELEASE_GPRS:
                case REQUEST_REPORT_GPRS_EVENT:
                case SEND_CHARGING_INFORMATION_GPRS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V3_SMS_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 61}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP_SMS:
                case CONNECT_SMS:
                case CONTINUE_SMS:
                case RELEASE_SMS:
                case REQUEST_REPORT_SMS_EVENT:
                case EVENT_REPORT_SMS:
                case FURNISH_CHARGING_INFORMATION_SMS:
                case RESET_TIMER_SMS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 3;
        }
    },
    CAP_V4_GSMSCF_TO_GPRSSSF_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 51}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCodes) {
            switch (capOpCodes) {
                case CONTINUE_GPRS:
                case ENTITY_RELEASED_GPRS:
                case INITIAL_DP_GPRS:
                case CONNECT_GPRS:
                case RELEASE_GPRS:
                case REQUEST_REPORT_GPRS_EVENT:
                case EVENT_REPORT_GPRS:
                case RESET_TIMER_GPRS:
                case FURNISH_CHARGING_INFORMATION_GPRS:
                case APPLY_CHARGING_GPRS:
                case APPLY_CHARGING_REPORT_GPRS:
                case ACTIVITY_TEST_GPRS:
                case CANCEL_GPRS:
                case SEND_CHARGING_INFORMATION_GPRS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GPRSSSF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 21, 3, 50}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCodes) {
            switch (capOpCodes) {
                case CONTINUE_GPRS:
                case ENTITY_RELEASED_GPRS:
                case INITIAL_DP_GPRS:
                case CONNECT_GPRS:
                case RELEASE_GPRS:
                case REQUEST_REPORT_GPRS_EVENT:
                case EVENT_REPORT_GPRS:
                case RESET_TIMER_GPRS:
                case FURNISH_CHARGING_INFORMATION_GPRS:
                case APPLY_CHARGING_GPRS:
                case APPLY_CHARGING_REPORT_GPRS:
                case ACTIVITY_TEST_GPRS:
                case CANCEL_GPRS:
                case SEND_CHARGING_INFORMATION_GPRS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GSMSMS_AC(new long[]{0, 4, 0, 0, 1, 23, 3, 61}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP_SMS:
                case CONNECT_SMS:
                case CONTINUE_SMS:
                case RELEASE_SMS:
                case REQUEST_REPORT_SMS_EVENT:
                case EVENT_REPORT_SMS:
                case FURNISH_CHARGING_INFORMATION_SMS:
                case RESET_TIMER_SMS:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GSMSCF_TO_GSMSSF_GENERIC_AC(new long[]{0, 4, 0, 0, 1, 23, 3, 8}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP:
                case ASSIST_REQUEST_INSTRUCTIONS:
                case ESTABLISH_TEMPORARY_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION_WITH_ARGUMENT:
                case CONNECT_TO_RESOURCE:
                case CONNECT:
                case RELEASE_CALL:
                case REQUEST_REPORT_BCSM_EVENT:
                case EVENT_REPORT_BCSM:
                case COLLECT_INFORMATION:
                case INITIATE_CALL_ATTEMPT:
                case RESET_TIMER:
                case FURNISH_CHARGING_INFORMATION:
                case APPLY_CHARGING:
                case APPLY_CHARGING_REPORT:
                case CALL_GAP:
                case CALL_INFORMATION_REQUEST:
                case CALL_INFORMATION_REPORT:
                case SEND_CHARGING_INFORMATION:
                case ACTIVITY_TEST:
                case CANCEL:
                case CONTINUE_WITH_ARGUMENT:
                case DISCONNECT_LEG:
                case MOVE_LEG:
                case SPLIT_LEG:
                case ENTITY_RELEASED:
                case PLAY_TONE:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GSMSSF_TO_GSMSCF_ASSIST_HANDOFF_AC(new long[]{0, 4, 0, 0, 1, 23, 3, 6}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP:
                case ASSIST_REQUEST_INSTRUCTIONS:
                case ESTABLISH_TEMPORARY_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION_WITH_ARGUMENT:
                case CONNECT_TO_RESOURCE:
                case CONNECT:
                case RELEASE_CALL:
                case REQUEST_REPORT_BCSM_EVENT:
                case EVENT_REPORT_BCSM:
                case COLLECT_INFORMATION:
                case INITIATE_CALL_ATTEMPT:
                case RESET_TIMER:
                case FURNISH_CHARGING_INFORMATION:
                case APPLY_CHARGING:
                case APPLY_CHARGING_REPORT:
                case CALL_GAP:
                case CALL_INFORMATION_REQUEST:
                case CALL_INFORMATION_REPORT:
                case SEND_CHARGING_INFORMATION:
                case ACTIVITY_TEST:
                case CANCEL:
                case CONTINUE_WITH_ARGUMENT:
                case DISCONNECT_LEG:
                case MOVE_LEG:
                case SPLIT_LEG:
                case ENTITY_RELEASED:
                case PLAY_TONE:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GSMSSF_TO_GSMSCF_GENERIC_AC(new long[]{0, 4, 0, 0, 1, 23, 3, 4}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case INITIAL_DP:
                case ASSIST_REQUEST_INSTRUCTIONS:
                case ESTABLISH_TEMPORARY_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION:
                case DISCONNECT_FORWARD_CONNECTION_WITH_ARGUMENT:
                case CONNECT_TO_RESOURCE:
                case CONNECT:
                case RELEASE_CALL:
                case REQUEST_REPORT_BCSM_EVENT:
                case EVENT_REPORT_BCSM:
                case COLLECT_INFORMATION:
                case INITIATE_CALL_ATTEMPT:
                case RESET_TIMER:
                case FURNISH_CHARGING_INFORMATION:
                case APPLY_CHARGING:
                case APPLY_CHARGING_REPORT:
                case CALL_GAP:
                case CALL_INFORMATION_REQUEST:
                case CALL_INFORMATION_REPORT:
                case SEND_CHARGING_INFORMATION:
                case ACTIVITY_TEST:
                case CANCEL:
                case CONTINUE_WITH_ARGUMENT:
                case DISCONNECT_LEG:
                case MOVE_LEG:
                case SPLIT_LEG:
                case ENTITY_RELEASED:
                case PLAY_TONE:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public int getVersion() {
            return 4;
        }
    },
    CAP_V4_GSMSRF_TO_GSMSCF_AC(new long[]{0, 4, 0, 0, 1, 22, 3, 14}) {
        @Override
        public boolean isOperationExists(OperationCodes capOpCode) {
            switch (capOpCode) {
                case PROMPT_AND_COLLECT_USER_INFORMATION:
                case PLAY_ANNOUNCEMENT:
                case SPECIALISED_RESOURCE_REPORT:
                case CANCEL:
                    return true;
            }
            return false;
        }

        @Override
        public int getVersion() {
            return 4;
        }
    };

    private final long[] oid;

    public abstract int getVersion();

    public abstract boolean isOperationExists(OperationCodes capOpCode);

    private CAPApplicationContexts(long[] oid) {
        this.oid = oid;
    }

    public long[] oid() {
        return this.oid;
    }

    public static CAPApplicationContexts getInstance(long[] oid) {
        if (Arrays.equals(oid, CAP_V1_GSMSSF_TO_GSMSCF_AC.oid)) {
            return CAP_V1_GSMSSF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V2_ASSIST_GSMSSF_TO_GSMSCF_AC.oid)) {
            return CAP_V2_ASSIST_GSMSSF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V2_GSMSRF_TO_GSMSCF_AC.oid)) {
            return CAP_V2_GSMSRF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V2_GSMSSF_TO_GSMSCF_AC.oid)) {
            return CAP_V2_GSMSSF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V3_SMS_AC.oid)) {
            return CAP_V3_SMS_AC;
        } else if (Arrays.equals(oid, CAP_V4_GPRSSSF_TO_GSMSCF_AC.oid)) {
            return CAP_V4_GPRSSSF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSCF_TO_GPRSSSF_AC.oid)) {
            return CAP_V4_GSMSCF_TO_GPRSSSF_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSCF_TO_GSMSSF_GENERIC_AC.oid)) {
            return CAP_V4_GSMSCF_TO_GSMSSF_GENERIC_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSMS_AC.oid)) {
            return CAP_V4_GSMSMS_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSRF_TO_GSMSCF_AC.oid)) {
            return CAP_V4_GSMSRF_TO_GSMSCF_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSSF_TO_GSMSCF_ASSIST_HANDOFF_AC.oid)) {
            return CAP_V4_GSMSSF_TO_GSMSCF_ASSIST_HANDOFF_AC;
        } else if (Arrays.equals(oid, CAP_V4_GSMSSF_TO_GSMSCF_GENERIC_AC.oid)) {
            return CAP_V4_GSMSSF_TO_GSMSCF_GENERIC_AC;
        }

        return null;
    }

}
