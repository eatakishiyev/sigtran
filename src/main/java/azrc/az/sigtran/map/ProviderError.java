/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

/**
 *
 * @author eatakishiyev
 */
public enum ProviderError {

    DUPLICATE_INVOKE_ID(400),
    NOT_SUPPORTED_SERVICE(401),
    MISTYPED_PARAMETER(402),
    RESOURCE_LIMITATION(403),
    INITIATING_RELEASE(404),
    UNEXPECTED_RESPONSE_FROM_PEER(405),
    SERVICE_COMPLETION_FAILURE(406),
    NO_RESPONSE_FROM_PEER(407),
    INVALID_RESPONSE_RECEIVED(408);

    private final int value;

    private ProviderError(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
