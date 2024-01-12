/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class UserTeleServiceInformation implements IsupParameter {

    private HighLayerCompability highLayerCompability;

    public UserTeleServiceInformation() {
    }

    public UserTeleServiceInformation(HighLayerCompability highLayerCompability) {
        this.highLayerCompability = highLayerCompability;
    }

    public HighLayerCompability getHighLayerCompability() {
        return highLayerCompability;
    }

    public void setHighLayerCompability(HighLayerCompability highLayerCompability) {
        this.highLayerCompability = highLayerCompability;
    }

    @Override
    public byte[] encode() throws IOException {
        return (highLayerCompability.encode());
    }

    @Override
    public void decode(byte[] data) {
        this.highLayerCompability = new HighLayerCompability();
        this.highLayerCompability.decode(new ByteArrayInputStream(data));
    }

    @Override
    public int getParameterCode() {
        return USER_TELESERVICE_INFORMATION;
    }

}
