/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import java.util.Arrays;

/**
 *
 * @author root
 */
public enum ObjectIdentifiers {

    DIALOGUE_AS_ID(new long[]{0, 0, 17, 773, 1, 1, 1}),//id-as-dialogue
    UNIDIALOGUE_AS_ID(new long[]{0, 0, 17, 773, 1, 2, 1}),//id-as-unidialogue
    MAP_DIALOGUE_AS(new long[]{0, 4, 0, 0, 1, 1, 1, 1}),//map-dialogue-as
    UNKNOWN(new long[]{-1});
    private final long[] oid;

    ObjectIdentifiers(long[] oid) {
        this.oid = oid;
    }

    public long[] value() {
        return oid;
    }

    public static ObjectIdentifiers getInstance(long[] oid) {
        if (Arrays.equals(DIALOGUE_AS_ID.value(), oid)) {
            return DIALOGUE_AS_ID;
        }
        if (Arrays.equals(UNIDIALOGUE_AS_ID.value(), oid)) {
            return UNIDIALOGUE_AS_ID;
        }
        if (Arrays.equals(MAP_DIALOGUE_AS.value(), oid)) {
            return MAP_DIALOGUE_AS;
        }
        return UNKNOWN;
    }
}
