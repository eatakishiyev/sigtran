/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import dev.ocean.sigtran.m3ua.M3UAUser;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPStack extends M3UAUser {

    public void addSCCPUser(SubSystemNumber ssn, SCCPUser user) throws Exception;

    public SCCPUser getSCCPUser(SubSystemNumber ssn);

    public void removeSCCPUser(SubSystemNumber ssn) throws Exception;

    public void setIgnoreSST(boolean ignoreSST);

    public boolean isIgnoreSST();

    public long getSstTimerMax();

    public void setSstTimerMax(long sstTimerMax);

    public long getSstTimerMin();

    public void setSstTimerMin(long sstTimerMin);

    public long getSstTimerIncreaseBy();

    public void setSstTimerIncreaseBy(long sstTimerIncreaseBy);
    
    public SCCPProvider getProvider();
    
    public boolean isReturnOnlyFirstSegment();
    
    public void setReturnOnlyFirstSegment(boolean returnOnlyFirstSegment);
    
    public int getMinNotSegmentedMessageSize();
    
    public void setMinNotSegmentedMessageSize(int minNotSegmentedMessageSize);
    
    public int getMaxMessageSize();
    
    public void setMaxMessageSize(int maxMessageSize);
    
    public boolean isRemoveSpc();
    
    public void setRemoveSpc(boolean removeSpc);
    
    public int getHopCounter();
    
    public void setHopCounter(int hopCounter);
    

    public long getReassemblyTimer();
    
    public void setReassemblyTimer(long reassemblyTimer);
}
