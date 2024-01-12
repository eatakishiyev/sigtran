/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.router;

/**
 *
 * @author eatakishiyev
 */
public enum LoadShareAlgo {

    ROUND_ROBIN,
    SLS,
    UNKNOWN;

    public static LoadShareAlgo getInstance(String value) {
        switch (value.trim().toUpperCase()) {
            case "ROUND_ROBIN":
                return ROUND_ROBIN;
            case "SLS":
                return SLS;
            default:
                return UNKNOWN;
        }
    }
}
