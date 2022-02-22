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
public class Change implements GlobalTitleModificator {

    private String newValue;

    public Change(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String execute(String modificatable) {
        return newValue;
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof Change)) {
            return false;
        }
        if (((Change) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.newValue);
        return hash;
    }
}
