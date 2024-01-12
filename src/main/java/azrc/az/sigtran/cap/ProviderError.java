/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public enum ProviderError {

    DUPLICATE_INVOKE_ID,
    NOT_SUPPORTED_SERVICE,
    MISTYPED_PARAMETER,
    RESOURCE_LIMITATION,
    INITIATING_RELEASE,
    UNEXPECTED_RESPONSE_FROM_PEER,
    SERVICE_COMPLETION_FAILURE,
    NO_RESPONSE_FROM_PEER,
    INVALID_RESPONSE_RECEIVED;
}
