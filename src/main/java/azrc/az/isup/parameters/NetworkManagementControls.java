/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class NetworkManagementControls implements IsupParameter {

    private byte[] networkManagementControls;

    public NetworkManagementControls() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < networkManagementControls.length; i++) {
            networkManagementControls[i] = (byte) (networkManagementControls[i] & 0b00000001);
        }
        networkManagementControls[networkManagementControls.length - 1]
                = (byte) (networkManagementControls[networkManagementControls.length - 1] | 0b10000000);
        return (networkManagementControls);
    }

    @Override
    public void decode(byte[] data) {
        this.networkManagementControls = data;
    }

    public byte[] getNetworkManagementControls() {
        return networkManagementControls;
    }

    public void setNetworkManagementControls(byte[] networkManagementControls) {
        this.networkManagementControls = networkManagementControls;
    }

    public static boolean isTemporaryAlternativeRoutingControlledCall(byte b) {
        return (b & 0b00000001) == 1;
    }

    public static byte encodeNetworkManagementControlsByte(boolean temporaryAlternativeRoutingControlledCall) {
        return (byte) (temporaryAlternativeRoutingControlledCall ? 1 : 0);
    }

    @Override
    public int getParameterCode() {
        return NETWORK_MANAGEMENT_CONTROLS;
    }

}
