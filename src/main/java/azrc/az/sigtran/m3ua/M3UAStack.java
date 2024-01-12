/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.parameters.Cause;

import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public interface M3UAStack {

    //
     static final int M3UA_PROTOCOL_ID = 3;

     void fireMTPPause(int pointCode);

     void fireMTPResume(int pointCode);

     void fireMTPStatus(int dpc, int congestionLevel);

    void fireMTPStatus(int dpc, Cause cause, ServiceIdentificator userId);

    void addUser(M3UAUser user);

    M3UAUser getUser(ServiceIdentificator si);

    NodeType getNodeType();

    void setNodeType(NodeType nodeType);
    
    M3UAProvider getM3uaProvider();
    
    int getWorkersCount();
    
    void setWorkersCount(int workersCount);

}
