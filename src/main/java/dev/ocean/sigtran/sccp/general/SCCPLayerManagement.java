/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dev.ocean.sigtran.m3ua.NetworkIndicator;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.sccp.access.point.LocalPointCode;
import dev.ocean.sigtran.sccp.access.point.MTPServiceAccessPoint;
import dev.ocean.sigtran.sccp.address.GlobalTitleIndicator;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.address.RoutingIndicator;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.globaltitle.GlobalTitle;
import dev.ocean.sigtran.sccp.gtt.GlobalTitleTranslationRule;
import dev.ocean.sigtran.sccp.gtt.GlobalTitleTranslator;
import dev.ocean.sigtran.sccp.gtt.SccpEntitySet;
import dev.ocean.sigtran.sccp.messages.MessageType;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class SCCPLayerManagement implements SCCPLayerManagementMBean {

    private static final Logger logger = LogManager.getLogger(SCCPLayerManagement.class);
    private final List<RemoteSignallingPoint> remoteSignallingPoints = new ArrayList<>();//From config
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
            RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getName().equals(name)
                    || remoteSignallingPoint.getSpc() == spc) {
                throw new IOException(String.format("RemoteSignallingPoint already exists. SPC = %d", spc));
            }
        }
        RemoteSignallingPoint remoteSignallingPoint = new RemoteSignallingPoint(name, spc, concerned, this.stack);
        this.remoteSignallingPoints.add(remoteSignallingPoint);

        logger.info("SCCP: Remote SP created: Name = " + name + ", SPC = " + spc + ", Concerned = " + concerned);
        if (concerned) {
            this.concernedSignallingPoints.addConcernedSignallingPoint(remoteSignallingPoint);
        }
    }

    public RemoteSignallingPoint getRemoteSignallingPoint(int spc) {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getSpc() == spc) {
                return remoteSignallingPoint;
            }
        }
        return null;
    }

    public RemoteSignallingPoint getRemoteSignallingPoint(String name) {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
            if (remoteSignallingPoint.getName().equals(name)) {
                return remoteSignallingPoint;
            }
        }
        return null;
    }

    public boolean isRemoteSubSystemAvailable(SubSystemNumber ssn, int dpc) {
        RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            return false;
        }
        return remoteSignallingPoint.isRemoteSubsystemAvailable(ssn);
    }

    @Override
    public void removeRemoteSignallingPoint(String name) throws IOException {
        for (int i = 0; i < this.remoteSignallingPoints.size(); i++) {
            RemoteSignallingPoint remoteSignallingPoint = this.remoteSignallingPoints.get(i);
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
        RemoteSignallingPoint remoteSpc = this.getRemoteSignallingPoint(remoteSpcName);
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
        RemoteSignallingPoint remoteSpc = this.getRemoteSignallingPoint(remoteSpcName);
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
        RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(remoteSignallingPointName);
        if (remoteSignallingPoint == null) {
            throw new IOException(String.format("RemoteSignallingPoint not found. Name = %s", remoteSignallingPointName));
        }

        for (int i = 0; i < this.getConcernedSignallingPoints().getConcernedSignallingPoint().size(); i++) {
            RemoteSignallingPoint remoteSpc = this.getConcernedSignallingPoints().getConcernedSignallingPoint().get(i);
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

        GlobalTitleTranslationRule rule =  globalTitleTranslator.addGlobalTitleTranslationRule(name, pattern, ri, sccpEntitySet, tt, np, nai, convetionRule);
        logger.info("SCCP: GTT rule added to GTT " + gtt+ ", Rule : " + rule);
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
        logger.info("SCCP: MTP-SAP " + sap +" set as master in SCCP-Entity-Set " + entitySet);
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
        logger.info("SCCP: MTP-SAP " + sap +" set as slave in SCCP-Entity-Set " + entitySet);
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

        RemoteSignallingPoint remoteSignallingPoint = this.getRemoteSignallingPoint(spc);
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

    public SccpEntitySet createSccpEntitySet(String name, SccpEntitySet.Mode mode,
            SubSystemNumber ssn, boolean xUdtEnabled) throws IOException {
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
    public void createMtpServiceAccessPoint(String name, int dpc, int opc, int ni, String targetMessageType) throws IOException {
        MTPServiceAccessPoint mtpSap = this.getMtpSap(name);
        if (mtpSap != null) {
            throw new IOException(String.format("MtpSap already defined.OPC = %s DPC = %s NI = %s NAME = %s", mtpSap.getOpc(), mtpSap.getDpc(), mtpSap.getNi(), mtpSap.getName()));
        }
        NetworkIndicator networkIndicator = NetworkIndicator.getInstance(ni);
        if (networkIndicator == NetworkIndicator.UNKNOWN) {
            throw new IOException("Unknown network indicator");
        }

        mtpSap = new MTPServiceAccessPoint(name, dpc, opc, networkIndicator);
        if (targetMessageType != null
                && !targetMessageType.trim().isEmpty()) {
            mtpSap.setTargetMessageType(MessageType.valueOf(targetMessageType));
        }
        this.mtpSaps.add(mtpSap);
        logger.info("---SCCP: MTP-SAP created: " + mtpSap);
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
    public void loadConfiguration() throws Exception {
        logger.info("SCCP: Loading configuration...");
        SAXBuilder saxBuilder = new SAXBuilder();

        Document document = saxBuilder.build(stack.getConfigFile());
        XPathFactory xpathFactory = XPathFactory.instance();

        logger.info("SCCP: Loading Local SPC configuration...");
        XPathExpression expression = xpathFactory.compile("sccp/localPointCode");
        Element localSpcElement = (Element) expression.evaluateFirst(document);

        this.createLocalPointCode(localSpcElement.getAttributeValue("name"),
                localSpcElement.getAttribute("spc").getIntValue(),
                NetworkIndicator.getInstance(localSpcElement.getAttribute("ni").getIntValue()));

//        Local subsystem initialization from the configuration
//        expression = xpathFactory.compile("localSubSystem");
//        List<Element> localSsnElements = expression.evaluate(localSpcElement);
//        for (Element localSsnElement : localSsnElements) {
//            this.createLocalSubSystem(SubSystemNumber.createInstance(localSsnElement.getAttribute("ssn").getIntValue()),
//                    localSsnElement.getAttributeValue("class"));
//        }
        expression = xpathFactory.compile("sccp/minNotSegmentedMessageSize");
        Element minNotSegmentedMessageSizeElement = (Element) expression.evaluateFirst(document);
        stack.setMinNotSegmentedMessageSize(Integer.valueOf(minNotSegmentedMessageSizeElement.getValue()));
        logger.info("SCCP: Set minimum not segmented message size to " + stack.getMinNotSegmentedMessageSize());

        expression = xpathFactory.compile("sccp/maxMessageSize");
        Element maxMessageSizeElement = (Element) expression.evaluateFirst(document);
        stack.setMaxMessageSize(Integer.valueOf(maxMessageSizeElement.getValue()));
        logger.info("SCCP: Set maximum message size to " + stack.getMaxMessageSize());

        expression = xpathFactory.compile("sccp/removeSpc");
        Element removeSpcElement = (Element) expression.evaluateFirst(document);
        stack.setRemoveSpc(Boolean.valueOf(removeSpcElement.getValue()));
        logger.info("SCCP: Set remove spc from SCCP Address flag to " + stack.isRemoveSpc());

        expression = xpathFactory.compile("sccp/hopCounter");
        Element hopCounterElement = (Element) expression.evaluateFirst(document);
        stack.setHopCounter(Integer.valueOf(hopCounterElement.getValue()));
        logger.info("SCCP: Set HopCounter to " + stack.getHopCounter());

        expression = xpathFactory.compile("sccp/reassemblyTimer");
        Element reassemblyTimerElement = (Element) expression.evaluateFirst(document);
        stack.setReassemblyTimer(Integer.valueOf(reassemblyTimerElement.getValue()));
        logger.info("SCCP: Set reassembly timer to " + stack.getReassemblyTimer());

        expression = xpathFactory.compile("sccp/sstTimerMin");
        Element sstTimerMinElement = (Element) expression.evaluateFirst(document);
        stack.setSstTimerMin(Integer.valueOf(sstTimerMinElement.getValue()));
        logger.info("SCCP: Set SST Timer Min to " + stack.getSstTimerMin());

        expression = xpathFactory.compile("sccp/sstTimerMax");
        Element sstTimerMaxElement = (Element) expression.evaluateFirst(document);
        stack.setSstTimerMax(Integer.valueOf(sstTimerMaxElement.getValue()));
        logger.info("SCCP: Set SST Timer Max to " + stack.getSstTimerMax());

        expression = xpathFactory.compile("sccp/sstTimerIncreaseBy");
        Element sstTimerIncreaseByElement = (Element) expression.evaluateFirst(document);
        stack.setSstTimerIncreaseBy(Integer.valueOf(sstTimerIncreaseByElement.getValue()));
        logger.info("SCCP: Set SST Timer Increase By to " + stack.getSstTimerIncreaseBy());

        expression = xpathFactory.compile("sccp/sstIgnore");
        Element ignoreSstElement = (Element) expression.evaluateFirst(document);
        stack.setIgnoreSST(Boolean.valueOf(ignoreSstElement.getValue()));
        logger.info("SCCP: Set Ignore SST to " + stack.isIgnoreSST());

        logger.info("SCCP: Loading RemoteSignallingPoints...");
        expression = xpathFactory.compile("sccp/remoteSignallingPoint");
        List<Element> remoteSpcElements = expression.evaluate(document);
        for (Element remoteSpcElement : remoteSpcElements) {
            this.createRemoteSignallingPoint(remoteSpcElement.getAttributeValue("name"),
                    remoteSpcElement.getAttribute("spc").getIntValue(),
                    remoteSpcElement.getAttribute("concerned").getBooleanValue());

            expression = xpathFactory.compile("remoteSubSystem");
            List<Element> remoteSsnElements = expression.evaluate(remoteSpcElement);
            for (Element remoteSsnElement : remoteSsnElements) {
                this.createRemoteSubsystem(remoteSpcElement.getAttributeValue("name"), SubSystemNumber.getInstance(remoteSsnElement.getAttribute("ssn").getIntValue()));
            }
        }

        logger.info("SCCP: Loading MTPSaps...");
        expression = xpathFactory.compile("sccp/mtpSap");
        List<Element> mtpSapElements = expression.evaluate(document);
        for (Element mtpSapElement : mtpSapElements) {
            this.createMtpServiceAccessPoint(mtpSapElement.getAttributeValue("name"),
                    mtpSapElement.getAttribute("dpc").getIntValue(),
                    mtpSapElement.getAttribute("opc").getIntValue(),
                    mtpSapElement.getAttribute("ni").getIntValue(),
                    mtpSapElement.getAttributeValue("targetMessageType"));
        }

        logger.info("SCCP: Loading SCCP Entity sets...");
        expression = xpathFactory.compile("sccp/sccpEntitySet");
        List<Element> sccpEntitySetElements = expression.evaluate(document);
        for (Element sccpEntitySetElement : sccpEntitySetElements) {
            this.createSccpEntitySet(sccpEntitySetElement.getAttributeValue("name"),
                    SccpEntitySet.Mode.getInstance(sccpEntitySetElement.getAttributeValue("mode")),
                    sccpEntitySetElement.getAttribute("ssn") != null
                    ? SubSystemNumber.getInstance(sccpEntitySetElement.
                            getAttribute("ssn").getIntValue()) : null,
                    sccpEntitySetElement.getAttribute("xudt") != null
                    ? sccpEntitySetElement.getAttribute("xudt").getBooleanValue()
                    : false);

            expression = xpathFactory.compile("mtpSap");
            mtpSapElements = expression.evaluate(sccpEntitySetElement);
            for (int i = 0; i < mtpSapElements.size(); i++) {
                Element mtpSapElement = mtpSapElements.get(i);
                switch (i) {
                    case 0:
                        this.setMasterSap(mtpSapElement.getAttributeValue("value"), sccpEntitySetElement.getAttributeValue("name"));
                        break;
                    case 1:
                        this.setSlaveSap(mtpSapElement.getAttributeValue("value"), sccpEntitySetElement.getAttributeValue("name"));
                        break;
                    default:
                        logger.warn("SCCP: More than 2 MTP SAP count is not acceptable for SCCP Entity Sets");
                        break;
                }
            }
        }

        logger.info("SCCP: Loading SCCP Global Title Translation Rules...");
        expression = xpathFactory.compile("sccp/translator");
        List<Element> translatorElements = expression.evaluate(document);
        for (Element translatorElement : translatorElements) {
            GlobalTitleIndicator gtIndicator = GlobalTitleIndicator.getInstance((byte) translatorElement.getAttribute("gtIndicator").getIntValue());
            switch (gtIndicator) {
                case NATURE_OF_ADDRESS_IND_ONLY:
                    this.createGlobalTitleTranslator(translatorElement.getAttributeValue("name"),
                            NatureOfAddress.getInstance((byte) translatorElement.getAttribute("natureOfAddress").getIntValue()));
                    break;
                case TRANSLATION_TYPE_NP_ENC:
                    this.createGlobalTitleTranslator(translatorElement.getAttributeValue("name"),
                            translatorElement.getAttribute("translationType").getIntValue(),
                            NumberingPlan.getInstance((byte) translatorElement.getAttribute("numberingPlan").getIntValue()));
                    break;
                case TRANSLATION_TYPE_ONLY:
                    this.createGlobalTitleTranslator(translatorElement.getAttributeValue("name"),
                            translatorElement.getAttribute("translationType").getIntValue());
                    break;
                case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                    this.createGlobalTitleTranslator(translatorElement.getAttributeValue("name"),
                            translatorElement.getAttribute("translationType").getIntValue(),
                            NumberingPlan.getInstance((byte) translatorElement.getAttribute("numberingPlan").getIntValue()),
                            NatureOfAddress.getInstance((byte) translatorElement.getAttribute("natureOfAddress").getIntValue()));
                    break;
                default:
                    continue;
            }

            expression = xpathFactory.compile("rule");
            List<Element> ruleElements = expression.evaluate(translatorElement);
            for (Element ruleElement : ruleElements) {
                Attribute riAttr = ruleElement.getAttribute("routingIndicator");
                Attribute ttAttr = ruleElement.getAttribute("translationType");
                Attribute naiAttr = ruleElement.getAttribute("natureOfAddress");
                Attribute npAttr = ruleElement.getAttribute("numberingPlan");

                this.createGlobalTitleTranslationRule(translatorElement.getAttributeValue("name"),
                        ruleElement.getAttributeValue("name"),
                        ruleElement.getAttributeValue("gtPattern"),
                        riAttr == null ? null : RoutingIndicator.getInstance((byte) riAttr.getIntValue()),
                        ruleElement.getAttributeValue("sccpEntitySet"),
                        ttAttr == null ? null : ttAttr.getIntValue(),
                        naiAttr == null ? null : NatureOfAddress.getInstance((byte) naiAttr.getIntValue()),
                        npAttr == null ? null : NumberingPlan.getInstance((byte) npAttr.getIntValue()),
                        ruleElement.getAttributeValue("conversionRule"));
            }

        }
    }

    @Override
    public void storeConfiguration() throws Exception {
        logger.info("SCCP: Storing configuration...");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        File originalFile = new File(stack.getConfigFile());
        File bkpFile = new File(originalFile.getName().concat(dateFormat.format(new Date())));
        originalFile.renameTo(bkpFile);

        Document document = new Document();
        Element rootElement = new Element("sccp");

        Element minNotSegmentedMessageSize = new Element("minNotSegmentedMessageSize");
        minNotSegmentedMessageSize.setText(String.valueOf(stack.getMinNotSegmentedMessageSize()));
        rootElement.addContent(minNotSegmentedMessageSize);

        Element maxMessageSize = new Element("maxMessageSize");
        maxMessageSize.setText(String.valueOf(stack.getMaxMessageSize()));
        rootElement.addContent(maxMessageSize);

        Element removeSpc = new Element("removeSpc");
        removeSpc.setText(String.valueOf(stack.isRemoveSpc()));
        rootElement.addContent(removeSpc);

        Element hopCounter = new Element("hopCounter");
        hopCounter.setText(String.valueOf(stack.getHopCounter()));
        rootElement.addContent(hopCounter);

        Element reassemblyTimer = new Element("reassemblyTimer");
        reassemblyTimer.setText(String.valueOf(stack.getReassemblyTimer()));
        rootElement.addContent(reassemblyTimer);

        Element sstTimerMin = new Element("sstTimerMin");
        sstTimerMin.setText(String.valueOf(stack.getSstTimerMin()));
        rootElement.addContent(sstTimerMin);

        Element sstTimerMax = new Element("sstTimerMax");
        sstTimerMax.setText(String.valueOf(stack.getSstTimerMax()));
        rootElement.addContent(sstTimerMax);

        Element sstTimerIncreaseBy = new Element("sstTimerIncreaseBy");
        sstTimerIncreaseBy.setText(String.valueOf(stack.getSstTimerIncreaseBy()));
        rootElement.addContent(sstTimerIncreaseBy);

        Element sstIgnore = new Element("sstIgnore");
        sstIgnore.setText(String.valueOf(stack.isIgnoreSST()));
        rootElement.addContent(sstIgnore);

        Element localSpcElement = new Element("localPointCode");
        localSpcElement.setAttribute("name", localPointCode.getName()).
                setAttribute("ni", String.valueOf(localPointCode.getNi().value())).
                setAttribute("spc", String.valueOf(localPointCode.getOpc()));

//        for (SubSystem localSubSystem : localSubSystems) {
//            Element localSsnElement = new Element("localSubSystem");
//            localSsnElement.setAttribute("ssn", String.valueOf(localSubSystem.getSsn().value()));
//            localSsnElement.setAttribute("class", localSubSystem.clazz);
//            localSpcElement.addContent(localSsnElement);
//        }
        rootElement.addContent(localSpcElement);

        for (RemoteSignallingPoint remoteSignallingPoint : remoteSignallingPoints) {
            Element remoteSpcElement = new Element("remoteSignallingPoint");
            remoteSpcElement.setAttribute("name", remoteSignallingPoint.getName()).
                    setAttribute("spc", String.valueOf(remoteSignallingPoint.getSpc())).
                    setAttribute("concerned", String.valueOf(remoteSignallingPoint.isConcerned()));
            for (RemoteSubSystem remoteSubSystem : remoteSignallingPoint.remoteSubSystems) {
                Element remoteSsnElement = new Element("remoteSubSystem");
                remoteSsnElement.setAttribute("ssn", String.valueOf(remoteSubSystem.getRemoteSSN().value()));
                remoteSpcElement.addContent(remoteSsnElement);
            }
            rootElement.addContent(remoteSpcElement);
        }

        for (MTPServiceAccessPoint mtpSap : mtpSaps) {
            Element mtpSapElement = new Element("mtpSap");
            mtpSapElement.setAttribute("name", mtpSap.getName()).
                    setAttribute("dpc", String.valueOf(mtpSap.getDpc())).
                    setAttribute("opc", String.valueOf(mtpSap.getOpc())).
                    setAttribute("ni", String.valueOf(mtpSap.getNi().value()));
            if (mtpSap.getTargetMessageType() != null) {
                mtpSapElement.setAttribute("targetMessageType", mtpSap.getTargetMessageType().name());
            }
            rootElement.addContent(mtpSapElement);
        }

        for (SccpEntitySet sccpEntitySet : sccpEntitySets) {
            Element sccpEntitySetElement = new Element("sccpEntitySet");
            sccpEntitySetElement.setAttribute("name", sccpEntitySet.getName());
            if (sccpEntitySet.getSsn() != null) {
                sccpEntitySetElement.setAttribute("ssn", String.valueOf(sccpEntitySet.getSsn().value()));
            }

            sccpEntitySetElement.setAttribute("mode", sccpEntitySet.getMode().name());

            if (sccpEntitySet.getMasterSap() != null) {
                Element masterMtpSapElement = new Element("mtpSap").setAttribute("value", sccpEntitySet.getMasterSap().getName());
                sccpEntitySetElement.addContent(masterMtpSapElement);
            }

            if (sccpEntitySet.getSlaveSap() != null) {
                Element slaveMtpSapElement = new Element("mtpSap").setAttribute("value", sccpEntitySet.getSlaveSap().getName());
                sccpEntitySetElement.addContent(slaveMtpSapElement);
            }

            rootElement.addContent(sccpEntitySetElement);
        }

        for (GlobalTitleTranslator gtTranslator : globalTitleTranslators) {
            Element gttElement = new Element("translator");
            gttElement.setAttribute("name", gtTranslator.getName()).
                    setAttribute("gtIndicator", String.valueOf(gtTranslator.getGlobalTitleIndicator().value()));
            switch (gtTranslator.getGlobalTitleIndicator()) {
                case NATURE_OF_ADDRESS_IND_ONLY:
                    gttElement.setAttribute("natureOfAddress", String.valueOf(gtTranslator.getNatureOfAddress().value()));
                    break;
                case TRANSLATION_TYPE_NP_ENC:
                    gttElement.setAttribute("translationType", String.valueOf(gtTranslator.getTranslationType())).
                            setAttribute("numberingPlan", String.valueOf(gtTranslator.getNumberingPlan().value()));
                    break;
                case TRANSLATION_TYPE_ONLY:
                    gttElement.setAttribute("translationType", String.valueOf(gtTranslator.getTranslationType()));
                    break;
                case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                    gttElement.setAttribute("translationType", String.valueOf(gtTranslator.getTranslationType())).
                            setAttribute("numberingPlan", String.valueOf(gtTranslator.getNumberingPlan().value())).
                            setAttribute("natureOfAddress", String.valueOf(gtTranslator.getNatureOfAddress().value()));
                    break;
            }

            for (GlobalTitleTranslationRule gttRule : gtTranslator.getGlobalTitleTranslationRules()) {
                Element gttRuleElement = new Element("rule");
                gttRuleElement.setAttribute("name", gttRule.getName()).
                        setAttribute("gtPattern", gttRule.getGtaiPattern().pattern()).
                        setAttribute("natureOfAddress", String.valueOf(gttRule.getNatureOfAddress().value())).
                        setAttribute("numberingPlan", String.valueOf(gttRule.getNumberingPlan().value())).
                        setAttribute("routingIndicator", String.valueOf(gttRule.getRoutingIndicator().value())).
                        setAttribute("translationType", String.valueOf(gttRule.getTranslationType())).
                        setAttribute("sccpEntitySet", gttRule.getSccpEntitySet().getName());
                if (gttRule.getConversionRule() != null) {
                    gttRuleElement.setAttribute("conversionRule", gttRule.getConversionRule());
                }

                gttElement.addContent(gttRuleElement);
            }

            rootElement.addContent(gttElement);
        }

        document.setRootElement(rootElement);
        XMLOutputter xMLOutputter = new XMLOutputter(Format.getPrettyFormat());

        xMLOutputter.output(document, new FileOutputStream(stack.getConfigFile()));
    }

    @Override
    public LocalPointCode createLocalPointCode(String name, int opc, NetworkIndicator ni) throws IOException {
        this.localPointCode = new LocalPointCode(name, opc, ni);
        logger.info("Local SPC created successfull: " + localPointCode);
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
    public List<RemoteSignallingPoint> getRemoteSignallingPoints() {
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
