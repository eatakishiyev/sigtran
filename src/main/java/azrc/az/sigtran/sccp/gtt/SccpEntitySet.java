/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.gtt;

import azrc.az.sigtran.sccp.access.point.MTPServiceAccessPoint;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
//                 |-------MTP SAP------------|
//  SCCPEntity = MTP_NI + MTP_OPC + MPT_SI + DPC + SSN
//                 |-------MTP SAP------------|
public class SccpEntitySet implements Serializable {

    private String name;
    //List of MTP-SAPs
    private MTPServiceAccessPoint masterSap;
    private MTPServiceAccessPoint slaveSap;

    private SubSystemNumber ssn;
    private Mode mode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SccpEntitySet[");
        sb.append("Name = ").append(name)
                .append(";Ssn = ").append(ssn)
                .append(";Mode = ").append(mode)
                .append(";Master SAP = ").append(masterSap)
                .append(";Slave SAP = ").append(slaveSap);
        sb.append("]");

        return sb.toString();
    }

    public SccpEntitySet() {
    }

    public SccpEntitySet(String name, Mode mode) {
        this.name = name;
        this.mode = mode;
    }

    public SccpEntitySet(String name, Mode mode, SubSystemNumber ssn) {
        this.name = name;
        this.mode = mode;
        this.ssn = ssn;
    }

    public void setMasterSap(MTPServiceAccessPoint masterSap) {
        this.masterSap = masterSap;
    }

    public void setSlaveSap(MTPServiceAccessPoint slaveSap) {
        this.slaveSap = slaveSap;
    }

    public MTPServiceAccessPoint getMasterSap() {
        return masterSap;
    }

    public MTPServiceAccessPoint getSlaveSap() {
        return slaveSap;
    }

    /**
     * @return the ssn
     */
    public SubSystemNumber getSsn() {
        return ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(SubSystemNumber ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public enum Mode {

        SOLITARY,
        DOMINANT,
        /**
         * Loadsharing among SCCP entities will be achieved on the basis of SLS
         * for class 1 traffic and on round-robin fashion for class 0 traffic.
         */
        LOADSHARING,
        UNKNOWN;

        public static Mode getInstance(String value) {
            switch (value.trim().toUpperCase()) {
                case "SOLITARY":
                    return SOLITARY;
                case "DOMINANT":
                    return DOMINANT;
                case "LOADSHARING":
                    return LOADSHARING;
                default:
                    return UNKNOWN;

            }
        }
    }
}
