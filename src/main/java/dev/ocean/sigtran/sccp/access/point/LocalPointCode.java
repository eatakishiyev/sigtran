/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.access.point;

import dev.ocean.sigtran.m3ua.NetworkIndicator;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public class LocalPointCode implements Serializable {

    private String name;
    private int opc;
    private NetworkIndicator ni;

    public LocalPointCode() {
    }

    public LocalPointCode(String name, int opc, NetworkIndicator ni) {
        this.name = name;
        this.opc = opc;
        this.ni = ni;
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
     * @return the opc
     */
    public int getOpc() {
        return opc;
    }

    /**
     * @param opc the opc to set
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

    public void setNi(NetworkIndicator ni) {
        this.ni = ni;
    }

    @Override
    public String toString() {
        return String.format("LocalSpc [name = %s ; spc = %s ; ni = %s]", name, opc, ni);
    }

}
