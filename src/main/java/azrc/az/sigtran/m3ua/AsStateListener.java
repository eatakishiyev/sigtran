/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface AsStateListener extends Serializable {

     void onAsActive(int rc);

     void onAsDown(int rc);
    
     String getName();
    
     void addAs(As as) throws Exception;

}
