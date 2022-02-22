/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.io.File;
import java.io.IOException;
import java.util.List;
import dev.ocean.sigtran.m3ua.parameters.TrafficMode;
import dev.ocean.sigtran.m3ua.router.Route;
import dev.ocean.sigtran.m3ua.router.Router;
import dev.ocean.sigtran.m3ua.router.RouteMode;
import java.io.FileOutputStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.apache.logging.log4j.*;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;

/**
 * ASP Mode Layer Management
 *
 * @author eatakishiyev
 */
public class M3UALayerManagement implements M3UALayerManagementMBean {

//  
    private final Logger logger = LogManager.getLogger(M3UALayerManagement.class);
//    
    private final M3UAStackImpl stack;

    protected M3UALayerManagement(M3UAStackImpl stack) {
        this.stack = stack;
    }

    @Override
    public void createAsp(String name, String localHost, String additionalAddress,
            int localPort, String peerHost, int peerPort, int inStreams,
            int outStreams, boolean autreconnect, boolean noDelay, int soSndBuf,
            int soRcvBuf, LinkType linkType, int aspUpTimeOut,
            int aspDownTimeOut, int aspActiveTimeOut, int aspInactiveTimeOut,
            int workersCount, boolean autoStart, Integer aspIdentifier, TrafficMode trafficMode) throws Exception {

        AspImpl asp = this.getAsp(name);
        if (asp != null) {
            throw new IOException("[M3UA]: Asp already exists.");
        }

        asp = new AspImpl(name, localHost, additionalAddress, localPort, peerHost,
                peerPort, inStreams, outStreams, autreconnect, noDelay, soSndBuf,
                soRcvBuf, linkType, aspUpTimeOut, aspDownTimeOut,
                aspActiveTimeOut, aspInactiveTimeOut, stack);
        asp.setWorkersCount(workersCount);
        asp.setAspId(aspIdentifier);
        asp.setTrafficMode(trafficMode);
        asp.setAutoStart(autoStart);
        stack.aspList.add(asp);

        if (autoStart == true) {
            asp.start();
        }

        logger.info(String.format("[M3UA]: Asp Created %s ", asp));
    }

    @Override
    public void removeAsp(String name) throws Exception {
        AspImpl asp = this.getAsp(name);

        if (asp == null) {
            throw new IOException("[M3UA]: Asp not configured: " + name);
        }

        for (AspStateMachine aspStateMachine : asp.aspStateMachines()) {
            if (!aspStateMachine.getCurrentState().getName().
                    equals(AspState.ASP_DOWN.name())) {
                throw new IOException(String.format("[M3UA]: Can not remove RemoteAsp, "
                        + "some RC[%s] are not in ASP_DOWN state. As = %s",
                        aspStateMachine.getAs().getRc(), aspStateMachine.as.getName()));
            }

        }

        if (asp.stateMachines.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (AspStateMachine aspStateMachine : asp.stateMachines.values()) {
                sb.append(aspStateMachine.as.getName()).
                        append(";");
            }
            throw new IOException("Asp is registered in more than one As. Please unregister it from this As-es: " + sb.toString());
        }

        stack.aspList.remove(asp);
        logger.info(String.format("Sgp removed: %s", asp));
    }

    /**
     * Creates remote SGP. Using in AS side to create remote SGP.
     *
     * @param name
     * @param sendRc
     * @param networkAppearance
     * @param routingContext
     * @throws Exception
     */
    @Override
    public void createAs(String name, int routingContext, boolean sendRc, int networkAppearance) throws Exception {
        AsImpl as = this.getAs(name);
        if (as != null) {
            throw new Exception(String.format("As with same name already configured: %s", name));
        }

        as = new AsImpl(name, routingContext, stack);
        as.setSendRoutingContext(sendRc);
        as.setNetworkAppearance(networkAppearance);

        stack.configuredAsList.add(as);

        logger.info(String.format("%s :/n Created successfully", as));

    }

