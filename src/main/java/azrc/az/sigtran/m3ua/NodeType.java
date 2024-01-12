/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum NodeType {

    ASP("AS"),
    SGW("SG"),
    IPSP_CLIENT("IPSP_CLIENT"),
    IPSP_SERVER("IPSP_SERVER"),
    UNKNOWN("UNKNOWN");
    private final String value;

    private NodeType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static NodeType getInstance(String value) {
        switch (value.toUpperCase()) {
            case "AS":
                return ASP;
            case "SG":
                return SGW;
            case "IPSP_CLIENT":
                return IPSP_CLIENT;
            case "IPSP_SERVER":
                return IPSP_SERVER;
            default:
                return UNKNOWN;
        }
    }
}
