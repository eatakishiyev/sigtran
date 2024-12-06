/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import azrc.az.sigtran.m3ua.NetworkIndicator;
import azrc.az.sigtran.sccp.access.point.LocalPointCode;
import azrc.az.sigtran.sccp.access.point.MTPServiceAccessPoint;
import azrc.az.sigtran.sccp.address.GlobalTitleIndicator;
import azrc.az.sigtran.sccp.address.NatureOfAddress;
import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.general.configuration.*;
import azrc.az.sigtran.sccp.globaltitle.GlobalTitle;
import azrc.az.sigtran.sccp.gtt.GlobalTitleTranslationRule;
import azrc.az.sigtran.sccp.gtt.GlobalTitleTranslator;
import azrc.az.sigtran.sccp.gtt.SccpEntitySet;
import azrc.az.sigtran.sccp.messages.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SCCPLayerManagement implements SCCPLayerManagementMBean {

    private static final Logger logger = LoggerFactory.getLogger(SCCPLayerManagement.class);
    private final List<azrc.az.sigtran.sccp.general.RemoteSignallingPoint> remoteSignallingPoints = new ArrayList<>();//From config
    private final List<GlobalTitleTranslator> globalTitleTranslators = new ArrayList<>();
    private final List<SccpEntitySet> sccpEntitySets = new ArrayList<>();
    private final List<MTPServiceAccessPoint> mtpSaps = new ArrayList<>();
    private final List<SubSystem> localSubSystems = new ArrayList<>();
    private LocalPointCode localPointCode = new LocalPointCode();// initialize it empty
    private final ConcernedSignallingPoints concernedSignallingPoints = new ConcernedSignallingPoints();
    private final SCCPStackImpl stack;

    public SCCPLayerManagement(SCCPStackImpl sccpStack) {
        this.stack = sccpStack;
    }

    @Override
    public void createRemoteSignallingPoint(String name, int spc, boolean concerned) throws IOException {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getName().equals(name)
                    || remoteSignallingPoint.getSpc() == spc) {
                throw new IOException(String.format("RemoteSignallingPoint already exists. SPC = %d", spc));
            }
        }
        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = new azrc.az.sigtran.sccp.general.RemoteSignallingPoint(name, spc, concerned, this.stack);
        this.remoteSignallingPoints.add(remoteSignallingPoint);

        logger.info("SCCP: Remote SP created: Name = " + name + ", SPC = " + spc + ", Concerned = " + concerned);
        if (concerned) {
            this.concernedSignallingPoints.addConcernedSignallingPoint(remoteSignallingPoint);
        }
    }

    public azrc.az.sigtran.sccp.general.RemoteSignallingPoint getRemoteSignallingPoint(int spc) {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getSpc() == spc) {
                return remoteSignallingPoint;
            }
        }
        return null;
    }

    public azrc.az.sigtran.sccp.general.RemoteSignallingPoint getRemoteSignallingPoint(String name) {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getName().equals(name)) {
                return remoteSignallingPoint;
            }
        }
        return null;
    }

    public boolean isRemoteSubSystemAvailable(SubSystemNumber ssn, int dpc) {
        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            return false;
        }
        return remoteSignallingPoint.isRemoteSubsystemAvailable(ssn);
    }

    @Override
    public void removeRemoteSignallingPoint(String name) throws IOException {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getName().equals(name)) {
                remoteSignallingPoints.remove(remoteSignallingPoint);
                concernedSignallingPoints.removeConcernedSignallingPoint(remoteSignallingPoint.getName());
                break;
            }
        }
    }

    @Override
    public void createRemoteSubsystem(String remoteSpcName, SubSystemNumber ssn) throws IOException {
        if (ssn == SubSystemNumber.SCCP_MANAGEMENT) {
            return;
        }
        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSpc = this.getRemoteSignallingPoint(remoteSpcName);
        if (remoteSpc == null) {
            throw new IOException(String.format("Unknown RemoteSignallingPoint. Name = %s", remoteSpcName));
        }

        RemoteSubSystem remoteSubSystem = remoteSpc.getRemoteSubsystem(ssn);
        if (remoteSubSystem != null) {
            throw new IOException(String.format("RemoteSubSystem already defined in RemoteSignallingPoint. RemoteSp = %s RemoteSsn = %s", remoteSpcName, ssn.name()));
        }

        remoteSubSystem = new RemoteSubSystem(remoteSpc, ssn, stack);
        remoteSpc.remoteSubSystems.add(remoteSubSystem);
        logger.info("SCCP: Remote SSN created: " + remoteSpc + ", " + remoteSubSystem);
    }

    @Override
    public void removeRemoteSubsystem(String remoteSpcName, SubSystemNumber ssn) throws IOException {
        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSpc = this.getRemoteSignallingPoint(remoteSpcName);
        if (remoteSpc == null) {
            throw new IOException(String.format("Unknown RemoteSignallingPoint. Name = %s", remoteSpcName));
        }

        RemoteSubSystem remoteSs = remoteSpc.getRemoteSubsystem(ssn);
        if (remoteSs == null) {
            throw new IOException(String.format("RemoteSubSystem not defined in RemoteSignallingPoint. RemoteSp = %s RemoteSsn = %s", remoteSpcName, ssn.name()));
        }

        remoteSpc.remoteSubSystems.remove(remoteSs);
        remoteSs.stopSst();
    }

    //Delegate method
    @Override
    public void registerConcernedSp(String remoteSignallingPointName) throws Exception {
        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(remoteSignallingPointName);
        if (remoteSignallingPoint == null) {
            throw new IOException(String.format("RemoteSignallingPoint not found. Name = %s", remoteSignallingPointName));
        }

        for (int i = 0; i < this.getConcernedSignallingPoints().getConcernedSignallingPoint().size(); i++) {
            azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSpc = this.getConcernedSignallingPoints().getConcernedSignallingPoint().get(i);
            if (remoteSpc.getSpc() == remoteSignallingPoint.getSpc()) {
                throw new IOException(String.format("RemoteSignallingPoint already added as concerned. SPC = %d", remoteSpc.getSpc()));
            }
        }
        this.getConcernedSignallingPoints().getConcernedSignallingPoint().add(remoteSignallingPoint);
    }

    @Override
    public void createGlobalTitleTranslationRule(String gtt, String name, String pattern, RoutingIndicator ri, String sccpEntitySetName, Integer tt, NatureOfAddress nai, NumberingPlan np, String convetionRule) throws IOException {
        GlobalTitleTranslator globalTitleTranslator = this.getGlobalTitleTranslator(gtt);
        if (globalTitleTranslator == null) {
            throw new IOException(String.format("No Global Title Translator found. Name = %s", gtt));
        }

        SccpEntitySet sccpEntitySet = this.getSccpEntitySet(sccpEntitySetName);
        if (sccpEntitySet == null) {
            throw new IOException(String.format("No SCCP EntitySet found. Name = %s", sccpEntitySetName));
        }

        GlobalTitleTranslationRule rule = globalTitleTranslator.addGlobalTitleTranslationRule(name, pattern, ri, sccpEntitySet, tt, np, nai, convetionRule);
        logger.info("SCCP: GTT rule added to GTT " + gtt + ", Rule : " + rule);
    }

    @Override
    public void removeGlobalTitleTranslationRule(String globalTitleTraslator, String rule) throws IOException {
        GlobalTitleTranslator globalTitleTranslator = this.getGlobalTitleTranslator(globalTitleTraslator);
        if (globalTitleTranslator == null) {
            throw new IOException(String.format("No Global Title Translator found. Name = %s", globalTitleTranslator));
        }

        if (!globalTitleTranslator.removeGlobalTitleTranslationRule(rule)) {
            throw new IOException(String.format("No Global Title Translation Rule found. Name = %s", rule));
        }
    }

    @Override
    public void removeGlobalTitleTranslator(String name) throws IOException {
        GlobalTitleTranslator gtt = this.getGlobalTitleTranslator(name);
        if (gtt == null) {
            throw new IOException(String.format("No Global Title Translator found. Name = %s", name));
        }

        this.globalTitleTranslators.remove(gtt);
    }

    @Override
    public void setMasterSap(String sap, String entitySet) throws IOException {
        MTPServiceAccessPoint mtpSap = this.getMtpSap(sap);
        if (sap == null) {
            throw new IOException(String.format("MtpSap not found. MtpSap = %s", sap));
        }

        SccpEntitySet sccpEntitySet = this.getSccpEntitySet(entitySet);
        if (sccpEntitySet == null) {
            throw new IOException(String.format("SccpEntitySet not found. SccpEntitySet = %s", entitySet));
        }

        sccpEntitySet.setMasterSap(mtpSap);
        logger.info("SCCP: MTP-SAP " + sap + " set as master in SCCP-Entity-Set " + entitySet);
    }

    @Override
    public void setSlaveSap(String sap, String entitySet) throws Exception {
        MTPServiceAccessPoint mtpSap = this.getMtpSap(sap);
        if (sap == null) {
            throw new IOException(String.format("MtpSap not found. MtpSap = %s", sap));
        }
        SccpEntitySet sccpEntitySet = this.getSccpEntitySet(entitySet);
        if (sccpEntitySet == null) {
            throw new IOException(String.format("SccpEntitySet not found. SccpEntitySet = %s", entitySet));
        }

        if (sccpEntitySet.getMasterSap() == null) {
            throw new IOException(String.format("Master Sap is not set. Please first set master Sap."));
        }

        sccpEntitySet.setSlaveSap(mtpSap);
        logger.info("SCCP: MTP-SAP " + sap + " set as slave in SCCP-Entity-Set " + entitySet);
    }

    public boolean isLocalSpc(int spc, NetworkIndicator ni) {
        return spc == localPointCode.getOpc() && ni.value() == localPointCode.getNi().value();
    }

    public boolean isLocalSpc(int spc) {
        return spc == localPointCode.getOpc();
    }

    public boolean isRemoteSpcAccessible(int spc, NetworkIndicator ni) {
        if (this.isLocalSpc(spc, ni)) {
            return true;//Own DPC and SCCP are always considered available;
        }

        azrc.az.sigtran.sccp.general.RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(spc);
        if (remoteSignallingPoint == null) {
            return false;
        }

        return !remoteSignallingPoint.isRemoteSpProhibited();
    }

    @Override
    public void createGlobalTitleTranslator(String name, NatureOfAddress natureOfAddress) throws IOException {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getGlobalTitleIndicator() == GlobalTitleIndicator.NATURE_OF_ADDRESS_IND_ONLY
                    && (globalTitleTranslator.getName().matches(name)
                    || globalTitleTranslator.getNatureOfAddress() == natureOfAddress)) {
                throw new IOException(String.format("Global Title Translator already exists. Name = %s NatureOfAddress = %s", globalTitleTranslator.getName(), globalTitleTranslator.getNatureOfAddress()));
            }
        }
        GlobalTitleTranslator gtt = new GlobalTitleTranslator(name, natureOfAddress);
        this.getGlobalTitleTranslators().add(gtt);
        logger.info("SCCP: GTT created " + gtt);
    }

    @Override
    public void createGlobalTitleTranslator(String name, Integer translationType) throws IOException {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getGlobalTitleIndicator() == GlobalTitleIndicator.TRANSLATION_TYPE_ONLY
                    && (globalTitleTranslator.getName().matches(name)
                    || globalTitleTranslator.getTranslationType() == translationType)) {
                throw new IOException(String.format("Global Title Translator already exists. Name = %s TranslationType = %d", globalTitleTranslator.getName(), globalTitleTranslator.getTranslationType()));
            }
        }
        GlobalTitleTranslator gtt = new GlobalTitleTranslator(name, translationType);
        this.globalTitleTranslators.add(gtt);
        logger.info("SCCP: GTT created " + gtt);
    }

    @Override
    public void createGlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan) throws IOException {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getGlobalTitleIndicator() == GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC
                    && (globalTitleTranslator.getName().matches(name)
                    || (globalTitleTranslator.getTranslationType() == translationType
                    && globalTitleTranslator.getNumberingPlan() == numberingPlan))) {
                throw new IOException(String.format("Global Title Translator already exists. Name = %s TranslationType = %d NumberingPlang = %s", globalTitleTranslator.getName(), globalTitleTranslator.getTranslationType(), globalTitleTranslator.getNumberingPlan()));
            }
        }
        GlobalTitleTranslator gtt = new GlobalTitleTranslator(name, translationType, numberingPlan);
        this.globalTitleTranslators.add(gtt);
        logger.info("SCCP: GTT created " + gtt);
    }

    @Override
    public void createGlobalTitleTranslator(String name, Integer translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress) throws IOException {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getGlobalTitleIndicator() == GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND
                    && (globalTitleTranslator.getName().matches(name)
                    || (globalTitleTranslator.getTranslationType() == translationType
                    && globalTitleTranslator.getNumberingPlan() == numberingPlan
                    && globalTitleTranslator.getNatureOfAddress() == natureOfAddress))) {
                throw new IOException(String.format("Global Title Translator already exists. Name =%s TranslationType = %d NumberingPlan = %s NatureOfAddress = %s", globalTitleTranslator.getName(), globalTitleTranslator.getTranslationType(), globalTitleTranslator.getNumberingPlan(), globalTitleTranslator.getNatureOfAddress()));
            }
        }
        GlobalTitleTranslator gtt = new GlobalTitleTranslator(name, translationType, numberingPlan, natureOfAddress);
        this.globalTitleTranslators.add(gtt);
        logger.info("SCCP: GTT created " + gtt);
    }

    public GlobalTitleTranslator getGlobalTitleTranslator(GlobalTitle globalTitle) {
        if(logger.isDebugEnabled()){
            logger.debug(String.format("Searching GlobalTitleTranslator. GlobalTitle = %s", globalTitle));
        }
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Got GlobalTitleTranslator. Try to check matching with incoming GT: %s", globalTitleTranslator));
            }
            if (globalTitleTranslator.matches(globalTitle)) {
                return globalTitleTranslator;
            }
        }
        return null;
    }

    public GlobalTitleTranslator getGlobalTitleTranslator(String name) {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getName().matches(name)) {
                return globalTitleTranslator;
            }
        }
        return null;
    }

    public GlobalTitleTranslator removeGlobalTitleTransaltor(String name) {
        for (int i = 0; i < this.globalTitleTranslators.size(); i++) {
            GlobalTitleTranslator globalTitleTranslator = this.globalTitleTranslators.get(i);
            if (globalTitleTranslator.getName().equals(name)) {
                this.globalTitleTranslators.remove(globalTitleTranslator);
                return globalTitleTranslator;
            }
        }
        return null;
    }

    @Override
    public SccpEntitySet createSccpEntitySet(String name, SccpEntitySet.Mode mode,
                                             boolean xUdtEnabled) throws IOException {
        for (int i = 0; i < this.sccpEntitySets.size(); i++) {
            SccpEntitySet sccpEntitySet = sccpEntitySets.get(i);
            if (sccpEntitySet.getName().equals(name)) {
                throw new IOException(String.format("SCCP Entity already exists. Name = %s Mode = %s SSN = %s", name, sccpEntitySet.getMode(), sccpEntitySet.getSsn()));
            }
        }
        SccpEntitySet sccpEntitySet = new SccpEntitySet(name, mode);
        this.sccpEntitySets.add(sccpEntitySet);
        return sccpEntitySet;
    }

    public SccpEntitySet createSccpEntitySet(String name, SccpEntitySet.Mode mode, SubSystemNumber ssn) throws IOException {
        for (int i = 0; i < this.sccpEntitySets.size(); i++) {
            SccpEntitySet sccpEntitySet = sccpEntitySets.get(i);
            if (sccpEntitySet.getName().equals(name)) {
                throw new IOException(String.format("SCCP Entity already exists. Name = %s Mode = %s SSN = %s", name, sccpEntitySet.getMode(), sccpEntitySet.getSsn()));
            }
        }
        SccpEntitySet sccpEntitySet = new SccpEntitySet(name, mode, ssn);
        this.sccpEntitySets.add(sccpEntitySet);
        logger.info("SCCP: EntitySet created: " + sccpEntitySet);
        return sccpEntitySet;
    }

    public SccpEntitySet getSccpEntitySet(String name) {
        for (int i = 0; i < this.sccpEntitySets.size(); i++) {
            SccpEntitySet sccpEntitySet = sccpEntitySets.get(i);
            if (sccpEntitySet.getName().equals(name)) {
                return sccpEntitySet;
            }
        }
        return null;
    }

    @Override
    public void removeSccpEntitySet(String name) throws IOException {
        for (int i = 0; i < this.sccpEntitySets.size(); i++) {
            SccpEntitySet sccpEntitySet = sccpEntitySets.get(i);
            if (sccpEntitySet.getName().equals(name)) {
                this.sccpEntitySets.remove(sccpEntitySet);
                return;
            }
        }
        throw new IOException(String.format("SCCP Entity Set not exists. Name = %s", name));
    }

    public MTPServiceAccessPoint getMtpSap(String name) {
        for (int i = 0; i < this.mtpSaps.size(); i++) {
            MTPServiceAccessPoint mtpSap = this.mtpSaps.get(i);
            if (mtpSap.getName().equals(name)) {
                return mtpSap;
            }
        }
        return null;
    }

    public MTPServiceAccessPoint getMtpSap(int dpc) {
        for (int i = 0; i < this.mtpSaps.size(); i++) {
            MTPServiceAccessPoint mtpSap = this.mtpSaps.get(i);
            if (mtpSap.getDpc() == dpc) {
                return mtpSap;
            }
        }
        return null;
    }

    @Override
    public void createMtpServiceAccessPoint(String name, int dpc, int opc, NetworkIndicator ni, MessageType targetMessageType) throws IOException {
        MTPServiceAccessPoint mtpSap = this.getMtpSap(name);
        if (mtpSap != null) {
            throw new IOException(String.format("MtpSap already defined.OPC = %s DPC = %s NI = %s NAME = %s", mtpSap.getOpc(), mtpSap.getDpc(), mtpSap.getNi(), mtpSap.getName()));
        }

        mtpSap = new MTPServiceAccessPoint(name, dpc, opc, ni);

        mtpSap.setTargetMessageType(targetMessageType);

        this.mtpSaps.add(mtpSap);
        logger.info("SCCP: MTP-SAP created: " + mtpSap);
    }

    @Override
    public void removeMtpServiceAccessPoint(String name) throws IOException {
        for (int i = 0; i < this.mtpSaps.size(); i++) {
            MTPServiceAccessPoint mtpSap = this.mtpSaps.get(i);
            if (mtpSap.getName().equals(name)) {
                this.mtpSaps.remove(mtpSap);
                return;
            }
        }
        throw new IOException(String.format("MtpSap not found. Name = %s ", name));
    }

    @Override
    public void loadConfiguration(SCCPConfiguration sccpConfiguration) throws Exception {
        logger.info("[SCCP]: Loading configuration...");
        logger.info("[SCCP]: Loading Local SPC configuration...");
        this.createLocalPointCode(sccpConfiguration.getLocalPointCode().getName(),
                sccpConfiguration.getLocalPointCode().getSpc(),
                sccpConfiguration.getLocalPointCode().getNetworkIndicator());

        logger.info("[SCCP]: Set minimum not segmented message size to " + stack.getMinNotSegmentedMessageSize());
        stack.setMinNotSegmentedMessageSize(sccpConfiguration.getMinNotSegmentedMessageSize());

        logger.info("[SCCP]: Set maximum message size to " + stack.getMaxMessageSize());
        stack.setMaxMessageSize(sccpConfiguration.getMaxMessageSize());

        logger.info("[SCCP]: Set remove spc from SCCP Address flag to " + stack.isRemoveSpc());
        stack.setRemoveSpc(sccpConfiguration.getRemoveSpc());

        logger.info("[SCCP]: Set HopCounter to " + stack.getHopCounter());
        stack.setHopCounter(sccpConfiguration.getHopCounter());

        logger.info("[SCCP]: Set reassembly timer to " + stack.getReassemblyTimer());
        stack.setReassemblyTimer(sccpConfiguration.getReassemblyTimer());

        logger.info("[SCCP]: Set SST Timer Min to " + stack.getSstTimerMin());
        stack.setSstTimerMin(sccpConfiguration.getSstTimerMin());

        logger.info("[SCCP]: Set SST Timer Max to " + stack.getSstTimerMax());
        stack.setSstTimerMax(sccpConfiguration.getSstTimerMax());

        logger.info("[SCCP]: Set SST Timer Increase By to " + stack.getSstTimerIncreaseBy());
        stack.setSstTimerIncreaseBy(sccpConfiguration.getSstTimerIncreaseBy());

        logger.info("[SCCP]: Set Ignore SST to " + stack.isIgnoreSST());
        stack.setIgnoreSST(sccpConfiguration.getSstIgnore());


        logger.info("[SCCP]: Loading RemoteSignallingPoints...");
        for (azrc.az.sigtran.sccp.general.configuration.RemoteSignallingPoint remoteSignallingPoint : sccpConfiguration.getRemoteSignallingPoints()) {
            this.createRemoteSignallingPoint(remoteSignallingPoint.getName(), remoteSignallingPoint.getSpc(),
                    remoteSignallingPoint.getConcerned());


            for (SubSystemNumber remoteSsn : remoteSignallingPoint.getRemoteSubSystems()) {
                logger.info("[SCCP]: Registering remote SSN " + remoteSsn +" on SPC " +
                        remoteSignallingPoint.getName() +" - " + remoteSignallingPoint.getSpc());
                this.createRemoteSubsystem(remoteSignallingPoint.getName(), remoteSsn);
            }
        }

        logger.info("[SCCP]: Loading MTPSaps...");
        for (MtpSap mtpSap : sccpConfiguration.getMtpSaps()) {
            this.createMtpServiceAccessPoint(mtpSap.getName(), mtpSap.getDpc(), mtpSap.getOpc(), mtpSap.getNi(),
                    mtpSap.getTargetMessageType());
        }

        logger.info("[SCCP]: Loading SCCP Entity sets...");

        for (SCCPEntitySet sccpEntitySet : sccpConfiguration.getSccpEntitySets()) {
            this.createSccpEntitySet(sccpEntitySet.getName(), sccpEntitySet.getMode(), sccpEntitySet.getSsn());

            for (int i = 0; i < sccpEntitySet.getMtpSaps().length; i++) {
                String mtpSapName = sccpEntitySet.getMtpSaps()[i];
                switch (i) {
                    case 0:
                        this.setMasterSap(mtpSapName, sccpEntitySet.getName());
                        break;
                    case 1:
                        this.setSlaveSap(mtpSapName, sccpEntitySet.getName());
                        break;
                    default:
                        logger.warn("[SCCP]: More than 2 MTP SAP count is not acceptable for SCCP Entity Sets");
                        break;
                }
            }
        }

        logger.info("[SCCP]: Loading SCCP Global Title Translation Rules...");

        for (Translator translator : sccpConfiguration.getTranslators()) {
            GlobalTitleIndicator gtIndicator = translator.getGlobalTitleIndicator();
            switch (gtIndicator) {
                case NATURE_OF_ADDRESS_IND_ONLY:
                    this.createGlobalTitleTranslator(translator.getName(), translator.getNatureOfAddress());
                    break;
                case TRANSLATION_TYPE_NP_ENC:
                    this.createGlobalTitleTranslator(translator.getName(), translator.getTranslationType(),
                            translator.getNumberingPlan());
                    break;
                case TRANSLATION_TYPE_ONLY:
                    this.createGlobalTitleTranslator(translator.getName(), translator.getTranslationType());
                    break;
                case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                    this.createGlobalTitleTranslator(translator.getName(), translator.getTranslationType(),
                            translator.getNumberingPlan(), translator.getNatureOfAddress());
                    break;
                default:
                    continue;
            }


            for (TranslatorRule translatorRule : translator.getRules()) {
                this.createGlobalTitleTranslationRule(translator.getName(), translatorRule.getName(),
                        translatorRule.getGtPattern(), translatorRule.getRoutingIndicator(),
                        translatorRule.getSccpEntitySet(), translatorRule.getTranslationType(),
                        translatorRule.getNatureOfAddress(), translatorRule.getNumberingPlan(),
                        translatorRule.getConversionRule());
            }
        }
    }



    @Override
    public LocalPointCode createLocalPointCode(String name, int opc, NetworkIndicator ni) throws IOException {
        this.localPointCode = new LocalPointCode(name, opc, ni);
        logger.info("[SCCP]:Local SPC created successfull: " + localPointCode);
        return this.localPointCode;
    }

    public SubSystem createLocalSubSystem(SubSystemNumber ssn) throws IOException {
        for (SubSystem localSubSystem : localSubSystems) {
            if (localSubSystem.getSsn() == ssn) {
                throw new IOException(String.format("Subsystem already registered. SSN = %s", ssn));
            }
        }

        SubSystem subsystem = new SubSystem(ssn, this.stack);
        this.localSubSystems.add(subsystem);
        return subsystem;
    }

    @Override
    public void removeLocalSubSystem(int ssn) throws IOException {
        for (SubSystem localSubSystem : localSubSystems) {
            if (localSubSystem.getSsn().value() == ssn) {
                if (localSubSystem.getState() == SubSystem.State.RUNNING) {
                    localSubSystems.remove(localSubSystem);
                } else {
                    throw new IOException(String.format("Subsystem is Running. "
                            + "Please first stop it. SSN = %s", ssn));
                }
                return;
            }
        }
    }

    @Override
    public void startLocalSubsystem(int ssn) throws RemoteException {
        SubSystem subSystem = this.getLocalSubsystem(SubSystemNumber.getInstance(ssn));
        if (subSystem == null) {
            throw new RemoteException("Local Subsystem not found");
        }
        subSystem.start();
    }

    @Override
    public void stopLocalSubsystem(int ssn) throws RemoteException {
        SubSystem subSystem = this.getLocalSubsystem(SubSystemNumber.getInstance(ssn));
        if (subSystem == null) {
            throw new RemoteException("Local Subsystem not found");
        }
        subSystem.stop();
    }

    public SubSystem getLocalSubsystem(SubSystemNumber ssn) {
        for (SubSystem subSystem : this.localSubSystems) {
            if (subSystem.getSsn() == ssn) {
                return subSystem;
            }
        }
        return null;
    }

    /**
     * @return the globalTitleTranslators
     */
    public List<GlobalTitleTranslator> getGlobalTitleTranslators() {
        return globalTitleTranslators;
    }

    /**
     * @return the concernedSignallingPoints
     */
    public ConcernedSignallingPoints getConcernedSignallingPoints() {
        return concernedSignallingPoints;
    }

    @Override
    public void setIgnoreSst(boolean ignoreSst) {
        stack.setIgnoreSST(ignoreSst);
    }

    @Override
    public boolean isIgnoreSst() {
        return stack.isIgnoreSST();
    }

    @Override
    public void setSstTimerIncreaseBy(long sstTimerIncreaseBy) {
        stack.setSstTimerIncreaseBy(sstTimerIncreaseBy);
    }

    @Override
    public long getSstTimerIncreaseBy() {
        return stack.getSstTimerIncreaseBy();
    }

    @Override
    public long getSstTimerMax() {
        return stack.getSstTimerMax();
    }

    @Override
    public void setSstTimerMax(long sstTimerMax) {
        stack.setSstTimerMax(sstTimerMax);
    }

    @Override
    public long getSstTimerMin() {
        return stack.getSstTimerMin();
    }

    @Override
    public void setSstTimerMin(long sstTimerMin) {
        stack.setSstTimerMin(sstTimerMin);
    }

    @Override
    public LocalPointCode getLocalPointCode() {
        return this.localPointCode;
    }

    @Override
    public List<azrc.az.sigtran.sccp.general.RemoteSignallingPoint> getRemoteSignallingPoints() {
        return remoteSignallingPoints;
    }

    @Override
    public List<MTPServiceAccessPoint> getMtpSaps() {
        return mtpSaps;
    }

    @Override
    public List<GlobalTitleTranslator> getGlobalTitleTranslator() {
        return globalTitleTranslators;
    }

    @Override
    public List<SccpEntitySet> getSccpEntitySet() {
        return sccpEntitySets;
    }

    @Override
    public ConcernedSignallingPoints getConcernedSpc() {
        return this.concernedSignallingPoints;
    }

    @Override
    public void removeConcernedSignallingPoint(String remoteSpcName) throws Exception {
        this.concernedSignallingPoints.removeConcernedSignallingPoint(remoteSpcName);
    }

    @Override
    public void removeRemoteSubsystem(String remoteSpcName, int ssn) throws Exception {
        this.removeRemoteSubsystem(remoteSpcName, SubSystemNumber.getInstance(ssn));
    }

    @Override
    public boolean isReturnOnlyFirstSegment() {
        return stack.isReturnOnlyFirstSegment();
    }

    @Override
    public void setReturnOnlyFirstSegment(boolean returnOnlyFirstSegment) {
        stack.setReturnOnlyFirstSegment(returnOnlyFirstSegment);
    }

    @Override
    public void setReassemblyTimer(long reassemblyTimer) {
        stack.setReassemblyTimer(reassemblyTimer);
    }

    @Override
    public long getReassemblyTimer() {
        return stack.getReassemblyTimer();
    }

    @Override
    public int getHopCounter() {
        return stack.getHopCounter();
    }

    @Override
    public void setHopCounter(int hopCounter) {
        stack.setHopCounter(hopCounter);
    }

}
