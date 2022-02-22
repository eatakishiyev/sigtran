/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.access.point;

import dev.ocean.sigtran.m3ua.NetworkIndicator;
import dev.ocean.sigtran.m3ua.ServiceIdentificator;
import dev.ocean.sigtran.sccp.messages.MessageType;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author eatakishiyev
 */
public class MTPServiceAccessPoint implements Serializable {

    private String name;
    private int dpc;
    private int opc;
    private NetworkIndicator ni;
    private final ServiceIdentificator si = ServiceIdentificator.SCCP;
    private MessageType targetMessageType;
//    private Priority priority;

    @Override
    public String toString() {
        return new StringBuilder().
                append("MTPSAP[Name = ").append(name)
                .append(";Dpc = ").append(dpc)
                .append(";Opc = ").append(opc)
                .append(";Ni = ").append(ni)
                .append(";Si = ").append(si).append("]").toString();
    }

    public MTPServiceAccessPoint() {
    }

    public MTPServiceAccessPoint(String name, int dpc, int opc, NetworkIndicator ni) {
        this.name = name;
        this.dpc = dpc;
        this.opc = opc;
        this.ni = ni;
    }

    /**
     * @return the si
     */
    public ServiceIdentificator getSi() {
        return si;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
    }

    /**
     * @param dpc the dpc to set
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
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

    /**
     * @return the localPointCode
     */
    public int getOpc() {
        return opc;
    }

    /**
     * @param opc the localPointCode to set
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * @return the ni
     */
    public NetworkIndicator getNi() {
        return ni;
    }

    /**
     * @param ni the ni to set
     */
    public void setNi(NetworkIndicator ni) {
        this.ni = ni;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MTPServiceAccessPoint) {
            return ((MTPServiceAccessPoint) obj).getName().equals(this.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + this.dpc;
        hash = 67 * hash + this.opc;
        hash = 67 * hash + Objects.hashCode(this.ni);
        hash = 67 * hash + Objects.hashCode(this.si);
        return hash;
    }

    public MessageType getTargetMessageType() {
        return targetMessageType;
    }

    public void setTargetMessageType(MessageType targetMessageType) {
        this.targetMessageType = targetMessageType;
    }

}
