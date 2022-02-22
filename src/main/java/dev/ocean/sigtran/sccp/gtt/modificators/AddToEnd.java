/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.gtt.modificators;

import java.util.Objects;

/**
 *
 * @author eatakishiyev
 */
public class AddToEnd implements GlobalTitleModificator {

    private String append;

    public AddToEnd(String append) {
        this.append = append;
    }

    @Override
    public String execute(String modificatable) {
        return modificatable + append;

    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof AddToEnd)) {
            return false;
        }
        if (((AddToEnd) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.append);
        return hash;
    }
}
