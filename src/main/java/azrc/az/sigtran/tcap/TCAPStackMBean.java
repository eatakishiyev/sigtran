/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import azrc.az.sigtran.sccp.general.SCCPUser;

/**
 *
 * @author eatakishiyev
 */
public interface TCAPStackMBean extends SCCPUser{

    public long getKeepAliveTimer();

    public void setKeepAliveTimer(long keepAliveTimer);

    
    public int totalDialogueCounts();
}
