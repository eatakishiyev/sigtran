/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface AsStateListener extends Serializable {

    public void onAsActive(int rc);

    public void onAsDown(int rc);
    
    public String getName();
    
    public void addAs(AsImpl as) throws Exception;

}
