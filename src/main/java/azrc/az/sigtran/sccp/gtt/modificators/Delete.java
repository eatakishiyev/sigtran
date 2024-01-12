/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.gtt.modificators;

/**
 *
 * @author eatakishiyev
 */
public class Delete implements GlobalTitleModificator {

    private int start;
    private int count;

    public Delete(int start, int count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public String execute(String modificatable) {
        return modificatable.substring(start, start + count);
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof Delete)) {
            return false;
        }

        if (((Delete) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.start;
        hash = 23 * hash + this.count;
        return hash;
    }
}
