/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.util.Arrays;

/**
 *
 * @author eatakishiyev
 */
public class MAPApplicationContextImpl {

    private MAPApplicationContextName mapApplicationContextName;
    private MAPApplicationContextVersion mapApplicationContextVersion;
    private final long[] oid;

    public MAPApplicationContextImpl(MAPApplicationContextName mapApplicationContextName, MAPApplicationContextVersion mapApplicationContextVersion) {
        this.mapApplicationContextName = mapApplicationContextName;
        this.mapApplicationContextVersion = mapApplicationContextVersion;
        this.oid = mapApplicationContextName.value();
        this.oid[this.oid.length - 1] = mapApplicationContextVersion.value();
    }

    public MAPApplicationContextImpl(long[] oid) {
        this.oid = oid;
        this.mapApplicationContextVersion = MAPApplicationContextVersion.getInstance((int) this.oid[this.oid.length - 1]);
        this.mapApplicationContextName = MAPApplicationContextName.getInstance(this.oid);
    }

    /**
     * @return the mapApplicationContextName
     */
    public MAPApplicationContextName getMapApplicationContextName() {
        return mapApplicationContextName;
    }

    /**
     * @param mapApplicationContextName the mapApplicationContextName to set
     */
    public void setMapApplicationContextName(MAPApplicationContextName mapApplicationContextName) {
        this.mapApplicationContextName = mapApplicationContextName;
    }

    /**
     * @return the mapApplicationContextVersion
     */
    public MAPApplicationContextVersion getMapApplicationContextVersion() {
        return mapApplicationContextVersion;
    }

    /**
     * @param mapApplicationContextVersion the mapApplicationContextVersion to
     * set
     */
    public void setMapApplicationContextVersion(MAPApplicationContextVersion mapApplicationContextVersion) {
        this.mapApplicationContextVersion = mapApplicationContextVersion;
    }

    public long[] getOid() {
        return this.oid;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj instanceof MAPApplicationContextImpl){
            MAPApplicationContextImpl other = (MAPApplicationContextImpl) obj;
            return other.mapApplicationContextName == mapApplicationContextName
                    && other.mapApplicationContextVersion == mapApplicationContextVersion;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("AC_NAME = %s "
                + "AC_VERSION = %s "
                + "AC = %s",
                this.mapApplicationContextName,
                this.mapApplicationContextVersion,
                Arrays.toString(this.oid));
    }
}