    @Override
    public void removeAs(String name) throws IOException {

        AsImpl as = this.getAs(name);
        if (as == null) {
            throw new IOException(String.format("As not configured: %s", name));
        }

        List<AsStateListener> asRegisteredRoutes = as.getAsStateListener();
        if (asRegisteredRoutes.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (AsStateListener asRegisteredRoute : asRegisteredRoutes) {
                sb.append(asRegisteredRoute.getName())
                        .append(";");
            }
            throw new IOException("As registered at least in one route .Please first,"
                    + " unregister this As from routes:" + sb.toString());
        }

        if (as.getState().equals(AsState.AS_DOWN.name())) {
            List<AspImpl> aspList = as.getAspList();
            for (AspImpl asp : aspList) {
                asp.removeAsByRc(as.getRc());
            }
            stack.configuredAsList.remove(as);
        } else {
            throw new IOException("Can not remove As in state " + as.getState());
        }

    }

    @Override
    public void startSgp(String sgpName) throws Exception {
        Asp asp = this.getAsp(sgpName);
        if (asp == null) {
            throw new IOException(String.format("Remote Asp not configured: %s", sgpName));
        }
        asp.start();
    }

    @Override
    public void stopSgp(String aspName) throws Exception {
        Asp asp = this.getAsp(aspName);
        if (asp == null) {
            throw new IOException(String.format("Remote Asp configured: %s", aspName));
        }

        asp.shutdown();
    }

    @Override
    public void inactivateAs(String as, String sgp) throws Exception {
        As _as = this.getAs(as);
        if (_as == null) {
            throw new IOException("No As found. Name = " + as);
        }

        Asp asp = this.getAsp(sgp);
        if (asp == null) {
            throw new IOException("No Asp found. Name = " + asp);
        }

        asp.sendAspInactive(_as.getRc());

    }

    @Override
    public Route createRoute(String name, int dpc, int opc, ServiceIdentificator si, RouteMode mode) throws Exception {
        switch (mode) {
            case BACKUP:
                return createRouteBkp(name, dpc, opc, si);
            case BROADCAST:
                return createRouteBroadcast(name, dpc, opc, si);
            case LOADSHARE:
                return createRouteBroadcast(name, dpc, opc, si);
        }
        return null;
    }

    @Override
    public void addAspToAs(String aspName, String asName) throws Exception {
        AspImpl asp = getAsp(aspName);
        if (asp == null) {
            throw new IOException("No appropriate ASP found. ");
        }

        AsImpl as = getAs(asName);
        if (as == null) {
            throw new IOException("No appropriate AS found.");
        }

        as.addAsp(asp);
    }

    @Override
    public void removeAspFromAs(String aspName, String asName) throws Exception {
        AspImpl asp = getAsp(aspName);
        if (asp == null) {
            throw new IOException("No appropriate ASP found. ");
        }

        AsImpl as = getAs(asName);
        if (as == null) {
            throw new IOException("No appropriate AS found. ");
        }

        as.removeAsp(asp);
    }

    @Override
    public Route createRouteBkp(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        return stack.router.createRouteBkp(name, dpc, opc, si);
    }

    @Override
    public Route createRouteLoadShare(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        return stack.router.createRouteLoadShare(name, dpc, opc, si);
    }

    @Override
    public Route createRouteBroadcast(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        return stack.router.createRouteBroadcast(name, dpc, opc, si);
    }

    @Override
    public void addLinkToRoute(String name, String aspName) throws Exception {
        stack.router.addLinkToRoute(name, aspName);
    }

    @Override
    public void removeLinkFromRoute(String name, String aspName) throws IOException {
        stack.router.removeLinkFromRoute(name, aspName);
    }

    @Override
    public void removeRoute(String name) throws IOException {
        stack.router.removeRoute(name);
    }

    @Override
    public As getNullRcAs() {
        return stack.configuredAsList.get(0);
    }

    @Override
    public As getAsByRc(int rc) {
        for (As as : stack.configuredAsList) {
            if (as.getRc() == rc) {
                return as;
            }
        }
        return null;
    }

    @Override
    public int AsCount() {
        return stack.configuredAsList.size();
    }

    @Override
    public void loadConfiguration() throws JDOMException, IOException {

        logger.info("[M3UA]: Loading configuration...");
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(stack.getConfigFile()));

        XPathFactory xpathFactory = XPathFactory.instance();

