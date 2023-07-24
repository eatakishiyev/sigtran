/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran;

import dev.ocean.sigtran.cap.CAPStackImpl;
import dev.ocean.sigtran.m3ua.M3UAStack;
import dev.ocean.sigtran.m3ua.M3UAStackImpl;
import dev.ocean.sigtran.m3ua.configuration.M3UAConfiguration;
import dev.ocean.sigtran.map.MAPStackImpl;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.general.SCCPStack;
import dev.ocean.sigtran.sccp.general.SCCPStackImpl;
import dev.ocean.sigtran.sccp.general.SCCPUser;
import dev.ocean.sigtran.sccp.general.configuration.SCCPConfiguration;
import dev.ocean.sigtran.tcap.TCAPStack;
import dev.ocean.sigtran.tcap.TCUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.IOException;

/**
 * @author eatakishiyev
 */
public class Sigtran {

    private static Logger logger = LogManager.getLogger(Sigtran.class);

    private M3UAStackImpl m3uaStack;
    private SCCPStack sccpStack;

    //Singleton constructor
    public Sigtran() throws Exception {

    }

    public M3UAStack createM3UAStack(M3UAConfiguration m3UAConfiguration) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, IOException, MBeanRegistrationException {
        logger.info("Starting Sigtran stack...");
        this.m3uaStack = new M3UAStackImpl(m3UAConfiguration);
        return m3uaStack;
    }


    public SCCPStack createSCCPStack(SCCPConfiguration sccpConfiguration) throws Exception {
        this.sccpStack = new SCCPStackImpl(sccpConfiguration, m3uaStack.getM3uaProvider());
        return sccpStack;
    }


    public MAPStackImpl createMAPStack(String stackName, long startTransactionId, long endTransactionId,
                                       long keepAliveTimer, SubSystemNumber ssn, int mapGuardTimer, int mapWorkersCount) throws Exception {
        TCAPStack tcapStack = new TCAPStack(stackName, startTransactionId, endTransactionId,
                keepAliveTimer);
        MAPStackImpl mapStack = new MAPStackImpl(tcapStack.getProvider(), mapGuardTimer, mapWorkersCount);
        tcapStack.setUser(mapStack);
        sccpStack.addSCCPUser(ssn, tcapStack);
        return mapStack;
    }

    public MAPStackImpl createMAPStack(TCAPStack tcap, SubSystemNumber ssn, int mapGuardTimer, int mapWorkersCount) throws Exception {
        MAPStackImpl mapStack = new MAPStackImpl(tcap.getProvider(), mapGuardTimer, mapWorkersCount);
        tcap.setUser(mapStack);
        sccpStack.addSCCPUser(ssn, tcap);
        return mapStack;
    }

    public CAPStackImpl createCAPStack(String stackName, long startTransactionId, long endTransactionId, long keepAliveTimer,
                                       SubSystemNumber ssn, int capGuardTimer, int capWorkersCount) throws Exception {
        TCAPStack tcapStack = new TCAPStack(stackName, startTransactionId, endTransactionId,
                keepAliveTimer);

        CAPStackImpl capStack = new CAPStackImpl(tcapStack.getProvider(), capWorkersCount);
        capStack.setGuardTimerValue(capGuardTimer);

        tcapStack.setUser(capStack);
        sccpStack.addSCCPUser(ssn, tcapStack);
        return capStack;
    }

    public MAPStackImpl findMAPStack(SubSystemNumber ssn) {
        SCCPUser user = sccpStack.getSCCPUser(ssn);
        if (user == null) {
            return null;
        }

        return (MAPStackImpl) user.getUser();
    }

    public CAPStackImpl findCAPStack(SubSystemNumber ssn) {
        SCCPUser user = sccpStack.getSCCPUser(ssn);
        if (user == null) {
            return null;
        }

        return (CAPStackImpl) user.getUser();
    }

    public TCUser findTCAPUser(SubSystemNumber ssn) {
        SCCPUser user = sccpStack.getSCCPUser(ssn);
        if (user == null) {
            return null;
        }
        return user.getUser();
    }

    public SCCPStack getSCCP() {
        return sccpStack;
    }

    public M3UAStackImpl getM3uaStack() {
        return m3uaStack;
    }
}
