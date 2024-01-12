/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import azrc.az.sigtran.tcap.parameters.UserInformationImpl;

/**
 *
 * @author eatakishiyev
 */
public class MapUAbort {

    private UserReason userReason;
    private DiagnosticInformation diagnosticInformation;
    private UserInformationImpl userInformation;

    /**
     * @return the userReason
     */
    public UserReason getUserReason() {
        return userReason;
    }

    /**
     * @param userReason the userReason to set
     */
    public void setUserReason(UserReason userReason) {
        this.userReason = userReason;
    }

    /**
     * @return the diagnosticInformation
     */
    public DiagnosticInformation getDiagnosticInformation() {
        return diagnosticInformation;
    }

    /**
     * @param diagnosticInformation the diagnosticInformation to set
     */
    public void setDiagnosticInformation(DiagnosticInformation diagnosticInformation) {
        this.diagnosticInformation = diagnosticInformation;
    }

    /**
     * @return the userInformation
     */
    public UserInformationImpl getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformationImpl userInformation) {
        this.userInformation = userInformation;
    }

    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder();
        return sb.append("MAPUAbor[")
                .append("UserReason = ").append(userInformation)
                .append(";DiagnosticInformation = ").append(diagnosticInformation)
                .append(";UserInformation = ").append(userInformation)
                .append("]").toString();
    }

    public enum UserReason {

        RESOURCE_LIMITATION,
        RESOURCE_UNAVAILABLE,
        APPLICATION_PROCEDURE_CANCELATION,
        PROCEDURE_ERROR;
    }

    public enum DiagnosticInformation {

        SHORT_TERM_PROBLEM,
        LONG_TERM_PROBLEM,
        HANDOVER_CANCELLATION,
        RADIO_CHANNEL_RELEASE,
        NETWORK_PATH_RELEASE,
        CALL_RELEASE,
        ASSOCIATED_PROCEDURE_FAILURE,
        TANDEM_DIALOGUE_RELEASED,
        REMOTE_OPERATION_FAILURE;
    }
}