        XPathExpression expression = xpathFactory.compile("/m3ua/nodeType");
        stack.nodeType = NodeType.getInstance(((Element) expression.evaluateFirst(document)).getTextNormalize());

        expression = xpathFactory.compile("/m3ua/m3uaWorkers");
        stack.workersCount = Integer.parseInt(((Element) expression.evaluateFirst(document)).getTextNormalize());

        expression = xpathFactory.compile("/m3ua/maxAsCountForRouting");
        stack.setMaxAsCountForRouting(Integer.parseInt(((Element) expression.evaluateFirst(document)).getTextNormalize()));

        //Configure Application server processes
        logger.info("[M3UA]: Loading Application Server Processes...");
        expression
                = xpathFactory.compile("m3ua/application-server-processes/application-server-process");
        List<Element> applicationServerProcessElements = expression.evaluate(document);
        for (Element applicationServerProcessElement : applicationServerProcessElements) {
            String name = applicationServerProcessElement.getAttributeValue("name");
            Integer aspIdentifier = applicationServerProcessElement.getAttribute("aspId") != null
                    ? applicationServerProcessElement.getAttribute("aspId").getIntValue() : null;
            String bindHost1 = applicationServerProcessElement.getAttributeValue("bindHost1");
            String bindHost2 = applicationServerProcessElement.getAttributeValue("bindHost2");
            int bindPort = applicationServerProcessElement.getAttribute("bindPort").getIntValue();
            String remoteHost = applicationServerProcessElement.getAttributeValue("remoteHost");
            int remotePort = applicationServerProcessElement.getAttribute("remotePort").getIntValue();
            int streams = applicationServerProcessElement.getAttribute("streams").getIntValue();
            LinkType linkType = LinkType.getInstance(applicationServerProcessElement.getAttributeValue("linkType"));
            boolean noDelay = applicationServerProcessElement.getAttribute("noDelay").getBooleanValue();
            int soSndBuf = applicationServerProcessElement.getAttribute("soSndBuf").getIntValue();
            int soRcvBuf = applicationServerProcessElement.getAttribute("soRcvBuf").getIntValue();
            boolean autoreconnect = applicationServerProcessElement.getAttribute("autoReconnect").getBooleanValue();
            int aspUpTimeOut = applicationServerProcessElement.getAttribute("aspUpTimeOut").getIntValue();
            int aspDownTimeOut = applicationServerProcessElement.getAttribute("aspDownTimeOut").getIntValue();
            int aspActiveTimeOut = applicationServerProcessElement.getAttribute("aspActiveTimeOut").getIntValue();
            int aspInactiveTimeOut = applicationServerProcessElement.getAttribute("aspInactiveTimeOut").getIntValue();
            TrafficMode trafficMode = TrafficMode.getInstance(applicationServerProcessElement.getAttributeValue("trafficMode"));
            int workers = applicationServerProcessElement.getAttribute("workers").getIntValue();
            boolean autoStart = applicationServerProcessElement.getAttribute("autoStart").getBooleanValue();

            try {
                this.createAsp(name, bindHost1, bindHost2, bindPort, remoteHost,
                        remotePort, streams, streams, autoreconnect, noDelay,
                        soSndBuf, soRcvBuf, linkType, aspUpTimeOut, aspDownTimeOut,
                        aspActiveTimeOut, aspInactiveTimeOut, workers, autoStart, aspIdentifier, trafficMode);
            } catch (Exception ex) {
                logger.error("M3UA:Can not load ApplicationServerProcess. :", ex);
            }
        }

