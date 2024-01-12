/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import azrc.az.sigtran.tcap.parameters.UserInformationImpl;

/**
 *
 * @author root
 */
public class MapClose {

    private ReleaseMethod releaseMethod;
    private UserInformationImpl specificInformation;

    /**
     * @return the releaseMethod
     */
    public ReleaseMethod getReleaseMethod() {
        return releaseMethod;
    }

    /**
     * @param releaseMethod the releaseMethod to set
     */
    public void setReleaseMethod(ReleaseMethod releaseMethod) {
        this.releaseMethod = releaseMethod;
    }

    /**
     * @return the specificInformation
     */
    public UserInformationImpl getSpecificInformation() {
        return specificInformation;
    }

    /**
     * @param specificInformation the specificInformation to set
     */
    public void setSpecificInformation(UserInformationImpl specificInformation) {
        this.specificInformation = specificInformation;
    }

    public enum ReleaseMethod {

        normal,
        prearranged;
    }
}
