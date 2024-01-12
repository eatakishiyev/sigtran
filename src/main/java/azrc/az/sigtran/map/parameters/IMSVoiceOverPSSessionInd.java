/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * IMS-VoiceOverPS-SessionsInd ::= ENUMERATED {
 * imsVoiceOverPS-SessionsNotSupported (0),
 * imsVoiceOverPS-SessionsSupported (1),
 * unknown (2)
 * }
 * -- "unknown" shall not be used within ProvideSubscriberInfoRe
 * @author eatakishiyev
 */
public enum IMSVoiceOverPSSessionInd {

    IMS_VOICE_OVER_PS_SESSION_NOT_SUPPORTED(0),
    IMS_VOICE_OVER_PS_SESSION_SUPPORTED(1),
    UNKNOWN(2);

    private final int value;

    private IMSVoiceOverPSSessionInd(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static IMSVoiceOverPSSessionInd getInstance(int value) {
        switch (value) {
            case 0:
                return IMS_VOICE_OVER_PS_SESSION_NOT_SUPPORTED;
            case 1:
                return IMS_VOICE_OVER_PS_SESSION_SUPPORTED;
            default:
                return UNKNOWN;
        }
    }
}
