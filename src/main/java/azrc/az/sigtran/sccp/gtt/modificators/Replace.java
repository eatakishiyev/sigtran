/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.gtt.modificators;

import java.util.Objects;

/**
 *
 * @author eatakishiyev
 */
public class Replace implements GlobalTitleModificator {

    private int start;
    private int end;
    private String with;

    public Replace(int start, int end, String with) {
        this.start = start;
        this.end = end;
        this.with = with;
    }

    @Override
    public String execute(String modificatable) {
        byte[] bWhat = modificatable.getBytes();
        byte[] bWith = with.getBytes();
        int j = 0;
        for (int i = start; i <= end; i++) {
            bWhat[i] = bWith[j++];
        }
        return new String(bWhat);
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof Replace)) {
            return false;
        }
        if (((Replace) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.start;
        hash = 17 * hash + this.end;
        hash = 17 * hash + Objects.hashCode(this.with);
        return hash;
    }
}
