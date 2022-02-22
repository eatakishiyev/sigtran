/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.IOException;
import java.rmi.RemoteException;
import dev.ocean.sigtran.m3ua.NetworkIndicator;
import dev.ocean.sigtran.sccp.access.point.LocalPointCode;
import dev.ocean.sigtran.sccp.access.point.MTPServiceAccessPoint;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.address.RoutingIndicator;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.gtt.GlobalTitleTranslator;
import dev.ocean.sigtran.sccp.gtt.SccpEntitySet;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPLayerManagementMBean {

    public void loadConfiguration() throws Exception;

    public void storeConfiguration() throws Exception;

    public void createGlobalTitleTranslationRule(String gtt, String name, String pattern, RoutingIndicator ri, String sccpEntitySet, Integer tt, NatureOfAddress nai, NumberingPlan np, String convetionRule) throws IOException;

    public void createGlobalTitleTranslator(String name, Integer translationType) throws IOException;

    public void createGlobalTitleTranslator(String name, NatureOfAddress natureOfAddress) throws IOException;

    public void createGlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan) throws IOException;

    public void createGlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress) throws IOException;

    public void createMtpServiceAccessPoint(String name, int remotePointCode, int opc, int ni, String targetMessageType) throws IOException;

    public void createRemoteSignallingPoint(String name, int spc, boolean concerned) throws IOException;

    public void createRemoteSubsystem(String remoteSpcName,SubSystemNumber ssn) throws IOException;

    public void removeRemoteSubsystem(String remoteSpcName, SubSystemNumber ssn) throws IOException;

    public void removeRemoteSubsystem(String remoteSpcName, int ssn) throws Exception;

    public SccpEntitySet createSccpEntitySet(String name, SccpEntitySet.Mode mode, 
            boolean xudtEnabled) throws IOException;

    public void registerConcernedSp(String remoteSignallingPoint) throws Exception;

    public void removeGlobalTitleTranslationRule(String globalTitleTraslator, String rule) throws IOException;

    public void setMasterSap(String sap, String entitySet) throws IOException;
    
    public void setSlaveSap(String sap, String entitySet) throws Exception;

    public void removeGlobalTitleTranslator(String name) throws IOException;

    public void removeMtpServiceAccessPoint(String name) throws IOException;

    public void removeRemoteSignallingPoint(String name) throws IOException;

    public void removeSccpEntitySet(String name) throws Exception;

    public void startLocalSubsystem(int ssn) throws RemoteException;

    public void stopLocalSubsystem(int ssn) throws RemoteException;

    public void removeLocalSubSystem(int ssn) throws Exception;
    
    public boolean isReturnOnlyFirstSegment();
    
    public void setReturnOnlyFirstSegment(boolean returnOnlyFirstSegment);

    public LocalPointCode createLocalPointCode(String name, int opc, NetworkIndicator ni) throws IOException;

    public void setIgnoreSst(boolean ignoreSst);

    public boolean isIgnoreSst();

    public void setSstTimerMin(long sstTimerMin);

    public long getSstTimerMin();

    public void setSstTimerMax(long sstTimerMax);

    public long getSstTimerMax();

    public void setSstTimerIncreaseBy(long sstTimerIncreaseBy);

    public long getSstTimerIncreaseBy();
    
    public void setReassemblyTimer(long reassemblyTimer);
    
    public long getReassemblyTimer();

    public LocalPointCode getLocalPointCode();

    public List<RemoteSignallingPoint> getRemoteSignallingPoints();

    public List<MTPServiceAccessPoint> getMtpSaps();

    public List<GlobalTitleTranslator> getGlobalTitleTranslator();

    public List<SccpEntitySet> getSccpEntitySet();

    public ConcernedSignallingPoints getConcernedSpc();

    public void removeConcernedSignallingPoint(String remoteSpcName) throws Exception;
    
    public void setHopCounter(int hopCounter);
    
    public int getHopCounter();
}
