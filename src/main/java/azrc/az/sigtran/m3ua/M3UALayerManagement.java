/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.configuration.ApplicationServer;
import azrc.az.sigtran.m3ua.configuration.ApplicationServerProcess;
import azrc.az.sigtran.m3ua.configuration.M3UAConfiguration;
import azrc.az.sigtran.m3ua.parameters.TrafficMode;
import azrc.az.sigtran.m3ua.router.Route;
import azrc.az.sigtran.m3ua.router.RouteMode;
import azrc.az.sigtran.m3ua.router.Router;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * ASP Mode Layer Management
 *
 * @author eatakishiyev
 */
public class M3UALayerManagement implements M3UALayerManagementMBean {

    //
    private final Logger logger = LoggerFactory.getLogger(M3UALayerManagement.class);
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

        Asp asp = this.getAsp(name);
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
        Asp asp = this.getAsp(name);

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
            throw new IOException("Asp is registered in more than one As. Please unregister it from this As-es: " + sb);
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
        As as = this.getAs(name);
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

        As as = this.getAs(name);
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
            List<Asp> aspList = as.getAspList();
            for (Asp asp : aspList) {
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
        Asp asp = getAsp(aspName);
        if (asp == null) {
            throw new IOException("No appropriate ASP found. ");
        }

        As as = getAs(asName);
        if (as == null) {
            throw new IOException("No appropriate AS found.");
        }

        as.addAsp(asp);
    }

    @Override
    public void removeAspFromAs(String aspName, String asName) throws Exception {
        Asp asp = getAsp(aspName);
        if (asp == null) {
            throw new IOException("No appropriate ASP found. ");
        }

        As as = getAs(asName);
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
    public void loadConfiguration(M3UAConfiguration m3UAConfiguration) throws JDOMException, IOException {

        logger.info("[M3UA]: Loading configuration...");
        stack.nodeType = m3UAConfiguration.getNodeType();
        stack.workersCount = m3UAConfiguration.getM3uaWorkers();
        stack.setMaxAsCountForRouting(m3UAConfiguration.getMaxAsCountForRouting());

        //Configure Application server processes
        logger.info("[M3UA]: Loading Application Server Processes...");
        for (ApplicationServerProcess applicationServerProcess : m3UAConfiguration.getApplicationServerProcesses()) {
            String name = applicationServerProcess.getName();
            Integer aspIdentifier = applicationServerProcess.getAspId();
            String bindHost1 = applicationServerProcess.getBindHost1();
            String bindHost2 = applicationServerProcess.getBindHost2();
            int bindPort = applicationServerProcess.getBindPort();
            String remoteHost = applicationServerProcess.getRemoteHost();
            int remotePort = applicationServerProcess.getRemotePort();
            int streams = applicationServerProcess.getStreams();
            LinkType linkType = applicationServerProcess.getLinkType();
            boolean noDelay = applicationServerProcess.getNoDelay();
            int soSndBuf = applicationServerProcess.getSoSndBuf();
            int soRcvBuf = applicationServerProcess.getSoRcvBuf();
            boolean autoreconnect = applicationServerProcess.getAutoReconnect();
            int aspUpTimeOut = applicationServerProcess.getAspUpTimeOut();
            int aspDownTimeOut = applicationServerProcess.getAspDownTimeOut();
            int aspActiveTimeOut = applicationServerProcess.getAspActiveTimeOut();
            int aspInactiveTimeOut = applicationServerProcess.getAspInactiveTimeOut();
            TrafficMode trafficMode = applicationServerProcess.getTrafficMode();
            int workers = applicationServerProcess.getWorkers();
            boolean autoStart = applicationServerProcess.getAutoStart();

            try {
                this.createAsp(name, bindHost1, bindHost2, bindPort, remoteHost,
                        remotePort, streams, streams, autoreconnect, noDelay,
                        soSndBuf, soRcvBuf, linkType, aspUpTimeOut, aspDownTimeOut,
                        aspActiveTimeOut, aspInactiveTimeOut, workers, autoStart, aspIdentifier, trafficMode);
            } catch (Exception ex) {
                logger.error("[M3UA]:Can not load ApplicationServerProcess. :", ex);
            }
        }

        //Configure Application servers
        logger.info("[M3UA]: Loading Application Servers...");

        for (ApplicationServer applicationServer : m3UAConfiguration.getApplicationServers()) {
            String name = applicationServer.getName();
            int routingContex = applicationServer.getRoutingContext();
            boolean sendRoutingContext = applicationServer.getSendRoutingContext();

            int networkAppearance = -1;
            if (applicationServer.getNetworkAppearance() != null) {
                networkAppearance = applicationServer.getNetworkAppearance();
            }

            try {
                AsImpl as = new AsImpl(name, routingContex, stack);
                as.setNetworkAppearance(networkAppearance);
                as.setSendRoutingContext(sendRoutingContext);
                stack.configuredAsList.add(as);

                for (String aspName : applicationServer.getApplicationServerProcesses()) {
                    Asp aspImpl = this.getAsp(aspName);
                    if (aspImpl == null) {
                        logger.error("[M3UA]: Can not find Asp with name " + aspName);
                    } else {
                        aspImpl.addAs(as);
                        as.addAsp(aspImpl);
                    }
                }
            } catch (Exception ex) {
                logger.error("[M3UA]:Can not load ApplictionServer. :", ex);
            }

        }

        //Configure route
        logger.info("[M3UA]: Loading M3UA routes...");
        for (azrc.az.sigtran.m3ua.configuration.Route routeConfig : m3UAConfiguration.getRoutes()) {
            try {
                String name = routeConfig.getName();
                int opc = routeConfig.getOpc();
                int dpc = routeConfig.getDpc();
                ServiceIdentificator si = routeConfig.getSi();
                RouteMode routeMode = routeConfig.getMode();

                Route route = stack.router.createRoute(name, opc, dpc, si, routeMode);


                for (String asName : routeConfig.getApplicationServers()) {
                    As as = stack.findAs(asName);
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
    public Asp getAsp(String name) {
        for (Asp asp : stack.aspList) {
            if (asp.getName().equals(name)) {
                return asp;
            }
        }
        return null;
    }

    @Override
    public As getAs(String name) {
        for (As as : stack.configuredAsList) {
            if (as.getName().equals(name)) {
                return as;
            }
        }
        return null;
    }

    public As getAs(int rc) {
        for (As as : stack.configuredAsList) {
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
    public List<Asp> getAspList() {
        return stack.aspList;
    }

    @Override
    public List<As> getAsList() {
        return stack.configuredAsList;
    }

    @Override
    public List<Route> getRoutes() {
        return stack.router.getRoutes();
    }
}
