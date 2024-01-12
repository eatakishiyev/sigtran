/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.CellGlobalIdOrServiceAreaIdFixedLength;
import azrc.az.sigtran.map.parameters.LAIFixedLength;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ChangeOfLocation {

    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength serviceAreaId;
    private LAIFixedLength locationAreaId;
    private Boolean interSystemHandover;
    private Boolean interPlmnHandover;
    private Boolean interMscHandover;
    private ChangeOfLocationAlt changeOfLocationAlt;
    private static Type type_;

    private ChangeOfLocation() {

    }

    public final static ChangeOfLocation create(Type type) {
        type_ = type;
        return new ChangeOfLocation();
    }

    public final static ChangeOfLocation createCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalId) {
        type_ = Type.CELL_GLOBAL_ID;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.cellGlobalId = cellGlobalId;
        return instance;
    }

    public final static ChangeOfLocation createServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength serviceAreaId) {
        type_ = Type.SERVICE_AREA_ID;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.serviceAreaId = serviceAreaId;
        return instance;
    }

    public final static ChangeOfLocation createLocationAreaId(LAIFixedLength locationAreaId) {
        type_ = Type.LOCATION_AREA_ID;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.locationAreaId = locationAreaId;
        return instance;
    }

    public final static ChangeOfLocation createInterSystemHandOver() {
        type_ = Type.INTER_SYSTEM_HANDOVER;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.interSystemHandover = true;
        return instance;
    }

    public final static ChangeOfLocation createInterPLMNHandOver() {
        type_ = Type.INTER_PLMN_HANDOVER;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.interPlmnHandover = true;
        return instance;
    }

    public final static ChangeOfLocation createInterMSCHandOver() {
        type_ = Type.INTER_MSC_HANDOVER;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.interMscHandover = true;
        return instance;
    }

    public final static ChangeOfLocation createChangeOfLocationAlt() {
        type_ = Type.CHANGE_OF_LOCATION_ALT;

        ChangeOfLocation instance = new ChangeOfLocation();
        instance.changeOfLocationAlt = new ChangeOfLocationAlt();
        return instance;
    }

    public void encode(AsnOutputStream aos) throws AsnException, IOException {
        switch (type_) {
            case CELL_GLOBAL_ID:
                cellGlobalId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
                break;
            case SERVICE_AREA_ID:
                this.serviceAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
                break;
            case LOCATION_AREA_ID:
                this.locationAreaId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
                break;
            case INTER_SYSTEM_HANDOVER:
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
                break;
            case INTER_PLMN_HANDOVER:
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 4);
                break;
            case INTER_MSC_HANDOVER:
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 5);
                break;
            case CHANGE_OF_LOCATION_ALT:
                changeOfLocationAlt.encode(6, Tag.CLASS_CONTEXT_SPECIFIC, aos);
                break;
        }
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        if (type_ == null) {
            throw new AsnException("ChangeOfLocation type not found. " + type_);
        }

        switch (type_) {
            case CELL_GLOBAL_ID:
                this.cellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLength();
                this.cellGlobalId.decode(ais);
                break;
            case SERVICE_AREA_ID:
                this.serviceAreaId = new CellGlobalIdOrServiceAreaIdFixedLength();
                this.serviceAreaId.decode(ais);
                break;
            case LOCATION_AREA_ID:
                this.locationAreaId = new LAIFixedLength();
                this.locationAreaId.decode(ais);
                break;
            case INTER_SYSTEM_HANDOVER:
                this.interSystemHandover = true;
                ais.readNull();
                break;
            case INTER_PLMN_HANDOVER:
                this.interPlmnHandover = true;
                ais.readNull();
                break;
            case INTER_MSC_HANDOVER:
                this.interMscHandover = true;
                ais.readNull();
                break;
            case CHANGE_OF_LOCATION_ALT:
                this.changeOfLocationAlt = new ChangeOfLocationAlt();
                this.changeOfLocationAlt.decode(ais);
                break;
        }
    }

    /**
     * @return the cellGlobalId
     */
    public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalId() {
        return cellGlobalId;
    }

    /**
     * @return the serviceAreaId
     */
    public CellGlobalIdOrServiceAreaIdFixedLength getServiceAreaId() {
        return serviceAreaId;
    }

    /**
     * @return the locationAreaId
     */
    public LAIFixedLength getLocationAreaId() {
        return locationAreaId;
    }

    /**
     * @return the interSystemHandover
     */
    public Boolean getInterSystemHandover() {
        return interSystemHandover;
    }

    /**
     * @return the interPlmnHandover
     */
    public Boolean getInterPlmnHandover() {
        return interPlmnHandover;
    }

    /**
     * @return the interMscHandover
     */
    public Boolean getInterMscHandover() {
        return interMscHandover;
    }

    /**
     * @return the changeOfLocationAlt
     */
    public ChangeOfLocationAlt getChangeOfLocationAlt() {
        return changeOfLocationAlt;
    }

    public enum Type {

        CELL_GLOBAL_ID(0),
        SERVICE_AREA_ID(1),
        LOCATION_AREA_ID(2),
        INTER_SYSTEM_HANDOVER(3),
        INTER_PLMN_HANDOVER(4),
        INTER_MSC_HANDOVER(5),
        CHANGE_OF_LOCATION_ALT(6);

        private int value;

        private Type(int value) {
            this.value = value;
        }

        public static Type getIntance(int value) {
            switch (value) {
                case 0:
                    return CELL_GLOBAL_ID;
                case 1:
                    return SERVICE_AREA_ID;
                case 2:
                    return LOCATION_AREA_ID;
                case 3:
                    return INTER_SYSTEM_HANDOVER;
                case 4:
                    return INTER_PLMN_HANDOVER;
                case 5:
                    return INTER_MSC_HANDOVER;
                case 6:
                    return CHANGE_OF_LOCATION_ALT;
                default:
                    return null;
            }
        }

    }
}
