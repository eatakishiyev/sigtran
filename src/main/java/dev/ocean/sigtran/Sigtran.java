/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran;

import dev.ocean.sigtran.cap.CAPStackImpl;
import dev.ocean.sigtran.m3ua.M3UAStackImpl;
import dev.ocean.sigtran.map.MAPStackImpl;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.general.SCCPStack;
import dev.ocean.sigtran.sccp.general.SCCPStackImpl;
import dev.ocean.sigtran.sccp.general.SCCPUser;
import dev.ocean.sigtran.tcap.TCAPStack;
import dev.ocean.sigtran.tcap.TCUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author eatakishiyev
 */
public class Sigtran {

    private static Logger logger = LogManager.getLogger(Sigtran.class);

    private static Sigtran instance;
    private final M3UAStackImpl m3uaStack;
    private final SCCPStack sccpStack;

    //Singleton constructor
    private Sigtran() throws Exception {
        logger.info("Starting Sigtran stack...");
        this.m3uaStack = M3UAStackImpl.getInstance();
        this.sccpStack = SCCPStackImpl.createInstance(m3uaStack.getM3uaProvider());
    }

    public static Sigtran getInstance() {
        if (instance == null) {
            synchronized (Sigtran.class) {
                try {
                    instance = new Sigtran();
                } catch (Exception ex) {
                    logger.error("Error occured while to get Sigtran instance: ", ex);
                    ex.printStackTrace();
                }
            }
        }
        return instance;
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
