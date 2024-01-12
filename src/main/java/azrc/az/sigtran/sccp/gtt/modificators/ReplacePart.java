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
public class ReplacePart implements GlobalTitleModificator {

    private String searchText;
    private String replaceWith;

    public ReplacePart(String searchText, String replaceWith) {
        this.searchText = searchText;
        this.replaceWith = replaceWith;
    }

    @Override
    public String execute(String modificatable) {
        return modificatable.replace(searchText, replaceWith);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.searchText);
        hash = 17 * hash + Objects.hashCode(this.replaceWith);
        return hash;
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof ReplacePart)) {
            return false;
        }

        if (((ReplacePart) ob).hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }
}
