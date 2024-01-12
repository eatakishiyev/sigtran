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
public class AddToBegin implements GlobalTitleModificator {

    private String append;

    public AddToBegin(String append) {
        this.append = append;
    }

    @Override
    public String execute(String modificatable) {
        return append + modificatable;
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof AddToBegin)) {
            return false;
        }
        if (((AddToBegin) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.append);
        return hash;
    }
}
