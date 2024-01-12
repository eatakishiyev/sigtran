/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum IPSPOperationType {

    CLIENT("CLIENT"),//Only for IPSP mode
    SERVER("SERVER"),//Only for IPSP mode
    DOUBLE_EXCHANGE("DE"),
    UNKNOWN("UNKNOWN");//For SGP and ASP
    private String value;

    private IPSPOperationType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static IPSPOperationType getInstance(String value) {
        switch (value.toUpperCase()) {
            case "CLIENT":
                return CLIENT;
            case "SERVER":
                return SERVER;
            case "DE":
                return DOUBLE_EXCHANGE;
            default:
                return UNKNOWN;
        }
    }
}