        //Configure Application servers
        logger.info("M3UA: Loading Application Servers...");
        expression = xpathFactory.compile("m3ua/application-servers/application-server");
        List<Element> applicationServerElements = expression.evaluate(document);
        for (Element applicationServerElement : applicationServerElements) {
            String name = applicationServerElement.getAttributeValue("name");
            int routingContex = applicationServerElement.
                    getAttribute("routingContext").getIntValue();
            boolean sendRoutingContext = applicationServerElement.
                    getAttribute("sendRoutingContext").getBooleanValue();

            int networkAppearance = -1;
            if (applicationServerElement.getAttribute("networkAppearance") != null) {
                networkAppearance = applicationServerElement.getAttribute("networkAppearance").getIntValue();
            }

            try {
                AsImpl as = new AsImpl(name, routingContex, stack);
                as.setNetworkAppearance(networkAppearance);
                as.setSendRoutingContext(sendRoutingContext);
                stack.configuredAsList.add(as);

                expression = xpathFactory.compile("application-server-process");
                applicationServerProcessElements = expression.evaluate(applicationServerElement);
                for (Element applicationServerProcessElement : applicationServerProcessElements) {
                    String aspName = applicationServerProcessElement.getAttributeValue("name");
                    AspImpl aspImpl = this.getAsp(aspName);
                    if (aspImpl == null) {
                        logger.error("M3UA: Can not find Asp with name " + aspName);
                    } else {
                        aspImpl.addAs(as);
                        as.addAsp(aspImpl);
                    }
                }
            } catch (Exception ex) {
                logger.error("M3UA:Can not load ApplictionServer. :", ex);
            }

        }

