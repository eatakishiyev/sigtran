/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.services.errors.params.IllegalEquipmentParam;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * illegalEquipment ERROR ::= {
 * PARAMETER
 * IllegalEquipmentParam
 * -- optional
 * -- IllegalEquipmentParam must not be used in version <3
 * CODE local:12 }
 * @author eatakis
 * hiyev
 */
public class IllegalEquipment implements MAPUserError {

    private IllegalEquipmentParam illegalEquipmentParam;

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.illegalEquipmentParam != null) {
            illegalEquipmentParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.illegalEquipmentParam = new IllegalEquipmentParam();
        illegalEquipmentParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ILLEGAL_EQUIPMENT;
    }

    /**
     * @return the illegalEquipmentParam
     */
    public IllegalEquipmentParam getIllegalEquipmentParam() {
        return illegalEquipmentParam;
    }

    /**
     * @param illegalEquipmentParam the illegalEquipmentParam to set
     */
    public void setIllegalEquipmentParam(IllegalEquipmentParam illegalEquipmentParam) {
        this.illegalEquipmentParam = illegalEquipmentParam;
    }

    @Override
    public String toString() {
        return "IllegalEquipment{" +
                "illegalEquipmentParam=" + illegalEquipmentParam +
                '}';
    }
}
