/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.parameters.Cause;

/**
 *
 * @author eatakishiyev
 */
public interface M3UAStack {

    //
    public static final int M3UA_PROTOCOL_ID = 3;

    public void fireMTPPause(int pointCode);

    public void fireMTPResume(int pointCode);

    public void fireMTPStatus(int dpc, int congestionLevel);

    public void fireMTPStatus(int dpc, Cause cause, ServiceIdentificator userId);

    public void addUser(M3UAUser user);

    public M3UAUser getUser(ServiceIdentificator si);

    public NodeType getNodeType();

    public void setNodeType(NodeType nodeType);
    
    public M3UAProvider getM3uaProvider();
    
    public int getWorkersCount();
    
    public void setWorkersCount(int workersCount);

}