        //Configure route
        logger.info("M3UA: Loading M3UA routes...");
        expression = xpathFactory.compile("m3ua/routes/route");
        List<Element> routeElements = expression.evaluate(document);
        for (Element routeElement : routeElements) {
            try {
                String name = routeElement.getAttributeValue("name");
                int opc = routeElement.getAttribute("opc").getIntValue();
                int dpc = routeElement.getAttribute("dpc").getIntValue();
                ServiceIdentificator si = ServiceIdentificator.getInstance(routeElement.getAttribute("si").getIntValue());
                RouteMode routeMode = RouteMode.getInstance(routeElement.getAttributeValue("mode"));

                Route route = stack.router.createRoute(name, opc, dpc, si, routeMode);

                XPathExpression exp = xpathFactory.compile("application-server");
                List<Element> applicationServers = exp.evaluate(routeElement);
                for (Element applicationServer : applicationServers) {
                    AsImpl as = stack.findAs(applicationServer.getAttributeValue("name"));
                    route.addAs(as);
                }
            } catch (Exception ex) {
                logger.error("error: ", ex);
            }
        }
    }

    @Override
    public String showAsp(String name) throws Exception {
        Asp asp = this.getAsp(name);
        return asp.toString();
    }

    @Override
    public String showAs(String name) throws Exception {
        As as = this.getAs(name);
        return as.toString();
    }

    @Override
    public void storeConfiguration() throws Exception {

        Document document = new Document();

        Element rootElement = new Element("m3ua");

        Element nodeType = new Element("nodeType");
        nodeType.setText(stack.getNodeType().value());
        rootElement.addContent(nodeType);

        Element m3uaWorkers = new Element("m3uaWorkers");
        m3uaWorkers.setText(String.valueOf(stack.getWorkersCount()));
        rootElement.addContent(m3uaWorkers);

        Element maxAsCountForRouting = new Element("maxAsCountForRouting");
        maxAsCountForRouting.setText(String.valueOf(stack.getMaxAsCountForRouting()));
        rootElement.addContent(maxAsCountForRouting);

        //ApplicationServerProcess storing
        Element applicationServerProcesses = new Element("application-server-processes");
        for (AspImpl asp : stack.aspList) {
            Element applicationServerProcess = new Element("application-server-process");

            applicationServerProcess.setAttribute("name", asp.getName())
                    .setAttribute("aspId", asp.getAspId() == null ? "-1" : String.valueOf(asp.getAspId()))
                    .setAttribute("bindHost1", asp.getSctpAssociation().getLocalAddress().getHostName())
                    .setAttribute("bindPort", String.valueOf(asp.getSctpAssociation().getLocalAddress().getPort()));

            if (asp.getSctpAssociation().getAdditionalAddress() != null) {
                applicationServerProcess.setAttribute("bindHost2", asp.getSctpAssociation().getAdditionalAddress().getHostName());
            }

            applicationServerProcess.setAttribute("remoteHost", asp.getSctpAssociation().getRemoteAddress().getHostName())
                    .setAttribute("remotePort", String.valueOf(asp.getSctpAssociation().getRemoteAddress().getPort()))
                    .setAttribute("streams", String.valueOf(asp.getSctpAssociation().getInStreams()))
                    .setAttribute("linkType", asp.getLinkType().getValue())
                    .setAttribute("noDelay", String.valueOf(asp.isNoDelay()))
                    .setAttribute("soSndBuf", String.valueOf(asp.getSoSndBuf()))
                    .setAttribute("soRcvBuf", String.valueOf(asp.getSoRcvBuf()))
                    .setAttribute("autoReconnect", String.valueOf(asp.getSctpAssociation().isAutoReconnect()))
                    .setAttribute("aspUpTimeOut", String.valueOf(asp.getAspUpTimeOut()))
                    .setAttribute("aspDownTimeOut", String.valueOf(asp.getAspDownTimeOut()))
                    .setAttribute("aspActiveTimeOut", String.valueOf(asp.getAspActiveTimeOut()))
                    .setAttribute("aspInactiveTimeOut", String.valueOf(asp.getAspInactiveTimeOut()))
                    .setAttribute("trafficMode", asp.getTrafficMode().name())
                    .setAttribute("workers", String.valueOf(asp.getWorkersCount()))
                    .setAttribute("autoStart", String.valueOf(asp.isAutoStart()));

            applicationServerProcesses.addContent(applicationServerProcess);
        }
        rootElement.addContent(applicationServerProcesses);

        //ApplicationServer storing
        Element applicationServers = new Element("application-servers");
        for (AsImpl as : stack.configuredAsList) {
            Element applicationServer = new Element("application-server");
            applicationServer.setAttribute("name", as.getName())
                    .setAttribute("routingContext", String.valueOf(as.getRc()))
                    .setAttribute("sendRoutingContext", String.valueOf(as.isSendRoutingContext()))
                    .setAttribute("networkAppearance", String.valueOf(as.getNetworkAppearance()));
            for (AspImpl aspImpl : as.getAspList()) {
                Element configuredApplicationServerProcess = new Element("application-server-process")
                        .setAttribute("name", aspImpl.getName());
                applicationServer.addContent(configuredApplicationServerProcess);
            }

            applicationServers.addContent(applicationServer);
        }
        rootElement.addContent(applicationServers);

        //Routes storing
        Element routes = new Element("routes");
        for (Route route : stack.router.getRoutes()) {
            Element routeElement = new Element("route")
                    .setAttribute("name", route.getName())
                    .setAttribute("opc", String.valueOf(route.getOpc()))
                    .setAttribute("dpc", String.valueOf(route.getDpc()))
                    .setAttribute("si", String.valueOf(route.getSi().value()))
                    .setAttribute("mode", route.getMode().name());
            for (AsImpl as : route.getAsList()) {
                Element configuredApplicationServer = new Element("application-server")
                        .setAttribute("name", as.getName());
                routeElement.addContent(configuredApplicationServer);
            }
            routes.addContent(routeElement);
        }

        rootElement.addContent(routes);

        document.setRootElement(rootElement);

        //Rename old file .
        File file = new File(stack.getConfigFile());
        file.renameTo(new File(String.format("%s.%s", stack.getConfigFile(),
                System.currentTimeMillis())));

        try {
            XMLOutputter xmlo = new XMLOutputter();
            xmlo.output(document, new FileOutputStream(new File(stack.getConfigFile())));
        } catch (Throwable th) {
            file.renameTo(new File(String.format("%s", stack.getConfigFile())));
            throw th;
        }

    }

    @Override
    public AspImpl getAsp(String name) {
        for (AspImpl asp : stack.aspList) {
            if (asp.getName().equals(name)) {
                return asp;
            }
        }
        return null;
    }

    @Override
    public AsImpl getAs(String name) {
        for (AsImpl as : stack.configuredAsList) {
            if (as.getName().equals(name)) {
                return as;
            }
        }
        return null;
    }

    public AsImpl getAs(int rc) {
        for (AsImpl as : stack.configuredAsList) {
            if (as.getRc() == rc) {
                return as;
            }
        }
        return null;
    }

    /**
     * @return the router
     */
    public Router getRouter() {
        return stack.router;
    }

    @Override
    public List<AspImpl> getAspList() {
        return stack.aspList;
    }

    @Override
    public List<AsImpl> getAsList() {
        return stack.configuredAsList;
    }

    @Override
    public List<Route> getRoutes() {
        return stack.router.getRoutes();
    }
}
